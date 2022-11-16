package com.lx.red

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lx.red.databinding.ActivityCaptureBinding
import com.permissionx.guolindev.PermissionX
import jp.wasabeef.glide.transformations.CropCircleTransformation
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class CaptureActivity : AppCompatActivity() {
    lateinit var binding : ActivityCaptureBinding
        // savefile변수
    var bitmap: Bitmap? = null

    // 저장할 파일명
    var filename:String? = null

    // 날짜 -> 글자, 글자 -> 날짜 바꿀때 사용하는 것
    val dateFormat1 = SimpleDateFormat("yyyyMMddHHmmss")
    val dateFormat2 = SimpleDateFormat("yyyy-MM-dd HH:mm")

    // 사진찍기를 위한 런처
    val caputureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        AppData.showToast(this,"사진 찍고 돌아옴")

        when(it.resultCode) {
            AppCompatActivity.RESULT_OK -> {
                bitmap = it.data?.extras?.get("data") as Bitmap
                binding.imageView.setImageBitmap(bitmap)

            }
            AppCompatActivity.RESULT_CANCELED -> {
                AppData.showToast(this, "사진찍다 취소함")
            }
        }
    }
    // 앨범에서 가져오기 위한 런쳐
    val albumLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        AppData.showToast(this,"앨범에서 돌아옴")

        when(it.resultCode) {
            AppCompatActivity.RESULT_OK -> {
                it.data?.apply {
                    val imageURI = this.data
                    imageURI?.let {
                        bitmap = MediaStore.Images.Media.getBitmap(contentResolver, it)
                        binding.imageView.setImageBitmap(bitmap)
                    }
                }
            }
            AppCompatActivity.RESULT_CANCELED -> {
                AppData.showToast(this, "앨범 선택 취소")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCaptureBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // 위험권한 요청하기
        PermissionX.init(this)
            .permissions(Manifest.permission.CAMERA)
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    AppData.showToast(this, "모든 위험권한 부여됨")
                } else {
                    AppData.showToast(this, "권한 거부됨")
                }
            }
        initView()


    }
    // 뷰 초기화
    fun initView() {
        // 사진 찍기버튼 눌렀을때
        binding.captureButton3.setOnClickListener {
            // 단말의 카메라 앱 띄워주기
            val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            caputureLauncher.launch(captureIntent)
        }
        // 사진 가져오기 버튼 눌렀을때
        binding.loadButton2.setOnClickListener {
            loadFile()
        }
        // 앨범에서 가져오기 버튼 눌렀을때
        binding.albumButton3.setOnClickListener {
            val albumIntent = Intent(Intent.ACTION_GET_CONTENT)
            albumIntent.type = "image/*"
            albumLauncher.launch(albumIntent)
        }
        // 저장 버튼 눌렀을떄
        binding.saveButton.setOnClickListener {
            saveFile()
        }
        // 리스트 화면으로 버튼 눌렀을때
        binding.showListButton2.setOnClickListener {
            val listIntent = Intent(this, CaptureActivity::class.java)
            startActivity(listIntent)
        }
    }
    // 파일 저장하는 함수
    fun saveFile() {

        // 파일 이름 만들기
        filename = dateFormat1.format(Date()) + ".jpg"

        // 일시 표시
        val date = dateFormat2.format(Date())
        binding.dateOutput5.text = date

        // 타이틀 (사용자가입력한글자)
        val title = binding.input4.text.toString()


        bitmap?.apply {
            openFileOutput(filename, Context.MODE_PRIVATE).use {
                this.compress(Bitmap.CompressFormat.JPEG, 100, it)
                it.close()

                // 리스트에 현재 사진정보 추가하기
                val file = File("${filesDir}/${filename}")
//                AppData.pictureList.add(PictureData(file.absolutePath, title, date, smalltitle = toString()))

                AppData.showToast(this@CaptureActivity, "이미지를 파일로 저장함 : ${AppData.pictureList.size}")
            }
        }
    }
    // 파일 불러오기
    fun loadFile() {

        filename?.apply {
            val file = File("${filesDir}/${this}")
            val uri = Uri.fromFile(file)

            Glide.with(this@CaptureActivity) // 글라이드를 사용하는데
                .load(uri)                      // 이미지 파일을 읽어와서,
                // 사진을 동그라미로 보여줌
                .apply(RequestOptions.bitmapTransform(CropCircleTransformation()))
                .into(binding.imageView)          // 이미지뷰에 넣어라

        }?:run { //null이 맞은경우를 의미
            AppData.showToast(this, "최근에 찍은 사진이 없습니다")
        }
    }

}