package com.lx.red

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lx.red.databinding.ActivityMyInfoMainBinding
import com.lx.red.databinding.ActivityMyInfoUpdateBinding

class MyInfoMainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMyInfoMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyInfoMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.outputName.text = "${MemberData.memberId+"의 정보"}"
        binding.outputTall.text= "${MemberData.memberHeight}"
        binding.outputWeight.text= "${MemberData.memberWeight}"
        binding.outputMedi.text= "${MemberData.memberMedicine}"
        binding.outputBlood.text= "${MemberData.memberBloodtype}"
        binding.outputSick.text= "${MemberData.memberDisease}"
        binding.outputOther.text= "${MemberData.memberOther}"
        binding.outputCertifi.text= "${MemberData.memberCertificate}"
        binding.outputEmer.text= "${MemberData.memberEmernum}"
        //내정보
        binding.goupdateButton.setOnClickListener {
            val intent = Intent(this,MyInfoUpdateActivity::class.java)
            startActivity(intent)
        }

    }

}



