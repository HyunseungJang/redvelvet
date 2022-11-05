package com.lx.red

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lx.red.databinding.ActivityConfirmationHelpBinding

class ConfirmationHelpActivity : AppCompatActivity() {
    lateinit var binding : ActivityConfirmationHelpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmationHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}