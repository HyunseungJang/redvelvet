package com.lx.red

import android.Manifest
import android.annotation.SuppressLint
import android.app.*
import android.app.PendingIntent.FLAG_MUTABLE
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RawRes
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.heatmaps.Gradient
import com.google.maps.android.heatmaps.HeatmapTileProvider
import com.google.maps.android.heatmaps.WeightedLatLng
import com.lx.api.BasicClient
import com.lx.data.DangerResponse
import com.lx.data.HelpResponse
import com.lx.data.MemberAreaResponse
import com.lx.red.databinding.ActivityMainBinding
import com.permissionx.guolindev.PermissionX
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONException
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
    var locationRequest : LocationRequest?=null
    var tileOverlay : TileOverlay?=null
    // 로그아웃
    val PREFS_NAME: String? = "LoginPrefs"

    @SuppressLint("UnspecifiedImmutableFlag")
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.actionBar) //커스텀한 toolbar를 액션바로 사용

        // 맨 위 상태바 투명
        val window = window
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

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

        time.scheduleAtFixedRate(1000, 3000) {
            updateArea(time)
        }
//        time.scheduleAtFixedRate(8000, 3000) {
//            searchDanger(time)
//        }
        time.scheduleAtFixedRate(10000, 10000) {
            searchHelp(time)
        }
        //구조요청
        binding.helpButton.setOnClickListener {
            launcher.launch(Intent(applicationContext,HelpRequestActivity::class.java))
        }

        //재난대피
        binding.safezoneButtonn.setOnClickListener {
            launcher.launch(Intent(applicationContext,SkyActivity::class.java))
        }

        //행동요령
        binding.accidentButtonn.setOnClickListener {
            launcher.launch(Intent(applicationContext,MediaActivity::class.java))
        }

        //상황대처
        binding.situationButtonn.setOnClickListener {
            launcher.launch(Intent(applicationContext,SituationActivity::class.java))
        }

        //음성변환
        binding.voiceButtonn.setOnClickListener {
            launcher.launch(Intent(applicationContext,VoiceActivity::class.java))
        }

        //블루투스
        binding.bluetoothButton.setOnClickListener{
            launcher.launch(Intent(applicationContext,BluetoothActivity::class.java))
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
//            addHeatMap()

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

        binding.applyButton.setOnClickListener {
            if(tileOverlay!=null){
                removeheatmap()
                addHeatMap()
            }else{
                addHeatMap()
            }
        }

    }

    //액션바 메뉴 연결 함수
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        return true
    }

    //액션바 버튼 눌렀을 때
    @SuppressLint("NonConstantResourceId")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.write -> {
                startActivity(Intent(this, PostActivity::class.java))
                true

                return super.onOptionsItemSelected(item)
            }
            R.id.userInfo -> {
                startActivity(Intent(this, MyInfoMainActivity::class.java))
                true
            }
            R.id.setting -> {
                // 체크박스 표시되는 함수 만들기!
                true
            }
            R.id.logOut -> {
                logout()
                true
            } else -> super.onOptionsItemSelected(item)
        }
    }

    fun logout(){
        AlertDialog.Builder(this,R.style.AppAlertDialogTheme)
            .setTitle("로그아웃").setMessage("로그아웃 하시겠습니까?")
            .setPositiveButton("로그아웃", DialogInterface.OnClickListener { dialog, whichButton ->
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
                moveTaskToBack(true);						// 태스크를 백그라운드로 이동
                finishAndRemoveTask();						// 액티비티 종료 + 태스크 리스트에서 지우기
                android.os.Process.killProcess(android.os.Process.myPid());	// 앱 프로세스 종료
            })
            .setNegativeButton("취소",
                DialogInterface.OnClickListener { dialog, whichButton -> })
            .show()
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
                interval = 1000000    //위치 새로고침 시간

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
                    HelpData.distance = response.body()?.data?.get(0)?.distance.toString()
                    // 알림 기능

                    showToast("알림 표시됨")

                    // 알림창 클릭시
                    val pintent = Intent(this@MainActivity, HelperActivity::class.java)
                    val pendingIntent = PendingIntent.getActivity(this@MainActivity, 0, pintent, FLAG_MUTABLE)

                    var builder = NotificationCompat.Builder(this@MainActivity, "MY_channel")
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle("RED 앱 구조요청 알림")
                        .setContentText("근처 200m 이내에 도움이 필요한 사람이 있어요!")
                        .setContentIntent(pendingIntent)

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // 오레오 버전 이후에는 알림을 받을 때 채널이 필요
                        val channel_id = "MY_channel" // 알림을 받을 채널 id 설정
                        val channel_name = "채널이름" // 채널 이름 설정
                        val descriptionText = "설명글" // 채널 설명글 설정
                        val importance = NotificationManager.IMPORTANCE_DEFAULT // 알림 우선순위 설정
                        val channel = NotificationChannel(channel_id, channel_name, importance).apply {
                            description = descriptionText
                        }

                        // 만든 채널 정보를 시스템에 등록
                        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        notificationManager.createNotificationChannel(channel)

                        // 알림 표시: 알림의 고유 ID(ex: 1002), 알림 결과
                        notificationManager.notify(1002, builder.build())
                    }


                    val intent = Intent(this@MainActivity,HelperActivity::class.java)
                    time.cancel()
                    startActivity(intent)
                    return
                }else{
                    
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

    // 히트맵
    private fun addHeatMap() {
        var latLngs: List<LatLng?>? = null

//        // 체크박스 체크 상황별
//        if(crimeCheck.isChecked == false && accidentCheck.isChecked == false && cctvCheck.isChecked == false) {
//            try { latLngs = readItems(R.raw.crime) } catch (e: JSONException) { }
//        }
//
//        else
            if(crimeCheck.isChecked && !accidentCheck.isChecked && !cctvCheck.isChecked) {
            try { latLngs = readItems(R.raw.crime) } catch (e: JSONException) { }
        } else if(crimeCheck.isChecked == false && accidentCheck.isChecked == true && cctvCheck.isChecked == false) {
            try { latLngs = readItems(R.raw.caraccident) } catch (e: JSONException) { }
        } else if(crimeCheck.isChecked == false && accidentCheck.isChecked == false && cctvCheck.isChecked == true){
            try { latLngs = readItems(R.raw.cctv) } catch (e: JSONException) { }
        }

        else if(crimeCheck.isChecked && accidentCheck.isChecked && cctvCheck.isChecked == false){
            try { latLngs = readItems(R.raw.mix1) } catch (e: JSONException) { }
        } else if(crimeCheck.isChecked && accidentCheck.isChecked == false && cctvCheck.isChecked){
            try { latLngs = readItems(R.raw.mix2) } catch (e: JSONException) { }
        } else if(crimeCheck.isChecked == false && cctvCheck.isChecked && cctvCheck.isChecked){
            try { latLngs = readItems(R.raw.mix3) } catch (e: JSONException) { }
        } else if(crimeCheck.isChecked && accidentCheck.isChecked && cctvCheck.isChecked){
            try { latLngs = readItems(R.raw.all) } catch (e: JSONException) { }
        }

        val provider = HeatmapTileProvider.Builder()
            .data(latLngs)
            .build()


        val colors = intArrayOf(
            Color.rgb(102, 225, 0),  // green
            Color.rgb(255, 0, 0) // red
        )
        val startPoints = floatArrayOf(0.7f, 1f)
        val gradient = Gradient(colors, startPoints)

        tileOverlay = map.addTileOverlay(
            TileOverlayOptions()
                .tileProvider(provider)
        )
        provider.setOpacity(0.55)
        tileOverlay?.clearTileCache()

        provider.setGradient(gradient)
        provider.setRadius(50)


    }
    fun removeheatmap(){
        tileOverlay?.remove()
    }

    @Throws(JSONException::class)
    private fun readItems(@RawRes resource: Int): List<LatLng?> {
        val result: MutableList<LatLng?> = ArrayList()
        val inputStream = this.resources.openRawResource(resource)
        val json = Scanner(inputStream).useDelimiter("\\A").next()
        val array = JSONArray(json)
        for (i in 0 until array.length()) {
            val `object` = array.getJSONObject(i)
            val lat = `object`.getDouble("lat")
            val lng = `object`.getDouble("lng")
            result.add(LatLng(lat, lng))
        }
        return result
    }

}