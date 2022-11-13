package com.lx.red

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.lx.api.BasicClient
import com.lx.red.databinding.ActivityPostBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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