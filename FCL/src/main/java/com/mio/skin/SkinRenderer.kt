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
 * 片元 alpha<0.1 丢弃（等价旧版 glAlphaFunc）；无光照（与旧版观感一致）。
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

    /** 拖拽结束后的偏航回正进行中（UI 线程置位，渲染线程消费） */
    @Volatile
    private var recentering = false

    /** 用户正在拖拽旋转（UI 线程写，渲染线程读），期间注视挂起 */
    @Volatile
    private var dragging = false

    // 渲染帧累计时间（秒），驱动点按压扁脉冲
    private var elapsed = 0f

    /** 点按压扁起始时刻（[elapsed] 秒），-1 表示无进行中的压扁 */
    private var squashStart = -1f

    /** 注视目标激活标志与归一化坐标 -1..1（UI 线程写，渲染线程读） */
    @Volatile
    private var gazeActive = false

    @Volatile
    private var gazeX = 0f

    @Volatile
    private var gazeY = 0f

    // 注视当前角（弧度，渲染线程每帧向目标平滑推进）
    private var gazeYaw = 0f
    private var gazePitch = 0f

    /** 注视切断锁存：模型背对时置位，拖回恢复角以内复位（渲染线程） */
    private var gazeCut = false

    /** 当前绑定的皮肤/披风位图（UI 线程读写，[setTexture] 更新） */
    var texture: Array<Bitmap?> = arrayOf(defaultSkin(), null)
        private set

    // 渲染线程状态
    private var program = 0
    private var positionLocation = 0
    private var texCoordLocation = 0
    private var mvpMatrixLocation = 0
    private var shadeLocation = 0
    private var skinTextureId = 0
    private var capeTextureId = 0
    private val projMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val pvMatrix = FloatArray(16)
    private val wrapperMatrix = FloatArray(16)
    private val modelMatrix = FloatArray(16)
    private val mvpMatrix = FloatArray(16)

    // UI 线程 → 渲染线程 的待处理纹理更新
    @Volatile
    private var pendingSkin: Bitmap? = null

    @Volatile
    private var pendingCape: Bitmap? = null

    @Volatile
    private var pendingSlim = false

    @Volatile
    private var pendingHasUpdate = false

    // ---- 对外 API（任意线程可调）----

    /**
     * 更新皮肤纹理，模型类型从皮肤图像自动检测。
     * 同步更新 [texture]（当前纹理可读回，attach 重喂时不会退回默认皮肤）。
     */
    fun updateTexture(skin: Bitmap?, cape: Bitmap?) {
        texture = arrayOf(skin, cape)
        scheduleTextureUpdate(skin, cape, null)
    }

    /**
     * 更新皮肤纹理并显式指定模型（[slim] 覆盖图像自动检测，null 时仍按图像检测）。
     */
    fun updateTexture(skin: Bitmap?, cape: Bitmap?, slim: Boolean?) {
        texture = arrayOf(skin, cape)
        scheduleTextureUpdate(skin, cape, slim)
    }

    /**
     * 设置当前皮肤/披风位图并触发渲染更新（与 [updateTexture] 等价，保留旧 API 名）。
     */
    fun setTexture(skin: Bitmap?, cape: Bitmap?) = updateTexture(skin, cape)

    /** 单指拖动旋转，输入为像素增量（已除以屏幕密度）；拖拽期间取消回正并挂起注视 */
    fun rotateStep(dx: Float, dy: Float) {
        dragging = true
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

    /** 拖拽结束，偏航与俯仰一起沿最短路径缓慢收敛回正面，注视恢复判定 */
    fun onDragEnd() {
        dragging = false
        recentering = true
    }

    /** 点按压扁：模型 Y 向压缩、XZ 鼓出的弹簧脉冲 */
    fun squashTap() {
        squashStart = elapsed
    }

    /** 设置注视目标（视图归一化坐标 -1..1），头部平滑转向目标方向 */
    fun setGaze(x: Float, y: Float) {
        gazeX = x
        gazeY = y
        gazeActive = true
    }

    /** 清除注视目标，头部缓慢回正 */
    fun clearGaze() {
        gazeActive = false
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
        mvpMatrixLocation = GLES20.glGetUniformLocation(program, "uMVPMatrix")
        shadeLocation = GLES20.glGetUniformLocation(program, "uShade")
        GLES20.glUniform1f(shadeLocation, 1f)
        GLES20.glEnableVertexAttribArray(positionLocation)
        GLES20.glEnableVertexAttribArray(texCoordLocation)
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LEQUAL)
        GLES20.glEnable(GLES20.GL_BLEND)
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA)
        texture[0]?.let { skinTextureId = uploadTexture(skinTextureId, it) }
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
        applyGaze(deltaSeconds)

        val distance = cameraDistance()
        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, distance, 0f, 0f, 0f, 0f, 1f, 0f)
        Matrix.multiplyMM(pvMatrix, 0, projMatrix, 0, viewMatrix, 0)
        // 玩家整体偏移与倾斜（动画驱动）→ 模型整体旋转（拖动手势）：先 X 后 Y，与旧版固定管线旋转顺序一致
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

    /**
     * 头部注视平滑跟随：目标方向指数逼近（tau 0.1s），叠加在动画姿态之后（动画会逐帧重写头部旋转）。
     * 拖拽期间与模型背对（超过切断角）时注视放弃、头部回落原位，模型拖回恢复角以内自动恢复；
     * 跟随时目标方位补偿视图偏航，使注视始终朝屏幕方向。
     */
    private fun applyGaze(deltaSeconds: Float) {
        val yawDegrees = wrapDegrees(rotationY)
        val yawAbs = abs(yawDegrees)
        if (dragging || yawAbs > GAZE_CUT_OFF) {
            gazeCut = true
        } else if (yawAbs < GAZE_CUT_ON) {
            gazeCut = false
        }
        val follow = gazeActive && !gazeCut
        val targetYaw = if (follow) {
            (gazeX * GAZE_RANGE_X - yawDegrees * DEG_TO_RAD).coerceIn(-GAZE_RANGE_X, GAZE_RANGE_X)
        } else {
            0f
        }
        val targetPitch = if (follow) gazeY * GAZE_RANGE_Y else 0f
        if (gazeYaw == targetYaw && gazePitch == targetPitch) {
            return
        }
        val factor = 1f - exp(-deltaSeconds / GAZE_TAU)
        gazeYaw += (targetYaw - gazeYaw) * factor
        gazePitch += (targetPitch - gazePitch) * factor
        // 收敛到位后精确贴合，避免无限小步进
        if (abs(gazeYaw - targetYaw) < GAZE_EPSILON && abs(gazePitch - targetPitch) < GAZE_EPSILON) {
            gazeYaw = targetYaw
            gazePitch = targetPitch
        }
        model.head.rotationY += gazeYaw
        model.head.rotationX += gazePitch
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
            Matrix.multiplyMM(mvpMatrix, 0, pvMatrix, 0, modelMatrix, 0)
            GLES20.glUniformMatrix4fv(mvpMatrixLocation, 1, false, mvpMatrix, 0)
            GLES20.glUniform1f(shadeLocation, 1f)
            mesh.box.draw(positionLocation, texCoordLocation)
            // 挤出侧壁与第二层网格同矩阵绘制，整体调暗模拟背光，增强立体感
            if (index == 1 && overlayEnabled) {
                part.extrusion?.let {
                    GLES20.glUniform1f(shadeLocation, EXTRUSION_SHADE)
                    it.draw(positionLocation, texCoordLocation)
                    GLES20.glUniform1f(shadeLocation, 1f)
                }
            }
        }
    }

    private fun cameraDistance(): Float {
        val distance = 4.5f + 16.5f / FOV_TAN / ZOOM / scale
        return distance.coerceIn(10f, 256f)
    }

    /** 在 UI 线程做皮肤归一化（旧格式转换/slim 检测），再交由渲染线程下一帧消费 */
    private fun scheduleTextureUpdate(skin: Bitmap?, cape: Bitmap?, slimOverride: Boolean?) {
        Schedulers.androidUIThread().execute {
            try {
                val normalized = NormalizedSkin(skin)
                pendingSkin =
                    if (normalized.isOldFormat) normalized.normalizedTexture else normalized.originalTexture
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
            // 用皮肤像素重建第二层轮廓挤出网格（边缘实心化）
            val pixels = IntArray(it.width * it.height)
            it.getPixels(pixels, 0, it.width, 0, 0, it.width, it.height)
            model.rebuildOverlayExtrusions(pixels, it.width)
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
            attribute vec4 aPosition;
            attribute vec2 aTexCoord;
            varying vec2 vTexCoord;
            void main() {
                gl_Position = uMVPMatrix * aPosition;
                vTexCoord = aTexCoord;
            }
        """

        private const val FRAGMENT_SHADER = """
            precision mediump float;
            uniform sampler2D uTexture;
            uniform float uShade;
            varying vec2 vTexCoord;
            void main() {
                vec4 color = texture2D(uTexture, vTexCoord);
                if (color.a < 0.1) discard;
                gl_FragColor = vec4(color.rgb * uShade, color.a);
            }
        """

        private const val FOV = 50f
        private const val ZOOM = 0.9f
        private const val NEAR_PLANE = 0.5f
        private const val FAR_PLANE = 1500f
        private const val MIN_SCALE = 0.7f
        private const val MAX_SCALE = 2.0f
        private const val ROTATE_STEP = 2f

        /** 注视切断角与迟滞恢复角：模型偏航越过切断角放弃注视，拖回恢复角以内才重新跟随 */
        private val GAZE_CUT_OFF = Math.toRadians(90.0).toFloat()
        private val GAZE_CUT_ON = Math.toRadians(75.0).toFloat()

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

        /** 压扁支点：模型脚底的局部 y */
        private const val SQUASH_PIVOT_Y = -16f

        /** 头部注视的水平/垂直限角（弧度）、平滑时间常数（秒）与收敛阈值 */
        private val GAZE_RANGE_X = Math.toRadians(60.0).toFloat()
        private val GAZE_RANGE_Y = Math.toRadians(53.0).toFloat()
        private const val GAZE_TAU = 0.1f
        private const val GAZE_EPSILON = 0.0005f

        private const val DEG_TO_RAD = (Math.PI / 180.0).toFloat()

        /** 挤出侧壁的背光亮度系数（参考 3d-Skin-Layers 的方向光照观感） */
        private const val EXTRUSION_SHADE = 0.92f

        private val FOV_TAN = tan(Math.toRadians(FOV / 2.0)).toFloat()

        private fun defaultSkin(): Bitmap =
            BitmapFactory.decodeStream(SkinRenderer::class.java.getResourceAsStream("/assets/img/alex.png"))
    }
}
