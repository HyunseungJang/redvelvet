package com.lx.red

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.lx.api.BasicClient
import com.lx.data.HelpResponse
import com.lx.red.databinding.ActivityConfirmationHelpBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ConfirmationHelpActivity : AppCompatActivity() {
    lateinit var binding : ActivityConfirmationHelpBinding
    var phoneNum: String = "tel:"

    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){}

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmationHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.blueButton.setText("${MemberData.memberId}")

        if (!checkNetworkState(this)) {
            launcher.launch(Intent(applicationContext, BluetoothActivity::class.java))
        }
        else {
            help()
        }
        // 문자발송
        val inputPhoneNum=MemberData.memberEmernum
        val myUri = Uri.parse("smsto:${inputPhoneNum}")
        val myIntent = Intent(Intent.ACTION_SENDTO, myUri)
        myIntent.putExtra("sms_body", "살려주세요!")
        startActivity(myIntent)
        // 다이얼로그
        val builder = AlertDialog.Builder(this,R.style.AppAlertDialogTheme)
        builder.setTitle("119 신고 완료")
            .setIcon(R.drawable.location)
            .setMessage("<반경 200m 이내> \n 레드 이용자에게 구조 신호를 보내고 있습니다.")
            .setPositiveButton("닫기"
            ) { _, _ ->

            }
        // 다이얼로그를 띄워주기//
        builder.show()

        binding.deleteHelp.setOnClickListener {
            deleteHelp()
        }

        binding.blueButton.setOnClickListener {
            launcher.launch(Intent(applicationContext, BluetoothActivity::class.java))
        }
    }

    fun help(){
        var id = MemberData.memberId.toString()
        var LAT= AppData.lat?.toDouble()
        var LNG= AppData.lng?.toDouble()

        BasicClient.api.help(
            requestCode = "1001",
            id = id,
            LAT = LAT,
            LNG = LNG
        ).enqueue(object : Callback<HelpResponse> {
            override fun onResponse(call: Call<HelpResponse>, response: Response<HelpResponse>) {

            }
            override fun onFailure(call: Call<HelpResponse>, t: Throwable) {
//                launcher.launch(Intent(applicationContext, BluetoothActivity::class.java))
            }
        })
    }
    fun help2(){
        var id = MemberData.memberId.toString()
        var LAT= AppData.lat?.toDouble()
        var LNG= AppData.lng?.toDouble()

        BasicClient.api.help(
            requestCode = "1001",
            id = id,
            LAT = LAT,
            LNG = LNG
        ).enqueue(object : Callback<HelpResponse> {
            override fun onResponse(call: Call<HelpResponse>, response: Response<HelpResponse>) {

            }
            override fun onFailure(call: Call<HelpResponse>, t: Throwable) {

            }
        })
    }

    fun deleteHelp(){
        var id = MemberData.memberId.toString()

        BasicClient.api.getMemberDelete(
            requestCode = "1001",
            id = id
        ).enqueue(object : Callback<HelpResponse> {
            override fun onResponse(call: Call<HelpResponse>, response: Response<HelpResponse>) {
            }
            override fun onFailure(call: Call<HelpResponse>, t: Throwable) {
                launcher.launch(Intent(applicationContext,MainActivity::class.java))
            }
        })
    }
    companion object {
        @RequiresApi(Build.VERSION_CODES.M)
        fun checkNetworkState(context: Context): Boolean {
            val connectivityManager: ConnectivityManager =
                context.getSystemService(ConnectivityManager::class.java)
            val network: Network = connectivityManager.activeNetwork ?: return false
            val actNetwork: NetworkCapabilities =
                connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                else -> false
            }
        }
    }

}