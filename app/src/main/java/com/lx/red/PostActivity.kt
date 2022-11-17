package com.lx.red

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.lx.api.BasicClient
import com.lx.data.CommunityListResponse
import com.lx.red.AppData
import com.lx.red.databinding.ActivityPostBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostActivity : AppCompatActivity() {

    lateinit var binding: ActivityPostBinding

    var listener : OnListItemClickListener? = null

    var listAdapter: ListAdapter? = null

//    var uploadFile : QuestionActivity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initList()
        initView()


        //데이터를 설정하기
        listAdapter?.apply {
            this.items = AppData.qusList
        }

        //질문하기 버튼 눌렀을 때
        binding.qusButton.setOnClickListener {
            val qusIntent = Intent(applicationContext, AddPostActivity::class.java)
            startActivity(qusIntent)
        }
    }

    fun initView() {
        getQuestionList()
    }

    fun initList() {
        //1.
        //linearlayoutManager = 리스트의 모양을 담당하는 것 (아래족으로 아아템이 보이는것,
        // gridlayoutManager = 격자형태로 보이는 것)
        val layoutManager = LinearLayoutManager(this)
        binding.qusList.layoutManager = layoutManager

        //2. 어댑터를 설정하는것
        //실제 데이터를 관리하고 각 아이템의 모양을 만들어 주는 것
        listAdapter = ListAdapter()
        binding.qusList.adapter = listAdapter

        listAdapter?.context = this

        // 4. 아이템을 클릭했을 때 동작할 코드 넣어주기
        listAdapter?.listener =  object: OnListItemClickListener {
            override fun onItemClick(holder: ListAdapter.ViewHolder?, view: View?, position: Int) {
                listAdapter?.apply {
                    val item = items.get(position)
                    showToast("아이템 선택됨 : ${position}, ${item.id}, ${item.title}")

                    //이거 혹시나 빨간줄 떠보여도 오류 아니니 신경 안써도됨 실행하는데 지장없음
//                    val intent = Intent(this@ListActivity, DetailActivity::class.java)
//                    startActivity(intent)
                }
            }
        }

    }

    fun getQuestionList() {

        // API에 있는 리스트 조회 요청하기
        BasicClient.api.getList(
            requestCode = "1001"
        ).enqueue(object: Callback<CommunityListResponse> {
            override fun onResponse(call: Call<CommunityListResponse>, response: Response<CommunityListResponse>) {
//                printLog("onResponse 호출됨 : ${response.body().toString()}")

                // 리스트에 추가
                addToList(response)
            }

            override fun onFailure(call: Call<CommunityListResponse>, t: Throwable) {
//                printLog("onFailure 호출됨 : ${t.message}")

            }
        })

//        printLog("getStudentList 요청함.")

    }

    // 응답받은 데이터를 화면에 있는 리스트에 추가하기
    fun addToList(response: Response<CommunityListResponse>) {

        listAdapter?.apply {
            response.body()?.output?.data?.let {

                this.items.clear()

                for (item in it) {
                    //this.items.add(StudentData(R.drawable.profile1, item.name, item.age, item.mobile))
                    var listData = ListData(item.id, item.title, item.filepath, item.content, item.area, item.time)
                    this.items.add(listData)
                    Log.i("mymy", listData.toString())
                }
            }
            this.notifyDataSetChanged()
        }
    }
    fun showToast(message:String){
        Toast.makeText(this,message, Toast.LENGTH_LONG).show()
    }

}