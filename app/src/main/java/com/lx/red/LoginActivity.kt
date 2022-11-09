package com.lx.red

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.lx.api.BasicClient
import com.lx.data.MemberListResponse
import com.lx.red.MemberData.Companion.memberId
import com.lx.red.MemberData.Companion.memberPw
import com.lx.red.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    lateinit var binding : ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //로그인 버튼
        binding.loginButton.setOnClickListener {
            readMember()
        }
        //회원가입 버튼
        binding.registrationButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
    fun readMember() {
        var id = binding.loginId.text.toString()
        var pw = binding.loginPassword.text.toString()

        BasicClient.api.postMemberRead(
            requestCode = "1001",
            id = id,
            pw = pw
        ).enqueue(object : Callback<MemberListResponse> {
            override fun onResponse(call: Call<MemberListResponse>, response: Response<MemberListResponse>) {
                val checkMember = response.body()?.header?.total.toString()
                if(checkMember == "1"){
                    memberId = memberId
                    memberPw = memberPw
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
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                } else if(checkMember == "0"){
                val builder = AlertDialog.Builder(this@LoginActivity)
                builder.setTitle("로그인")
                builder.setMessage("아이디/비밀번호를 다시 입력해주세요.")
                builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
                }
                builder.show()
                binding.loginId.setText("")
                binding.loginPassword.setText("")
                }
            }

            override fun onFailure(call: Call<MemberListResponse>, t: Throwable) {
                printLog("${t.message}")
            }
        })
    }
    fun toast(message:String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
    fun printLog(message: String){

    }
}