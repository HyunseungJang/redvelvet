package com.lx.red

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.VideoView
import com.lx.red.common.logger.Log

import com.lx.red.databinding.ActivityVideoBinding
import kotlinx.android.synthetic.main.activity_video.*
import retrofit2.http.Url


class VideoActivity : AppCompatActivity() {

    lateinit var binding : ActivityVideoBinding

    var video: VideoView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // 비디오 영상 재생하기
        var video = binding.videoView2
//        video?.setVideoURI(Uri.parse("${AppData.selectedPicture?.path}"))
//        video?.setVideoURI(Uri.parse("android.resource://MyRed/" + R.raw.haha))

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
            binding.titleOutput1.text= this.title
        }

        AppData.selectedPicture?.apply {
            binding.explainOutput.text=this.date
        }

        AppData.selectedPicture?.apply {
            binding.explainOutput.text=this.smalltitle
        }

    }

}