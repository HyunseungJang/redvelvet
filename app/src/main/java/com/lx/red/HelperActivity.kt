package com.lx.red

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_confirmation_help.*

class HelperActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    var latMe = AppData.lat?.toDouble()
    var lngMe = AppData.lng?.toDouble()
    var nameMe = MemberData.memberName

    var latHelp = HelpData.lat?.toDouble()
    var lngHelp = HelpData.lng?.toDouble()
    var nameHelp = HelpData.id

    val soser = LatLng(latHelp!!, lngHelp!!)
    val saver = LatLng(latMe!!, lngMe!!)
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
//        }//

        val sos = mMap.addMarker(
            MarkerOptions()
                .position(soser)
                .title("$nameHelp")
                .snippet("도움이 필요한 사람의 위치")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.sos))
        )

        val save = mMap.addMarker(
            MarkerOptions()
                .position(saver)
                .title("$nameMe")
                .snippet("나의 위치")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.you))
        )

        mMap.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener {
            override fun onMarkerClick(marker: Marker): Boolean {

                if(marker.snippet == "도움이 필요한 사람의 위치") {

                    // 다이얼로그를 생성하기 위해 Builder 클래스 생성자를 이용해 줍니다.
                    val builder = AlertDialog.Builder(this@HelperActivity)
                    builder.setTitle("$nameHelp 님에게로 가는 경로 탐색")
                        .setMessage("구글맵으로 이동합니다.")
                        .setPositiveButton("확인",
                            DialogInterface.OnClickListener { dialog, id ->
                                goToMap()
                            })
                        .setNegativeButton("취소",
                            DialogInterface.OnClickListener { dialog, id ->

                            })
                    // 다이얼로그를 띄워주기
                    builder.show()

                } else {

                }

                return false
            }
        })

    }

    // 구글맵 앱으로 이동하기
    fun goToMap() {
        val mapIntent: Intent = Uri.parse(
            "geo:0,0?q=${latHelp},${lngHelp}?z=18"
        ).let { location ->
            // Or map point based on latitude/longitude
            //val location: Uri = Uri.parse("geo:37.5157852,127.0354285?z=14") // z param is zoom level
            Intent(Intent.ACTION_VIEW, location)
        }
        startActivity(mapIntent)
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}