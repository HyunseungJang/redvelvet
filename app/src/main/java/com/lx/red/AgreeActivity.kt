package com.lx.red

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.lx.red.databinding.ActivityAgreeBinding

class AgreeActivity : AppCompatActivity() {
    lateinit var binding : ActivityAgreeBinding
    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgreeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}