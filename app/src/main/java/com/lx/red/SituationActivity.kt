package com.lx.red

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lx.red.databinding.ActivitySituationBinding

class SituationActivity : AppCompatActivity() {
    lateinit var binding : ActivitySituationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySituationBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}