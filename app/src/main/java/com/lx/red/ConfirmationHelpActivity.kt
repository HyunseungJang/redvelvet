package com.lx.red

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.lx.api.BasicClient
import com.lx.data.HelpResponse
import com.lx.red.databinding.ActivityConfirmationHelpBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Context
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.android.synthetic.main.activity_confirmation_help.*
import java.lang.reflect.Member

class ConfirmationHelpActivity : AppCompatActivity() {
    lateinit var binding : ActivityConfirmationHelpBinding
    var phoneNum: String = "tel:"

    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmationHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        help()
        // 문자발송
        val inputPhoneNum=MemberData.memberEmernum
        val myUri = Uri.parse("smsto:${inputPhoneNum}")
        val myIntent = Intent(Intent.ACTION_SENDTO, myUri)
        myIntent.putExtra("sms_body", "살려주세요!")
        startActivity(myIntent)
        // 다이얼로그
        val builder = AlertDialog.Builder(this)
        builder.setTitle("112 신고 완료")
            .setIcon(R.drawable.location)
            .setMessage("<반경 200m 이내> 레드 이용자에게 구조 신호를 보내고 있습니다.")
            .setPositiveButton("닫기",
                DialogInterface.OnClickListener { dialog, id ->
                    binding.resultText.text = "닫기 클릭"
                })
        // 다이얼로그를 띄워주기
        builder.show()

        binding.deleteHelp.setOnClickListener {
            deleteHelp()
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
}