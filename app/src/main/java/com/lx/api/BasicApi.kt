package com.lx.api


import android.util.Log
import com.lx.data.*
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.lang.Exception
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.*
import kotlin.collections.HashMap

/**
 * 웹서버 요청/응답을 처리하기 위한 기본 API
 */
interface BasicApi {

    /**
     * member 멤버 DB
     */

    /**
     * POST 방식으로 멤버 로그인 요청
     */

    @FormUrlEncoded
    @POST("red/memberRead")
    fun postMemberRead(
        @Field("requestCode") requestCode: String,
        @Field("id") id: String,
        @Field("pw") pw: String
    ): Call<MemberListResponse>

    /**
     * POST 방식으로 멤버 회원가입 요청
     */

    @FormUrlEncoded
    @POST("red/memberAdd")
    fun memberAdd(
        @Field("requestCode") requestCode: String,
        @Field("id") id: String,
        @Field("pw") pw: String,
        @Field("name") name: String,
        @Field("birth") birth: String,
        @Field("gender") gender: String,
        @Field("phone") phone: String,
        @Field("agreep") agreep: String,
        @Field("agrees1") agrees1: String,
        @Field("agrees2") agrees2: String
    ): Call<MemberListResponse>

    /**
     * POST 방식으로 회원가입시 위치정보추가
     */

    @FormUrlEncoded
    @POST("red/registerArea")
    fun memberArea(
        @Field("requestCode") requestCode: String,
        @Field("id") id: String,
        @Field("lat") lat: Int,
        @Field("lng") lng: Int
    ): Call<MemberAreaResponse>

    /**
     * POST 방식으로 위치정보 새로고침
     */

    @FormUrlEncoded
    @POST("red/myAreaUpdate")
    fun myAreaUpdate(
        @Field("requestCode") requestCode: String,
        @Field("id") id: String,
        @Field("lat") lat: Double?,
        @Field("lng") lng: Double?
    ): Call<MemberAreaResponse>

    /**
     * POST 방식으로 멤버 정보수정 요청
     */

    @FormUrlEncoded
    @POST("red/memberUpdate")
    fun memberUpdate(
        @Field("requestCode") requestCode: String,
        @Field("id") id: String,
        @Field("birth") birth: String,
        @Field("height") height: String,
        @Field("weight") weight: String,
        @Field("emernum") emernum: String,
        @Field("medicine") medicine: String,
        @Field("disease") disease: String,
        @Field("bloodtype") bloodtype: String,
        @Field("certificate") certificate: String,
        @Field("others") others: String,
    ): Call<MemberListResponse>

    /**
     * POST 방식으로 아이디 중복체크 요청
     */

    @FormUrlEncoded
    @POST("red/memberIdcheck")
    fun postMemberCheckId(
        @Field("requestCode") requestCode: String,
        @Field("id") id: String
    ): Call<MemberListResponse>

    /**
     * POST 방식으로 멤버 로그인 요청
     */

    @FormUrlEncoded
    @POST("red/dangerzone")
    fun dangerzone(
        @Field("requestCode") requestCode: String,
        @Field("LAT") LAT: Double?,
        @Field("LNG") LNG: Double?,
        @Field("LAT2") LAT2: Double?
    ): Call<DangerResponse>

    /**
     * 내위치에서 구조신호 보내기 + 구조신호 리스트 추가
     */

    @FormUrlEncoded
    @POST("red/sendMyAreaAdd")
    fun sendMyArea(
        @Field("requestCode") requestCode: String,
        @Field("id") id: String,
        @Field("lat") lat: Double?,
        @Field("lng") lng: Double?
    ): Call<SendMyAreaResponse>

    /**
     * 도움요청자 스캔하기
     */

    @FormUrlEncoded
    @POST("red/scanHelp")
    fun scanhelp(
        @Field("requestCode") requestCode: String,
        @Field("id") id: String,
        @Field("LAT") LAT: Double?,
        @Field("LNG") LNG: Double?,
        @Field("LAT2") LAT2: Double?
    ): Call<HelpResponse>


    /**
     * GET 방식으로 게시판 리스트 보기
     */

    @GET("red/communityList")
    fun getCommunityList(
        @Query("requestCode") requestCode: String
    ): Call<CommunityResponse>


    /**
     * 게시판 추가
     */

