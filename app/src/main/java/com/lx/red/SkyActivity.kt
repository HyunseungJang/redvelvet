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
    val danger4 = LatLng(37.5191, 127.0333)
    val danger5 = LatLng(37.5116, 127.0287)
    val danger6 = LatLng(37.5085, 127.0251)
    val danger7 = LatLng(37.5181, 127.018)
    val danger8 = LatLng(37.5101, 127.0322)
    val danger9 = LatLng(37.5184, 127.0472)
    val danger10 = LatLng(37.5206, 37.5206)
    val danger11 = LatLng(37.5307, 127.0329)
    val danger12 = LatLng(37.5089, 127.0484)
    val danger13 = LatLng(37.5136, 127.0555)
    val danger14 = LatLng(37.5208, 127.0524)
    val danger15 = LatLng(37.5102, 127.0323)
    val danger16 = LatLng(37.5142, 127.0468)
    val danger17 = LatLng(37.5200, 27.0457)




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
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mappin))

        )

        val save2 = mMap.addMarker(
            MarkerOptions()
                .position(danger2)
                .title("신구초등학교")
                .snippet("지진실내구호소")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mappin))

        )

        val save3 = mMap.addMarker(
            MarkerOptions()
                .position(danger3)
                .title("청담중학교")
                .snippet("지진실내구호소")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mappin))


        )

        val save4 = mMap.addMarker(
            MarkerOptions()
                .position(danger4)
                .title("하늘중학교")
                .snippet("지진실내구호소")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mappin))


        )

        val save6 = mMap.addMarker(
            MarkerOptions()
                .position(danger6)
                .title("논현1동주민센터")
                .snippet("지진실내구호소")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mappin))


        )

        val save7 = mMap.addMarker(
            MarkerOptions()
                .position(danger7)
                .title("논현1동주민센터")
                .snippet("지진실내구호소")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mappin))


        )

        val save8 = mMap.addMarker(
            MarkerOptions()
                .position(danger8)
                .title("논현1동주민센터")
                .snippet("지진실내구호소")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mappin))


        )

        val save9 = mMap.addMarker(
            MarkerOptions()
                .position(danger9)
                .title("논현1동주민센터")
                .snippet("지진실내구호소")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mappin))


        )

        val save10 = mMap.addMarker(
            MarkerOptions()
                .position(danger10)
                .title("논현1동주민센터")
                .snippet("지진실내구호소")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mappin))


        )

        val save11 = mMap.addMarker(
            MarkerOptions()
                .position(danger11)
                .title("논현1동주민센터")
                .snippet("지진실내구호소")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mappin))


        )

        val save12 = mMap.addMarker(
            MarkerOptions()
                .position(danger12)
                .title("논현1동주민센터")
                .snippet("지진실내구호소")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mappin))


        )

        val save13 = mMap.addMarker(
            MarkerOptions()
                .position(danger13)
                .title("논현1동주민센터")
                .snippet("지진실내구호소")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mappin))


        )

        val save14 = mMap.addMarker(
            MarkerOptions()
                .position(danger14)
                .title("논현1동주민센터")
                .snippet("지진실내구호소")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mappin))


        )

        val save15 = mMap.addMarker(
            MarkerOptions()
                .position(danger15)
                .title("논현1동주민센터")
                .snippet("지진실내구호소")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mappin))


        )

        val save16 = mMap.addMarker(
            MarkerOptions()
                .position(danger16)
                .title("논현1동주민센터")
                .snippet("지진실내구호소")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mappin))


        )

        val save17 = mMap.addMarker(
            MarkerOptions()
                .position(danger17)
                .title("논현1동주민센터")
                .snippet("지진실내구호소")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mappin))


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