//
// Created by mio on 2024/8/22.
//
#include <vulkan/vulkan.h>
#include <dlfcn.h>
#include "utils.h"
#include "fcl_internal.h"

void printVulkanInfo(void *handle) {
    PFN_vkCreateInstance vkCreateInstance;
    PFN_vkDestroyInstance vkDestroyInstance;
    PFN_vkEnumeratePhysicalDevices vkEnumeratePhysicalDevices;
    PFN_vkGetPhysicalDeviceFeatures vkGetPhysicalDeviceFeatures;
    PFN_vkGetPhysicalDeviceFeatures vkGetPhysicalDeviceProperties;
    vkCreateInstance = dlsym(handle, "vkCreateInstance");
    vkDestroyInstance = dlsym(handle, "vkDestroyInstance");
    vkEnumeratePhysicalDevices = dlsym(handle, "vkEnumeratePhysicalDevices");
    vkGetPhysicalDeviceFeatures = dlsym(handle, "vkGetPhysicalDeviceFeatures");
    vkGetPhysicalDeviceProperties = dlsym(handle, "vkGetPhysicalDeviceProperties");
    VkInstanceCreateInfo instanceCreateInfo = {
            .sType = VK_STRUCTURE_TYPE_APPLICATION_INFO
    };
    VkInstance instance/* = malloc(sizeof(VkInstance))*/;
    if (vkCreateInstance(&instanceCreateInfo, 0, &instance) != 0) {
        FCL_INTERNAL_LOG("vkCreateInstance error");
        return;
    }
    uint32_t gpu_count;
    if (vkEnumeratePhysicalDevices(instance, &gpu_count, NULL) != 0) {
        FCL_INTERNAL_LOG("vkEnumeratePhysicalDevices error");
        return;
    }
    VkPhysicalDevice *gpus = (VkPhysicalDevice *) malloc(sizeof(VkPhysicalDevice) * gpu_count);
    if (vkEnumeratePhysicalDevices(instance, &gpu_count, gpus) != 0) {
        FCL_INTERNAL_LOG("vkEnumeratePhysicalDevices error");
        return;
    }
    VkPhysicalDevice gpu = gpus[0];
    VkPhysicalDeviceProperties gpuProperties;
    vkGetPhysicalDeviceProperties(gpu, &gpuProperties);
    FCL_INTERNAL_LOG("Vulkan api version:%d.%d.%d",
                     VK_VERSION_MAJOR(gpuProperties.apiVersion),
                     VK_VERSION_MINOR(gpuProperties.apiVersion),
                     VK_VERSION_PATCH(gpuProperties.apiVersion));
    vkDestroyInstance(instance, 0);
}
