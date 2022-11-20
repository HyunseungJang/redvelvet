package com.lx.red

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.VideoView

import com.lx.red.databinding.ActivityVideoBinding


class VideoActivity : AppCompatActivity() {

    lateinit var binding : ActivityVideoBinding

    var video: VideoView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // 비디오 영상 재생하기
        video = findViewById(R.id.videoView2)
        video?.setVideoPath("android.resource://" + packageName + "/" + R.raw.test)
//        video?.setVideoURI(Uri.parse("${AppData.selectedPicture?.path}"))
        //video?.setVideoURI(Uri.parse("http://download.atmark-techno.com/sample/bbb/big-buck-bunny-30sec-800x480.mp4"))
//        video?.setVideoURI(Uri.parse("android.resource://MyRed/" + R.raw.ha))
//        video?.setOnPreparedListener {
//            it.start()
//        }



        binding.startButton.setOnClickListener {
            video?.start()
        }

        binding.stopButton.setOnClickListener {
            video?.pause()
        }

        binding.backButton.setOnClickListener {
            finish()
        }

        AppData.selectedPicture?.apply {
            binding.titleOutput1.text="${this.title}"
        }

        AppData.selectedPicture?.apply {
            binding.explainOutput.text="${this.date}"
        }

        AppData.selectedPicture?.apply {
            binding.explainOutput.text="${this.smalltitle}"
        }



    }

}