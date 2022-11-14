package com.lx.red

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.lx.api.BasicClient
import com.lx.data.CommunityListResponse
import com.lx.data.FileUploadResponse
import com.lx.red.AppData
import com.lx.red.ListData
import com.lx.red.databinding.ActivityAddPostBinding
import com.permissionx.guolindev.PermissionX
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class AddPostActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddPostBinding

    val mainLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback {

        }
    )

    var bitmap: Bitmap? = null

    var listAdapter: ListAdapter? = null

    //날짜  -> 글자, 글자 -> 날짜 바꿀때 사용하는 것
    val dateFormat = SimpleDateFormat("yyyyMMddHHmmss")

    //저장할 파일 명
    val filename:String? = null

    //사진찍기를 위한 런처1
    val captureLauncher1 = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//        AppData.showToast(this, "돌아옴")

        when(it.resultCode) {
            RESULT_OK -> {
                bitmap = it.data?.extras?.get("data") as Bitmap
                binding.qusImage1.setImageBitmap(bitmap)

            }
            RESULT_CANCELED -> {
//                AppData.showToast(this, "사진취소")
            }
        }
    }
    //사진찍기를 위한 런처2
    val captureLauncher2 = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//        AppData.showToast(this, "돌아옴")

        when(it.resultCode) {
            RESULT_OK -> {
                bitmap = it.data?.extras?.get("data") as Bitmap
                binding.qusImage2.setImageBitmap(bitmap)

            }
            RESULT_CANCELED -> {
//                AppData.showToast(this, "사진취소")
            }
        }
    }
    //사진찍기를 위한 런처3
    val captureLauncher3 = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//        AppData.showToast(this, "돌아옴")

        when(it.resultCode) {
            RESULT_OK -> {
                bitmap = it.data?.extras?.get("data") as Bitmap
                binding.qusImage3.setImageBitmap(bitmap)

            }
            RESULT_CANCELED -> {
//                AppData.showToast(this, "사진취소")
            }
        }
    }

    //앨범에서 가져오기 런처 만들기
    val albumLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//        AppData.showToast(this, "앨범에서 돌아옴")

//        when(it.resultCode) {
//            RESULT_OK -> {
//                it.data?.apply {
//                    val imageUri = this.data
//                    imageUri.let {
//                        bitmap = MediaStore.Images.Media.getBitmap(contentResolver, it)
//                        binding.qusImage1.setImageBitmap(bitmap)
//                    }
//                }
//            }
//            RESULT_CANCELED -> {
////                AppData.showToast(this, "앨범 선택 취소")
//            }
//        }
        when(it.resultCode) {
            RESULT_OK -> {
                it.data?.apply {
                    val imageUri = this.data
                    imageUri.let {
                        bitmap = MediaStore.Images.Media.getBitmap(contentResolver, it)
                        binding.qusImage2.setImageBitmap(bitmap)
                    }
                }
            }
            RESULT_CANCELED -> {
//                AppData.showToast(this, "앨범 선택 취소")
            }
        }
        when(it.resultCode) {
            RESULT_OK -> {
                it.data?.apply {
                    val imageUri = this.data
                    imageUri.let {
                        bitmap = MediaStore.Images.Media.getBitmap(contentResolver, it)
                        binding.qusImage3.setImageBitmap(bitmap)
                    }
                }
            }
            RESULT_CANCELED -> {
//                AppData.showToast(this, "앨범 선택 취소")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //위험권한 요청하기
        PermissionX.init(this)
            .permissions(Manifest.permission.CAMERA)
            .request { allGranted, grantedList, deniedList ->
                if(allGranted) {
//                    AppData.showToast(this, "모든권한 부여됨")
                }else {
//                    AppData.showToast(this, "권한 거부됨")
                }
            }

        initView()

        binding.backButton2.setOnClickListener{
            finish()
        }
//        binding.albumButton.setOnClickListener{
//
//            val albumIntent = Intent(Intent.ACTION_GET_CONTENT)
//            albumIntent.type = "image/*"
//            albumLauncher.launch(albumIntent)
//        }

        // 리스트 화면으로 버튼 눌렀을 때
        binding.uploadButton.setOnClickListener{

            saveFile()
            Thread.sleep(3000)
//            getList()

            val listIntent = Intent(this, PostActivity::class.java)
            startActivity(listIntent)

        }
    }


    //뷰 초기화
    fun initView() {

        //사진찍기 버튼 눌렀을 때 Start
        binding.cameraButton1.setOnClickListener {
            //단말의 카메라 앱 띄워주기
            val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE) // 액션이미지캡쳐는 카메라 앱을 의미한다
            captureLauncher1.launch(captureIntent)
            //이렇게 하고 매니페스트 가서 권한을 입력해준다
        }
        binding.cameraButton2.setOnClickListener {

            val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE) // 액션이미지캡쳐는 카메라 앱을 의미한다
            captureLauncher2.launch(captureIntent)
        }
        binding.cameraButton3.setOnClickListener {

            val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE) // 액션이미지캡쳐는 카메라 앱을 의미한다
            captureLauncher3.launch(captureIntent)
        }
        //사진찍기 버튼 눌렀을 때 End


