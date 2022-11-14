package com.lx.red

import android.view.View

interface OnPictureItemClickListner {

    fun onItemClick(holder: PictureListAdapter.ViewHolder, view: View?, position: Int)

}