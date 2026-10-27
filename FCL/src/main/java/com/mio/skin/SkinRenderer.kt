package com.mio.skin

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.GLUtils
import android.opengl.Matrix
import android.widget.Toast
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.util.skin.InvalidSkinException
import com.tungsten.fclcore.util.skin.NormalizedSkin
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.tan

/**
 * 皮肤 3D 渲染器（GLES 2.0，由 [SkinViewer] 的渲染线程驱动 onSurfaceCreated /
 * onSurfaceChanged / onDrawFrame）。模型与动画按 skinview3d 移植。
 *
 * 相机在 +Z 轴上对准原点：fov=50、zoom=0.9，
 * 距离 = 4.5 + 16.5/tan(fov/2)/zoom/scale（clamp 10~256），
 * scale 为手势缩放（等价旧版 0.7~2.0 的模型缩放）。
 * 纹理经 GLUtils.texImage2D 上传（预乘 alpha），混合 GL_ONE/GL_ONE_MINUS_SRC_ALPHA，
 * 片元 alpha<0.1 丢弃（等价旧版 glAlphaFunc）。
 * 3D 皮肤层开启时第二层由体素化网格（[SolidSkinLayer]，复刻 3D Skin Layers）呈现：
 * 全不透明像素立体化并施加方向光照（环境光 0.70 + 漫反射 0.42），半透明像素回落
 * 零厚度面片（第二遍关深度写绘制）；关闭时第二层整体回落原版面片。
 */
class SkinRenderer(context: Context) {

    private val appContext = context.applicationContext

    private val model = PlayerModel()

    /** 当前播放的动画（默认走路），切换时重置模型姿态与动画进度 */
    var animation: PlayerAnimation = WalkingAnimation()
        private set

    /** 第二层（overlay 网格与轮廓挤出）是否绘制 */
    var overlayEnabled: Boolean = true

    // 手势状态（UI 线程写、渲染线程读，单字段读写无需同步）
    private var scale = 1f
    private var rotationX = 0f
    private var rotationY = 0f

    /** 拖拽结束后的回正进行中（UI 线程置位，渲染线程消费） */
    @Volatile
    private var recentering = false

    // 渲染帧累计时间（秒），驱动点按压扁脉冲
    private var elapsed = 0f

    /** 点按压扁起始时刻（[elapsed] 秒），-1 表示无进行中的压扁 */
    private var squashStart = -1f

    /** 当前绑定的皮肤/披风位图，皮肤为归一化后的布局（surface 重建时 onSurfaceCreated 直接重上传） */
    var texture: Array<Bitmap?> = arrayOf(defaultSkin(), null)
        private set

    // 渲染线程状态
    private var program = 0
    private var positionLocation = 0
    private var texCoordLocation = 0
    private var normalLocation = 0
    private var mvpMatrixLocation = 0
    private var normalMatrixLocation = 0
    private var lightMixLocation = 0
    private var skinTextureId = 0
    private var capeTextureId = 0
    private val projMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val pvMatrix = FloatArray(16)
    private val wrapperMatrix = FloatArray(16)
    private val modelMatrix = FloatArray(16)
    private val mvpMatrix = FloatArray(16)
    private val solidMatrix = FloatArray(16)
    private val solidMvpMatrix = FloatArray(16)

    // UI 线程 → 渲染线程 的待处理纹理更新
    @Volatile
    private var pendingSkin: Bitmap? = null

    @Volatile
    private var pendingCape: Bitmap? = null

    @Volatile
    private var pendingSlim = true

    @Volatile
    private var pendingHasUpdate = false

    init {
        // 初始默认皮肤 alex 为 slim 布局，模型须与之匹配，否则手臂贴图采样到透明区域
        model.setSlim(true)
    }

    // ---- 对外 API（任意线程可调）----

    /**
     * 更新皮肤纹理，模型类型从皮肤图像自动检测。
     * [texture] 在归一化完成后由 [scheduleTextureUpdate] 同步更新，
     * surface 重建重上传时与渲染实际使用的纹理一致。
     */
    fun updateTexture(skin: Bitmap?, cape: Bitmap?) {
        scheduleTextureUpdate(skin, cape, null)
    }

    /**
     * 更新皮肤纹理并指定模型：[slim] 非空时覆盖图像自动检测，为空时从皮肤图像检测。
     */
    fun updateTexture(skin: Bitmap?, cape: Bitmap?, slim: Boolean?) {
        scheduleTextureUpdate(skin, cape, slim)
    }

