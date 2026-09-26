// vkshim —— 系统 Vulkan 加载器包装层。
// 启动器检测到设备存在驱动能力缺口时（VKSHIM_ENABLE=1）经本层加载，按缺口类型合成：
// 一、驱动缺失 VK_EXT_vertex_attribute_divisor 但提供 VK_KHR_vertex_attribute_divisor：
//   1. vkEnumerateDeviceExtensionProperties 补报 EXT 扩展；
//   2. vkCreateDevice 把启用列表里的 EXT 换成 KHR；EXT v1/v2 应用不传 features 结构，
//      而 KHR/核心把 divisor 用法门控在其后，链上缺位时补写一个全开节点；
//   3. vkGetPhysicalDeviceProperties2 把 EXT properties 节点转接为 KHR 节点
//      （KHR 结构多一个 supportsNonZeroFirstInstance 字段，须用自有存储承接后回填）。
//   features（sType 1000190002）与 pipeline divisor state（sType 1000190001）的 sType
//   在注册表中 KHR 与 EXT 同值，经包装后可直达驱动，无需改写。
//   驱动原生带有 EXT 时全链路直通。
// 二、驱动核心能力位 fillModeNonSolid 缺失（线框/点填充多边形模式）：模拟为降级运行——
//   1. vkGetPhysicalDeviceFeatures(2) 谎报该能力位为开启，保证应用与游戏侧检测通过；
//   2. vkCreateDevice 时从启用 features（含 pNext 链 VkPhysicalDeviceFeatures2）剥除该位，
//      避免真驱动拒绝设备创建；调用后恢复应用内存；
//   3. vkCreateGraphicsPipelines 把非 VK_POLYGON_MODE_FILL 的管线降级为 FILL：
//      线框语义让位于可运行性（与 VulkanFix-Renderer 的 fill_mode_non_solid 模块同策略）。
//   能力探测针对每个物理设备独立进行，原生支持则全链路直通。

#include <vulkan/vulkan.h>

#include <dlfcn.h>
#include <pthread.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "log.h"

#if defined(__aarch64__) || defined(__x86_64__)
#define SYSTEM_VULKAN_LOADER_PATH "/system/lib64/libvulkan.so"
#else
#define SYSTEM_VULKAN_LOADER_PATH "/system/lib/libvulkan.so"
#endif

#define EXT_SPEC_VERSION 3
#define MAX_TRACKED_PHYSICAL_DEVICES 8
#define MAX_TRACKED_DEVICES 16

// 驱动能力判定：
// translate 表示 EXT divisor 需经 KHR 合成；add_khr 表示设备创建时需补上 KHR 扩展名；
// fns_emulated 表示核心能力位 fillModeNonSolid 原生缺失，需模拟（谎报 + 管线降级）
struct driver_mode {
    bool translate;
    bool add_khr;
    bool fns_emulated;
};

// 已创建的逻辑设备是否处于 fillModeNonSolid 模拟（管线降级只对这些设备生效）
static VkDevice g_tracked_devices[MAX_TRACKED_DEVICES];
static bool g_device_fns_emulated[MAX_TRACKED_DEVICES];
static int g_tracked_device_count;

// 与 VkBaseOutStructure ABI 相同，pNext 用 void* 便于链改写
struct chain_node {
    VkStructureType sType;
    void* pNext;
};

static void* g_loader;
static bool g_ready;

static PFN_vkCreateInstance real_create_instance;
static PFN_vkDestroyInstance real_destroy_instance;
static PFN_vkEnumerateInstanceExtensionProperties real_enumerate_instance_extension_properties;
static PFN_vkEnumerateInstanceLayerProperties real_enumerate_instance_layer_properties;
static PFN_vkEnumerateInstanceVersion real_enumerate_instance_version;
static PFN_vkGetInstanceProcAddr real_get_instance_proc_addr;
static PFN_vkGetDeviceProcAddr real_get_device_proc_addr;

// 加载器返回的实例级桩按传入句柄分派，跨实例通用，解析一次即可
static struct {
    PFN_vkEnumeratePhysicalDevices enumerate_physical_devices;
    PFN_vkEnumerateDeviceExtensionProperties enumerate_device_extension_properties;
    PFN_vkGetPhysicalDeviceFeatures get_physical_device_features;
    PFN_vkGetPhysicalDeviceFeatures2 get_physical_device_features2;
    PFN_vkGetPhysicalDeviceProperties get_physical_device_properties;
    PFN_vkGetPhysicalDeviceProperties2 get_physical_device_properties2;
    PFN_vkGetPhysicalDeviceProperties2KHR get_physical_device_properties2_khr;
    PFN_vkCreateDevice create_device;
    // device 级命令，经实例级 gipa 解析到的 trampoline，按传入 device 分派
    PFN_vkCreateGraphicsPipelines create_graphics_pipelines;
} real;