//        binding.albumButton.setOnClickListener {
//
//            loadFile()
//        }

//        getList()

    }

    fun saveFile() {

        //파일 이름 만들기
        val filename = dateFormat.format(Date()) + ".jpg"

        val title = binding.titleInput.text.toString()
        val content = binding.contentInput.text.toString()
        val id = binding.nameInput.text.toString()

        bitmap?.apply {

            openFileOutput(filename, Context.MODE_PRIVATE).use {
                this.compress(Bitmap.CompressFormat.JPEG, 100, it)
                it.close()

                //리스트에 현재 사진정보 추가하기
                val file = File("${filesDir}/${filename}")
                AppData.qusList.add(ListData(id, title, file.absolutePath, content, "논현동", "date"))

                uploadFile(filename)
//                AppData.showToast(this@QuestionActivity, "이미지를 파일로 저장함 ${AppData.qusList}")
            }
        }


    }

    fun uploadFile(filename:String) {
        println("uploadFile 호출됨 : ${filename}")

        val title = binding.titleInput.text.toString()
        val content = binding.contentInput.text.toString()
        val id = binding.nameInput.text.toString()

        // 저장한 파일에 대한 정보를 filePart로 만들기
        val file = File("${filesDir}/${filename}")

        val filePart = MultipartBody.Part.createFormData(
            "photo",
            filename,
            file.asRequestBody("image/jpg".toMediaTypeOrNull())
        )

        // 추가 파라미터가 있는 경우
        val params = hashMapOf<String, String>()

        // API에 있는 리스트 조회 요청하기
        BasicClient.api.communityUploadFile(
            file = filePart,
            params = params
        ).enqueue(object: Callback<FileUploadResponse> {
            override fun onResponse(call: Call<FileUploadResponse>, response: Response<FileUploadResponse>) {
                printLog("onResponse 호출됨 : ${response.body().toString()}")

                response.body()?.output?.filename?.apply {
                    postListAdd(id, title, this, content, "논현동", "date")
                }

            }

            override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                printLog("onFailure 호출됨 : ${t.message}")

            }
        })

        printLog("uploadFile 요청함")
    }

    fun postListAdd(id:String, title: String, filepath: String, content: String, area: String, date: String) {

        // API에 있는 리스트 조회 요청하기
        BasicClient.api.postCommunityAdd(
            requestCode = "1001",
            id = id,
            title = title,
            filepath = filepath,
            content = content,
            area = area,
            time = date
        ).enqueue(object: Callback<CommunityListResponse> {
            override fun onResponse(call: Call<CommunityListResponse>, response: Response<CommunityListResponse>) {
                // 성공 응답
                printLog("onResponse 호출됨 : ${response.body().toString()}")

//                response.body()?.output?.filename?.apply{
//
//                    postStudentAdd("홍길동31", "31", "010-8000-1234", this)
//                }
            }

            override fun onFailure(call: Call<CommunityListResponse>, t: Throwable) {
                // 실패 응답
                printLog("onFailure 호출됨 : ${t.message}")

            }
        })

        printLog("uploadFile 요청함")


    }

    /*    fun loadFile() {

            filename?.apply {
                val file = File("${filesDir}/${this}")
                val uri = Uri.fromFile(file)

                Glide.with(this@QuestionMainActivity)       //글라이드를 사용하는데
                    .load(uri)                             //이미지 파일을 읽어와서
                    .into(binding.qusImage1)                 //이미지뷰에 넣어주세여
            }?:run {
    //            AppData.showToast(this,"최근에 찍은 사진이 없습니다.")
            }
            filename?.apply {
                val file = File("${filesDir}/${this}")
                val uri = Uri.fromFile(file)

                Glide.with(this@QuestionMainActivity)       //글라이드를 사용하는데
                    .load(uri)                             //이미지 파일을 읽어와서
                    .into(binding.qusImage2)                 //이미지뷰에 넣어주세여
            }?:run {
    //            AppData.showToast(this,"최근에 찍은 사진이 없습니다.")
            }
            filename?.apply {
                val file = File("${filesDir}/${this}")
                val uri = Uri.fromFile(file)

                Glide.with(this@QuestionMainActivity)       //글라이드를 사용하는데
                    .load(uri)                             //이미지 파일을 읽어와서
                    .into(binding.qusImage3)                 //이미지뷰에 넣어주세여
            }?:run {
    //            AppData.showToast(this,"최근에 찍은 사진이 없습니다.")
            }

        }

        fun getList() {

            // API에 있는 리스트 조회 요청하기
            BasicClient.api.getList(
                requestCode = "1001"
            ).enqueue(object: Callback<CommunityListResponse> {
                override fun onResponse(call: Call<CommunityListResponse>, response: Response<CommunityListResponse>) {
                    // 성공 응답
                    printLog("onResponse 호출됨 : ${response.body().toString()}")

                    // 리스트에 추가
                    addToList(response)
                }

                override fun onFailure(call: Call<CommunityListResponse>, t: Throwable) {
                    // 실패 응답
                    printLog("onFailure 호출됨 : ${t.message}")

                }
            })

            printLog("getList 요청함")
        }

     */
    fun printLog(message: String) {
//        binding.uploadButton.append("${message} \n")
    }

}