    /**
     * 设置当前皮肤/披风位图并触发渲染更新（与 [updateTexture] 等价，保留旧 API 名）。
     */
    fun setTexture(skin: Bitmap?, cape: Bitmap?) = updateTexture(skin, cape)

    /** 单指拖动旋转，输入为像素增量（已除以屏幕密度）；拖拽期间取消回正 */
    fun rotateStep(dx: Float, dy: Float) {
        recentering = false
        if (abs(dx) >= 1f) {
            rotationY += ROTATE_STEP * dx
        }
        if (abs(dy) >= 1f) {
            rotationX += ROTATE_STEP * dy
        }
    }

    fun getScale(): Float = scale

    fun setScale(value: Float) {
        scale = value.coerceIn(MIN_SCALE, MAX_SCALE)
    }

    /** 拖拽结束，偏航与俯仰一起沿最短路径缓慢收敛回正面 */
    fun onDragEnd() {
        recentering = true
    }

    /** 点按压扁：模型 Y 向压缩、XZ 鼓出的弹簧脉冲 */
    fun squashTap() {
        squashStart = elapsed
    }

    /**
     * 切换动画并重置姿态与进度（语义同 skinview3d 的 viewer.animation setter）。
     */
    fun playAnimation(animation: PlayerAnimation) {
        this.animation = animation
        model.resetJoints()
    }

    // ---- 渲染线程回调（由 SkinViewer 驱动）----

