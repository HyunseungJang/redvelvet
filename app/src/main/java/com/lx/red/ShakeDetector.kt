package com.lx.red

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

// detector를 따로 선언
class ShakeDetector : SensorEventListener {
    // listener
    private var mListener: OnShakeListener? = null

    // 시간 기록용
    private var mShakeTimestamp: Long = 0

    // 횟수
    private var mShakeCount = 0

    // listener setting
    fun setOnShakeListener(listener: OnShakeListener?) {
        mListener = listener
    }

    // listener interface
    interface OnShakeListener {
        fun onShake(count: Int)
    }

    // 정확도가 변할 때? 사용하지 않는다
    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // ignore
    }

    // 센서 method.
    override fun onSensorChanged(event: SensorEvent) {
        if (mListener != null) {
            // x,y,z 축의 값을 받아온다
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            // 중력 가속도값으로 나눈 값으로 만든다
            val gX = x / SensorManager.GRAVITY_EARTH
            val gY = y / SensorManager.GRAVITY_EARTH
            val gZ = z / SensorManager.GRAVITY_EARTH
            // gforce는 중력가속도를 포함하는 물체가 받는 힘
            // 1일때는 평소에 받는 중력(정지)
            // 1이하일때(아래로 떨어지며 힘을 덜받을 때)
            // 1이상일 때(위로 올라가면서 힘을 더 받을 때)
            // 단순히 힘의 크기를 계산하기 때문에 피타고라스로 구한다
            // gForce will be close to 1 when there is no movement.
            val gForce = Math.sqrt((gX * gX + gY * gY + gZ * gZ).toDouble()).toFloat()
            // 진동을 감지했을 때
            // gforce가 기준치 이상일 경우
            if (gForce > SHAKE_THRESHOLD_GRAVITY) {
                val now = System.currentTimeMillis()
                // 진동 간격이 너무 짧을 때는 무시
                // ignore shake events too close to each other (500ms)
                if (mShakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
                    return
                }
                // 3초 이상 걸렸을 때 reset한다
                // reset the shake count after 3 seconds of no shakes
                if (mShakeTimestamp + SHAKE_COUNT_RESET_TIME_MS < now) {
                    mShakeCount = 0
                }
                // 업데이트한다
                mShakeTimestamp = now
                mShakeCount++
                // 흔들렸을 때 행동을 설정한다
                mListener!!.onShake(mShakeCount)
            }
        }
    }

    companion object {
        // 센서 이벤트를 들을 listener를 충족하는 class
        // 중력, 중력가속도을 기준으로 삼아서 진동, 움직임을 측정한다.
        // 흔들림 감지할 때 기준이 되는 가해지는 힘
        private const val SHAKE_THRESHOLD_GRAVITY = 2.7f

        // 흔들림 감지할때 최소 0.5초를 기준으로 측정한다.
        private const val SHAKE_SLOP_TIME_MS = 500

        // 흔드는 횟수는 3초마다 초기화
        private const val SHAKE_COUNT_RESET_TIME_MS = 3000
    }
}