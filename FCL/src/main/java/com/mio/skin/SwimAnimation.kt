package com.mio.skin

import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

/**
 * 游泳动画，公式与 skinview3d 的 SwimAnimation 一致：
 * 入水下潜 + 手臂四元数关键帧划水（slerp 插值）+ 双腿小幅打水。
 * 关键帧欧拉角 (z,y,x)° 经 qx·qy·qz 合成四元数（旋转顺序与部件的 X/Y/Z 一致），
 * 插值结果转回欧拉角写入部件（鞘翅部分未移植）。
 */
class SwimAnimation : PlayerAnimation() {

    private var lock = false

    override fun animate(player: PlayerModel, delta: Float) {
        if (progress == 0f) {
            lock = false
        }
        val period = 1.3f
        val t = progress % period
        val phase = t / period
        // 关键帧时间点与姿态（x, y, z 欧拉角，度）
        val times = floatArrayOf(0f, 0.7f / period, 1.1f / period, 1.0f)
        val leftKeys = arrayOf(
            floatArrayOf(0f, 180f, 180f),
            floatArrayOf(0f, 180f, 287.2f),
            floatArrayOf(90f, 180f, 180f),
            floatArrayOf(0f, 180f, 180f)
        )
        val rightKeys = arrayOf(
            floatArrayOf(0f, -180f, 180f),
            floatArrayOf(0f, -180f, -287.2f),
            floatArrayOf(90f, -180f, 180f),
            floatArrayOf(0f, -180f, 180f)
        )

        var segment = times.size - 2
        for (i in 0 until times.size - 1) {
            if (phase >= times[i] && phase <= times[i + 1]) {
                segment = i
                break
            }
        }
        val p = (phase - times[segment]) / (times[segment + 1] - times[segment])

        // 入水下潜（仅第一段，且未锁定）
        if (!lock) {
            val k = 1.3f
            if (segment == 0 && p * k < 1f) {
                player.rootY = -5f * p * k
                player.rootRotationX = 1.3f * p * PI / 2f
                player.head.rotationX = -PI / 4f * p * k
                player.cape.rotationX = PI / 4f * p * k
            } else {
                lock = true
            }
        }

        // 手臂四元数关键帧插值 → 欧拉角
        val leftQuats = arrayOf(
            quatFromEulerDeg(leftKeys[0]), quatFromEulerDeg(leftKeys[1]),
            quatFromEulerDeg(leftKeys[2]), quatFromEulerDeg(leftKeys[3])
        )
        val rightQuats = arrayOf(
            quatFromEulerDeg(rightKeys[0]), quatFromEulerDeg(rightKeys[1]),
            quatFromEulerDeg(rightKeys[2]), quatFromEulerDeg(rightKeys[3])
        )
        val qLeft = slerp(leftQuats[segment], leftQuats[segment + 1], p).toEulerXYZ()
        player.leftArm.rotationX = qLeft[0]
        player.leftArm.rotationY = qLeft[1]
        player.leftArm.rotationZ = qLeft[2]
        val qRight = slerp(rightQuats[segment], rightQuats[segment + 1], p).toEulerXYZ()
        player.rightArm.rotationX = qRight[0]
        player.rightArm.rotationY = qRight[1]
        player.rightArm.rotationZ = qRight[2]

        // 双腿小幅打水
        val legFreq = 390f / 180f * PI
        val legAmp = 17.2f / 180f * PI
        player.leftLeg.rotationX = legAmp * cos(progress * legFreq + PI)
        player.leftLeg.rotationY = -0.1f / 180f * PI
        player.leftLeg.rotationZ = -0.1f / 180f * PI
        player.rightLeg.rotationX = legAmp * cos(progress * legFreq)
        player.rightLeg.rotationY = 0.1f / 180f * PI
        player.rightLeg.rotationZ = 0.1f / 180f * PI
    }

    /** 关键帧欧拉角（度，应用于 X/Y/Z 轴，合成顺序 Rx·Ry·Rz）→ 四元数 */
    private fun quatFromEulerDeg(euler: FloatArray): Quat {
        val qx = Quat.fromAxisAngle(1f, 0f, 0f, euler[0])
        val qy = Quat.fromAxisAngle(0f, 1f, 0f, euler[1])
        val qz = Quat.fromAxisAngle(0f, 0f, 1f, euler[2])
        return qx.multiply(qy).multiply(qz)
    }

    /** 球面插值（短路径） */
    private fun slerp(from: Quat, to: Quat, t: Float): Quat {
        var dot = from.w * to.w + from.x * to.x + from.y * to.y + from.z * to.z
        var w = to.w; var x = to.x; var y = to.y; var z = to.z
        if (dot < 0f) {
            dot = -dot
            w = -w; x = -x; y = -y; z = -z
        }
        if (dot > 0.9995f) {
            // 角度极近时线性插值后归一化
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

    /** 单精度四元数（w, x, y, z），仅游泳动画使用 */
    private class Quat(val w: Float, val x: Float, val y: Float, val z: Float) {

        fun multiply(o: Quat): Quat = Quat(
            w * o.w - x * o.x - y * o.y - z * o.z,
            w * o.x + x * o.w + y * o.z - z * o.y,
            w * o.y - x * o.z + y * o.w + z * o.x,
            w * o.z + x * o.y - y * o.x + z * o.w
        )

        fun normalized(): Quat {
            val len = kotlin.math.sqrt(w * w + x * x + y * y + z * z)
            return Quat(w / len, x / len, y / len, z / len)
        }

        /** 转欧拉角（弧度），合成顺序 Rx·Ry·Rz，返回 (X, Y, Z) */
        fun toEulerXYZ(): FloatArray {
            val m02 = 2f * (x * z + w * y)
            val m12 = 2f * (y * z - w * x)
            val m22 = 1f - 2f * (x * x + y * y)
            val m01 = 2f * (x * y - w * z)
            val m00 = 1f - 2f * (y * y + z * z)
            val b = asin(m02.coerceIn(-1f, 1f))
            return if (abs(m02) < 0.9999f) {
                floatArrayOf(atan2(-m12, m22), b, atan2(-m01, m00))
            } else {
                // 万向锁：Y=±90°，将全部绕 Z 分量并入 X=0
                floatArrayOf(0f, b, atan2(m12, m22))
            }
        }

        companion object {
            /** 轴角构造（角度为度） */
            fun fromAxisAngle(ax: Float, ay: Float, az: Float, degrees: Float): Quat {
                val half = Math.toRadians(degrees.toDouble()).toFloat() / 2f
                val s = sin(half)
                return Quat(cos(half), ax * s, ay * s, az * s)
            }
        }
    }
}
