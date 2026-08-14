package com.ridenorth.driver.data

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

// Backend base URL. For a physical phone, replace 10.0.2.2 with your computer's
// LAN IP (e.g. http://192.168.1.50:8080/). The emulator can reach the host at 10.0.2.2.
const val API_BASE_URL = "http://10.120.216.143:8080/"

enum class VehicleType { BODA, TUKTUK, CAR, TRUCK, LORRY }

enum class ApplicationStatus { PENDING, APPROVED, REJECTED }

data class SubmitDriverApplicationRequest(
    val phoneNumber: String,
    val fullName: String,
    val ninNumber: String? = null,
    val licenseNumber: String,
    val vehicleType: VehicleType,
    val plateNumber: String,
    val make: String? = null,
    val model: String? = null,
    val year: String? = null,
    val capacity: Int = 1,
    val documents: String? = null
)

data class DriverApplicationDto(
    val id: String,
    val applicationRef: String,
    val phoneNumber: String,
    val fullName: String,
    val ninNumber: String?,
    val licenseNumber: String,
    val vehicleType: VehicleType,
    val plateNumber: String,
    val make: String?,
    val model: String?,
    val year: String?,
    val capacity: Int,
    val documents: String?,
    val status: ApplicationStatus,
    val rejectionReason: String?,
    val submittedAt: String?,
    val reviewedAt: String?
)

data class PhoneOtpRequest(val phoneNumber: String)

data class VerifyOtpRequest(val phoneNumber: String, val otp: String)

data class OtpMessageResponse(val message: String? = null)

interface DriverApiService {

    @POST("api/auth/request-otp")
    suspend fun requestOtp(@Body body: PhoneOtpRequest): OtpMessageResponse

    @POST("api/auth/verify-otp")
    suspend fun verifyOtp(@Body body: VerifyOtpRequest): OtpMessageResponse

    @POST("api/public/driver-application")
    suspend fun submitApplication(@Body body: SubmitDriverApplicationRequest): DriverApplicationDto

    @GET("api/public/driver-application/status")
    suspend fun getApplicationStatus(@Query("phoneNumber") phoneNumber: String): DriverApplicationDto
}

object DriverApi {
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val service: DriverApiService by lazy {
        Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(DriverApiService::class.java)
    }
}