static pthread_mutex_t g_lock = PTHREAD_MUTEX_INITIALIZER;
static VkPhysicalDevice g_physical_devices[MAX_TRACKED_PHYSICAL_DEVICES];
static struct driver_mode g_physical_device_modes[MAX_TRACKED_PHYSICAL_DEVICES];
static int g_physical_device_count;

static VkResult shim_enumerate_physical_devices(VkInstance instance, uint32_t* pPhysicalDeviceCount,
                                                VkPhysicalDevice* pPhysicalDevices);
static VkResult shim_enumerate_device_extension_properties(VkPhysicalDevice physicalDevice, const char* pLayerName,
                                                           uint32_t* pPropertyCount,
                                                           VkExtensionProperties* pProperties);
static void shim_get_physical_device_properties2(VkPhysicalDevice physicalDevice,
                                                 VkPhysicalDeviceProperties2* pProperties);
static void shim_get_physical_device_properties2_khr(VkPhysicalDevice physicalDevice,
                                                     VkPhysicalDeviceProperties2* pProperties);
static void shim_get_physical_device_features(VkPhysicalDevice physicalDevice, VkPhysicalDeviceFeatures* pFeatures);
static void shim_get_physical_device_features2(VkPhysicalDevice physicalDevice, VkPhysicalDeviceFeatures2* pFeatures);
static void shim_get_physical_device_features2_khr(VkPhysicalDevice physicalDevice,
                                                   VkPhysicalDeviceFeatures2* pFeatures);
static VkResult shim_create_device(VkPhysicalDevice physicalDevice, const VkDeviceCreateInfo* pCreateInfo,
                                   const VkAllocationCallbacks* pAllocator, VkDevice* pDevice);
static VkResult shim_create_graphics_pipelines(VkDevice device, VkPipelineCache pipelineCache, uint32_t createInfoCount,
                                               const VkGraphicsPipelineCreateInfo* pCreateInfos,
                                               const VkAllocationCallbacks* pAllocator, VkPipeline* pPipelines);

static bool extension_list_contains(const VkExtensionProperties* list, uint32_t count, const char* name) {
    for (uint32_t i = 0; i < count; i++) {
        if (strcmp(list[i].extensionName, name) == 0) return true;
    }
    return false;
}

// 链上是否存在指定 sType 的节点
static bool chain_contains(const void* pNext, VkStructureType sType) {
    const struct chain_node* node = pNext;
    while (node != NULL) {
        if (node->sType == sType) return true;
        node = (const struct chain_node*) node->pNext;
    }
    return false;
}

// 把 node 接到 *head_link 链尾，返回原链尾的链接槽（原值必为 NULL），调用后须将其置回 NULL 摘除
static void** chain_splice_tail(void** head_link, void* node) {
    struct chain_node* last = *head_link;
    if (last == NULL) {
        *head_link = node;
        return head_link;
    }
    while (last->pNext != NULL) last = (struct chain_node*) last->pNext;
    last->pNext = node;
    return &last->pNext;
}

static struct driver_mode compute_driver_mode(VkPhysicalDevice physicalDevice) {
    struct driver_mode mode = {false, false, false};
    if (real.enumerate_device_extension_properties == NULL) return mode;

    uint32_t count = 0;
    if (real.enumerate_device_extension_properties(physicalDevice, NULL, &count, NULL) != VK_SUCCESS) {
        return mode;
    }
    VkExtensionProperties* list = NULL;
    if (count > 0) {
        list = malloc(count * sizeof(VkExtensionProperties));
        if (list == NULL ||
            real.enumerate_device_extension_properties(physicalDevice, NULL, &count, list) != VK_SUCCESS) {
            free(list);
            return mode;
        }
    }
    bool hasExt = extension_list_contains(list, count, VK_EXT_VERTEX_ATTRIBUTE_DIVISOR_EXTENSION_NAME);
    bool hasKHR = extension_list_contains(list, count, VK_KHR_VERTEX_ATTRIBUTE_DIVISOR_EXTENSION_NAME);
    free(list);

    if (hasExt) return mode;
    // 仅在驱动声明 KHR 扩展时合成；经启动器检测门控后本函数只应在该形态下被启用，
    // 无 KHR 扩展名的设备（含仅 Vulkan 1.3 核心功能者）不做合成
    if (hasKHR) {
        mode.translate = true;
        mode.add_khr = true;
    }
    return mode;
}

// fillModeNonSolid 原生支持探测：核心能力位与扩展不同，没有可转接的等价接口，
// 缺失时走降级模拟（谎报 + 管线 fill mode 回退为 FILL），原生支持则直通
static bool compute_fns_emulated(VkPhysicalDevice physicalDevice) {
    if (real.get_physical_device_features == NULL) return false;
    VkPhysicalDeviceFeatures feat;
    real.get_physical_device_features(physicalDevice, &feat);
    return feat.fillModeNonSolid == VK_FALSE;
}

