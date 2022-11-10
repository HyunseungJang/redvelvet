package com.lx.red

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.lx.red.databinding.ActivityEmergencyCallBinding

class EmergencyCallActivity : AppCompatActivity() {
    lateinit var binding : ActivityEmergencyCallBinding

    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmergencyCallBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}