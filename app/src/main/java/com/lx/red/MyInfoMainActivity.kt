package com.lx.red

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lx.red.databinding.ActivityMyInfoMainBinding

class MyInfoMainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMyInfoMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyInfoMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //긴급전화 등록
        binding.addcallButton.setOnClickListener {
            val intent = Intent(this,EmergencyCallActivity::class.java)
            startActivity(intent)
        }
    }
}