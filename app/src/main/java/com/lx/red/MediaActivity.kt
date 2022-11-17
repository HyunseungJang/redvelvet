package com.lx.red

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.VideoView
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lx.red.PictureListFragment
import com.lx.red.R
import com.lx.red.databinding.ActivityMediaBinding
import com.permissionx.guolindev.PermissionX
import jp.wasabeef.glide.transformations.CropCircleTransformation
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.xml.transform.Source


class MediaActivity : AppCompatActivity() {
    lateinit var binding: ActivityMediaBinding

    /** 부분화면 구분자 */
    enum class FragmentItem {
        ITEM1, ITEM2, ITEM3
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /** 첫 화면설정 */
        onFragmentChanged(FragmentItem.ITEM1)

        binding.backButton3.setOnClickListener {
            finish()
        }
    }

    /** 부분 화면 변경하고자 하는 경우 호출 */
    fun onFragmentChanged(index:FragmentItem) {

        when(index) {
            FragmentItem.ITEM1 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, PictureListFragment()).commit()
            }
            FragmentItem.ITEM2 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, PictureListFragment()).commit()
            }
            FragmentItem.ITEM3 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, PictureListFragment()).commit()
            }
        }
        supportFragmentManager.beginTransaction().replace(R.id.container, PictureListFragment()).commit()
    }
}