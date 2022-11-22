package com.lx.red

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.lx.api.BasicClient
import com.lx.data.MemberAreaResponse
import com.lx.data.MemberListResponse
import com.lx.red.HelpData.Companion.id
import com.lx.red.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    lateinit var binding : ActivityRegisterBinding

    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.sameButton.setOnClickListener {
            checkId()
        }

        binding.btnRegistration.setOnClickListener {
            checkPw()
        }
    }

    //비밀번호 확인 맞을때만 회원가입 가능하게 하기
    fun checkPw() {
        val registerPw = binding.registrationPassword.text.toString()
        val registerPwCheck = binding.registrationPwCheck.text.toString()

        if(registerPw.equals(registerPwCheck)) {
            postMemberAdd()
            addMemberArea()
        } else {
            var builder = AlertDialog.Builder(this,R.style.AppAlertDialogTheme)
            builder.setTitle("회원가입")
            builder.setMessage("비밀번호를 다시 입력해주세요.")
            builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
            }
            builder.show()
        }
    }

    //중복체크
    fun checkId() {
        var registerId = binding.registrationId.text.toString()

        BasicClient.api.postMemberCheckId(
            requestCode = "1001",
            id = registerId
        ).enqueue(object:Callback<MemberListResponse>{
            override fun onResponse(call: Call<MemberListResponse>,response: Response<MemberListResponse>){
                val checkId = response.body()?.header?.total.toString()

                if(checkId == "1"){
                    var builder = AlertDialog.Builder(this@RegisterActivity,R.style.AppAlertDialogTheme)
                    builder.setTitle("중복체크")
                    builder.setMessage("이미 있는 아이디입니다.")
                    builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->

                    }
                    builder.show()
                    binding.registrationId.setText("")
                }
                if(checkId == "0") {
                    var builder = AlertDialog.Builder(this@RegisterActivity,R.style.AppAlertDialogTheme)
                    builder.setTitle("중복체크")
                    builder.setMessage("사용 가능한 아이디입니다.")
                    builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->

                    }
                    builder.show()
                }
            }

            override fun onFailure(call: Call<MemberListResponse>, t: Throwable) {
            }
        })
    }

    //회원리스트 추가 [파라미터]
    fun postMemberAdd(){
        var registerId = binding.registrationId.text.toString()
        var registerName = binding.registrationName.text.toString()
        var registerMobile= binding.registrationMobile.text.toString()
        var registerGender = binding.registrationGender.text.toString()
        var registerBirth = binding.registrationBirthday.text.toString()
        var registerPw = binding.registrationPassword.text.toString()
        var registerAgreeP = binding.registrationBirthday.text.toString()
        var registerAgreeS1 = binding.registrationBirthday.text.toString()
        var registerAgreeS2 = binding.registrationBirthday.text.toString()
        BasicClient.api.memberAdd(
            requestCode = "1001",
            id = registerId,
            pw = registerPw,
            name =registerName,
            birth =registerBirth,
            gender =registerGender,
            phone =registerMobile,
            agreep=registerAgreeP,
            agrees1=registerAgreeS1,
            agrees2=registerAgreeS2
        ).enqueue(object : Callback<MemberListResponse> {
            override fun onResponse(call: Call<MemberListResponse>, response: Response<MemberListResponse>) {
            }

            override fun onFailure(call: Call<MemberListResponse>, t: Throwable) {
                launcher.launch(Intent(applicationContext,LoginActivity::class.java))
            }
        })
    }

    fun addMemberArea(){

        var id = binding.registrationId.text.toString()
        var lat = 0
        var lng = 0
        BasicClient.api.memberArea(
            requestCode = "1001",
            id = id,
            lat = lat,
            lng = lng

        ).enqueue(object : Callback<MemberAreaResponse> {
            override fun onResponse(call: Call<MemberAreaResponse>, response: Response<MemberAreaResponse>) {
            }

            override fun onFailure(call: Call<MemberAreaResponse>, t: Throwable) {
            }
        })
    }

    fun toast(message:String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}