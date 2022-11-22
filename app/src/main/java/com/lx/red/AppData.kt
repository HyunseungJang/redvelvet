package com.lx.red

import android.content.Context
import android.widget.Toast

class AppData {
    companion object{
        var lat: String? = null
        var lng: String? = null
        var id: String? = null

        var data1:String? = null
        var listItem: ListData?= null
        val qusList = ArrayList<ListData>()

        fun showToast(context: Context, message:String) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
        var pictureList = ArrayList<PictureData>()
        var selectedPicture:PictureData? = null
    }
}