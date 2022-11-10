package com.lx.red

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.lx.red.databinding.ActivityHelpRequestBinding

class HelpRequestActivity : AppCompatActivity() {
    lateinit var binding : ActivityHelpRequestBinding
    var helpCheck = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        object : CountDownTimer(10000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                binding.count.text = "" + millisUntilFinished / 1000
            }

            override fun onFinish() {
                if(helpCheck==2) {

                } else {
                    run()
                }
            }
        }.start()

        binding.confirmhelpButton.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            helpCheck = 2
        }
    }

    fun run() {
        val intent = Intent(this,ConfirmationHelpActivity::class.java)
        startActivity(intent)
    }
}