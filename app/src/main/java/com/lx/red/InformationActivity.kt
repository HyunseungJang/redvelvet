package com.lx.red

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.lx.red.databinding.ActivityInformationBinding

class InformationActivity : AppCompatActivity() {
    lateinit var binding : ActivityInformationBinding

    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //재난대피지역
        binding.safezoneButton.setOnClickListener {
            launcher.launch(Intent(applicationContext,SkyActivity::class.java))
        }

        //상황대처
        binding.situationButton.setOnClickListener {
            launcher.launch(Intent(applicationContext,SituationActivity::class.java))
        }

        //목소리(변조,녹음)
        binding.voiceButton.setOnClickListener {
            launcher.launch(Intent(applicationContext,VoiceActivity::class.java))
        }

        //사고대처요령 영상
        binding.accidentButton.setOnClickListener {
            launcher.launch(Intent(applicationContext,MediaActivity::class.java))
        }
    }
}