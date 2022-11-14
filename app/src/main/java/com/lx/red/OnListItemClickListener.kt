package com.lx.red

import android.view.View

interface OnListItemClickListener {

    fun onItemClick(holder: ListAdapter.ViewHolder?, view: View?, position: Int)

}