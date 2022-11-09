package com.lx.red

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lx.red.MyIntroPagerViewHolder
import com.lx.red.PageItem
import com.lx.red.R

class MyIntroPagerRecyclerAdapter(private var pageList: ArrayList<PageItem>) : RecyclerView.Adapter<MyIntroPagerViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyIntroPagerViewHolder {
        return MyIntroPagerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_intro_pager_item, parent, false))
    }

    override fun getItemCount(): Int {
        return pageList.size
    }

    override fun onBindViewHolder(holder: MyIntroPagerViewHolder, position: Int) {
        holder.bindWithView(pageList[position])
    }

}