// 查询并登记物理设备的驱动模式；重复查询幂等
static struct driver_mode driver_mode_of(VkPhysicalDevice physicalDevice) {
    pthread_mutex_lock(&g_lock);
    for (int i = 0; i < g_physical_device_count; i++) {
        if (g_physical_devices[i] == physicalDevice) {
            struct driver_mode cached = g_physical_device_modes[i];
            pthread_mutex_unlock(&g_lock);
            return cached;
        }
    }
    pthread_mutex_unlock(&g_lock);
    // compute 经真实加载器查询，放在锁外，避免与真驱动内部的锁重入；
    // 并发首查由登记前的重查兜底（重复登记无害但避免）
    struct driver_mode mode = compute_driver_mode(physicalDevice);
    mode.fns_emulated = compute_fns_emulated(physicalDevice);
    FCL_LOG("vkshim: pd %p mode translate=%d add_khr=%d fns_emulated=%d", physicalDevice, mode.translate,
            mode.add_khr, mode.fns_emulated);
    pthread_mutex_lock(&g_lock);
    bool registered = false;
    for (int i = 0; i < g_physical_device_count; i++) {
        if (g_physical_devices[i] == physicalDevice) registered = true;
    }
    if (!registered && g_physical_device_count < MAX_TRACKED_PHYSICAL_DEVICES) {
        g_physical_devices[g_physical_device_count] = physicalDevice;
        g_physical_device_modes[g_physical_device_count] = mode;
        g_physical_device_count++;
    }
    pthread_mutex_unlock(&g_lock);
    return mode;
}

// 解析实例级真实函数指针。加载器返回的桩按传入句柄分派，对任意 VkInstance /
// VkPhysicalDevice 均有效，故除 vkCreateInstance 外还可由任一包装入口惰性触发
// （应用可能经 gipa(NULL, "vkCreateInstance") 拿到真加载器的创建函数，绕过本 shim）
static void ensure_instance_procs(const void* dispatchable) {
    if (dispatchable == NULL || real.create_device != NULL) return;
    pthread_mutex_lock(&g_lock);
    if (real.create_device == NULL) {
        real.enumerate_physical_devices = (PFN_vkEnumeratePhysicalDevices) real_get_instance_proc_addr(
                (VkInstance) dispatchable, "vkEnumeratePhysicalDevices");
        real.enumerate_device_extension_properties = (PFN_vkEnumerateDeviceExtensionProperties)
                real_get_instance_proc_addr((VkInstance) dispatchable, "vkEnumerateDeviceExtensionProperties");
        real.get_physical_device_features = (PFN_vkGetPhysicalDeviceFeatures) real_get_instance_proc_addr(
                (VkInstance) dispatchable, "vkGetPhysicalDeviceFeatures");
        real.get_physical_device_features2 = (PFN_vkGetPhysicalDeviceFeatures2) real_get_instance_proc_addr(
                (VkInstance) dispatchable, "vkGetPhysicalDeviceFeatures2");
        real.get_physical_device_properties = (PFN_vkGetPhysicalDeviceProperties) real_get_instance_proc_addr(
                (VkInstance) dispatchable, "vkGetPhysicalDeviceProperties");
        real.get_physical_device_properties2 = (PFN_vkGetPhysicalDeviceProperties2) real_get_instance_proc_addr(
                (VkInstance) dispatchable, "vkGetPhysicalDeviceProperties2");
        real.get_physical_device_properties2_khr = (PFN_vkGetPhysicalDeviceProperties2KHR)
                real_get_instance_proc_addr((VkInstance) dispatchable, "vkGetPhysicalDeviceProperties2KHR");
        real.create_device = (PFN_vkCreateDevice) real_get_instance_proc_addr((VkInstance) dispatchable,
                                                                              "vkCreateDevice");
        // device 级 trampoline，按后续传入的 device 分派，全局解析一次即可
        real.create_graphics_pipelines = (PFN_vkCreateGraphicsPipelines) real_get_instance_proc_addr(
                (VkInstance) dispatchable, "vkCreateGraphicsPipelines");
    }
    pthread_mutex_unlock(&g_lock);
}

