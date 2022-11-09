package com.lx.red

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lx.red.databinding.ActivityNoticeBinding

class NoticeActivity : AppCompatActivity() {
    lateinit var binding : ActivityNoticeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textView9.text= "위도 : ${AppData.lat}, 경도:${AppData.lng}"
    }
}