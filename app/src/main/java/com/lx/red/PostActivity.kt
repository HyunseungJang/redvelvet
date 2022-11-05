package com.lx.red

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lx.red.databinding.ActivityPostBinding

class PostActivity : AppCompatActivity() {
    lateinit var binding : ActivityPostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //게시물 올리기
        binding.addpostButton.setOnClickListener {
            val intent = Intent(this,AddPostActivity::class.java)
            startActivity(intent)
        }

        //게시물 테스트 자세히 보기
        binding.postdetailButton.setOnClickListener {
            val intent = Intent(this,PostDetailActivity::class.java)
            startActivity(intent)
        }
    }
}