static void __attribute__((constructor)) vkshim_init(void) {
    // 测试与诊断用：允许指定被包装的加载器路径
    const char* overridePath = getenv("VKSHIM_LOADER_PATH");
    const char* path = (overridePath != NULL && overridePath[0] != '\0') ? overridePath
                                                                         : SYSTEM_VULKAN_LOADER_PATH;
    g_loader = dlopen(path, RTLD_LAZY | RTLD_LOCAL);
    if (g_loader == NULL && overridePath == NULL) {
        g_loader = dlopen("libvulkan.so", RTLD_LAZY | RTLD_LOCAL);
    }
    if (g_loader == NULL) {
        FCL_LOG("vkshim: failed to load vulkan loader: %s", dlerror());
        return;
    }

    real_create_instance = (PFN_vkCreateInstance) dlsym(g_loader, "vkCreateInstance");
    real_destroy_instance = (PFN_vkDestroyInstance) dlsym(g_loader, "vkDestroyInstance");
    real_enumerate_instance_extension_properties =
            (PFN_vkEnumerateInstanceExtensionProperties) dlsym(g_loader, "vkEnumerateInstanceExtensionProperties");
    real_enumerate_instance_layer_properties =
            (PFN_vkEnumerateInstanceLayerProperties) dlsym(g_loader, "vkEnumerateInstanceLayerProperties");
    real_enumerate_instance_version = (PFN_vkEnumerateInstanceVersion) dlsym(g_loader, "vkEnumerateInstanceVersion");
    real_get_instance_proc_addr = (PFN_vkGetInstanceProcAddr) dlsym(g_loader, "vkGetInstanceProcAddr");
    real_get_device_proc_addr = (PFN_vkGetDeviceProcAddr) dlsym(g_loader, "vkGetDeviceProcAddr");

    g_ready = real_create_instance != NULL && real_get_instance_proc_addr != NULL &&
              real_enumerate_instance_extension_properties != NULL;
    FCL_LOG("vkshim: wrapping %s, ready=%d", path, g_ready);
}

static VkResult shim_enumerate_device_extension_properties(VkPhysicalDevice physicalDevice, const char* pLayerName,
                                                           uint32_t* pPropertyCount,
                                                           VkExtensionProperties* pProperties) {
    ensure_instance_procs(physicalDevice);
    if (pLayerName != NULL) {
        return real.enumerate_device_extension_properties(physicalDevice, pLayerName, pPropertyCount, pProperties);
    }
    if (real.enumerate_device_extension_properties == NULL) return VK_ERROR_INITIALIZATION_FAILED;

    struct driver_mode mode = driver_mode_of(physicalDevice);
    if (!mode.translate) {
        return real.enumerate_device_extension_properties(physicalDevice, NULL, pPropertyCount, pProperties);
    }

    uint32_t realCount = 0;
    VkResult result = real.enumerate_device_extension_properties(physicalDevice, NULL, &realCount, NULL);
    if (result != VK_SUCCESS && result != VK_INCOMPLETE) return result;

    VkExtensionProperties* realList = NULL;
    if (realCount > 0) {
        realList = malloc(realCount * sizeof(VkExtensionProperties));
        if (realList == NULL) return VK_ERROR_OUT_OF_HOST_MEMORY;
        result = real.enumerate_device_extension_properties(physicalDevice, NULL, &realCount, realList);
        if (result != VK_SUCCESS && result != VK_INCOMPLETE) {
            free(realList);
            return result;
        }
    }

    bool native = extension_list_contains(realList, realCount, VK_EXT_VERTEX_ATTRIBUTE_DIVISOR_EXTENSION_NAME);
    uint32_t total = native ? realCount : realCount + 1;

    if (pProperties == NULL) {
        *pPropertyCount = total;
        free(realList);
        return VK_SUCCESS;
    }

    uint32_t limit = *pPropertyCount < total ? *pPropertyCount : total;
    uint32_t fromReal = limit < realCount ? limit : realCount;
    if (fromReal > 0) memcpy(pProperties, realList, fromReal * sizeof(VkExtensionProperties));
    if (!native && limit > fromReal) {
        VkExtensionProperties* entry = &pProperties[fromReal];
        strcpy(entry->extensionName, VK_EXT_VERTEX_ATTRIBUTE_DIVISOR_EXTENSION_NAME);
        entry->specVersion = EXT_SPEC_VERSION;
    }
    *pPropertyCount = limit;
    free(realList);
    return limit < total ? VK_INCOMPLETE : VK_SUCCESS;
}

static void translate_properties2(VkPhysicalDevice physicalDevice, VkPhysicalDeviceProperties2* pProperties,
                                  PFN_vkGetPhysicalDeviceProperties2 realFn) {
    ensure_instance_procs(physicalDevice);
    struct driver_mode mode = driver_mode_of(physicalDevice);
    if (!mode.translate || pProperties == NULL) {
        realFn(physicalDevice, pProperties);
        return;
    }

    // EXT properties 结构比 KHR 少一个 supportsNonZeroFirstInstance 字段，
    // 不能原地改 sType 让驱动直写，须用自有 KHR 存储承接后回填
    void** link = &pProperties->pNext;
    VkPhysicalDeviceVertexAttributeDivisorPropertiesEXT* ext = NULL;
    while (*link != NULL) {
        struct chain_node* node = *link;
        if (node->sType == VK_STRUCTURE_TYPE_PHYSICAL_DEVICE_VERTEX_ATTRIBUTE_DIVISOR_PROPERTIES_EXT) {
            ext = (VkPhysicalDeviceVertexAttributeDivisorPropertiesEXT*) node;
            break;
        }
        link = &node->pNext;
    }
    if (ext == NULL) {
        realFn(physicalDevice, pProperties);
        return;
    }

    VkPhysicalDeviceVertexAttributeDivisorPropertiesKHR khr;
    khr.sType = VK_STRUCTURE_TYPE_PHYSICAL_DEVICE_VERTEX_ATTRIBUTE_DIVISOR_PROPERTIES_KHR;
    khr.pNext = ext->pNext;
    khr.maxVertexAttribDivisor = 0;
    khr.supportsNonZeroFirstInstance = VK_FALSE;

    *link = &khr;
    realFn(physicalDevice, pProperties);
    ext->maxVertexAttribDivisor = khr.maxVertexAttribDivisor;
    *link = ext;
}

