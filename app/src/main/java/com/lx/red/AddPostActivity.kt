package com.lx.red

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.lx.red.databinding.ActivityAddPostBinding

class AddPostActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddPostBinding

    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){}
    // 강한구혼구
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}