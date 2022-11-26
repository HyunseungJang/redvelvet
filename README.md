# R.E.D. (Rescue Emergency Disaster)

No matter what danger comes, 'R.E.D.' protects you.

![image](https://user-images.githubusercontent.com/112330490/204070400-7a9d30cc-275a-42da-a57d-da646bb10cf3.png)

## :man_technologist: **About**
* Is there any way to ask for help in an emergency?
* What if you could let people know in a disaster situation?

## :hammer_and_wrench: **Technologies**
* Android Studio : App development
* Visual Studio Code : Web development, Controller, SQL
* Vue.js : Front-end web development
* SketchUp : 3D Modeling
* nodemon : Server
* MySQL : DBMS
* Geocoder-Xr : Geocoder
* Postman : API Test
* HeidiSQL : Database Tool

## :rocket: **Features**

:heavy_check_mark: Information on dangerous areas

  * Dangerous areas of various standards can be set (traffic accident-prone areas, CCTV-free areas, etc.)
  * Push notification when approaching dangerous areas
  * Dangerous areas can be identified on the map of the main screen
  
:heavy_check_mark: Rescue request

  * Call emergency number using shake function
  * Send rescue signals to nearby users
  * Help recipients can find directions for help requestors
  * Request for rescue using Bluetooth even when the mobile network has collapsed
  * Increasing the Step-by-Step Scope of Rescue Requests through Bluetooth Propagation
  

:heavy_check_mark: Additional function


  * Disaster notification through real-time notification
  * Confirmation of disaster evacuation site
  * Learning content related to emergencies
  * Voice modulation
  * Bulletin boards, etc

## :iphone: **Screenshots**

![image](https://user-images.githubusercontent.com/112330490/204071783-7a68eee8-9004-4605-816b-2f69c3fd404a.png)

## :earth_asia: **Run**

* Go to ubiaccess-framework
```kotlin
cd ubiaccess-framework
```

* Start Server
```kotlin
nodemon index.js
```

* Change BasicAPI.kt
```kotlin
private const val BASE_URL = "YOUR IP"
```
