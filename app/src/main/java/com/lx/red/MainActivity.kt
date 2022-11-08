package com.lx.red

import android.Manifest
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.lx.red.databinding.ActivityMainBinding
import com.permissionx.guolindev.PermissionX
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding

    var locationClient:FusedLocationProviderClient?=null

    lateinit var map: GoogleMap

    var myMarker: Marker? = null

    val timer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //위험권한 요청하기
        PermissionX.init(this)
            .permissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    showToast("권한부여")
                } else {
                    showToast("권한거부")
                }
            }



        //공지사항
        binding.noticeButton.setOnClickListener {
            val intent = Intent(this,NoticeActivity::class.java)
            startActivity(intent)
        }

        //구조요청
        binding.helpButton.setOnClickListener {
            val intent = Intent(this,HelpRequestActivity::class.java)
            startActivity(intent)
        }

        //상황대처 정보
        binding.infoButton.setOnClickListener {
            val intent = Intent(this,InformationActivity::class.java)
            startActivity(intent)
        }

        //내정보
        binding.myinfoButton.setOnClickListener {
            val intent = Intent(this,MyInfoMainActivity::class.java)
            startActivity(intent)
        }

        //게시판
        binding.postButton.setOnClickListener {
            val intent = Intent(this,PostActivity::class.java)
            startActivity(intent)
        }


        // --펼치기 레이아웃 start --
        binding.plusLayout.setOnClickListener {
            if (binding.layoutDetail02.visibility == View.VISIBLE) {
                binding.layoutDetail02.visibility = View.GONE
                binding.layoutBtn01.animate().apply {
                    duration = 300
                    rotation(0f)
                }
            } else {
                binding.layoutDetail02.visibility = View.VISIBLE
                binding.layoutBtn01.animate().apply {
                    duration = 300
                    rotation(180f)
                }
            }
        }
        // --펼치기 레이아웃 end--

        //지도 초기화하기
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        //구글맵 객체를 컨트롤
        mapFragment.getMapAsync{
            //초기화가 끝난 지도
            map = it
//            val timerTask: TimerTask = object : TimerTask() {
//                override fun run() {
//
//                }
//            }
            //내위치 요청하기
            requestLocation()

            //마커 클릭시 처리
            map.setOnMarkerClickListener {
                showToast("마커 클릭됨 : ${it.tag},${it.title}")

                //필요시, 다른화면으로 이동(tag 정보를 이용해서 구분함)

                //리턴
                true
            }

            //보고있는 지도영역에 대한 구분
            map.setOnCameraIdleListener {
                //현재위치
                val bounds = map.projection.visibleRegion.latLngBounds


                //줌 레벨
                val zoomLevel = map.cameraPosition.zoom
                println("zoomLevel: ${zoomLevel}")
            }

        }

    }
    fun requestLocation(){

        try {
            //가장 최근에 확인된 위치 알려주기
            locationClient?.lastLocation?.addOnSuccessListener {
                showToast("최근위치 : ${it.latitude}, ${it.longitude}")
            }

            //위치 클라이언트 만들기
            locationClient = LocationServices.getFusedLocationProviderClient(this)

            //내위치를 요청할 때 필요한 설정값
            val locationRequest = LocationRequest.create()
            locationRequest.run{
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                interval = 1000    //위치 새로고침 시간

            }

            //내 위치를 받았을 때 자동으로 호출되는 함수
            val locationCallback = object : LocationCallback(){
                override fun onLocationResult(result: LocationResult) {
                    super.onLocationResult(result)  //내위치 확인 - result
                    for((index,location)in result.locations.withIndex()){
                        showCurrentLocation(location)
                    }
                }
            }
            //내 위치 요청
            locationClient?.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
        }catch(e:SecurityException){
            e.printStackTrace()
        }
    }

    fun showCurrentLocation(location: Location){
        val curPoint = LatLng(location.latitude, location.longitude)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(curPoint,17.0f))
        AppData.lat = location.latitude.toString()
        AppData.lng = location.longitude.toString()
        showMarker(curPoint)
    }
    fun showMarker(curPoint:LatLng){
        myMarker?.remove()

        MarkerOptions().also{
            it.position(curPoint)
            it.title("내위치")
            it.icon(BitmapDescriptorFactory.fromResource(R.drawable.location))

            myMarker = map.addMarker(it)
            //태그정보
            myMarker?.tag = "1001"

        }
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
