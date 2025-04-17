package com.example.kiosk

import AuthInterceptor
import com.example.kiosk.ApiService.DeviceApiService
import com.example.kiosk.ApiService.EmployeeApiService
import com.example.kiosk.ApiService.VisitorApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.X509Certificate
import javax.net.ssl.*

object RetrofitClient {
    private const val BASE_URL = "https://34.73.139.149/api/"
    private const val TOKEN = "4381371b-d31e-45ae-956b-7f1ac0dc5a19"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val authInterceptor = AuthInterceptor(TOKEN)

    // Create an Unsafe OkHttpClient to ignore SSL certificate errors
    private val unsafeHttpClient: OkHttpClient by lazy {
        try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts = arrayOf<TrustManager>(
                object : X509TrustManager {
                    override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
                    override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
                    override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
                }
            )

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())

            // Create an SSL socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory

            OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                .hostnameVerifier { _, _ -> true } // Accept any hostname
                .addInterceptor(loggingInterceptor)
                .addInterceptor(authInterceptor) // Add the AuthInterceptor
                .build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(unsafeHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Create instances of each API service
    val visitorApi: VisitorApiService = retrofit.create(VisitorApiService::class.java)
    val employeeApi: EmployeeApiService = retrofit.create(EmployeeApiService::class.java)
    val deviceApi: DeviceApiService = retrofit.create(DeviceApiService::class.java)
}
