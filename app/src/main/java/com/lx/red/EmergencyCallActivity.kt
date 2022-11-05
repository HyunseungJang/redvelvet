package com.lx.red

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lx.red.databinding.ActivityEmergencyCallBinding

class EmergencyCallActivity : AppCompatActivity() {
    lateinit var binding : ActivityEmergencyCallBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmergencyCallBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}