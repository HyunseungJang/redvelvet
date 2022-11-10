package com.lx.red

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lx.red.databinding.ActivityGetHelpBinding

class GetHelpActivity : AppCompatActivity() {
    lateinit var binding : ActivityGetHelpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityGetHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}