// 登记 fillModeNonSolid 模拟的逻辑设备；表满时不再跟踪（该设备管线不降级，极端边界）
static void register_device_fns_emulation(VkDevice device, bool fnsEmulated) {
    if (!fnsEmulated) return;
    pthread_mutex_lock(&g_lock);
    bool registered = false;
    for (int i = 0; i < g_tracked_device_count; i++) {
        if (g_tracked_devices[i] == device) registered = true;
    }
    if (!registered && g_tracked_device_count < MAX_TRACKED_DEVICES) {
        g_tracked_devices[g_tracked_device_count] = device;
        g_device_fns_emulated[g_tracked_device_count] = true;
        g_tracked_device_count++;
    }
    pthread_mutex_unlock(&g_lock);
}

static bool device_fns_emulated(VkDevice device) {
    pthread_mutex_lock(&g_lock);
    for (int i = 0; i < g_tracked_device_count; i++) {
        if (g_tracked_devices[i] == device) {
            pthread_mutex_unlock(&g_lock);
            return true;
        }
    }
    pthread_mutex_unlock(&g_lock);
    return false;
}

static VkResult shim_create_device(VkPhysicalDevice physicalDevice, const VkDeviceCreateInfo* pCreateInfo,
                                   const VkAllocationCallbacks* pAllocator, VkDevice* pDevice) {
    ensure_instance_procs(physicalDevice);
    struct driver_mode mode = driver_mode_of(physicalDevice);
    if (!mode.translate && !mode.fns_emulated) {
        return real.create_device(physicalDevice, pCreateInfo, pAllocator, pDevice);
    }

    bool extEnabled = false;
    bool khrEnabled = false;
    for (uint32_t i = 0; i < pCreateInfo->enabledExtensionCount; i++) {
        const char* name = pCreateInfo->ppEnabledExtensionNames[i];
        if (strcmp(name, VK_EXT_VERTEX_ATTRIBUTE_DIVISOR_EXTENSION_NAME) == 0) {
            extEnabled = true;
        } else if (strcmp(name, VK_KHR_VERTEX_ATTRIBUTE_DIVISOR_EXTENSION_NAME) == 0) {
            khrEnabled = true;
        }
    }

    // fillModeNonSolid 模拟：应用因谎报而在启用列表中带上该能力位，真驱动会拒绝
    // 设备创建，须以副本剥除（pEnabledFeatures 可能与 pNext 链内 Features2 别名，
    // 统一改为指向自有副本）；pNext 链内的 Features2 原位改写，调用后恢复原值
    VkPhysicalDeviceFeatures patchedFeatures;
    bool featuresCopied = false;
    VkPhysicalDeviceFeatures2* chainedFeatures = NULL;
    VkBool32 chainedOriginalFillMode = VK_FALSE;
    if (mode.fns_emulated) {
        void* curr = (void*) pCreateInfo->pNext;
        while (curr != NULL) {
            VkBaseOutStructure* node = (VkBaseOutStructure*) curr;
            if (node->sType == VK_STRUCTURE_TYPE_PHYSICAL_DEVICE_FEATURES_2) {
                chainedFeatures = (VkPhysicalDeviceFeatures2*) node;
                break;
            }
            curr = node->pNext;
        }
        if (chainedFeatures != NULL && chainedFeatures->features.fillModeNonSolid == VK_TRUE) {
            chainedOriginalFillMode = VK_TRUE;
            chainedFeatures->features.fillModeNonSolid = VK_FALSE;
        }
        if (pCreateInfo->pEnabledFeatures != NULL && pCreateInfo->pEnabledFeatures->fillModeNonSolid == VK_TRUE &&
            pCreateInfo->pEnabledFeatures != (chainedFeatures != NULL ? &chainedFeatures->features : NULL)) {
            patchedFeatures = *pCreateInfo->pEnabledFeatures;
            patchedFeatures.fillModeNonSolid = VK_FALSE;
            featuresCopied = true;
        }
    }

    VkResult result;
    if (mode.translate && extEnabled) {
        const char** names = malloc((pCreateInfo->enabledExtensionCount + 1) * sizeof(char*));
        if (names == NULL) return VK_ERROR_OUT_OF_HOST_MEMORY;
        uint32_t count = 0;
        for (uint32_t i = 0; i < pCreateInfo->enabledExtensionCount; i++) {
            const char* name = pCreateInfo->ppEnabledExtensionNames[i];
            if (strcmp(name, VK_EXT_VERTEX_ATTRIBUTE_DIVISOR_EXTENSION_NAME) != 0) names[count++] = name;
        }
        if (mode.add_khr && !khrEnabled) names[count++] = VK_KHR_VERTEX_ATTRIBUTE_DIVISOR_EXTENSION_NAME;

        VkDeviceCreateInfo patched = *pCreateInfo;
        if (featuresCopied) patched.pEnabledFeatures = &patchedFeatures;
        patched.ppEnabledExtensionNames = names;
        patched.enabledExtensionCount = count;

        // EXT v1/v2 语义下 divisor 无需显式启用，而 KHR/核心要求 features 结构置位；
        // 应用未提供时补写一个全开节点，用后摘除
        void** restore = NULL;
        VkPhysicalDeviceVertexAttributeDivisorFeaturesKHR injected = {
                VK_STRUCTURE_TYPE_PHYSICAL_DEVICE_VERTEX_ATTRIBUTE_DIVISOR_FEATURES_KHR,
                NULL,
                VK_TRUE,
                VK_TRUE,
        };
        if (!chain_contains(patched.pNext, VK_STRUCTURE_TYPE_PHYSICAL_DEVICE_VERTEX_ATTRIBUTE_DIVISOR_FEATURES_KHR)) {
            restore = chain_splice_tail((void**) &patched.pNext, &injected);
        }

        result = real.create_device(physicalDevice, &patched, pAllocator, pDevice);
        if (restore != NULL) *restore = NULL;
        free(names);
    } else {
        VkDeviceCreateInfo patched = *pCreateInfo;
        if (featuresCopied) patched.pEnabledFeatures = &patchedFeatures;
        result = real.create_device(physicalDevice, &patched, pAllocator, pDevice);
    }

    // pNext 链内的 Features2 是应用内存，调用结束后恢复原值
    if (mode.fns_emulated && chainedFeatures != NULL && chainedOriginalFillMode == VK_TRUE &&
        pCreateInfo->pEnabledFeatures != &chainedFeatures->features) {
        chainedFeatures->features.fillModeNonSolid = VK_TRUE;
    }

    if (result == VK_SUCCESS && pDevice != NULL) {
        register_device_fns_emulation(*pDevice, mode.fns_emulated);
    }
    return result;
}