    @FormUrlEncoded
    @POST("red/communityAdd")
    fun addPost(
        @Field("requestCode") requestCode: String,
        @Field("id") id: String,
        @Field("title") title: String,
        @Field("picture") picture: String,
        @Field("content") content: String,
        @Field("area") area: String
    ): Call<CommunityResponse>

    /**
     * 내위치에서 구조신호 보냈을때 리스트 추가
     */

    @FormUrlEncoded
    @POST("red/helpMyAreaAdd")
    fun help(
        @Field("requestCode") requestCode: String,
        @Field("id") id: String,
        @Field("LAT") LAT: Double?,
        @Field("LNG") LNG: Double?
    ): Call<HelpResponse>

    /**
     *  구조요청 완료 요청(송신자)
     */

    @GET("red/deleteHelp")
    fun getMemberDelete(
        @Query("requestCode") requestCode: String,
        @Query("id") id: String
    ): Call<HelpResponse>


    /**
     * 파일 업로드 요청
     */

    @Multipart
    @POST("/farm/upload")
    fun uploadFile(
        @Part file: MultipartBody.Part,
        @Part(value="params", encoding="UTF-8") params: HashMap<String,String> = hashMapOf()
    ): Call<FileUploadResponse>

}

class BasicClient {

    companion object {
        const val TAG = "BasicClient"

        private var instance : BasicApi? = null

        val api: BasicApi
            get() {
                return getInstance()
            }

        @Synchronized
        fun getInstance(): BasicApi {
            if (instance == null)
                instance = create()
            return instance as BasicApi
        }

        // 프로토콜
        private const val PROTOCOL = "http"

        // 기본 URL
        private const val BASE_URL = "http://172.168.10.63:8001/"

        // 헤더 속성
        private const val CLIENT_ID = ""
        private const val CLIENT_SECRET = ""
        var userId:String = ""

        fun create(): BasicApi {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            val headerInterceptor = Interceptor {
                val request = it.request()
                    .newBuilder()
                    .addHeader("X-Client-Id", CLIENT_ID)
                    .addHeader("X-Client-Secret", CLIENT_SECRET)
                    .addHeader("X-Client-UserId", userId)
                    .build()
                return@Interceptor it.proceed(request)
            }

            val clientBuilder = OkHttpClient.Builder()

            if (PROTOCOL == "https") {

                val x509TrustManager: X509TrustManager = object : X509TrustManager {
                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        val x509Certificates = arrayOf<X509Certificate>()
                        return x509Certificates
                    }

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
                        Log.d(TAG, ": authType: $authType")
                    }

                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
                        Log.d(TAG, ": authType: $authType")
                    }
                }

                try {
                    val sslContext = SSLContext.getInstance("TLS")
                    sslContext.init(null, trustAllCerts, SecureRandom())
                    val sslSocketFactory = sslContext.socketFactory
                    clientBuilder.sslSocketFactory(sslSocketFactory, x509TrustManager)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                clientBuilder.hostnameVerifier(RelaxedHostNameVerifier())

            }

            clientBuilder.addInterceptor(headerInterceptor)
            clientBuilder.addInterceptor(httpLoggingInterceptor)
            clientBuilder.callTimeout(60, TimeUnit.SECONDS)       // 호출 타임아웃 시간 설정 60초
            clientBuilder.connectTimeout(60, TimeUnit.SECONDS)    // 연결 타임아웃 시간 설정 60초
            clientBuilder.readTimeout(60, TimeUnit.SECONDS)
            clientBuilder.writeTimeout(60, TimeUnit.SECONDS)

            val client = clientBuilder.build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(BasicApi::class.java)
        }

        private val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }

            @Throws(CertificateException::class)
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}

            @Throws(CertificateException::class)
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
        })

        class RelaxedHostNameVerifier : HostnameVerifier {
            override fun verify(hostname: String, session: SSLSession): Boolean {
                return true
            }
        }

        private var format = SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.KOREAN)
        private var seqCode = 0


        /**
         * 요청 코드 생성
         */
        @Synchronized
        fun generateRequestCode(): String {
            seqCode += 1
            if (seqCode > 999) {
                seqCode = 1
            }

            var seqCodeStr = seqCode.toString()
            if (seqCodeStr.length == 1) {
                seqCodeStr = "00$seqCodeStr"
            } else if (seqCodeStr.length == 2) {
                seqCodeStr = "0$seqCodeStr"
            }

            val date = Date()
            val dateStr = format.format(date)

            return dateStr + seqCodeStr
        }

    }
}
