package com.lx.red

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lx.red.databinding.PictureItemBinding
import jp.wasabeef.glide.transformations.CropCircleTransformation
import java.io.File

class PictureListAdapter : RecyclerView.Adapter<PictureListAdapter.ViewHolder>() {
    // 각 아이템에 보여질 데이터를 담고 있는 것
    var items = ArrayList<PictureData>()

    var listener: OnPictureItemClickListner? = null

    var context: Context? = null

    // 리싸이클러뷰가 아이템 갯수가 몇개인지 물어볼 때 / 아이템몇개 가지고있는지 알수있는 함수
    override fun getItemCount() = items.size

    // 각 아이템의 모양이 처음 만들어질 때
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PictureItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // 이미 만들어진 아이템의 모양에 데이터만 설정할 때
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.setItem(item)
    }
    //각 아이템의 모양을 재사용 하기위해 만들어진것
    inner class ViewHolder(val binding: PictureItemBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener{
                listener?.onItemClick(this, binding.root, adapterPosition)
            }
        }
        // 데이터 설정
        fun setItem(item: PictureData) {

            // 이미지 표시하기
            val file = File(item.path)
            val uri = Uri.fromFile(file)

            context?.apply {
                Glide.with(this) // 글라이드를 사용하는데
                    .load(uri)                      // 이미지 파일을 읽어와서,
                    // 사진을 동그라미로 보여줌
                    .apply(RequestOptions.bitmapTransform(CropCircleTransformation()))
                    .into(binding.pictureView)          // 이미지뷰에 넣어라
            }


            //이미지 표시하기
            item.photo?.let{binding.pictureView.setImageResource(it)}

            // 제목 표시하기
            binding.titleOutput.text = item.title
            // 일시 표시하기
            binding.timeOutput.text = item.date
            // 소제목 표시하기
            binding.smallTitleOutput.text = item.smalltitle
        }
    }

}