VKAPI_ATTR VkResult VKAPI_CALL vkCreateInstance(const VkInstanceCreateInfo* pCreateInfo,
                                                const VkAllocationCallbacks* pAllocator, VkInstance* pInstance) {
    if (!g_ready) return VK_ERROR_INCOMPATIBLE_DRIVER;
    VkResult result = real_create_instance(pCreateInfo, pAllocator, pInstance);
    if (result == VK_SUCCESS && pInstance != NULL) ensure_instance_procs(*pInstance);
    // 自诊断：实例创建后立即经真实加载器枚举一次，区分"游戏进程内 ICD 加载失败"与
    // "应用的枚举调用未经过 shim"
    if (result == VK_SUCCESS && real.enumerate_physical_devices != NULL) {
        uint32_t count = 0;
        VkResult probe = real.enumerate_physical_devices(*pInstance, &count, NULL);
        FCL_LOG("vkshim: self-probe vkEnumeratePhysicalDevices=%d count=%u", probe, count);
        if (probe == VK_SUCCESS && count > 0 && real.get_physical_device_properties != NULL) {
            VkPhysicalDevice pd;
            count = 1;
            if (real.enumerate_physical_devices(*pInstance, &count, &pd) == VK_SUCCESS) {
                VkPhysicalDeviceProperties properties;
                real.get_physical_device_properties(pd, &properties);
                FCL_LOG("vkshim: driver %s (apiVersion=0x%x)", properties.deviceName,
                        properties.apiVersion);
            }
        }
    }
    return result;
}

VKAPI_ATTR void VKAPI_CALL vkDestroyInstance(VkInstance instance, const VkAllocationCallbacks* pAllocator) {
    if (real_destroy_instance != NULL) real_destroy_instance(instance, pAllocator);
}

VKAPI_ATTR VkResult VKAPI_CALL vkEnumerateInstanceExtensionProperties(const char* pLayerName, uint32_t* pPropertyCount,
                                                                      VkExtensionProperties* pProperties) {
    if (!g_ready) return VK_ERROR_INCOMPATIBLE_DRIVER;
    return real_enumerate_instance_extension_properties(pLayerName, pPropertyCount, pProperties);
}

VKAPI_ATTR VkResult VKAPI_CALL vkEnumerateInstanceLayerProperties(uint32_t* pPropertyCount,
                                                                  VkLayerProperties* pProperties) {
    if (!g_ready) return VK_ERROR_INCOMPATIBLE_DRIVER;
    return real_enumerate_instance_layer_properties(pPropertyCount, pProperties);
}