    fun onSurfaceCreated() {
        // EGL context 是新建的：旧 context 的纹理/VBO id 均已失效，先归零再重建
        skinTextureId = 0
        capeTextureId = 0
        model.resetGpuResources()
        program = buildProgram()
        positionLocation = GLES20.glGetAttribLocation(program, "aPosition")
        texCoordLocation = GLES20.glGetAttribLocation(program, "aTexCoord")
        normalLocation = GLES20.glGetAttribLocation(program, "aNormal")
        mvpMatrixLocation = GLES20.glGetUniformLocation(program, "uMVPMatrix")
        normalMatrixLocation = GLES20.glGetUniformLocation(program, "uNormalMatrix")
        lightMixLocation = GLES20.glGetUniformLocation(program, "uLightMix")
        GLES20.glEnableVertexAttribArray(positionLocation)
        GLES20.glEnableVertexAttribArray(texCoordLocation)
        // 面片不携带法线：禁用法线属性并给固定常量，片元光照恒为 1（与旧无光照观感一致）
        GLES20.glDisableVertexAttribArray(normalLocation)
        GLES20.glVertexAttrib3f(normalLocation, 0f, 0f, 1f)
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LEQUAL)
        GLES20.glEnable(GLES20.GL_BLEND)
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA)
        texture[0]?.let { skinTextureId = uploadTexture(skinTextureId, it) }
        texture[1]?.let { capeTextureId = uploadTexture(capeTextureId, it) }
    }

    fun onSurfaceChanged(width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        Matrix.perspectiveM(projMatrix, 0, FOV, width.toFloat() / height, NEAR_PLANE, FAR_PLANE)
    }

    fun onDrawFrame(deltaSeconds: Float) {
        consumePendingUpdate()
        GLES20.glClearColor(0f, 0f, 0f, 0f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        elapsed += deltaSeconds
        if (recentering) {
            // 偏航归一化到 ±180 后与俯仰一起指数收敛回正面
            rotationY = wrapDegrees(rotationY)
            val nextYaw = rotationY * exp(-deltaSeconds / RECENTER_TAU)
            rotationY = if (abs(nextYaw) < RECENTER_EPSILON) 0f else nextYaw
            val nextPitch = rotationX * exp(-deltaSeconds / RECENTER_TAU)
            rotationX = if (abs(nextPitch) < RECENTER_EPSILON) 0f else nextPitch
            if (rotationY == 0f && rotationX == 0f) {
                recentering = false
            }
        }
        animation.update(model, deltaSeconds)

        val distance = cameraDistance()
        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, distance, 0f, 0f, 0f, 0f, 1f, 0f)
        Matrix.multiplyMM(pvMatrix, 0, projMatrix, 0, viewMatrix, 0)
        // 玩家整体偏移与旋转（动画驱动，XYZ 序同部件）→ 模型整体旋转（拖动手势）：先 X 后 Y，与旧版固定管线旋转顺序一致
        Matrix.setIdentityM(wrapperMatrix, 0)
        Matrix.translateM(wrapperMatrix, 0, model.rootX, model.rootY, model.rootZ)
        Matrix.rotateM(
            wrapperMatrix,
            0,
            Math.toDegrees(model.rootRotationX.toDouble()).toFloat(),
            1f,
            0f,
            0f
        )
        Matrix.rotateM(
            wrapperMatrix,
            0,
            Math.toDegrees(model.rootRotationY.toDouble()).toFloat(),
            0f,
            1f,
            0f
        )
        Matrix.rotateM(
            wrapperMatrix,
            0,
            Math.toDegrees(model.rootRotationZ.toDouble()).toFloat(),
            0f,
            0f,
            1f
        )
        val squash = currentSquash()
        if (squash < 1f) {
            // 点按压扁以脚底为支点：Y 压缩、XZ 等比鼓出
            val bulge = 1f + (1f - squash) * 0.5f
            Matrix.translateM(wrapperMatrix, 0, 0f, SQUASH_PIVOT_Y, 0f)
            Matrix.scaleM(wrapperMatrix, 0, bulge, squash, bulge)
            Matrix.translateM(wrapperMatrix, 0, 0f, -SQUASH_PIVOT_Y, 0f)
        }
        if (model.rootScaleY != 1f || model.rootScaleXZ != 1f) {
            // 动画驱动的整体形变（Q 弹压扁拉伸），同样以脚底为支点
            Matrix.translateM(wrapperMatrix, 0, 0f, SQUASH_PIVOT_Y, 0f)
            Matrix.scaleM(wrapperMatrix, 0, model.rootScaleXZ, model.rootScaleY, model.rootScaleXZ)
            Matrix.translateM(wrapperMatrix, 0, 0f, -SQUASH_PIVOT_Y, 0f)
        }
        Matrix.rotateM(wrapperMatrix, 0, rotationX, 1f, 0f, 0f)
        Matrix.rotateM(wrapperMatrix, 0, rotationY, 0f, 1f, 0f)

        GLES20.glUseProgram(program)
        if (skinTextureId != 0) {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, skinTextureId)
            for (part in model.skinParts) {
                drawPart(part)
            }
        }
        if (capeTextureId != 0) {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, capeTextureId)
            drawPart(model.cape)
        }
    }

    // ---- 内部实现 ----

    /** 当前压扁系数（1 为原状）：阻尼弹簧脉冲，t=0 瞬间压至最低点后振荡回弹 */
    private fun currentSquash(): Float {
        if (squashStart < 0f) {
            return 1f
        }
        val t = elapsed - squashStart
        if (t > SQUASH_DURATION) {
            squashStart = -1f
            return 1f
        }
        return 1f - SQUASH_DEPTH * exp(-SQUASH_DAMPING * t) * cos(SQUASH_FREQUENCY * t)
    }

    /** 归一化角度到 (-180, 180] */
    private fun wrapDegrees(degrees: Float): Float {
        val wrapped = (degrees + 180f) % 360f
        return (if (wrapped < 0f) wrapped + 360f else wrapped) - 180f
    }

    private fun drawPart(part: PlayerModel.Part) {
        for ((index, mesh) in part.meshes.withIndex()) {
            // wrapper 旋转 → 部件挂点 → 部件旋转（XYZ 顺序，同 three.js）→ pivot → 网格偏移
            System.arraycopy(wrapperMatrix, 0, modelMatrix, 0, 16)
            Matrix.translateM(modelMatrix, 0, part.posX, part.posY, part.posZ)
            Matrix.rotateM(
                modelMatrix,
                0,
                Math.toDegrees(part.rotationX.toDouble()).toFloat(),
                1f,
                0f,
                0f
            )
            Matrix.rotateM(
                modelMatrix,
                0,
                Math.toDegrees(part.rotationY.toDouble()).toFloat(),
                0f,
                1f,
                0f
            )
            Matrix.rotateM(
                modelMatrix,
                0,
                Math.toDegrees(part.rotationZ.toDouble()).toFloat(),
                0f,
                0f,
                1f
            )
            Matrix.translateM(modelMatrix, 0, part.pivotX, part.pivotY, part.pivotZ)
            Matrix.translateM(modelMatrix, 0, mesh.offsetX, mesh.offsetY, mesh.offsetZ)

            val solid = if (index == 1 && overlayEnabled) part.solidLayer else null
            if (solid != null) {
                // 体素立方体先画（写深度，方向光照）。体素空间 x/z 与部件局部空间反向
                // （同 GLTF 资产经 blockbench_export 节点 rotY(180°) 对齐），绘制矩阵补同款旋转
                System.arraycopy(modelMatrix, 0, solidMatrix, 0, 16)
                Matrix.rotateM(solidMatrix, 0, 180f, 0f, 1f, 0f)
                Matrix.multiplyMM(solidMvpMatrix, 0, pvMatrix, 0, solidMatrix, 0)
                GLES20.glUniformMatrix4fv(mvpMatrixLocation, 1, false, solidMvpMatrix, 0)
                GLES20.glUniformMatrix4fv(normalMatrixLocation, 1, false, solidMatrix, 0)
                solid.layer.draw(
                    positionLocation,
                    texCoordLocation,
                    normalLocation,
                    lightMixLocation,
                    mvpMatrixLocation,
                    normalMatrixLocation,
                    solidMvpMatrix,
                    solidMatrix
                )
                // 第二遍：全不透明像素已由体素呈现，面片仅用于呈现半透明像素——
                // 关深度写（不遮挡披风等后续混合）并把深度比较收紧为 GL_LESS
                // （面片位于体素表面内侧，深度量化同值时不盖染立方体）
                GLES20.glDepthMask(false)
                GLES20.glDepthFunc(GLES20.GL_LESS)
                bindUnlitAttribs()
                Matrix.multiplyMM(mvpMatrix, 0, pvMatrix, 0, modelMatrix, 0)
                GLES20.glUniformMatrix4fv(mvpMatrixLocation, 1, false, mvpMatrix, 0)
                mesh.box.draw(positionLocation, texCoordLocation)
                GLES20.glDepthFunc(GLES20.GL_LEQUAL)
                GLES20.glDepthMask(true)
            } else {
                bindUnlitAttribs()
                Matrix.multiplyMM(mvpMatrix, 0, pvMatrix, 0, modelMatrix, 0)
                GLES20.glUniformMatrix4fv(mvpMatrixLocation, 1, false, mvpMatrix, 0)
                mesh.box.draw(positionLocation, texCoordLocation)
            }
        }
    }

    /** 面片绘制前的属性/uniform 状态：法线属性禁用（固定常量），光照混合为 0（无光照） */
    private fun bindUnlitAttribs() {
        GLES20.glDisableVertexAttribArray(normalLocation)
        GLES20.glVertexAttrib3f(normalLocation, 0f, 0f, 1f)
        GLES20.glUniform1f(lightMixLocation, 0f)
    }

    private fun cameraDistance(): Float {
        val distance = 4.5f + 16.5f / FOV_TAN / ZOOM / scale
        return distance.coerceIn(10f, 256f)
    }

    /** 在 UI 线程做皮肤归一化（旧格式转换/slim 检测），归一化结果回写 [texture] 供 surface 重建时重上传，再交由渲染线程下一帧消费 */
    private fun scheduleTextureUpdate(skin: Bitmap?, cape: Bitmap?, slimOverride: Boolean?) {
        Schedulers.androidUIThread().execute {
            try {
                val normalized = NormalizedSkin(skin)
                val normalizedSkin =
                    if (normalized.isOldFormat) normalized.normalizedTexture else normalized.originalTexture
                // surface 重建重上传时会按 64x64 布局的 UV 采样，texture 若存原始 64x32 位图会导致渲染错乱
                texture = arrayOf(normalizedSkin, cape)
                pendingSkin = normalizedSkin
                pendingCape = cape
                pendingSlim = slimOverride ?: normalized.isSlim
                pendingHasUpdate = true
            } catch (e: InvalidSkinException) {
                e.printStackTrace()
                Toast.makeText(appContext, "Skin Renderer: $e", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun consumePendingUpdate() {
        if (!pendingHasUpdate) {
            return
        }
        pendingHasUpdate = false
        model.setSlim(pendingSlim)
        pendingSkin?.let {
            skinTextureId = uploadTexture(skinTextureId, it)
            // 用皮肤像素重建体素化第二层（3D 皮肤层开启时的实心渲染网格）
            val pixels = IntArray(it.width * it.height)
            it.getPixels(pixels, 0, it.width, 0, 0, it.width, it.height)
            model.rebuildSolidLayers(pixels, it.width)
        }
        pendingSkin = null
        val cape = pendingCape
        if (cape != null) {
            capeTextureId = uploadTexture(capeTextureId, cape)
        } else if (capeTextureId != 0) {
            GLES20.glDeleteTextures(1, intArrayOf(capeTextureId), 0)
            capeTextureId = 0
        }
        pendingCape = null
    }

    private fun uploadTexture(existing: Int, bitmap: Bitmap): Int {
        if (existing != 0) {
            GLES20.glDeleteTextures(1, intArrayOf(existing), 0)
        }
        val ids = IntArray(1)
        GLES20.glGenTextures(1, ids, 0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, ids[0])
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_MIN_FILTER,
            GLES20.GL_NEAREST
        )
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_MAG_FILTER,
            GLES20.GL_NEAREST
        )
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_WRAP_S,
            GLES20.GL_CLAMP_TO_EDGE
        )
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_WRAP_T,
            GLES20.GL_CLAMP_TO_EDGE
        )
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)
        return ids[0]
    }

    private fun buildProgram(): Int {
        val vertexShader = compileShader(GLES20.GL_VERTEX_SHADER, VERTEX_SHADER)
        val fragmentShader = compileShader(GLES20.GL_FRAGMENT_SHADER, FRAGMENT_SHADER)
        val program = GLES20.glCreateProgram()
        GLES20.glAttachShader(program, vertexShader)
        GLES20.glAttachShader(program, fragmentShader)
        GLES20.glLinkProgram(program)
        val status = IntArray(1)
        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, status, 0)
        if (status[0] == 0) {
            val log = GLES20.glGetProgramInfoLog(program)
            GLES20.glDeleteProgram(program)
            throw IllegalStateException("Skin shader link failed: $log")
        }
        return program
    }

    private fun compileShader(type: Int, source: String): Int {
        val shader = GLES20.glCreateShader(type)
        GLES20.glShaderSource(shader, source)
        GLES20.glCompileShader(shader)
        val status = IntArray(1)
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, status, 0)
        if (status[0] == 0) {
            val log = GLES20.glGetShaderInfoLog(shader)
            GLES20.glDeleteShader(shader)
            throw IllegalStateException("Skin shader compile failed: $log")
        }
        return shader
    }

    companion object {
        private const val VERTEX_SHADER = """
            uniform mat4 uMVPMatrix;
            uniform mat4 uNormalMatrix;
            attribute vec4 aPosition;
            attribute vec2 aTexCoord;
            attribute vec3 aNormal;
            varying vec2 vTexCoord;
            varying vec3 vNormal;
            void main() {
                gl_Position = uMVPMatrix * aPosition;
                vTexCoord = aTexCoord;
                vNormal = (uNormalMatrix * vec4(aNormal, 0.0)).xyz;
            }
        """

        private const val FRAGMENT_SHADER = """
            precision mediump float;
            uniform sampler2D uTexture;
            uniform float uLightMix;
            varying vec2 vTexCoord;
            varying vec3 vNormal;
            void main() {
                vec4 color = texture2D(uTexture, vTexCoord);
                if (color.a < 0.1) discard;
                // 体素第二层：环境光 0.70 + 方向漫反射 0.42（光源归一化 (2,4,3)，同 Axolotl）；
                // 面片 uLightMix=0 保持无光照观感
                float light = mix(1.0, 0.70 + 0.42 * max(dot(normalize(vNormal), vec3(0.371, 0.743, 0.557)), 0.0), uLightMix);
                gl_FragColor = vec4(color.rgb * light, color.a);
            }
        """

        private const val FOV = 50f
        private const val ZOOM = 0.9f
        private const val NEAR_PLANE = 0.5f
        private const val FAR_PLANE = 1500f
        private const val MIN_SCALE = 0.7f
        private const val MAX_SCALE = 2.0f
        private const val ROTATE_STEP = 2f

        /** 拖拽松手后偏航回正的时间常数（秒） */
        private const val RECENTER_TAU = 0.35f

        /** 偏航回正的收敛阈值（度），小于此直接归零结束 */
        private const val RECENTER_EPSILON = 0.1f

        /** 点按压扁深度（相对模型高度）与弹簧参数 */
        private const val SQUASH_DEPTH = 0.08f
        private const val SQUASH_DAMPING = 10f
        private const val SQUASH_FREQUENCY = 24f

        /** 点按压扁弹簧截断时长（秒） */
        private const val SQUASH_DURATION = 1f

        /** 压扁/形变支点：模型脚底的局部 y */
        private const val SQUASH_PIVOT_Y = -16f

        private val FOV_TAN = tan(Math.toRadians(FOV / 2.0)).toFloat()

        private fun defaultSkin(): Bitmap =
            BitmapFactory.decodeStream(SkinRenderer::class.java.getResourceAsStream("/assets/img/alex.png"))
    }
}
