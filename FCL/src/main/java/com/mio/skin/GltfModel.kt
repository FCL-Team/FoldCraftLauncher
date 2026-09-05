package com.mio.skin

import android.content.Context
import android.opengl.GLES20
import android.opengl.Matrix
import android.util.Base64
import org.json.JSONArray
import org.json.JSONObject
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * 玩家模型 GLTF 加载与渲染（刚体节点层级，无蒙皮）。
 * 模型资产来自 Modrinth App (modrinth/code) packages/assets/models，GPL-3.0。
 *
 * 模型为刚体层级（部件挂点旋转带动子网格），与逐部件矩阵渲染同构：
 * 绘制时按节点树深度优先遍历，世界矩阵 = 父世界 × 节点局部（T·R·S）。
 * 加载时统一预缩放（模型单位 = 1/16 MC 像素 → 像素单位）并把模型平移到
 * 以原点为中心（对齐相机的固定距离公式），非腿部部件整体抬高 1 像素
 * （复刻 3D Skin Layers 的偏移校正，同 Axolotl）。
 *
 * 烘焙动画（[GltfClip]）按 GLTF 规范以 LINEAR 插值绝对覆写节点局部平移/旋转，
 * 无通道的节点保持 rest 姿势；平移通道同样预缩放为像素单位。
 * 所有 GL 调用须在渲染线程执行。
 */
class GltfModel private constructor() {

    /** 层级节点：rest 姿势 + clip 采样写入的运行时姿势 */
    inner class Node internal constructor(val name: String?) {
        val children = ArrayList<Node>()
        var meshes: List<GltfMesh> = emptyList()

        // rest 姿势（像素单位，含居中平移与部件抬高）
        var restTranslation = FloatArray(3)
        var restRotation = Quat.IDENTITY
        var restScale = FloatArray(3) { 1f }

        // 运行时姿势（clip 采样覆写，无通道时与 rest 相同）
        var translation = FloatArray(3)
        var rotation = Quat.IDENTITY

        var localMatrix = FloatArray(16)
        var worldMatrix = FloatArray(16)

        fun resetPose() {
            System.arraycopy(restTranslation, 0, translation, 0, 3)
            rotation = restRotation
            rebuildLocalMatrix()
        }

        fun rebuildLocalMatrix() {
            rotation.writeRotationTo(localMatrix, 0, restScale[0], restScale[1], restScale[2])
            localMatrix[12] = translation[0]
            localMatrix[13] = translation[1]
            localMatrix[14] = translation[2]
            localMatrix[3] = 0f
            localMatrix[7] = 0f
            localMatrix[11] = 0f
            localMatrix[15] = 1f
        }
    }