VKAPI_ATTR VkResult VKAPI_CALL vkEnumerateInstanceVersion(uint32_t* pApiVersion) {
    // 1.0 时代加载器未导出该函数，按其最高能力上报
    if (!g_ready || real_enumerate_instance_version == NULL) {
        if (pApiVersion != NULL) *pApiVersion = VK_API_VERSION_1_0;
        return VK_SUCCESS;
    }
    return real_enumerate_instance_version(pApiVersion);
}

VKAPI_ATTR PFN_vkVoidFunction VKAPI_CALL vkGetInstanceProcAddr(VkInstance instance, const char* pName) {
    if (!g_ready || pName == NULL) return NULL;
    // 全局命令一并返回包装导出：应用常经 gipa(NULL, ...) 而非 dlsym 获取它们
    if (strcmp(pName, "vkGetInstanceProcAddr") == 0) return (PFN_vkVoidFunction) &vkGetInstanceProcAddr;
    if (strcmp(pName, "vkGetDeviceProcAddr") == 0) return (PFN_vkVoidFunction) &vkGetDeviceProcAddr;
    if (strcmp(pName, "vkCreateInstance") == 0) return (PFN_vkVoidFunction) &vkCreateInstance;
    if (strcmp(pName, "vkEnumerateInstanceExtensionProperties") == 0) {
        return (PFN_vkVoidFunction) &vkEnumerateInstanceExtensionProperties;
    }
    if (strcmp(pName, "vkEnumerateInstanceLayerProperties") == 0) {
        return (PFN_vkVoidFunction) &vkEnumerateInstanceLayerProperties;
    }
    if (strcmp(pName, "vkEnumerateInstanceVersion") == 0) return (PFN_vkVoidFunction) &vkEnumerateInstanceVersion;
    if (instance != NULL) {
        if (strcmp(pName, "vkEnumeratePhysicalDevices") == 0) {
            return (PFN_vkVoidFunction) &shim_enumerate_physical_devices;
        } else if (strcmp(pName, "vkEnumerateDeviceExtensionProperties") == 0) {
            return (PFN_vkVoidFunction) &shim_enumerate_device_extension_properties;
        } else if (strcmp(pName, "vkGetPhysicalDeviceFeatures") == 0) {
            return (PFN_vkVoidFunction) &shim_get_physical_device_features;
        } else if (strcmp(pName, "vkGetPhysicalDeviceFeatures2") == 0) {
            return (PFN_vkVoidFunction) &shim_get_physical_device_features2;
        } else if (strcmp(pName, "vkGetPhysicalDeviceFeatures2KHR") == 0) {
            return (PFN_vkVoidFunction) &shim_get_physical_device_features2_khr;
        } else if (strcmp(pName, "vkGetPhysicalDeviceProperties2") == 0) {
            return (PFN_vkVoidFunction) &shim_get_physical_device_properties2;
        } else if (strcmp(pName, "vkGetPhysicalDeviceProperties2KHR") == 0) {
            return (PFN_vkVoidFunction) &shim_get_physical_device_properties2_khr;
        } else if (strcmp(pName, "vkCreateDevice") == 0) {
            return (PFN_vkVoidFunction) &shim_create_device;
        } else if (strcmp(pName, "vkCreateGraphicsPipelines") == 0) {
            return (PFN_vkVoidFunction) &shim_create_graphics_pipelines;
        }
    }
    return real_get_instance_proc_addr(instance, pName);
}

VKAPI_ATTR PFN_vkVoidFunction VKAPI_CALL vkGetDeviceProcAddr(VkDevice device, const char* pName) {
    if (!g_ready || device == NULL || pName == NULL) return NULL;
    if (strcmp(pName, "vkCreateGraphicsPipelines") == 0) {
        return (PFN_vkVoidFunction) &shim_create_graphics_pipelines;
    }
    return real_get_device_proc_addr(device, pName);
}

static VkResult shim_enumerate_physical_devices(VkInstance instance, uint32_t* pPhysicalDeviceCount,
                                                VkPhysicalDevice* pPhysicalDevices) {
    ensure_instance_procs(instance);
    if (real.enumerate_physical_devices == NULL) {
        FCL_LOG("vkshim: vkEnumeratePhysicalDevices dropped, real proc unresolved");
        return VK_ERROR_INITIALIZATION_FAILED;
    }
    VkResult result = real.enumerate_physical_devices(instance, pPhysicalDeviceCount, pPhysicalDevices);
    FCL_LOG("vkshim: vkEnumeratePhysicalDevices -> %d count=%u", result,
            (result == VK_SUCCESS || result == VK_INCOMPLETE) && pPhysicalDeviceCount != NULL
                    ? *pPhysicalDeviceCount : 0);
    if ((result == VK_SUCCESS || result == VK_INCOMPLETE) && pPhysicalDevices != NULL) {
        for (uint32_t i = 0; i < *pPhysicalDeviceCount; i++) {
            driver_mode_of(pPhysicalDevices[i]);
        }
    }
    return result;
}

