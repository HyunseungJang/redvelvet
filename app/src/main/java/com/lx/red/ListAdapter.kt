package com.lx.red

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lx.red.databinding.ListItemBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ListAdapter : RecyclerView.Adapter<ListAdapter.ViewHolder>(){

    //각 아이템에 보여질 데이터를 담고 있는 것
    var items = ArrayList<ListData>()

    var listener : OnListItemClickListener? = null

    var context:Context? = null

    val dateFormat1 = SimpleDateFormat("yyyy-MM-dd HH:mm")

    override fun getItemCount(): Int = items.size

    //각 아이템의 모양이 처음 만들어질때
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapter.ViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        Log.i("mymy","onCreateViewHolder")
        return ViewHolder(binding)
    }

    // 이미 만들어진 아이템의 모양에 데이터만 설정할 때
    override fun onBindViewHolder(holder: ListAdapter.ViewHolder, position: Int) {
        Log.i("mymy","onBindViewHolder")
        val item = items[position]
        holder.setItem(item)
    }
    inner class ViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        init{
            binding.root.setOnClickListener{
                listener?.onItemClick(this, binding.root, adapterPosition)
            }
        }

        //데이터 설정
        fun setItem(item : ListData) {

            //이미지 표시하기
//            val file = File(item.profile)
//            val uri = Uri.fromFile(file)
//
//            context?.apply {
//                Glide.with(this)
//                    .load(uri)
//                    .into(binding.profileView)
//            }

            //닉네임 설정
            binding.nameOutput.text = item.id

            //제목 설정
            binding.titleOutput.text = item.title

            // 글라이드를 이용해서 웹서버에 올린 이미지를 가져와서 보여주기
            item.profile?.apply {
                //val uri = Uri.parse("http://172.168.10.11:8001/images/profile21664852389750.jpg")
                val uri = Uri.parse("http://172.168.10.79:8001${this}")

                Glide.with(binding.profileView)         // 글라이드를 사용하는데,
                    .load(uri)                              // 이미지 파일을 읽어와서,
                    .into(binding.profileView)                  // 이미지뷰에 넣어주세요
                Log.i("mymy", binding.profileView.toString())
            }

            //내용 설정
            binding.contentOutput.text = item.content

            //작성일자 설정
            val date = dateFormat1.format(Date())
            binding.dateOutput.text = date
        }
    }
}