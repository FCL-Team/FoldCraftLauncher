// SPDX-License-Identifier: GPL-3.0-or-later
/* Off-screen Qualcomm HAL diagnostic. No app files/settings are modified.
 * Run only after saving/closing Minecraft; a driver defect can reset the GPU.
 * Usage: depth-probe vertex.spv fragment.spv [width height rounds]
 */
#define _GNU_SOURCE
#define VK_NO_PROTOTYPES
#include <dlfcn.h>
#ifdef __ANDROID__
#include <hardware/hwvulkan.h>
#endif
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <vulkan/vulkan.h>

#define CHECK(c) do { VkResult r_ = (c); if (r_ != VK_SUCCESS) { \
    fprintf(stderr,"FAIL %s: VkResult %d\n",#c,r_); fflush(NULL); _Exit(2); } } while(0)
#define FUNCTIONS(X) \
 X(GetDeviceQueue) X(CreateImage) X(GetImageMemoryRequirements) \
 X(AllocateMemory) X(BindImageMemory) X(CreateImageView) X(CreateBuffer) \
 X(GetBufferMemoryRequirements) X(BindBufferMemory) X(MapMemory) \
 X(CreateRenderPass2) X(CreateFramebuffer) X(CreateShaderModule) \
 X(CreatePipelineLayout) X(CreateGraphicsPipelines) X(CreateCommandPool) \
 X(AllocateCommandBuffers) X(BeginCommandBuffer) X(EndCommandBuffer) \
 X(ResetCommandBuffer) X(CmdPipelineBarrier2) X(CmdClearDepthStencilImage) \
 X(CmdBeginRenderPass2) X(CmdEndRenderPass2) X(CmdBindPipeline) \
 X(CmdPushConstants) X(CmdDraw) X(CmdCopyImageToBuffer) \
 X(CreateFence) X(WaitForFences) X(ResetFences) X(QueueSubmit2)
#define DECL(n) static PFN_vk##n vk##n;
FUNCTIONS(DECL)
static VkDevice device;
static VkPhysicalDeviceMemoryProperties memory_props;
static uint32_t width, height;

static uint32_t memory_type(uint32_t bits, VkMemoryPropertyFlags flags) {
    for (uint32_t i=0;i<memory_props.memoryTypeCount;i++)
        if ((bits & (1u<<i)) && (memory_props.memoryTypes[i].propertyFlags & flags)==flags) return i;
    fprintf(stderr,"No memory type for bits %x flags %x\n",bits,flags); _Exit(2);
}
static VkDeviceMemory alloc(VkMemoryRequirements req, VkMemoryPropertyFlags flags) {
    VkMemoryAllocateInfo a={.sType=VK_STRUCTURE_TYPE_MEMORY_ALLOCATE_INFO,
        .allocationSize=req.size,.memoryTypeIndex=memory_type(req.memoryTypeBits,flags)};
    VkDeviceMemory memory; CHECK(vkAllocateMemory(device,&a,NULL,&memory)); return memory;
}
typedef struct { VkImage image; VkImageView view; VkDeviceMemory memory; VkImageAspectFlags aspect; } Image;
static Image image(VkFormat format, VkImageUsageFlags usage, VkImageAspectFlags aspect) {
    Image im={.aspect=aspect};
    VkImageCreateInfo ci={.sType=VK_STRUCTURE_TYPE_IMAGE_CREATE_INFO,.imageType=VK_IMAGE_TYPE_2D,
        .format=format,.extent={width,height,1},.mipLevels=1,.arrayLayers=1,
        .samples=VK_SAMPLE_COUNT_1_BIT,.tiling=VK_IMAGE_TILING_OPTIMAL,.usage=usage,
        .sharingMode=VK_SHARING_MODE_EXCLUSIVE,.initialLayout=VK_IMAGE_LAYOUT_UNDEFINED};
    CHECK(vkCreateImage(device,&ci,NULL,&im.image));
    VkMemoryRequirements req; vkGetImageMemoryRequirements(device,im.image,&req);
    im.memory=alloc(req,VK_MEMORY_PROPERTY_DEVICE_LOCAL_BIT);
    CHECK(vkBindImageMemory(device,im.image,im.memory,0));
    VkImageViewCreateInfo vi={.sType=VK_STRUCTURE_TYPE_IMAGE_VIEW_CREATE_INFO,.image=im.image,
        .viewType=VK_IMAGE_VIEW_TYPE_2D,.format=format,.subresourceRange={aspect,0,1,0,1}};
    CHECK(vkCreateImageView(device,&vi,NULL,&im.view)); return im;
}
static VkRenderPass pass(VkAttachmentLoadOp color_load, VkAttachmentLoadOp depth_load) {
    VkAttachmentDescription2 at[2]={
        {.sType=VK_STRUCTURE_TYPE_ATTACHMENT_DESCRIPTION_2,.format=VK_FORMAT_R8G8B8A8_UNORM,
         .samples=VK_SAMPLE_COUNT_1_BIT,.loadOp=color_load,.storeOp=VK_ATTACHMENT_STORE_OP_STORE,
         .stencilLoadOp=VK_ATTACHMENT_LOAD_OP_DONT_CARE,.stencilStoreOp=VK_ATTACHMENT_STORE_OP_DONT_CARE,
         .initialLayout=VK_IMAGE_LAYOUT_GENERAL,.finalLayout=VK_IMAGE_LAYOUT_GENERAL},
        {.sType=VK_STRUCTURE_TYPE_ATTACHMENT_DESCRIPTION_2,.format=VK_FORMAT_D32_SFLOAT,
         .samples=VK_SAMPLE_COUNT_1_BIT,.loadOp=depth_load,.storeOp=VK_ATTACHMENT_STORE_OP_STORE,
         .stencilLoadOp=VK_ATTACHMENT_LOAD_OP_DONT_CARE,.stencilStoreOp=VK_ATTACHMENT_STORE_OP_DONT_CARE,
         .initialLayout=VK_IMAGE_LAYOUT_GENERAL,.finalLayout=VK_IMAGE_LAYOUT_GENERAL}};
    VkAttachmentReference2 color={.sType=VK_STRUCTURE_TYPE_ATTACHMENT_REFERENCE_2,.attachment=0,
        .layout=VK_IMAGE_LAYOUT_GENERAL,.aspectMask=VK_IMAGE_ASPECT_COLOR_BIT};
    VkAttachmentReference2 depth={.sType=VK_STRUCTURE_TYPE_ATTACHMENT_REFERENCE_2,.attachment=1,
        .layout=VK_IMAGE_LAYOUT_GENERAL,.aspectMask=VK_IMAGE_ASPECT_DEPTH_BIT};
    VkSubpassDescription2 sub={.sType=VK_STRUCTURE_TYPE_SUBPASS_DESCRIPTION_2,
        .pipelineBindPoint=VK_PIPELINE_BIND_POINT_GRAPHICS,.colorAttachmentCount=1,
        .pColorAttachments=&color,.pDepthStencilAttachment=&depth};
    VkRenderPassCreateInfo2 ci={.sType=VK_STRUCTURE_TYPE_RENDER_PASS_CREATE_INFO_2,
        .attachmentCount=2,.pAttachments=at,.subpassCount=1,.pSubpasses=&sub};
    VkRenderPass rp; CHECK(vkCreateRenderPass2(device,&ci,NULL,&rp)); return rp;
}
static VkShaderModule shader(const char *path) {
    FILE *f=fopen(path,"rb"); if(!f) { perror(path); _Exit(2); }
    if(fseek(f,0,SEEK_END)) _Exit(2);
    long size=ftell(f); if(size<=0 || size%4 || fseek(f,0,SEEK_SET)) _Exit(2);
    uint32_t *data=malloc((size_t)size); if(!data || fread(data,1,(size_t)size,f)!=(size_t)size) _Exit(2);
    fclose(f);
    VkShaderModuleCreateInfo ci={.sType=VK_STRUCTURE_TYPE_SHADER_MODULE_CREATE_INFO,.codeSize=(size_t)size,.pCode=data};
    VkShaderModule module; CHECK(vkCreateShaderModule(device,&ci,NULL,&module)); free(data); return module;
}
static void barrier(VkCommandBuffer cmd) {
    VkMemoryBarrier2 b={.sType=VK_STRUCTURE_TYPE_MEMORY_BARRIER_2,
        .srcStageMask=VK_PIPELINE_STAGE_2_ALL_COMMANDS_BIT,
        .srcAccessMask=VK_ACCESS_2_MEMORY_READ_BIT|VK_ACCESS_2_MEMORY_WRITE_BIT,
        .dstStageMask=VK_PIPELINE_STAGE_2_ALL_COMMANDS_BIT,
        .dstAccessMask=VK_ACCESS_2_MEMORY_READ_BIT|VK_ACCESS_2_MEMORY_WRITE_BIT};
    VkDependencyInfo dep={.sType=VK_STRUCTURE_TYPE_DEPENDENCY_INFO,.memoryBarrierCount=1,.pMemoryBarriers=&b};
    vkCmdPipelineBarrier2(cmd,&dep);
}
static void draw(VkCommandBuffer cmd,VkRenderPass rp,VkFramebuffer fb,VkPipeline pipeline,
                 VkPipelineLayout layout,int world) {
    VkClearValue clears[2]={ {.color={{0,0,0,1}}}, {.depthStencil={0,0}} };
    VkRenderPassBeginInfo begin={.sType=VK_STRUCTURE_TYPE_RENDER_PASS_BEGIN_INFO,.renderPass=rp,
        .framebuffer=fb,.renderArea={{0,0},{width,height}},.clearValueCount=2,.pClearValues=clears};
    VkSubpassBeginInfo sub={.sType=VK_STRUCTURE_TYPE_SUBPASS_BEGIN_INFO,.contents=VK_SUBPASS_CONTENTS_INLINE};
    VkSubpassEndInfo end={.sType=VK_STRUCTURE_TYPE_SUBPASS_END_INFO};
    vkCmdBeginRenderPass2(cmd,&begin,&sub);
    vkCmdBindPipeline(cmd,VK_PIPELINE_BIND_POINT_GRAPHICS,pipeline);
    float params[4]={world ? .7f : .5f,world ? .05f : 0.f,world ? 1.f : 0.f,0.f};
    vkCmdPushConstants(cmd,layout,VK_SHADER_STAGE_VERTEX_BIT|VK_SHADER_STAGE_FRAGMENT_BIT,0,sizeof(params),params);
    vkCmdDraw(cmd,3,1,0,0);
    vkCmdEndRenderPass2(cmd,&end);
}
int main(int argc,char **argv) {
    setvbuf(stdout,NULL,_IONBF,0);
    if(argc!=3 && argc!=6) { fprintf(stderr,"Usage: %s vertex.spv fragment.spv [width height rounds]\n",argv[0]); return 2; }
    width=argc==6 ? (uint32_t)atoi(argv[3]):512;
    height=argc==6 ? (uint32_t)atoi(argv[4]):256;
    int rounds=argc==6 ? atoi(argv[5]):2;
    if(!width || !height || width>4096 || height>4096 || rounds<1 || rounds>32) return 2;
#ifdef __ANDROID__
    void *hal_lib=dlopen("/vendor/lib64/hw/vulkan.adreno.so",RTLD_NOW|RTLD_LOCAL);
    if(!hal_lib) { puts(dlerror()); return 2; }
    hwvulkan_module_t *module=dlsym(hal_lib,"HMI"); hw_device_t *hal=NULL;
    if(!module || module->common.methods->open(&module->common,HWVULKAN_DEVICE_0,&hal)) return 2;
    PFN_vkGetInstanceProcAddr gipa=((hwvulkan_device_t*)hal)->GetInstanceProcAddr;
#else
    void *hal_lib=dlopen("libvulkan.so.1",RTLD_NOW|RTLD_LOCAL);
    if(!hal_lib) { puts(dlerror()); return 2; }
    PFN_vkGetInstanceProcAddr gipa=dlsym(hal_lib,"vkGetInstanceProcAddr");
    if(!gipa) return 2;
#endif
    PFN_vkCreateInstance create=(void*)gipa(NULL,"vkCreateInstance");
    VkApplicationInfo app={.sType=VK_STRUCTURE_TYPE_APPLICATION_INFO,.pApplicationName="FCL depth diagnostic",.apiVersion=VK_API_VERSION_1_3};
    VkInstanceCreateInfo ici={.sType=VK_STRUCTURE_TYPE_INSTANCE_CREATE_INFO,.pApplicationInfo=&app};
    VkInstance instance; CHECK(create(&ici,NULL,&instance));
#define IP(n) PFN_vk##n vk##n=(void*)gipa(instance,"vk"#n)
    IP(EnumeratePhysicalDevices); IP(GetPhysicalDeviceProperties); IP(GetPhysicalDeviceMemoryProperties);
    IP(GetPhysicalDeviceQueueFamilyProperties); IP(GetPhysicalDeviceFormatProperties); IP(CreateDevice);
    uint32_t count=1; VkPhysicalDevice physical; CHECK(vkEnumeratePhysicalDevices(instance,&count,&physical));
    VkPhysicalDeviceProperties props; vkGetPhysicalDeviceProperties(physical,&props);
    printf("Device %s API %u.%u.%u driver=0x%x; test=%ux%u rounds=%d\n",props.deviceName,
        VK_VERSION_MAJOR(props.apiVersion),VK_VERSION_MINOR(props.apiVersion),VK_VERSION_PATCH(props.apiVersion),props.driverVersion,width,height,rounds);
    vkGetPhysicalDeviceMemoryProperties(physical,&memory_props);
    VkFormatProperties fp; vkGetPhysicalDeviceFormatProperties(physical,VK_FORMAT_D32_SFLOAT,&fp);
    VkFormatFeatureFlags needed=VK_FORMAT_FEATURE_DEPTH_STENCIL_ATTACHMENT_BIT|VK_FORMAT_FEATURE_TRANSFER_SRC_BIT|VK_FORMAT_FEATURE_TRANSFER_DST_BIT;
    if((fp.optimalTilingFeatures & needed)!=needed) { fprintf(stderr,"D32 required features unavailable\n"); return 2; }
    vkGetPhysicalDeviceQueueFamilyProperties(physical,&count,NULL);
    VkQueueFamilyProperties *families=calloc(count,sizeof(*families));
    vkGetPhysicalDeviceQueueFamilyProperties(physical,&count,families);
    uint32_t family; for(family=0;family<count;family++) if(families[family].queueCount && (families[family].queueFlags&VK_QUEUE_GRAPHICS_BIT)) break;
    if(family==count) return 2;
    free(families);
    float priority=1;
    VkDeviceQueueCreateInfo qci={.sType=VK_STRUCTURE_TYPE_DEVICE_QUEUE_CREATE_INFO,.queueFamilyIndex=family,.queueCount=1,.pQueuePriorities=&priority};
    VkPhysicalDeviceSynchronization2Features sync={.sType=VK_STRUCTURE_TYPE_PHYSICAL_DEVICE_SYNCHRONIZATION_2_FEATURES,.synchronization2=VK_TRUE};
    VkDeviceCreateInfo dci={.sType=VK_STRUCTURE_TYPE_DEVICE_CREATE_INFO,.pNext=&sync,.queueCreateInfoCount=1,.pQueueCreateInfos=&qci};
    CHECK(vkCreateDevice(physical,&dci,NULL,&device));
    PFN_vkGetDeviceProcAddr gdpa=(void*)gipa(instance,"vkGetDeviceProcAddr");
#define LOAD(n) vk##n=(void*)gdpa(device,"vk"#n); if(!vk##n) { fprintf(stderr,"Missing vk%s\n",#n); return 2; } \
    { Dl_info a_; if(dladdr((void*)vk##n,&a_)) printf("ENTRY vk%s %s +0x%lx\n",#n,a_.dli_fname,(unsigned long)((char*)(void*)vk##n-(char*)a_.dli_fbase)); }
    FUNCTIONS(LOAD)
#ifdef __ANDROID__
    /* Bypass every remaining profiler-wrapped entry used by this probe,
     * including the previously verified duplicate submission. Resolve existing
     * exports only; never patch or alter the installed driver binary. */
#define CORE(n,s) vk##n=dlsym(hal_lib,s); if(!vk##n) { fprintf(stderr,"Missing Qualcomm core vk%s\n",#n); return 2; }
    CORE(GetDeviceQueue,"_ZN11qglinternal16vkGetDeviceQueueEP10VkDevice_TjjPP9VkQueue_T")
    CORE(CreateCommandPool,"_ZN11qglinternal19vkCreateCommandPoolEP10VkDevice_TPK23VkCommandPoolCreateInfoPK21VkAllocationCallbacksPP15VkCommandPool_T")
    CORE(AllocateCommandBuffers,"_ZN11qglinternal24vkAllocateCommandBuffersEP10VkDevice_TPK27VkCommandBufferAllocateInfoPP17VkCommandBuffer_T")
    CORE(BeginCommandBuffer,"_ZN11qglinternal20vkBeginCommandBufferEP17VkCommandBuffer_TPK24VkCommandBufferBeginInfo")
    CORE(EndCommandBuffer,"_ZN11qglinternal18vkEndCommandBufferEP17VkCommandBuffer_T")
    CORE(ResetCommandBuffer,"_ZN11qglinternal20vkResetCommandBufferEP17VkCommandBuffer_Tj")
    CORE(CmdClearDepthStencilImage,"_ZN11qglinternal27vkCmdClearDepthStencilImageEP17VkCommandBuffer_TP9VkImage_T13VkImageLayoutPK24VkClearDepthStencilValuejPK23VkImageSubresourceRange")
    CORE(CmdBindPipeline,"_ZN11qglinternal17vkCmdBindPipelineEP17VkCommandBuffer_T19VkPipelineBindPointP12VkPipeline_T")
    CORE(CmdDraw,"_ZN11qglinternal9vkCmdDrawEP17VkCommandBuffer_Tjjjj")
    CORE(QueueSubmit2,"_ZN11qglinternal14vkQueueSubmit2EP9VkQueue_TjPK13VkSubmitInfo2P9VkFence_T")
    puts("All probe device commands now resolve directly to Qualcomm HAL, not profiler wrappers");
#endif
    Dl_info address; if(dladdr((void*)vkQueueSubmit2,&address))
        printf("Submit core %s offset=0x%lx\n",address.dli_fname,(unsigned long)((char*)(void*)vkQueueSubmit2-(char*)address.dli_fbase));
    VkQueue queue; vkGetDeviceQueue(device,family,0,&queue);
    Image color=image(VK_FORMAT_R8G8B8A8_UNORM,VK_IMAGE_USAGE_COLOR_ATTACHMENT_BIT|VK_IMAGE_USAGE_TRANSFER_SRC_BIT,VK_IMAGE_ASPECT_COLOR_BIT);
    Image depth=image(VK_FORMAT_D32_SFLOAT,VK_IMAGE_USAGE_DEPTH_STENCIL_ATTACHMENT_BIT|VK_IMAGE_USAGE_TRANSFER_SRC_BIT|VK_IMAGE_USAGE_TRANSFER_DST_BIT,VK_IMAGE_ASPECT_DEPTH_BIT);
    VkDeviceSize pixels=(VkDeviceSize)width*height, bytes=pixels*4;
    VkBufferCreateInfo bci={.sType=VK_STRUCTURE_TYPE_BUFFER_CREATE_INFO,.size=bytes*2,.usage=VK_BUFFER_USAGE_TRANSFER_DST_BIT,.sharingMode=VK_SHARING_MODE_EXCLUSIVE};
    VkBuffer buffer; CHECK(vkCreateBuffer(device,&bci,NULL,&buffer));
    VkMemoryRequirements req; vkGetBufferMemoryRequirements(device,buffer,&req);
    VkDeviceMemory read_memory=alloc(req,VK_MEMORY_PROPERTY_HOST_VISIBLE_BIT|VK_MEMORY_PROPERTY_HOST_COHERENT_BIT);
    CHECK(vkBindBufferMemory(device,buffer,read_memory,0));
    uint8_t *read; CHECK(vkMapMemory(device,read_memory,0,VK_WHOLE_SIZE,0,(void**)&read));
    VkRenderPass world=pass(VK_ATTACHMENT_LOAD_OP_CLEAR,VK_ATTACHMENT_LOAD_OP_CLEAR);
    VkRenderPass load=pass(VK_ATTACHMENT_LOAD_OP_LOAD,VK_ATTACHMENT_LOAD_OP_LOAD);
    VkRenderPass clear=pass(VK_ATTACHMENT_LOAD_OP_LOAD,VK_ATTACHMENT_LOAD_OP_CLEAR);
    VkImageView views[2]={color.view,depth.view};
    VkFramebufferCreateInfo fci={.sType=VK_STRUCTURE_TYPE_FRAMEBUFFER_CREATE_INFO,.renderPass=world,.attachmentCount=2,.pAttachments=views,.width=width,.height=height,.layers=1};
    VkFramebuffer fb; CHECK(vkCreateFramebuffer(device,&fci,NULL,&fb));
    VkPushConstantRange push={VK_SHADER_STAGE_VERTEX_BIT|VK_SHADER_STAGE_FRAGMENT_BIT,0,16};
    VkPipelineLayoutCreateInfo lci={.sType=VK_STRUCTURE_TYPE_PIPELINE_LAYOUT_CREATE_INFO,.pushConstantRangeCount=1,.pPushConstantRanges=&push};
    VkPipelineLayout layout; CHECK(vkCreatePipelineLayout(device,&lci,NULL,&layout));
    VkPipelineShaderStageCreateInfo stages[2]={
        {.sType=VK_STRUCTURE_TYPE_PIPELINE_SHADER_STAGE_CREATE_INFO,.stage=VK_SHADER_STAGE_VERTEX_BIT,.module=shader(argv[1]),.pName="main"},
        {.sType=VK_STRUCTURE_TYPE_PIPELINE_SHADER_STAGE_CREATE_INFO,.stage=VK_SHADER_STAGE_FRAGMENT_BIT,.module=shader(argv[2]),.pName="main"}};
    VkPipelineVertexInputStateCreateInfo vi={.sType=VK_STRUCTURE_TYPE_PIPELINE_VERTEX_INPUT_STATE_CREATE_INFO};
    VkPipelineInputAssemblyStateCreateInfo ia={.sType=VK_STRUCTURE_TYPE_PIPELINE_INPUT_ASSEMBLY_STATE_CREATE_INFO,.topology=VK_PRIMITIVE_TOPOLOGY_TRIANGLE_LIST};
    VkViewport viewport={0,0,(float)width,(float)height,0,1}; VkRect2D scissor={{0,0},{width,height}};
    VkPipelineViewportStateCreateInfo vp={.sType=VK_STRUCTURE_TYPE_PIPELINE_VIEWPORT_STATE_CREATE_INFO,.viewportCount=1,.pViewports=&viewport,.scissorCount=1,.pScissors=&scissor};
    VkPipelineRasterizationStateCreateInfo raster={.sType=VK_STRUCTURE_TYPE_PIPELINE_RASTERIZATION_STATE_CREATE_INFO,.polygonMode=VK_POLYGON_MODE_FILL,.cullMode=VK_CULL_MODE_NONE,.frontFace=VK_FRONT_FACE_COUNTER_CLOCKWISE,.lineWidth=1};
    VkPipelineMultisampleStateCreateInfo ms={.sType=VK_STRUCTURE_TYPE_PIPELINE_MULTISAMPLE_STATE_CREATE_INFO,.rasterizationSamples=VK_SAMPLE_COUNT_1_BIT};
    VkPipelineDepthStencilStateCreateInfo ds={.sType=VK_STRUCTURE_TYPE_PIPELINE_DEPTH_STENCIL_STATE_CREATE_INFO,.depthTestEnable=VK_TRUE,.depthWriteEnable=VK_TRUE,.depthCompareOp=VK_COMPARE_OP_GREATER};
    VkPipelineColorBlendAttachmentState blend={.colorWriteMask=15};
    VkPipelineColorBlendStateCreateInfo cb={.sType=VK_STRUCTURE_TYPE_PIPELINE_COLOR_BLEND_STATE_CREATE_INFO,.attachmentCount=1,.pAttachments=&blend};
    VkGraphicsPipelineCreateInfo pci={.sType=VK_STRUCTURE_TYPE_GRAPHICS_PIPELINE_CREATE_INFO,.stageCount=2,.pStages=stages,.pVertexInputState=&vi,.pInputAssemblyState=&ia,.pViewportState=&vp,.pRasterizationState=&raster,.pMultisampleState=&ms,.pDepthStencilState=&ds,.pColorBlendState=&cb,.layout=layout,.renderPass=world};
    VkPipeline pipeline; CHECK(vkCreateGraphicsPipelines(device,VK_NULL_HANDLE,1,&pci,NULL,&pipeline));
    VkCommandPoolCreateInfo cpci={.sType=VK_STRUCTURE_TYPE_COMMAND_POOL_CREATE_INFO,.flags=VK_COMMAND_POOL_CREATE_RESET_COMMAND_BUFFER_BIT,.queueFamilyIndex=family};
    VkCommandPool pool; CHECK(vkCreateCommandPool(device,&cpci,NULL,&pool));
    VkCommandBufferAllocateInfo cai={.sType=VK_STRUCTURE_TYPE_COMMAND_BUFFER_ALLOCATE_INFO,.commandPool=pool,.level=VK_COMMAND_BUFFER_LEVEL_PRIMARY,.commandBufferCount=1};
    VkCommandBuffer cmd; CHECK(vkAllocateCommandBuffers(device,&cai,&cmd));
    VkFenceCreateInfo fenceci={.sType=VK_STRUCTURE_TYPE_FENCE_CREATE_INFO};
    VkFence fence; CHECK(vkCreateFence(device,&fenceci,NULL,&fence));
    size_t total_bad=0;
    for(int round=0;round<rounds;round++) for(int mode=0;mode<5;mode++) {
        CHECK(vkResetCommandBuffer(cmd,0));
        VkCommandBufferBeginInfo bi={.sType=VK_STRUCTURE_TYPE_COMMAND_BUFFER_BEGIN_INFO,.flags=VK_COMMAND_BUFFER_USAGE_ONE_TIME_SUBMIT_BIT};
        CHECK(vkBeginCommandBuffer(cmd,&bi));
        if(round==0 && mode==0) {
            VkImageMemoryBarrier2 ib[2]={
                {.sType=VK_STRUCTURE_TYPE_IMAGE_MEMORY_BARRIER_2,.dstStageMask=VK_PIPELINE_STAGE_2_ALL_COMMANDS_BIT,.dstAccessMask=VK_ACCESS_2_MEMORY_READ_BIT|VK_ACCESS_2_MEMORY_WRITE_BIT,.oldLayout=VK_IMAGE_LAYOUT_UNDEFINED,.newLayout=VK_IMAGE_LAYOUT_GENERAL,.srcQueueFamilyIndex=VK_QUEUE_FAMILY_IGNORED,.dstQueueFamilyIndex=VK_QUEUE_FAMILY_IGNORED,.image=color.image,.subresourceRange={color.aspect,0,1,0,1}},
                {.sType=VK_STRUCTURE_TYPE_IMAGE_MEMORY_BARRIER_2,.dstStageMask=VK_PIPELINE_STAGE_2_ALL_COMMANDS_BIT,.dstAccessMask=VK_ACCESS_2_MEMORY_READ_BIT|VK_ACCESS_2_MEMORY_WRITE_BIT,.oldLayout=VK_IMAGE_LAYOUT_UNDEFINED,.newLayout=VK_IMAGE_LAYOUT_GENERAL,.srcQueueFamilyIndex=VK_QUEUE_FAMILY_IGNORED,.dstQueueFamilyIndex=VK_QUEUE_FAMILY_IGNORED,.image=depth.image,.subresourceRange={depth.aspect,0,1,0,1}}};
            VkDependencyInfo dep={.sType=VK_STRUCTURE_TYPE_DEPENDENCY_INFO,.imageMemoryBarrierCount=2,.pImageMemoryBarriers=ib};
            vkCmdPipelineBarrier2(cmd,&dep);
        }
        barrier(cmd); draw(cmd,world,fb,pipeline,layout,1); barrier(cmd);
        if(mode==0 || mode==3 || mode==4) {
            VkClearDepthStencilValue value={0,0}; VkImageSubresourceRange range={VK_IMAGE_ASPECT_DEPTH_BIT,0,1,0,1};
            vkCmdClearDepthStencilImage(cmd,depth.image,VK_IMAGE_LAYOUT_GENERAL,&value,1,&range);
            barrier(cmd);
        }
        if(mode!=3) draw(cmd,(mode==1 || mode==4) ? clear : load,fb,pipeline,layout,0);
        barrier(cmd);
        VkBufferImageCopy copy={.imageSubresource={VK_IMAGE_ASPECT_COLOR_BIT,0,0,1},.imageExtent={width,height,1}};
        vkCmdCopyImageToBuffer(cmd,color.image,VK_IMAGE_LAYOUT_GENERAL,buffer,1,&copy);
        copy.bufferOffset=bytes; copy.imageSubresource.aspectMask=VK_IMAGE_ASPECT_DEPTH_BIT;
        vkCmdCopyImageToBuffer(cmd,depth.image,VK_IMAGE_LAYOUT_GENERAL,buffer,1,&copy);
        VkMemoryBarrier2 host={.sType=VK_STRUCTURE_TYPE_MEMORY_BARRIER_2,.srcStageMask=VK_PIPELINE_STAGE_2_TRANSFER_BIT,.srcAccessMask=VK_ACCESS_2_TRANSFER_WRITE_BIT,.dstStageMask=VK_PIPELINE_STAGE_2_HOST_BIT,.dstAccessMask=VK_ACCESS_2_HOST_READ_BIT};
        VkDependencyInfo dep={.sType=VK_STRUCTURE_TYPE_DEPENDENCY_INFO,.memoryBarrierCount=1,.pMemoryBarriers=&host};
        vkCmdPipelineBarrier2(cmd,&dep); CHECK(vkEndCommandBuffer(cmd));
        CHECK(vkResetFences(device,1,&fence));
        VkCommandBufferSubmitInfo cbsi={.sType=VK_STRUCTURE_TYPE_COMMAND_BUFFER_SUBMIT_INFO,.commandBuffer=cmd};
        VkSubmitInfo2 submit={.sType=VK_STRUCTURE_TYPE_SUBMIT_INFO_2,.commandBufferInfoCount=1,.pCommandBufferInfos=&cbsi};
        CHECK(vkQueueSubmit2(queue,1,&submit,fence));
        CHECK(vkWaitForFences(device,1,&fence,VK_TRUE,3000000000ULL));
        size_t bad_color=0,bad_depth=0;
        float *depth_read=(float*)(read+bytes);
        for(VkDeviceSize i=0;i<pixels;i++) {
            uint8_t *p=read+i*4;
            int red=mode==2 || mode==3;
            if(p[0]!=(red ? 255:0) || p[1]!=(red ? 0:255) || p[2]!=0 || p[3]!=255) bad_color++;
            float expected=mode==3 ? 0.f:mode==2 ? .7f+.05f*(2.f*((float)(i%width)+.5f)/(float)width-1.f):.5f;
            if(!isfinite(depth_read[i]) || fabsf(depth_read[i]-expected)>.00001f) bad_depth++;
        }
        total_bad+=bad_color+bad_depth;
        printf("round=%d mode=%s pixels=%llu badColor=%zu badDepth=%zu firstRGBA=%u,%u,%u,%u firstDepth=%.8f\n",round,
            mode==0 ? "transfer-clear+LOAD":mode==1 ? "attachment-CLEAR":mode==2 ? "no-clear-negative-control":mode==3 ? "transfer-clear-only-readback":"transfer-clear+attachment-CLEAR",
            (unsigned long long)pixels,bad_color,bad_depth,read[0],read[1],read[2],read[3],depth_read[0]);
        if(mode==0 && round==0) {
            const char *output_path=getenv("FCL_PROBE_OUTPUT");
            FILE *out=output_path ? fopen(output_path,"wb"):NULL;
            if(output_path && !out) perror(output_path);
            if(out) {
                fprintf(out,"P6\n%u %u\n255\n",width,height);
                for(VkDeviceSize i=0;i<pixels;i++) fwrite(read+i*4,1,3,out);
                fclose(out);
            }
            size_t shown=0;
            for(VkDeviceSize i=0;i<pixels && shown<8;i++) if(read[i*4]==255) {
                printf("REJECTED_HAND x=%u y=%u depth=%.8f\n",(uint32_t)(i%width),(uint32_t)(i/width),depth_read[i]); shown++;
            }
        }
    }
    printf("%s off-screen depth-clear test; not full Minecraft validation\n",total_bad ? "FAIL":"PASS");
    /* All work has completed and readback was checked. Process exit releases
     * driver resources without interacting with FCL or any world files. */
    fflush(NULL); _Exit(total_bad ? 1:0);
}