static void shim_get_physical_device_properties2(VkPhysicalDevice physicalDevice,
                                                 VkPhysicalDeviceProperties2* pProperties) {
    if (real.get_physical_device_properties2 == NULL) return;
    translate_properties2(physicalDevice, pProperties, real.get_physical_device_properties2);
}

static void shim_get_physical_device_properties2_khr(VkPhysicalDevice physicalDevice,
                                                     VkPhysicalDeviceProperties2* pProperties) {
    if (real.get_physical_device_properties2_khr == NULL) return;
    translate_properties2(physicalDevice, pProperties,
                          (PFN_vkGetPhysicalDeviceProperties2) real.get_physical_device_properties2_khr);
}

static void shim_get_physical_device_features(VkPhysicalDevice physicalDevice, VkPhysicalDeviceFeatures* pFeatures) {
    if (real.get_physical_device_features == NULL || pFeatures == NULL) return;
    real.get_physical_device_features(physicalDevice, pFeatures);
    if (driver_mode_of(physicalDevice).fns_emulated) {
        pFeatures->fillModeNonSolid = VK_TRUE;
    }
}

static void shim_get_physical_device_features2(VkPhysicalDevice physicalDevice, VkPhysicalDeviceFeatures2* pFeatures) {
    if (real.get_physical_device_features2 == NULL || pFeatures == NULL) return;
    real.get_physical_device_features2(physicalDevice, pFeatures);
    if (driver_mode_of(physicalDevice).fns_emulated) {
        pFeatures->features.fillModeNonSolid = VK_TRUE;
    }
}

static void shim_get_physical_device_features2_khr(VkPhysicalDevice physicalDevice,
                                                   VkPhysicalDeviceFeatures2* pFeatures) {
    if (real.get_physical_device_features2 == NULL || pFeatures == NULL) return;
    real.get_physical_device_features2(physicalDevice, pFeatures);
    if (driver_mode_of(physicalDevice).fns_emulated) {
        pFeatures->features.fillModeNonSolid = VK_TRUE;
    }
}

// fillModeNonSolid 模拟的降级路径：线框/点填充管线统一改回 FILL，
// 牺牲线框视觉换取可运行性；只对模拟设备生效，原生支持的设备早已直通
static VkResult shim_create_graphics_pipelines(VkDevice device, VkPipelineCache pipelineCache, uint32_t createInfoCount,
                                               const VkGraphicsPipelineCreateInfo* pCreateInfos,
                                               const VkAllocationCallbacks* pAllocator, VkPipeline* pPipelines) {
    if (real.create_graphics_pipelines == NULL) return VK_ERROR_INITIALIZATION_FAILED;
    if (!device_fns_emulated(device) || pCreateInfos == NULL || createInfoCount == 0) {
        return real.create_graphics_pipelines(device, pipelineCache, createInfoCount, pCreateInfos, pAllocator,
                                              pPipelines);
    }

    // 仅在存在非 FILL 光栅化状态时才复制改写；pNext 浅拷贝在调用期间有效
    VkGraphicsPipelineCreateInfo* patched = NULL;
    VkPipelineRasterizationStateCreateInfo** replaced = NULL;
    uint32_t replacedCount = 0;
    for (uint32_t i = 0; i < createInfoCount; i++) {
        const VkPipelineRasterizationStateCreateInfo* raster = pCreateInfos[i].pRasterizationState;
        if (raster != NULL && raster->polygonMode != VK_POLYGON_MODE_FILL) replacedCount++;
    }
    if (replacedCount > 0) {
        patched = malloc(createInfoCount * sizeof(VkGraphicsPipelineCreateInfo));
        replaced = malloc(replacedCount * sizeof(VkPipelineRasterizationStateCreateInfo*));
        if (patched == NULL || replaced == NULL) {
            free(patched);
            free(replaced);
            return VK_ERROR_OUT_OF_HOST_MEMORY;
        }
        memcpy(patched, pCreateInfos, createInfoCount * sizeof(VkGraphicsPipelineCreateInfo));
        uint32_t idx = 0;
        for (uint32_t i = 0; i < createInfoCount; i++) {
            const VkPipelineRasterizationStateCreateInfo* raster = patched[i].pRasterizationState;
            if (raster != NULL && raster->polygonMode != VK_POLYGON_MODE_FILL) {
                VkPipelineRasterizationStateCreateInfo* mod =
                        malloc(sizeof(VkPipelineRasterizationStateCreateInfo));
                if (mod == NULL) continue;
                *mod = *raster;
                mod->polygonMode = VK_POLYGON_MODE_FILL;
                replaced[idx++] = mod;
                patched[i].pRasterizationState = mod;
            }
        }
        replacedCount = idx;
    }

    VkResult result = real.create_graphics_pipelines(device, pipelineCache, createInfoCount,
                                                     patched != NULL ? patched : pCreateInfos, pAllocator,
                                                     pPipelines);
    if (patched != NULL) {
        for (uint32_t i = 0; i < replacedCount; i++) free(replaced[i]);
        free(replaced);
        free(patched);
    }
    return result;
}
