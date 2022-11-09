package com.lx.red

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.activity_situation.*


class SituationActivity : AppCompatActivity() {

    companion object {
        const val TAG: String = "로그"
    }

    private var pageItemList  = ArrayList<PageItem>()
    private var pageItemList2 = ArrayList<PageItem>()
    private var pageItemList3 = ArrayList<PageItem>()

    private lateinit var myIntroPagerRecyclerAdapter: MyIntroPagerRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_situation)


        //첫번째 요령

        previous_btn.setOnClickListener {
            Log.d(TAG, "MainActivity - 이전 버튼 클릭")

            my_intro_view_pager.currentItem = my_intro_view_pager.currentItem - 1
        }

        next_btn.setOnClickListener {
            Log.d(TAG, "MainActivity - 다음 버튼 클릭")
            my_intro_view_pager.currentItem = my_intro_view_pager.currentItem + 1
        }

        pageItemList.add(PageItem(R.color.colorWhite, R.drawable.cpr1, "1. 심정지 확인 후 도움 및 119 신고요청"))
        pageItemList.add(PageItem(R.color.colorWhite, R.drawable.cpr2, "2. 가슴압박 30회 실시"))
        pageItemList.add(PageItem(R.color.colorWhite, R.drawable.cpr3, "3. 영아에서의 흉부압박"))
        pageItemList.add(PageItem(R.color.colorWhite, R.drawable.cpr4, "4. 인공호흡 2회 실시"))
        pageItemList.add(PageItem(R.color.colorWhite, R.drawable.cpr5, "5. 가슴압박과 인공호흡을 반복"))

        myIntroPagerRecyclerAdapter = MyIntroPagerRecyclerAdapter(pageItemList)

        if (Build.VERSION.SDK_INT < 16) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()

        // 뷰페이저에 설정
        my_intro_view_pager.apply {

            adapter = myIntroPagerRecyclerAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL



        }


        // 두번째 상황대처 및 요령
        previous_btn2.setOnClickListener {
            Log.d(TAG, "MainActivity - 이전 버튼 클릭")

            my_intro_view_pager2.currentItem = my_intro_view_pager2.currentItem - 1
        }

        next_btn2.setOnClickListener {
            Log.d(TAG, "MainActivity - 다음 버튼 클릭")
            my_intro_view_pager2.currentItem = my_intro_view_pager2.currentItem + 1
        }

        pageItemList2.add(PageItem(R.color.colorWhite, R.drawable.hosinsul1, "1. 앞에서 다가올 때"))
        pageItemList2.add(PageItem(R.color.colorWhite, R.drawable.hosinsul2, "2. 앞에서 다가올 때(2)"))
        pageItemList2.add(PageItem(R.color.colorWhite, R.drawable.hosinsul3, "3. 옆에서 다가올 때"))
        pageItemList2.add(PageItem(R.color.colorWhite, R.drawable.hosinsul4, "3. 벽으로 밀쳐올 때"))

        var myIntroPagerRecyclerAdapter2 = MyIntroPagerRecyclerAdapter2(pageItemList2)

        if (Build.VERSION.SDK_INT < 16) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }


        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()

        // 뷰페이저에 설정
        my_intro_view_pager2.apply {

            adapter = myIntroPagerRecyclerAdapter2
            orientation = ViewPager2.ORIENTATION_HORIZONTAL




        }

        //세번째 상황대처 및 요령
        previous_btn3.setOnClickListener {
            Log.d(TAG, "MainActivity - 이전 버튼 클릭")

            my_intro_view_pager3.currentItem = my_intro_view_pager3.currentItem - 1
        }

        next_btn3.setOnClickListener {
            Log.d(TAG, "MainActivity - 다음 버튼 클릭")
            my_intro_view_pager3.currentItem = my_intro_view_pager3.currentItem + 1
        }

        pageItemList3.add(PageItem(R.color.colorWhite, R.drawable.absa1, "1.앞으로 나란히"))
        pageItemList3.add(PageItem(R.color.colorWhite, R.drawable.absa2, "2.한 손으로 반대 팔 안쪽을 잡기"))
        pageItemList3.add(PageItem(R.color.colorWhite, R.drawable.absa3, "3.남은 손으로 반대쪽 팔꿈치 잡기"))

        var myIntroPagerRecyclerAdapter3 = MyIntroPagerRecyclerAdapter3(pageItemList3)

        if (Build.VERSION.SDK_INT < 16) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }


        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()

        // 뷰페이저에 설정
        my_intro_view_pager3.apply {

            adapter = myIntroPagerRecyclerAdapter3
            orientation = ViewPager2.ORIENTATION_HORIZONTAL




        }



    }

}
