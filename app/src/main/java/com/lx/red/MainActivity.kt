package com.lx.red

import android.Manifest
import android.annotation.SuppressLint
import android.app.*
import android.app.PendingIntent.FLAG_MUTABLE
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RawRes
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.heatmaps.Gradient
import com.google.maps.android.heatmaps.HeatmapTileProvider
import com.lx.api.BasicClient
import com.lx.data.DangerResponse
import com.lx.data.HelpResponse
import com.lx.data.MemberAreaResponse
import com.lx.red.databinding.ActivityMainBinding
import com.permissionx.guolindev.PermissionX
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
    // ????????? + ????????????
    private var mSensorManager: SensorManager? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var mAccelerometer: Sensor? = null

    private var mShakeDetector: ShakeDetector? = null

    var locationRequest : LocationRequest?=null

    var tileOverlay : TileOverlay?=null
    // ????????????
    val PREFS_NAME: String? = "LoginPrefs"

    @SuppressLint("UnspecifiedImmutableFlag")
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.actionBar) //???????????? toolbar??? ???????????? ??????

        // ??? ??? ????????? ??????
        val window = window
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        // ?????? ???????????? ????????? ??????( ?????????????????? ????????? ????????? ???)
        Thread { time() }.start()
        if(MemberData.memberId ==null){
            finish()
        }

        // ????????? + ???????????? + ????????????
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
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            return
//        }
//        fusedLocationClient.lastLocation
//            .addOnSuccessListener { location : Location? ->
//            }
        val builder = AlertDialog.Builder(this)
        //???????????? ????????????
        PermissionX.init(this)
            .permissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {

                } else {

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
        //????????????
        binding.helpButton.setOnClickListener {
            launcher.launch(Intent(applicationContext,HelpRequestActivity::class.java))
        }

        //????????????
        binding.safezoneButtonn.setOnClickListener {
            launcher.launch(Intent(applicationContext,SkyActivity::class.java))
        }

        //????????????
        binding.accidentButtonn.setOnClickListener {
            launcher.launch(Intent(applicationContext,MediaActivity::class.java))
        }

        //????????????
        binding.situationButtonn.setOnClickListener {
            launcher.launch(Intent(applicationContext,SituationActivity::class.java))
        }

        //????????????
        binding.voiceButtonn.setOnClickListener {
            launcher.launch(Intent(applicationContext,VoiceActivity::class.java))
        }

        //????????????
        binding.bluetoothButton.setOnClickListener{
            launcher.launch(Intent(applicationContext,BluetoothActivity::class.java))
        }

        // --????????? ???????????? start --
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
        //?????? ???????????????
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        //????????? ????????? ?????????
        mapFragment.getMapAsync{
            //???????????? ?????? ??????
            map = it

            //????????? ????????????
            requestLocation()
            //?????? ????????? ??????
            map.setOnMarkerClickListener {
                true
            }
            //???????????? ??????????????? ?????? ??????
            map.setOnCameraIdleListener {
                //????????????
                val bounds = map.projection.visibleRegion.latLngBounds
                //??? ??????
                val zoomLevel = map.cameraPosition.zoom
                println("zoomLevel: ${zoomLevel}")
            }
        }
        //?????????????????? ???????????? MyService??? ???????????? ???????????? ?????????(foreground??? ????????? ?????? ??????????????????)
        val intent = Intent(this, BackgroundService::class.java)
        startForegroundService(intent)
        // --????????????????????? ?????? ????????? ?????? start--
        val alarmMgr = getSystemService(ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(this, NotificationBroadcastReceiver::class.java) // ???????????? ??????
        val pendingIntent = PendingIntent.getBroadcast(
            this, 0, alarmIntent,
            PendingIntent.FLAG_MUTABLE)
        val triggerTime = (SystemClock.elapsedRealtime()  // 5??? ????????? ?????? ?????????
                + 20 * 1000)
        alarmMgr.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            pendingIntent
        )
        // --????????????????????? ?????? ????????? ?????? end--
        binding.applyButton.setOnClickListener {
            if(!crimeCheck.isChecked && !accidentCheck.isChecked && !cctvCheck.isChecked){
                removeheatmap()
            }else if(tileOverlay!=null){
                removeheatmap()
                addHeatMap()
            }else if(tileOverlay==null){
                addHeatMap()
            }
        }
    }

    //????????? ?????? ?????? ??????
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    //????????? ?????? ????????? ???
    @SuppressLint("NonConstantResourceId")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.write -> {
                startActivity(Intent(this, PostActivity::class.java))
                true
                return super.onOptionsItemSelected(item)
            }
            R.id.userInfo -> {
                startActivity(Intent(this, MyInfoUpdateActivity::class.java))
                true
            }
            R.id.setting -> {
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
            .setTitle("????????????").setMessage("???????????? ???????????????????")
            .setPositiveButton("????????????", DialogInterface.OnClickListener { dialog, whichButton ->
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
                moveTaskToBack(true)
                finishAndRemoveTask()
                android.os.Process.killProcess(android.os.Process.myPid())
            })
            .setNegativeButton("??????",
                DialogInterface.OnClickListener { dialog, whichButton -> })
            .show()
    }

    fun requestLocation(){

        try {

            //?????? ????????? ????????? ?????? ????????????
            locationClient?.lastLocation?.addOnSuccessListener {
            }

            //?????? ??????????????? ?????????
            locationClient = LocationServices.getFusedLocationProviderClient(this)

            //???????????? ????????? ??? ????????? ?????????
            val locationRequest = LocationRequest.create()
            locationRequest.run{
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                interval = 1000000    //?????? ???????????? ??????

            }

            //??? ????????? ????????? ??? ???????????? ???????????? ??????
            val locationCallback = object : LocationCallback(){
                override fun onLocationResult(result: LocationResult) {
                    super.onLocationResult(result)  //????????? ?????? - result
                    for((index,location)in result.locations.withIndex()){
                        showCurrentLocation(location)
                    }
                }
            }
            //??? ?????? ??????//
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
            it.title("?????????")
            it.icon(BitmapDescriptorFactory.fromResource(R.drawable.location))

            myMarker = map.addMarker(it)
            //????????????
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

    // background ??????????????? ???????????? ???????????? ????????? ????????? ??????
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

                    // ????????? ?????????
                    val pintent = Intent(this@MainActivity, HelperActivity::class.java)
                    val pendingIntent = PendingIntent.getActivity(this@MainActivity, 0, pintent, FLAG_MUTABLE)

                    var builder = NotificationCompat.Builder(this@MainActivity, "MY_channel")
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle("RED ??? ???????????? ??????")
                        .setContentText("?????? 200m ????????? ????????? ????????? ????????? ?????????!")
                        .setContentIntent(pendingIntent)

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // ????????? ?????? ???????????? ????????? ?????? ??? ????????? ??????
                        val channel_id = "MY_channel" // ????????? ?????? ?????? id ??????
                        val channel_name = "????????????" // ?????? ?????? ??????
                        val descriptionText = "?????????" // ?????? ????????? ??????
                        val importance = NotificationManager.IMPORTANCE_DEFAULT // ?????? ???????????? ??????
                        val channel = NotificationChannel(channel_id, channel_name, importance).apply {
                            description = descriptionText
                        }

                        // ?????? ?????? ????????? ???????????? ??????
                        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        notificationManager.createNotificationChannel(channel)

                        // ?????? ??????: ????????? ?????? ID(ex: 1002), ?????? ??????
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
    //10??? ????????? ?????? (1?????????)
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

    // ?????????
    private fun addHeatMap() {
        var latLngs: List<LatLng?>? = null

        if(crimeCheck.isChecked && accidentCheck.isChecked && cctvCheck.isChecked){
            try { latLngs = readItems(R.raw.crime) } catch (e: JSONException) { }
            try { latLngs = readItems(R.raw.caraccident) } catch (e: JSONException) { }
            try { latLngs = readItems(R.raw.cctv) } catch (e: JSONException) { }
        }else if(crimeCheck.isChecked && accidentCheck.isChecked && !cctvCheck.isChecked){
            try { latLngs = readItems(R.raw.crime) } catch (e: JSONException) { }
            try { latLngs = readItems(R.raw.caraccident) } catch (e: JSONException) { }
        } else if(crimeCheck.isChecked && !accidentCheck.isChecked && cctvCheck.isChecked){
            try { latLngs = readItems(R.raw.crime) } catch (e: JSONException) { }
            try { latLngs = readItems(R.raw.cctv) } catch (e: JSONException) { }
        } else if(!crimeCheck.isChecked && accidentCheck.isChecked && cctvCheck.isChecked){
            try { latLngs = readItems(R.raw.caraccident) } catch (e: JSONException) { }
            try { latLngs = readItems(R.raw.cctv) } catch (e: JSONException) { }
        }else if(crimeCheck.isChecked && !accidentCheck.isChecked && !cctvCheck.isChecked) {
            try { latLngs = readItems(R.raw.crime) } catch (e: JSONException) { }
        } else if(!crimeCheck.isChecked && accidentCheck.isChecked && !cctvCheck.isChecked) {
            try { latLngs = readItems(R.raw.caraccident) } catch (e: JSONException) { }
        } else if(!crimeCheck.isChecked && !accidentCheck.isChecked && cctvCheck.isChecked){
            try { latLngs = readItems(R.raw.cctv) } catch (e: JSONException) { }
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