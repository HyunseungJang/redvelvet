package com.lx.red

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.service.autofill.UserData
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lx.red.databinding.FragmentPictureListBinding
import com.permissionx.guolindev.PermissionX
import jp.wasabeef.glide.transformations.CropCircleTransformation
import java.io.File
import java.text.SimpleDateFormat
import java.util.*



class PictureListFragment : Fragment() {
    //  바인드처리
    var _binding: FragmentPictureListBinding? = null
    val binding get() = _binding!!
    var pictureAdapter:PictureListAdapter? = null
    //  리사이클 뷰의 리스트 아이템
    var items = ArrayList<UserData>()

    fun getItemCount()= items.size

    //  화면을 넘겨주는 런처
    val classLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        showToast("돌아옴")
    }

    //  화면 열렸을 시
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        _binding = FragmentPictureListBinding.inflate(inflater, container, false)

        initClassList()

        return binding.root

    }

    //  안전 관련 리스트
    fun initClassList() {
        val layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL,false)
        binding.pictureList.layoutManager = layoutManager
        pictureAdapter = PictureListAdapter()
        binding.pictureList.adapter = pictureAdapter

        pictureAdapter?.apply {
            this.items.add(PictureData(R.drawable.janan1,"1","CPR","2022-11-29","따라하는 CPR"))
            this.items.add(PictureData(R.drawable.janan2,"2","재난","2022-11-29","지진났을 때"))
            this.items.add(PictureData(R.drawable.janan3,"3","압사","2022-11-29", "행동요령입니다"))
//            this.items.add(PictureData("4","압사","행동요령"))
//            this.items.add(PictureData("5","압사","행동요령"))
//            this.items.add(PictureData("6","압사","행동요령"))
        }

        pictureAdapter?.listener = object: OnPictureItemClickListner {
            override fun onItemClick(holder: PictureListAdapter.ViewHolder, view: View?, position: Int) {
                pictureAdapter?.apply {
                    val item = items.get(position)
                    showToast("아이템 선택됨 : ${position}, ${item.title}")

                    AppData.pictureList = items
                    AppData.selectedPicture = PictureData(item.photo,item.path,item.title,item.date,item.smalltitle)
                    val classIntent = Intent(activity,VideoActivity::class.java)
                    classLauncher.launch(classIntent)
                }
            }
        }

    }

//    fun initView() {
//        // 업로드 버튼 눌렀을때
//        binding.uploadButton.setOnClickListener {
//            val intent = Intent(this,PictureListFragment::class.java)
//            startActivity(intent)
//            showToast("업로드 버튼 눌림")
//        }
//    }



    /** 토스트 메시지 보여주기 */
    fun showToast(message:String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }
}