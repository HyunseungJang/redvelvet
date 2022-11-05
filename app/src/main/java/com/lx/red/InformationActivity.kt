package com.lx.red

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lx.red.databinding.ActivityInformationBinding

class InformationActivity : AppCompatActivity() {
    lateinit var binding : ActivityInformationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //재난대피지역
        binding.safezoneButton.setOnClickListener {
            val intent = Intent(this,SafeDisasterActivity::class.java)
            startActivity(intent)
        }

        //상황대처
        binding.situationButton.setOnClickListener {
            val intent = Intent(this,SituationActivity::class.java)
            startActivity(intent)
        }

        //목소리(변조,녹음)
        binding.voiceButton.setOnClickListener {
            val intent = Intent(this,VoiceActivity::class.java)
            startActivity(intent)
        }
    }
}