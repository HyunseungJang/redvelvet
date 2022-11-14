package com.lx.red

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.lx.api.BasicClient
import com.lx.data.DangerResponse
import com.lx.data.HelpResponse
import com.lx.data.MemberAreaResponse
import com.lx.red.databinding.ActivityMainBinding
import com.permissionx.guolindev.PermissionX
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding

    var locationClient:FusedLocationProviderClient?=null

    lateinit var map: GoogleMap

    var myMarker: Marker? = null

    val timer = Timer()

    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){}

    // 쉐이크 + 전화걸기
    private var mSensorManager: SensorManager? = null
    private var mAccelerometer: Sensor? = null
    private var mShakeDetector: ShakeDetector? = null

    @SuppressLint("UnspecifiedImmutableFlag")
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // --백그라운드 작업 start--

        // 앱이 실행되면 카운트 시작( 테스트용이라 나중에 지워도 됨)
        Thread { time() }.start()

        // 쉐이크 + 전화걸기 + 문자발송
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mAccelerometer = mSensorManager!!
            .getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mShakeDetector = ShakeDetector()
        with(mShakeDetector) {
            this?.setOnShakeListener(object : ShakeDetector.OnShakeListener {
                override fun onShake(count: Int) {

                    run()

                }
            })
        }
        val builder = AlertDialog.Builder(this)
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
        val time = Timer()

        time.scheduleAtFixedRate(1000, 5000) {
            updateArea(time)
        }
        time.scheduleAtFixedRate(10000, 30000) {
            searchDanger(time)
        }
        time.scheduleAtFixedRate(10000, 30000) {
            searchHelp(time)
        }
        //공지사항
        binding.noticeButton.setOnClickListener {
            val intent = Intent(this,BluetoothChatActivity::class.java)
            startActivity(intent)
        }

        //구조요청
        binding.helpButton.setOnClickListener {
            launcher.launch(Intent(applicationContext,HelperActivity::class.java))
        }

        //상황대처 정보
        binding.infoButton.setOnClickListener {
            launcher.launch(Intent(applicationContext,InformationActivity::class.java))
        }

        //내정보
        binding.myinfoButton.setOnClickListener {
            launcher.launch(Intent(applicationContext,MyInfoMainActivity::class.java))
        }

        //게시판
        binding.postButton.setOnClickListener {
            launcher.launch(Intent(applicationContext,PostActivity::class.java))
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
        //백그라운드가 실행되는 MyService로 넘어가서 실행되는 서비스(foreground가 꺼져도 계속 실행되는것임)
        val intent = Intent(this, BackgroundService::class.java)
        startForegroundService(intent)

        // --백그라운드에서 알람 울리기 기능 start--
        val alarmMgr = getSystemService(ALARM_SERVICE) as AlarmManager

        val alarmIntent = Intent(this, NotificationBroadcastReceiver::class.java) // 리시버로 전달

        val pendingIntent = PendingIntent.getBroadcast(
            this, 0, alarmIntent,
            PendingIntent.FLAG_MUTABLE)

        val triggerTime = (SystemClock.elapsedRealtime()  // 5초 지나면 알람 울리기
                + 20 * 1000)
        alarmMgr.setExactAndAllowWhileIdle(   // setExactAndAllowWhileIdle -> 절전모드에서도 동작하는 코드(절전모드 원치 않으면 setExact)
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            pendingIntent
        )
        // --백그라운드에서 알람 울리기 기능 end--
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
            //내 위치 요청//
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

    public override fun onResume() {
        super.onResume()
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager?.registerListener(
            mShakeDetector,
            mAccelerometer,
            SensorManager.SENSOR_DELAY_UI
        )
    }

    // background 상황에서도 흔들림을 감지하고 적용할 필요는 없다
    public override fun onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager?.unregisterListener(mShakeDetector)
        super.onPause()
    }

    fun updateArea(time:Timer){
        val id = MemberData.memberId.toString()
        val lat= AppData.lat?.toDouble()
        val lng= AppData.lng?.toDouble()

        BasicClient.api.myAreaUpdate(
            requestCode = "1001",
            id = id,
            lat = lat,
            lng = lng
        ).enqueue(object : Callback<MemberAreaResponse> {
            override fun onResponse(call: Call<MemberAreaResponse>, response: Response<MemberAreaResponse>) {

            }
            override fun onFailure(call: Call<MemberAreaResponse>, t: Throwable) {

            }
        })
    }
    fun searchDanger(time:Timer) {
        val lat = AppData.lat?.toDouble()
        val lng = AppData.lng?.toDouble()
        val lat2 = AppData.lat?.toDouble()

        BasicClient.api.dangerzone(
            requestCode = "1001",
            LAT = lat,
            LNG = lng,
            LAT2 = lat2
        ).enqueue(object : Callback<DangerResponse> {
            override fun onResponse(call: Call<DangerResponse>, response: Response<DangerResponse>) {
                val checkDanger = response.body()?.header?.total.toString()
                if(checkDanger !="0"){
                    val intent = Intent(this@MainActivity,WarningActivity::class.java)
                    time.cancel()
                    startActivity(intent)
                    return
                }else{
                    return
                }
            }
            override fun onFailure(call: Call<DangerResponse>, t: Throwable) {

            }
        })
    }

    fun searchHelp(time:Timer){
        val id = MemberData.memberId.toString()
        val lat = AppData.lat?.toDouble()
        val lng = AppData.lng?.toDouble()
        val lat2 = AppData.lat?.toDouble()

        BasicClient.api.scanhelp(
            requestCode = "1001",
            id = id,
            lat = lat,
            lng = lng,
            lat2 = lat2
        ).enqueue(object : Callback<HelpResponse> {
            override fun onResponse(call: Call<HelpResponse>, response: Response<HelpResponse>) {
                val checkDanger = response.body()?.header?.total.toString()
                if(checkDanger !="0"){
                    HelpData.id= response.body()?.data?.get(0)?.id.toString()
                    HelpData.lat= response.body()?.data?.get(0)?.lat.toString()
                    HelpData.lng= response.body()?.data?.get(0)?.lng.toString()
                    binding.textView7.text=response.body()?.data.toString()
                    val intent = Intent(this@MainActivity,HelperActivity::class.java)
                    time.cancel()
                    startActivity(intent)
                    return
                }else{
                    return
                }
            }
            override fun onFailure(call: Call<HelpResponse>, t: Throwable) {

            }
        })
    }

    //10초 카운트 세기 (1초마다)
    fun time() {
        for (i in 0..1) {
            try {
                Thread.sleep(1000)
                Log.d("test", "count: $i")
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            stopService(intent)
        }
    }

    fun run() {
        launcher.launch(Intent(applicationContext,HelpRequestActivity::class.java))
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    //타이머 객체 하나씩 만들어서 죽여버리는 함수

}
