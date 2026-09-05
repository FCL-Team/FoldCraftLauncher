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
    private val animation = WalkingAnimation(model)

    // 手势状态（UI 线程写、渲染线程读，单字段读写无需同步）
    private var scale = 1f
    private var rotationX = 0f
    private var rotationY = 0f

    /** 当前绑定的皮肤/披风位图（UI 线程读写，[setTexture] 更新） */
    var texture: Array<Bitmap?> = arrayOf(defaultSkin(), null)
        private set

    // 渲染线程状态
    private var program = 0
    private var positionLocation = 0
    private var texCoordLocation = 0
    private var mvpMatrixLocation = 0
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
     * 更新皮肤纹理并显式指定模型（[slim] 覆盖图像自动检测）。
     */
    fun updateTexture(skin: Bitmap?, cape: Bitmap?, slim: Boolean) {
        texture = arrayOf(skin, cape)
        scheduleTextureUpdate(skin, cape, slim)
    }

    /**
     * 设置当前皮肤/披风位图并触发渲染更新（与 [updateTexture] 等价，保留旧 API 名）。
     */
    fun setTexture(skin: Bitmap?, cape: Bitmap?) = updateTexture(skin, cape)

    /** 单指拖动旋转，输入为像素增量（已除以屏幕密度） */
    fun rotateStep(dx: Float, dy: Float) {
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
        animation.update(deltaSeconds)

        val distance = cameraDistance()
        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, distance, 0f, 0f, 0f, 0f, 1f, 0f)
        Matrix.multiplyMM(pvMatrix, 0, projMatrix, 0, viewMatrix, 0)
        // 模型整体旋转（拖动手势）：先 X 后 Y，与旧版固定管线旋转顺序一致
        Matrix.setIdentityM(wrapperMatrix, 0)
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

    private fun drawPart(part: PlayerModel.Part) {
        for (mesh in part.meshes) {
            // wrapper 旋转 → 部件挂点 → 部件旋转（XYZ 顺序，同 three.js）→ pivot → 网格偏移
            System.arraycopy(wrapperMatrix, 0, modelMatrix, 0, 16)
            Matrix.translateM(modelMatrix, 0, part.posX, part.posY, part.posZ)
            Matrix.rotateM(modelMatrix, 0, Math.toDegrees(part.rotationX.toDouble()).toFloat(), 1f, 0f, 0f)
            Matrix.rotateM(modelMatrix, 0, Math.toDegrees(part.rotationY.toDouble()).toFloat(), 0f, 1f, 0f)
            Matrix.rotateM(modelMatrix, 0, Math.toDegrees(part.rotationZ.toDouble()).toFloat(), 0f, 0f, 1f)
            Matrix.translateM(modelMatrix, 0, part.pivotX, part.pivotY, part.pivotZ)
            Matrix.translateM(modelMatrix, 0, mesh.offsetX, mesh.offsetY, mesh.offsetZ)
            Matrix.multiplyMM(mvpMatrix, 0, pvMatrix, 0, modelMatrix, 0)
            GLES20.glUniformMatrix4fv(mvpMatrixLocation, 1, false, mvpMatrix, 0)
            mesh.box.draw(positionLocation, texCoordLocation)
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
                pendingSkin = if (normalized.isOldFormat) normalized.normalizedTexture else normalized.originalTexture
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
        pendingSkin?.let { skinTextureId = uploadTexture(skinTextureId, it) }
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
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE)
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
            varying vec2 vTexCoord;
            void main() {
                vec4 color = texture2D(uTexture, vTexCoord);
                if (color.a < 0.1) discard;
                gl_FragColor = color;
            }
        """

        private const val FOV = 50f
        private const val ZOOM = 0.9f
        private const val NEAR_PLANE = 0.5f
        private const val FAR_PLANE = 1500f
        private const val MIN_SCALE = 0.7f
        private const val MAX_SCALE = 2.0f
        private const val ROTATE_STEP = 2f

        private val FOV_TAN = tan(Math.toRadians(FOV / 2.0)).toFloat()

        private fun defaultSkin(): Bitmap =
            BitmapFactory.decodeStream(SkinRenderer::class.java.getResourceAsStream("/assets/img/alex.png"))
    }
}
