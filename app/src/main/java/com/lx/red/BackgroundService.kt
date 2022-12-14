package com.lx.red

import android.R
import android.app.*
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat

class BackgroundService : Service() {
    val CHANNEL_ID = ""

    var mSensorManager: SensorManager? = null
    var mAccelerometer: Sensor? = null
    var mShakeDetector: ShakeDetector? = null

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if (intent == null) {
            return START_STICKY
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "백그라운드 실행 중입니다",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(
                NotificationManager::class.java
            )!!
            manager.createNotificationChannel(serviceChannel)
        }
        val notificationIntent = Intent(this, LoginActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("백그라운드")
            .setContentText("실행중")
            .setSmallIcon(R.drawable.sym_action_email)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)
        Thread { time() }.start()

        return super.onStartCommand(intent, flags, startId)
    }

    fun time() {
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

}
