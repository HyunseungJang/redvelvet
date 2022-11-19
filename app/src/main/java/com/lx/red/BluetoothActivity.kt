package com.lx.red

import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.app.ActionBar
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import android.widget.Toolbar
import android.widget.ViewAnimator
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.lx.red.common.logger.Log
import com.lx.red.common.logger.LogFragment
import com.lx.red.common.logger.LogWrapper
import com.lx.red.common.logger.MessageOnlyLogFilter
import kotlinx.android.synthetic.main.activity_bluetooth.*
import kotlinx.android.synthetic.main.activity_main.*


class BluetoothActivity : AppCompatActivity() {
    private var mLogShown = false
    val TAG = "BluetoothActivity"
    val PERMISSIONS = arrayOf(
        ACCESS_FINE_LOCATION
    )
    val PERMISSIONS_S_ABOVE = arrayOf(
        BLUETOOTH_SCAN,
        BLUETOOTH_CONNECT,
        ACCESS_FINE_LOCATION
    )
    val REQUEST_ALL_PERMISSION = 2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth)


//
//        val toolbar = blueAction
//        setSupportActionBar(toolbar) //커스텀한 toolbar를 액션바로 사용

        

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            if (!hasPermissions(this, PERMISSIONS_S_ABOVE)) {
                requestPermissions(PERMISSIONS_S_ABOVE, REQUEST_ALL_PERMISSION)
            }
        }else{
            if (!hasPermissions(this, PERMISSIONS)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(PERMISSIONS, REQUEST_ALL_PERMISSION)
                }
            }
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.sampleContentFragment, BluetoothChatFragment())
                .commit()
        }
    }



    //액션바 메뉴 연결 함수
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.bluetooth_chat, menu)
//
//        return true
//    }

//    @SuppressLint("NonConstantResourceId")
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.secure_connect_scan -> {
//                mLogShown = !mLogShown
//                val output: ViewAnimator = findViewById(R.id.sampleOutput)
//                if (mLogShown) {
//                    output.displayedChild = 1
//                } else {
//                    output.displayedChild = 0
//                }
//                invalidateOptionsMenu()
//                return true
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }


    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val logToggle = menu.findItem(R.id.secure_connect_scan)
        logToggle.isVisible = findViewById<View>(R.id.sampleOutput) is ViewAnimator
        logToggle.setTitle(if (mLogShown) R.string.sample_hide_log else R.string.sample_show_log)
        return super.onPrepareOptionsMenu(menu)
    }



    /*
    override fun initializeLogging() {
        val logWrapper = LogWrapper()
        Log.setLogNode(logWrapper)

        val msgFilter = MessageOnlyLogFilter()
        logWrapper.next = msgFilter

        // On screen logging via a fragment with a TextView.
        val logFragment = supportFragmentManager
            .findFragmentById(R.id.logFragment) as LogFragment?
        msgFilter.next = logFragment!!.logView
        Log.i(TAG, "Ready")
    }
       */

    private fun hasPermissions(context: Context, permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_ALL_PERMISSION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permissions granted!", Toast.LENGTH_SHORT).show()
                } else {
                    requestPermissions(permissions, REQUEST_ALL_PERMISSION)
                    Toast.makeText(this, "Permissions must be granted", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}