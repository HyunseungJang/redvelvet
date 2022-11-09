package com.lx.red

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lx.red.databinding.ActivityWarningBinding

class WarningActivity : AppCompatActivity() {
    lateinit var binding : ActivityWarningBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWarningBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button2.setOnClickListener {
            var intent = Intent(this,HelpRequestActivity::class.java)
            startActivity(intent)
        }
        binding.button3.setOnClickListener {
            finish()
        }
    }
}