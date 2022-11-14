package com.lx.red

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.lx.red.databinding.ActivitySkyBinding
import kotlinx.android.synthetic.main.activity_sky.*


class SkyActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var binding : ActivitySkyBinding

    private lateinit var mMap: GoogleMap

    var selectedMarker: Marker? = null

    val danger = LatLng(37.52433880265412,127.03511441844459)
    val danger2 = LatLng(37.523377508176814,127.0240164157561)
    val danger3 = LatLng(37.529047510768976, 127.04546833662607)

    val save: Marker? = null
    val save1: Marker? = null
    val save3: Marker? = null


    private var locationArrayList: ArrayList<LatLng>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySkyBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val map = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        map.getMapAsync(this)

    }


    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(danger,17.0f))

        val save = mMap.addMarker(
            MarkerOptions()
                .position(danger)
                .title("도산은행나무공원")
                .snippet("지진옥외대피장소")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.location))

        )

        val save2 = mMap.addMarker(
            MarkerOptions()
                .position(danger2)
                .title("신구초등학교")
                .snippet("지진실내구호소")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.location))

        )

        val save3 = mMap.addMarker(
            MarkerOptions()
                .position(danger3)
                .title("청담중학교")
                .snippet("지진실내구호소")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.location))


        )

        // 카드뷰 안보였다가 누르면 카드뷰1 보여주기
        if (binding.cardView1.visibility == View.VISIBLE){
            TransitionManager.beginDelayedTransition(binding.cardView1,
                AutoTransition()
            )
            binding.cardView1.visibility = View.GONE


//            binding.historyExpandIv.setImageResource(R.drawable.arrow_down_float)
        } else{
            TransitionManager.beginDelayedTransition(binding.cardView1,
                AutoTransition())
            binding.cardView1.visibility = View.VISIBLE
//            binding.historyExpandIv.setImageResource(R.drawable.arrow_up_float)
        }

        // 카드뷰 안보였다가 누르면 카드뷰2 보여주기
        if (binding.cardView2.visibility == View.VISIBLE){
            TransitionManager.beginDelayedTransition(binding.cardView2,
                AutoTransition()
            )
            binding.cardView2.visibility = View.GONE


//            binding.historyExpandIv.setImageResource(R.drawable.arrow_down_float)
        } else{
            TransitionManager.beginDelayedTransition(binding.cardView2,
                AutoTransition())
            binding.cardView2.visibility = View.VISIBLE
//            binding.historyExpandIv.setImageResource(R.drawable.arrow_up_float)
        }

        // 카드뷰 안보였다가 누르면 카드뷰3 보여주기
        if (binding.cardView3.visibility == View.VISIBLE){
            TransitionManager.beginDelayedTransition(binding.cardView3,
                AutoTransition()
            )
            binding.cardView3.visibility = View.GONE


//            binding.historyExpandIv.setImageResource(R.drawable.arrow_down_float)
        } else{
            TransitionManager.beginDelayedTransition(binding.cardView3,
                AutoTransition())
            binding.cardView3.visibility = View.VISIBLE
//            binding.historyExpandIv.setImageResource(R.drawable.arrow_up_float)
        }

        // 카드뷰 보였다 안보였다하기
        mMap.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener {
            override fun onMarkerClick(marker: Marker): Boolean {

                if(marker.title == "도산은행나무공원") {
                    card_view1.visibility = View.VISIBLE
                } else if(marker.title == "신구초등학교") {
                    card_view2.visibility = View.VISIBLE
                } else {marker.title == "청담중학교"
                    card_view3.visibility = View.VISIBLE
                }

                return false
            }
        })

        //맵 클릭 리스너-맵 클릭하면 카드뷰 없어짐
        googleMap!!.setOnMapClickListener(object : GoogleMap.OnMapClickListener {
            override fun onMapClick(latLng: LatLng) {
                card_view1.visibility = View.GONE
                card_view2.visibility = View.GONE
                card_view3.visibility = View.GONE

            }
        })

    }

}