package com.mio.skin

import kotlin.math.acos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * 单精度四元数（w, x, y, z），用于 GLTF 节点旋转与动画关键帧插值。
 * GLTF 的 rotation 数组按 (x, y, z, w) 存储，解析时转换为本类顺序。
 */
class Quat(val w: Float, val x: Float, val y: Float, val z: Float) {

    fun multiply(o: Quat): Quat = Quat(
        w * o.w - x * o.x - y * o.y - z * o.z,
        w * o.x + x * o.w + y * o.z - z * o.y,
        w * o.y - x * o.z + y * o.w + z * o.x,
        w * o.z + x * o.y - y * o.x + z * o.w
    )

    fun normalized(): Quat {
        val len = sqrt(w * w + x * x + y * y + z * z)
        return Quat(w / len, x / len, y / len, z / len)
    }

    /**
     * 把旋转矩阵的三列写入列主序 4x4 矩阵的旋转区，并乘以各轴缩放，
     * 平移与末行由调用方填充。
     */
    fun writeRotationTo(m: FloatArray, offset: Int, sx: Float, sy: Float, sz: Float) {
        val xx = x * x
        val yy = y * y
        val zz = z * z
        val xy = x * y
        val xz = x * z
        val yz = y * z
        val wx = w * x
        val wy = w * y
        val wz = w * z
        m[offset] = (1f - 2f * (yy + zz)) * sx
        m[offset + 1] = (2f * (xy + wz)) * sx
        m[offset + 2] = (2f * (xz - wy)) * sx
        m[offset + 4] = (2f * (xy - wz)) * sy
        m[offset + 5] = (1f - 2f * (xx + zz)) * sy
        m[offset + 6] = (2f * (yz + wx)) * sy
        m[offset + 8] = (2f * (xz + wy)) * sz
        m[offset + 9] = (2f * (yz - wx)) * sz
        m[offset + 10] = (1f - 2f * (xx + yy)) * sz
    }

    companion object {
        val IDENTITY = Quat(1f, 0f, 0f, 0f)

        /** 球面插值（短路径），角度极近时退化为线性插值后归一化 */
        fun slerp(from: Quat, to: Quat, t: Float): Quat {
            var dot = from.w * to.w + from.x * to.x + from.y * to.y + from.z * to.z
            var w = to.w
            var x = to.x
            var y = to.y
            var z = to.z
            if (dot < 0f) {
                dot = -dot
                w = -w
                x = -x
                y = -y
                z = -z
            }
            if (dot > 0.9995f) {
                val q = Quat(
                    from.w + (w - from.w) * t, from.x + (x - from.x) * t,
                    from.y + (y - from.y) * t, from.z + (z - from.z) * t
                )
                return q.normalized()
            }
            val theta0 = acos(dot)
            val theta = theta0 * t
            val sinTheta0 = sin(theta0)
            val s0 = sin(theta0 - theta) / sinTheta0
            val s1 = sin(theta) / sinTheta0
            return Quat(
                from.w * s0 + w * s1,
                from.x * s0 + x * s1,
                from.y * s0 + y * s1,
                from.z * s0 + z * s1
            )
        }
    }
}
