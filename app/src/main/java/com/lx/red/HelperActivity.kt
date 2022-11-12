package com.lx.red

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.lx.api.BasicClient
import com.lx.data.DangerResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HelperActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    var lat = HelpData.lat?.toDouble()
    var lng = HelpData.lng?.toDouble()

    val soser = LatLng(37.5306182,127.0306218)
    val saver = LatLng(37.5306182, 127.0306218)
    private var locationArrayList: ArrayList<LatLng>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_helper)

        val map = supportFragmentManager
            .findFragmentById(R.id.myMap) as SupportMapFragment
        map.getMapAsync(this)

        //locationArrayList = ArrayList()

        //locationArrayList!!.add(saver)
        //locationArrayList!!.add(saver2)
    }

    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(soser,17.0f))


//        for (i in locationArrayList!!.indices) {
//            mMap.addMarker(MarkerOptions().position(locationArrayList!![i]).title("Marker"))
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(18.0f))
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(locationArrayList!!.get(i)))
//        }

        val sos = mMap.addMarker(
            MarkerOptions()
                .position(soser)
                .title("구조받는 사람")
                .snippet("도와줘!")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.sos))
        )

        val save = mMap.addMarker(
            MarkerOptions()
                .position(saver)
                .title("구조해주는 사람")
                .snippet("도와줄게!")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.you))
        )

    }
}