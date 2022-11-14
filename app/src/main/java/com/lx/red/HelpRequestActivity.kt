package com.lx.red

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.result.contract.ActivityResultContracts
import com.lx.red.databinding.ActivityHelpRequestBinding

class HelpRequestActivity : AppCompatActivity() {
    lateinit var binding : ActivityHelpRequestBinding

    var helpCheck = 1//

    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){}

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
            lunActivity()
            helpCheck = 2
        }
    }

    fun run() {
        launcher.launch(Intent(applicationContext,ConfirmationHelpActivity::class.java))
    }
    fun lunActivity(){
        launcher.launch(Intent(applicationContext,MainActivity::class.java))
    }
}