package com.lx.red

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lx.red.databinding.ActivitySafeDisasterBinding

class SafeDisasterActivity : AppCompatActivity() {
    lateinit var binding : ActivitySafeDisasterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySafeDisasterBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}