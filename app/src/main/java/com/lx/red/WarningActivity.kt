package com.lx.red

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.lx.red.databinding.ActivityWarningBinding

class WarningActivity : AppCompatActivity() {
    lateinit var binding : ActivityWarningBinding

    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWarningBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button2.setOnClickListener {
            launcher.launch(Intent(applicationContext,HelpRequestActivity::class.java))
        }
        binding.button3.setOnClickListener {
            finish()
        }
    }
}