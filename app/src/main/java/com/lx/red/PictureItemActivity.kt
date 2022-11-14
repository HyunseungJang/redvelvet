package com.lx.red


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts



class PictureItemActivity : AppCompatActivity() {

//    lateinit var binding : ActivityPictureListBinding

    val videoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = ActivityPictureListBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        // 강의보러가기 버튼 눌렀을 때
//        binding.pictureList.setOnClickListener {
//
//            val videoIntent = Intent(applicationContext, VideoActivity::class.java)
//            videoLauncher.launch(videoIntent)
//        }

        }



    }
