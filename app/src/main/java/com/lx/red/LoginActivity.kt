package com.lx.red

import android.app.ActionBar
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.lx.api.BasicClient
import com.lx.data.MemberListResponse
import com.lx.red.SampleActivityBase.TAG
import com.lx.red.common.logger.Log
import com.lx.red.databinding.ActivityLoginBinding
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    lateinit var binding : ActivityLoginBinding

    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 자동로그인
        if(MemberData.memberId.isNullOrBlank()
            || MemberData.memberPw.isNullOrBlank()) {
            autoLogin()
        }
        else {
            Toast.makeText(this, "${MemberData.memberId}님 자동 로그인 되었습니다.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val actionBar: androidx.appcompat.app.ActionBar? = supportActionBar
        actionBar?.hide()

        //로그인 버튼
        binding.loginButton.setOnClickListener {
            readMember()
        }

        //회원가입 버튼
        binding.registrationButton.setOnClickListener {
            launcher.launch(Intent(applicationContext,AgreeActivity::class.java))
        }

//        블루투스 버튼
//        binding.bluetooth.setOnClickListener {
//            var intent = Intent(this,BluetoothActivity::class.java)
//            startActivity(intent)
//        }

    }

    fun autoLogin() {
        var id = binding.loginId.text.toString()
        var pw = binding.loginPw.text.toString()

        BasicClient.api.postMemberRead(
            requestCode = "1001",
            id = id,
            pw = pw
        ).enqueue(object : Callback<MemberListResponse> {
            override fun onResponse(call: Call<MemberListResponse>, response: Response<MemberListResponse>) {
                val checkMember = response.body()?.header?.total.toString()
                if(checkMember == "1"){
                    MemberData.memberId = response.body()?.data?.get(0)?.id.toString()
                    MemberData.memberPw = response.body()?.data?.get(0)?.pw.toString()
                    MemberData.memberName = response.body()?.data?.get(0)?.name.toString()
                    MemberData.memberBirth = response.body()?.data?.get(0)?.birth.toString()
                    MemberData.memberGender = response.body()?.data?.get(0)?.gender.toString()
                    MemberData.memberPhone = response.body()?.data?.get(0)?.phone.toString()
                    MemberData.memberHeight = response.body()?.data?.get(0)?.height.toString()
                    MemberData.memberWeight = response.body()?.data?.get(0)?.weight.toString()
                    MemberData.memberEmernum = response.body()?.data?.get(0)?.emernum.toString()
                    MemberData.memberMedicine = response.body()?.data?.get(0)?.medicine.toString()
                    MemberData.memberDisease = response.body()?.data?.get(0)?.disease.toString()
                    MemberData.memberBloodtype = response.body()?.data?.get(0)?.bloodtype.toString()
                    MemberData.memberCertificate = response.body()?.data?.get(0)?.certificate.toString()
                    MemberData.memberOther = response.body()?.data?.get(0)?.others.toString()
                    MemberData.memberAgreeP = response.body()?.data?.get(0)?.agreep.toString()
                    MemberData.memberAgreeS1 = response.body()?.data?.get(0)?.agrees1.toString()
                    MemberData.memberAgreeS2 = response.body()?.data?.get(0)?.agrees2.toString()
                    launcher.launch(Intent(applicationContext,MainActivity::class.java))
                } else {

                }
            }

            override fun onFailure(call: Call<MemberListResponse>, t: Throwable) {
            }
        })
    }

    fun readMember() {
        var id = binding.loginId.text.toString()
        var pw = binding.loginPw.text.toString()

        BasicClient.api.postMemberRead(
            requestCode = "1001",
            id = id,
            pw = pw
        ).enqueue(object : Callback<MemberListResponse> {
            override fun onResponse(call: Call<MemberListResponse>, response: Response<MemberListResponse>) {
                val checkMember = response.body()?.header?.total.toString()
                if(checkMember == "1"){
                    MemberData.memberId = response.body()?.data?.get(0)?.id.toString()
                    MemberData.memberPw = response.body()?.data?.get(0)?.pw.toString()
                    MemberData.memberName = response.body()?.data?.get(0)?.name.toString()
                    MemberData.memberBirth = response.body()?.data?.get(0)?.birth.toString()
                    MemberData.memberGender = response.body()?.data?.get(0)?.gender.toString()
                    MemberData.memberPhone = response.body()?.data?.get(0)?.phone.toString()
                    MemberData.memberHeight = response.body()?.data?.get(0)?.height.toString()
                    MemberData.memberWeight = response.body()?.data?.get(0)?.weight.toString()
                    MemberData.memberEmernum = response.body()?.data?.get(0)?.emernum.toString()
                    MemberData.memberMedicine = response.body()?.data?.get(0)?.medicine.toString()
                    MemberData.memberDisease = response.body()?.data?.get(0)?.disease.toString()
                    MemberData.memberBloodtype = response.body()?.data?.get(0)?.bloodtype.toString()
                    MemberData.memberCertificate = response.body()?.data?.get(0)?.certificate.toString()
                    MemberData.memberOther = response.body()?.data?.get(0)?.others.toString()
                    MemberData.memberAgreeP = response.body()?.data?.get(0)?.agreep.toString()
                    MemberData.memberAgreeS1 = response.body()?.data?.get(0)?.agrees1.toString()
                    MemberData.memberAgreeS2 = response.body()?.data?.get(0)?.agrees2.toString()
                    launcher.launch(Intent(applicationContext,MainActivity::class.java))
                } else if(checkMember == "0"){
                val builder = AlertDialog.Builder(this@LoginActivity)
                builder.setTitle("로그인")
                builder.setMessage("아이디/비밀번호를 다시 입력해주세요.")
                builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
                }
                builder.show()
                binding.loginId.setText("")
                binding.loginPw.setText("")
                }
            }

            override fun onFailure(call: Call<MemberListResponse>, t: Throwable) {
            }
        })
    }
    fun toast(message:String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
    fun printLog(message: String){

    }
}