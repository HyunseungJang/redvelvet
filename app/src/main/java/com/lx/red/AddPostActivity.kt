package com.lx.red

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lx.red.databinding.ActivityAddPostBinding

class AddPostActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddPostBinding

    // 강한구혼구
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}