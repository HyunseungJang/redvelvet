package com.lx.red

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.lx.red.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //공지사항
        binding.noticeButton.setOnClickListener {
            val intent = Intent(this,NoticeActivity::class.java)
            startActivity(intent)
        }

        //구조요청
        binding.helpButton.setOnClickListener {
            val intent = Intent(this,HelpRequestActivity::class.java)
            startActivity(intent)
        }

        //상황대처 정보
        binding.infoButton.setOnClickListener {
            val intent = Intent(this,InformationActivity::class.java)
            startActivity(intent)
        }

        //내정보
        binding.myinfoButton.setOnClickListener {
            val intent = Intent(this,MyInfoMainActivity::class.java)
            startActivity(intent)
        }

        //게시판
        binding.postButton.setOnClickListener {
            val intent = Intent(this,PostActivity::class.java)
            startActivity(intent)
        }


        // --펼치기 레이아웃 start 강헌구--
        binding.plusLayout.setOnClickListener {
            if(binding.layoutDetail02.visibility == View.VISIBLE) {
                binding.layoutDetail02.visibility = View.GONE
                binding.layoutBtn01.animate().apply {
                    duration = 300
                    rotation(0f)
                }
            } else {
                binding.layoutDetail02.visibility = View.VISIBLE
                binding.layoutBtn01.animate().apply {
                    duration = 300
                    rotation(180f)
                }
            }
        }
        // --펼치기 레이아웃 end--


    }
}
