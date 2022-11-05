package com.lx.red

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lx.red.databinding.ActivityHelpRequestBinding

class HelpRequestActivity : AppCompatActivity() {
    lateinit var binding : ActivityHelpRequestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.confirmhelpButton.setOnClickListener {
            val intent = Intent(this,ConfirmationHelpActivity::class.java)
            startActivity(intent)
        }
    }
}