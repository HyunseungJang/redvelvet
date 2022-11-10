package com.lx.red

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lx.api.BasicClient
import com.lx.data.MemberListResponse
import com.lx.red.databinding.ActivityMyInfoUpdateBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyInfoUpdateActivity : AppCompatActivity() {
    lateinit var binding : ActivityMyInfoUpdateBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyInfoUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.outputName.text = "${MemberData.memberId+"의 정보"}"
        binding.infoinputTall.setText("${MemberData.memberHeight}")
        binding.infoinputWeight.setText("${MemberData.memberWeight}")
        binding.infoinputMedi.setText("${MemberData.memberMedicine}")
        binding.infoinputBlood.setText("${MemberData.memberBloodtype}")
        binding.infoinputSick.setText("${MemberData.memberDisease}")
        binding.infoinputOther.setText("${MemberData.memberOther}")
        binding.infoinputCertifi.setText("${MemberData.memberCertificate}")
        binding.infoinputEmer.setText("${MemberData.memberEmernum}")


        //긴급전화 등록
        binding.addcallButton.setOnClickListener {
            val intent = Intent(this, EmergencyCallActivity::class.java)
            startActivity(intent)
        }

        binding.updateButton.setOnClickListener {
            updateMember()

        }

    }

    //회원 정보 수정 요청하기
    fun updateMember() {
        val memberWeight = binding.infoinputWeight.text.toString()
        val memberHeight = binding.infoinputTall.text.toString()
        val memberCertificate = binding.infoinputCertifi.text.toString()
        val memberMedicine = binding.infoinputMedi.text.toString()
        val memberDisease = binding.infoinputSick.text.toString()
        val memberEmernum = binding.infoinputEmer.text.toString()
        val memberOther = binding.infoinputOther.text.toString()
        val memberBlood = binding.infoinputBlood.text.toString()

        BasicClient.api.memberUpdate(
            requestCode = "1001",
            id = MemberData.memberId.toString(),
            height = memberHeight,
            weight = memberWeight,
            certificate = memberCertificate,
            medicine = memberMedicine,
            disease = memberDisease,
            emernum = memberEmernum,
            others = memberOther,
            bloodtype = memberBlood
        ).enqueue(object : Callback<MemberListResponse> {
            override fun onResponse(call: Call<MemberListResponse>, response: Response<MemberListResponse>) {
                val intent = Intent(this@MyInfoUpdateActivity, MainActivity::class.java)
                startActivity(intent)
            }
            override fun onFailure(call: Call<MemberListResponse>, t: Throwable) {
                binding.outputName.text = "${t.message}"
            }

        })

    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}