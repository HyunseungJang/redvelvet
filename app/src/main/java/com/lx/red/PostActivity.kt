package com.lx.red

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.lx.red.databinding.ActivityPostBinding

class PostActivity : AppCompatActivity() {
    lateinit var binding : ActivityPostBinding

    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //게시물 올리기
        binding.addpostButton.setOnClickListener {
            launcher.launch(Intent(applicationContext,AddPostActivity::class.java))
        }
    }
}