    /** 网格（单个 primitive）：顶点位置/UV/索引 VBO，惰性上传 */
    inner class GltfMesh internal constructor(
        val name: String?,
        val materialName: String,
        private val positions: FloatArray,
        private val uvs: FloatArray,
        private val indices: ShortArray
    ) {
        val indexCount = indices.size
        val boundsMin = FloatArray(3)
        val boundsMax = FloatArray(3)

        /** 体素化第二层定义（仅 *_Layer 网格，[宽, 高, 深, u, v]），皮肤更新时重建 */
        var layerDefinition: IntArray? = null

        /** 已重建的体素化第二层（存在时替代零厚度面片绘制） */
        var solidLayer: SolidSkinLayer? = null

        private var vboPositions = 0
        private var vboUvs = 0
        private var vboIndices = 0

        fun draw(
            positionLocation: Int,
            texCoordLocation: Int,
            normalLocation: Int,
            lightMixLocation: Int,
            mvpMatrixLocation: Int,
            normalMatrixLocation: Int,
            mvpMatrix: FloatArray,
            normalMatrix: FloatArray
        ) {
            if (vboPositions == 0) {
                uploadToGpu()
            }
            GLES20.glUniformMatrix4fv(mvpMatrixLocation, 1, false, mvpMatrix, 0)
            GLES20.glUniformMatrix4fv(normalMatrixLocation, 1, false, normalMatrix, 0)
            GLES20.glUniform1f(lightMixLocation, 0f)
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboPositions)
            GLES20.glVertexAttribPointer(positionLocation, 3, GLES20.GL_FLOAT, false, 0, 0)
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboUvs)
            GLES20.glVertexAttribPointer(texCoordLocation, 2, GLES20.GL_FLOAT, false, 0, 0)
            // 基础网格无法线：法线属性禁用并给常量值（lightMix=0 时不参与光照）
            GLES20.glDisableVertexAttribArray(normalLocation)
            GLES20.glVertexAttrib3f(normalLocation, 0f, 1f, 0f)
            GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, vboIndices)
            GLES20.glDrawElements(GLES20.GL_TRIANGLES, indexCount, GLES20.GL_UNSIGNED_SHORT, 0)
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0)
        }

        fun resetGpuResources() {
            if (vboPositions != 0) {
                GLES20.glDeleteBuffers(3, intArrayOf(vboPositions, vboUvs, vboIndices), 0)
                vboPositions = 0
                vboUvs = 0
                vboIndices = 0
            }
        }

        private fun uploadToGpu() {
            val ids = IntArray(3)
            GLES20.glGenBuffers(3, ids, 0)
            vboPositions = ids[0]
            vboUvs = ids[1]
            vboIndices = ids[2]
            uploadBuffer(vboPositions, positions)
            uploadBuffer(vboUvs, uvs)
            GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, vboIndices)
            GLES20.glBufferData(
                GLES20.GL_ELEMENT_ARRAY_BUFFER,
                indices.size * 2,
                shortBuffer(indices),
                GLES20.GL_STATIC_DRAW
            )
            GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0)
        }

        private fun uploadBuffer(id: Int, data: FloatArray) {
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, id)
            GLES20.glBufferData(
                GLES20.GL_ARRAY_BUFFER,
                data.size * 4,
                floatBuffer(data),
                GLES20.GL_STATIC_DRAW
            )
        }
    }

    /** 烘焙动画片段：按 GLTF 规范 LINEAR 插值，绝对覆写目标节点局部平移/旋转 */
    inner class GltfClip internal constructor(
        val name: String,
        val duration: Float,
        internal val channels: List<ClipChannel>
    )

    inner class ClipChannel internal constructor(
        private val node: Node,
        private val times: FloatArray,
        private val translations: FloatArray?,
        private val rotations: Array<Quat>?
    ) {
        /** 在 time 处采样并写入节点姿势 */
        fun apply(time: Float) {
            if (times.isEmpty()) {
                return
            }
            // 定位时间所在段（times 升序），段外钳制到端点
            var seg = 0
            while (seg < times.size - 2 && time > times[seg + 1]) {
                seg++
            }
            val f = segmentFraction(time, seg)
            if (translations != null) {
                val t0 = seg * 3
                for (k in 0 until 3) {
                    node.translation[k] =
                        translations[t0 + k] + (translations[t0 + 3 + k] - translations[t0 + k]) * f
                }
            } else if (rotations != null) {
                node.rotation = Quat.slerp(rotations[seg], rotations[seg + 1], f)
            }
            node.rebuildLocalMatrix()
        }

        private fun segmentFraction(time: Float, seg: Int): Float {
            if (seg >= times.size - 1) {
                return 0f
            }
            val span = times[seg + 1] - times[seg]
            return if (span <= 0f) 0f else ((time - times[seg]) / span).coerceIn(0f, 1f)
        }
    }

    private val nodes = ArrayList<Node>()
    private val rootNodes = ArrayList<Node>()
    private val drawOrder = ArrayList<Node>()
    private val clips = ArrayList<GltfClip>()

    private var currentClip: GltfClip? = null
    private var time = 0f

    private val tempMatrix = FloatArray(16)

    // ---- 对外 API ----

    fun findClip(id: String): GltfClip? = clips.firstOrNull { it.name == id }

    /** 切换动画：全部节点恢复 rest 后由新 clip 驱动；未知 id 返回 false */
    fun playAnimation(id: String): Boolean {
        val clip = findClip(id) ?: return false
        currentClip = clip
        time = 0f
        nodes.forEach { it.resetPose() }
        return true
    }

    fun update(deltaSeconds: Float) {
        val clip = currentClip
        if (clip != null) {
            time = (time + deltaSeconds) % clip.duration
            for (channel in clip.channels) {
                channel.apply(time)
            }
        }
        computeWorldMatrices()
    }

    /** 按 material 分组绘制：capeOnly=true 画披风网格，false 画其余（皮肤）网格；
     * 已重建体素层的 *_Layer 网格以体素网格替代零厚度面片 */
    fun draw(
        positionLocation: Int,
        texCoordLocation: Int,
        normalLocation: Int,
        lightMixLocation: Int,
        mvpMatrixLocation: Int,
        normalMatrixLocation: Int,
        mvpBase: FloatArray,
        modelBase: FloatArray,
        capeOnly: Boolean
    ) {
        val normalMatrix = FloatArray(16)
        for (node in drawOrder) {
            if (node.meshes.isEmpty()) {
                continue
            }
            Matrix.multiplyMM(tempMatrix, 0, mvpBase, 0, node.worldMatrix, 0)
            Matrix.multiplyMM(normalMatrix, 0, modelBase, 0, node.worldMatrix, 0)
            for (mesh in node.meshes) {
                if ((mesh.materialName == CAPE_MATERIAL) != capeOnly) {
                    continue
                }
                if (!capeOnly) {
                    val layer = mesh.solidLayer
                    if (layer != null) {
                        layer.draw(
                            positionLocation, texCoordLocation, normalLocation, lightMixLocation,
                            mvpMatrixLocation, normalMatrixLocation, tempMatrix, normalMatrix
                        )
                        continue
                    }
                }
                mesh.draw(
                    positionLocation, texCoordLocation, normalLocation, lightMixLocation,
                    mvpMatrixLocation, normalMatrixLocation, tempMatrix, normalMatrix
                )
            }
        }
    }

    /**
     * 用皮肤像素重建全部体素化第二层（仅渲染线程调用，皮肤纹理更新时触发）。
     * [pixels] 为 ARGB 行主序像素，[bitmapWidth] 为位图宽度（归一化后恒为 64）。
     */
    fun rebuildSolidLayers(pixels: IntArray, bitmapWidth: Int) {
        for (node in nodes) {
            for (mesh in node.meshes) {
                val def = mesh.layerDefinition ?: continue
                val layer = mesh.solidLayer ?: SolidSkinLayer().also { mesh.solidLayer = it }
                layer.rebuild(mesh.boundsMin, mesh.boundsMax, mesh.name.orEmpty(), def, pixels, bitmapWidth)
            }
        }
    }

    fun resetGpuResources() {
        for (node in nodes) {
            node.meshes.forEach {
                it.resetGpuResources()
                it.solidLayer?.resetGpuResources()
            }
        }
    }

    // ---- 姿势与绘制辅助 ----

    private fun computeWorldMatrices() {
        for (root in rootNodes) {
            computeWorldRecursive(root, IDENTITY_MATRIX)
        }
    }

    private fun computeWorldRecursive(node: Node, parent: FloatArray) {
        Matrix.multiplyMM(node.worldMatrix, 0, parent, 0, node.localMatrix, 0)
        for (child in node.children) {
            computeWorldRecursive(child, node.worldMatrix)
        }
    }

    // ---- 解析 ----

    private fun parse(root: JSONObject, buffer: ByteArray, isSlim: Boolean) {
        val views = root.getJSONArray("bufferViews")
        val accessors = root.getJSONArray("accessors")
        val meshesJson = root.getJSONArray("meshes")
        val materialsJson = root.getJSONArray("materials")
        val nodesJson = root.getJSONArray("nodes")

        // 节点 TRS 与 mesh 引用
        for (i in 0 until nodesJson.length()) {
            val json = nodesJson.getJSONObject(i)
            val node = Node(json.optString("name"))
            nodes.add(node)
            json.optJSONArray("translation")?.let {
                node.restTranslation[0] = it.getDouble(0).toFloat() * UNIT_TO_PIXEL
                node.restTranslation[1] = it.getDouble(1).toFloat() * UNIT_TO_PIXEL
                node.restTranslation[2] = it.getDouble(2).toFloat() * UNIT_TO_PIXEL
            }
            json.optJSONArray("rotation")?.let {
                // GLTF 四元数组序 (x, y, z, w)
                node.restRotation = Quat(
                    it.getDouble(3).toFloat(),
                    it.getDouble(0).toFloat(),
                    it.getDouble(1).toFloat(),
                    it.getDouble(2).toFloat()
                )
            }
            json.optJSONArray("scale")?.let {
                node.restScale[0] = it.getDouble(0).toFloat()
                node.restScale[1] = it.getDouble(1).toFloat()
                node.restScale[2] = it.getDouble(2).toFloat()
            }
            if (json.has("mesh")) {
                node.meshes = parseMeshes(
                    meshesJson.getJSONObject(json.getInt("mesh")),
                    materialsJson,
                    accessors,
                    buffer,
                    views,
                    isSlim
                )
            }
        }
        // 层级
        for (i in nodes.indices) {
            nodesJson.getJSONObject(i).optJSONArray("children")?.let { children ->
                for (j in 0 until children.length()) {
                    nodes[i].children.add(nodes[children.getInt(j)])
                }
            }
        }
        root.getJSONArray("scenes").getJSONObject(0).getJSONArray("nodes").let { roots ->
            for (i in 0 until roots.length()) {
                rootNodes.add(nodes[roots.getInt(i)])
            }
        }

        // 烘焙动画（跳过无通道的空 clip）
        root.optJSONArray("animations")?.let { animations ->
            for (i in 0 until animations.length()) {
                parseClip(animations.getJSONObject(i), accessors, buffer, views)?.let { clips.add(it) }
            }
        }

        // rest 局部矩阵 → 部件抬高 → 绘制顺序 → 以原点为中心 → 运行时姿势初始化
        nodes.forEach { it.rebuildLocalMatrix() }
        nodes.forEach { node ->
            if (node.name in LIFT_NODES) {
                node.restTranslation[1] += 1f
            }
        }
        collectDrawOrder(rootNodes)
        centerModel()
        nodes.forEach { it.resetPose() }
    }

    private fun parseMeshes(
        meshJson: JSONObject,
        materialsJson: JSONArray,
        accessors: JSONArray,
        buffer: ByteArray,
        views: JSONArray,
        isSlim: Boolean
    ): List<GltfMesh> {
        val result = ArrayList<GltfMesh>()
        val primitives = meshJson.getJSONArray("primitives")
        for (i in 0 until primitives.length()) {
            val prim = primitives.getJSONObject(i)
            val attributes = prim.getJSONObject("attributes")
            val positions = readFloatAccessor(accessors, attributes.getInt("POSITION"), buffer, views)
            val uvs = readFloatAccessor(accessors, attributes.getInt("TEXCOORD_0"), buffer, views)
            // 预缩放到像素单位
            for (k in positions.indices) {
                positions[k] *= UNIT_TO_PIXEL
            }
            val mesh = GltfMesh(
                meshJson.optString("name"),
                materialsJson.getJSONObject(prim.getInt("material")).optString("name"),
                positions,
                uvs,
                readIndices(accessors, prim.getInt("indices"), buffer, views)
            )
            if (mesh.name?.endsWith("_Layer") == true) {
                mesh.layerDefinition = layerDefinition(mesh.name!!, isSlim)
            }
            for (k in 0 until 3) {
                var axisMin = Float.MAX_VALUE
                var axisMax = -Float.MAX_VALUE
                for (v in k until positions.size step 3) {
                    axisMin = minOf(axisMin, positions[v])
                    axisMax = maxOf(axisMax, positions[v])
                }
                mesh.boundsMin[k] = axisMin
                mesh.boundsMax[k] = axisMax
            }
            result.add(mesh)
        }
        return result
    }

    private fun parseClip(
        clipJson: JSONObject,
        accessors: JSONArray,
        buffer: ByteArray,
        views: JSONArray
    ): GltfClip? {
        val channelsJson = clipJson.optJSONArray("channels") ?: return null
        if (channelsJson.length() == 0) {
            return null
        }
        val samplersJson = clipJson.getJSONArray("samplers")
        val channels = ArrayList<ClipChannel>()
        var duration = 0f
        for (i in 0 until channelsJson.length()) {
            val channel = channelsJson.getJSONObject(i)
            val sampler = samplersJson.getJSONObject(channel.getInt("sampler"))
            val times = readFloatAccessor(accessors, sampler.getInt("input"), buffer, views)
            val values = readFloatAccessor(accessors, sampler.getInt("output"), buffer, views)
            val target = channel.getJSONObject("target")
            val node = nodes[target.getInt("node")]
            when (target.getString("path")) {
                "translation" -> {
                    for (k in values.indices) {
                        values[k] *= UNIT_TO_PIXEL
                    }
                    channels.add(ClipChannel(node, times, values, null))
                }
                "rotation" -> {
                    // GLTF 四元数组序 (x, y, z, w)
                    val rotations = Array(times.size) { j ->
                        Quat(
                            values[j * 4 + 3], values[j * 4], values[j * 4 + 1], values[j * 4 + 2]
                        )
                    }
                    channels.add(ClipChannel(node, times, null, rotations))
                }
                else -> continue
            }
            for (t in times) {
                if (t > duration) {
                    duration = t
                }
            }
        }
        if (channels.isEmpty()) {
            return null
        }
        return GltfClip(clipJson.optString("name"), duration, channels)
    }

    /** 居中：按 rest 世界包围盒把模型平移到以原点为中心（对齐相机距离公式） */
    private fun centerModel() {
        computeWorldMatrices()
        val min = floatArrayOf(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE)
        val max = floatArrayOf(-Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE)
        val corner = FloatArray(4)
        val transformed = FloatArray(4)
        for (node in drawOrder) {
            for (mesh in node.meshes) {
                for (cx in 0 until 2) for (cy in 0 until 2) for (cz in 0 until 2) {
                    corner[0] = if (cx == 0) mesh.boundsMin[0] else mesh.boundsMax[0]
                    corner[1] = if (cy == 0) mesh.boundsMin[1] else mesh.boundsMax[1]
                    corner[2] = if (cz == 0) mesh.boundsMin[2] else mesh.boundsMax[2]
                    corner[3] = 1f
                    Matrix.multiplyMV(transformed, 0, node.worldMatrix, 0, corner, 0)
                    for (k in 0 until 3) {
                        min[k] = minOf(min[k], transformed[k])
                        max[k] = maxOf(max[k], transformed[k])
                    }
                }
            }
        }
        if (min[0] > max[0]) {
            return
        }
        val shiftX = -(min[0] + max[0]) / 2f
        val shiftY = -(min[1] + max[1]) / 2f
        val shiftZ = -(min[2] + max[2]) / 2f
        for (root in rootNodes) {
            root.restTranslation[0] += shiftX
            root.restTranslation[1] += shiftY
            root.restTranslation[2] += shiftZ
        }
        rootNodes.forEach { it.rebuildLocalMatrix() }
        computeWorldMatrices()
    }

    private fun collectDrawOrder(from: List<Node>) {
        for (node in from) {
            drawOrder.add(node)
            collectDrawOrder(node.children)
        }
    }

    companion object {
        /** 模型单位 = 1/16 MC 像素 */
        private const val UNIT_TO_PIXEL = 16f
        private const val CAPE_MATERIAL = "cape"
        private const val COMPONENT_FLOAT = 5126

        /** 整体抬高 1 像素的非腿部部件节点（复刻 Axolotl 对该模型的校正） */
        private val LIFT_NODES = setOf("Head", "Right_Arm", "Left_Arm", "Body_2", "Body_Layer", "Cape")

        private val IDENTITY_MATRIX = FloatArray(16).also { Matrix.setIdentityM(it, 0) }

        fun load(context: Context, assetPath: String, isSlim: Boolean): GltfModel {
            val text = context.assets.open(assetPath).use { stream ->
                stream.bufferedReader().readText()
            }
            val root = JSONObject(text)
            val buffer = decodeBuffer(root)
            val model = GltfModel()
            model.parse(root, buffer, isSlim)
            return model
        }

        /**
         * 第二层部件 UV 定义（[宽, 高, 深, u, v]，与 Minecraft 皮肤规范一致）；
         * slim 模型的手臂宽 3。
         */
        private fun layerDefinition(name: String, isSlim: Boolean): IntArray? = when (name) {
            "Hat_Layer" -> intArrayOf(8, 8, 8, 32, 0)
            "Body_Layer" -> intArrayOf(8, 12, 4, 16, 32)
            "Right_Leg_Layer" -> intArrayOf(4, 12, 4, 0, 32)
            "Left_Leg_Layer" -> intArrayOf(4, 12, 4, 0, 48)
            "Right_Arm_Layer" -> intArrayOf(if (isSlim) 3 else 4, 12, 4, 40, 32)
            "Left_Arm_Layer" -> intArrayOf(if (isSlim) 3 else 4, 12, 4, 48, 48)
            else -> null
        }

        /** 解码内嵌 base64 的 buffer（data:application/octet-stream;base64,...） */
        private fun decodeBuffer(root: JSONObject): ByteArray {
            val uri = root.getJSONArray("buffers").getJSONObject(0).getString("uri")
            require(uri.startsWith("data:") && uri.contains("base64,")) {
                "GltfModel: 仅支持内嵌 base64 buffer"
            }
            return Base64.decode(uri.substringAfter("base64,"), Base64.DEFAULT)
        }

        /** 读取 FLOAT accessor（支持 bufferView stride），返回扁平浮点数组 */
        private fun readFloatAccessor(
            accessors: JSONArray,
            index: Int,
            buffer: ByteArray,
            views: JSONArray
        ): FloatArray {
            val acc = accessors.getJSONObject(index)
            val view = views.getJSONObject(acc.getInt("bufferView"))
            val byteOffset = view.optInt("byteOffset", 0) + acc.optInt("byteOffset", 0)
            val count = acc.getInt("count")
            if (acc.getInt("componentType") != COMPONENT_FLOAT) {
                throw IllegalStateException("GltfModel: 仅支持 FLOAT accessor")
            }
            val comp = componentCount(acc.getString("type"))
            val result = FloatArray(count * comp)
            val stride = view.optInt("byteStride", 0)
            if (stride == 0 || stride == comp * 4) {
                ByteBuffer.wrap(buffer, byteOffset, count * comp * 4)
                    .order(ByteOrder.LITTLE_ENDIAN)
                    .asFloatBuffer()
                    .get(result)
            } else {
                val wrapped = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN)
                for (i in 0 until count) {
                    wrapped.position(byteOffset + i * stride)
                    for (k in 0 until comp) {
                        result[i * comp + k] = wrapped.float
                    }
                }
            }
            return result
        }

        /** 读取索引 accessor（5121/5123/5125），统一转为 ShortArray（模型顶点数远小于 65536） */
        private fun readIndices(
            accessors: JSONArray,
            index: Int,
            buffer: ByteArray,
            views: JSONArray
        ): ShortArray {
            val acc = accessors.getJSONObject(index)
            val view = views.getJSONObject(acc.getInt("bufferView"))
            val byteOffset = view.optInt("byteOffset", 0) + acc.optInt("byteOffset", 0)
            val count = acc.getInt("count")
            val wrapped = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN)
            return when (acc.getInt("componentType")) {
                5121 -> ShortArray(count) { i ->
                    wrapped.position(byteOffset + i)
                    wrapped.get().toInt().toShort()
                }
                5123 -> ShortArray(count) { i ->
                    wrapped.position(byteOffset + i * 2)
                    wrapped.short
                }
                5125 -> ShortArray(count) { i ->
                    wrapped.position(byteOffset + i * 4)
                    wrapped.int.toShort()
                }
                else -> throw IllegalStateException("GltfModel: 不支持的索引类型 ${acc.getInt("componentType")}")
            }
        }

        private fun componentCount(type: String): Int = when (type) {
            "SCALAR" -> 1
            "VEC2" -> 2
            "VEC3" -> 3
            "VEC4" -> 4
            else -> throw IllegalStateException("GltfModel: 不支持的 accessor 类型 $type")
        }

        private fun floatBuffer(data: FloatArray) =
            ByteBuffer.allocateDirect(data.size * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(data)
                .apply { position(0) }

        private fun shortBuffer(data: ShortArray) =
            ByteBuffer.allocateDirect(data.size * 2)
                .order(ByteOrder.nativeOrder())
                .asShortBuffer()
                .put(data)
                .apply { position(0) }
    }
}
