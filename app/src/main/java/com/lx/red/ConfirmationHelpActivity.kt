package com.lx.red

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.lx.red.databinding.ActivityConfirmationHelpBinding
import kotlinx.android.synthetic.main.activity_confirmation_help.*

class ConfirmationHelpActivity : AppCompatActivity() {
    lateinit var binding : ActivityConfirmationHelpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmationHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 다이얼로그
        val builder = AlertDialog.Builder(this)
        builder.setTitle("112 신고 완료")
            .setIcon(R.drawable.location)
            .setMessage("<반경 10m 이내> 레드 이용자에게 구조 신호를 보내고 있습니다.")
            .setPositiveButton("닫기",
                DialogInterface.OnClickListener { dialog, id ->
                    resultText.text = "닫기 클릭"
                })
        // 다이얼로그를 띄워주기
        builder.show()
    }
}