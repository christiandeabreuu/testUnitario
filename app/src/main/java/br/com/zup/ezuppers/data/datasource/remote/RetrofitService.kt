package br.com.zup.ezuppers.data.datasource.remote

import androidx.viewbinding.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitService {

    companion object {

        private const val BASE_URL = "https://servicodados.ibge.gov.br/"
        const val CEP_URL = "https://viacep.com.br/ws/{cep}/json/"

        private val retrofit: Retrofit by lazy {

            val httpClient = OkHttpClient.Builder()
            httpClient.readTimeout(30, TimeUnit.SECONDS)
            httpClient.connectTimeout(30, TimeUnit.SECONDS)
            httpClient.writeTimeout(30, TimeUnit.SECONDS)

            if (BuildConfig.DEBUG) {
                val logInterceptor = HttpLoggingInterceptor()
                logInterceptor.level = HttpLoggingInterceptor.Level.BODY
                httpClient.addInterceptor(logInterceptor)
            }

            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build()
        }

        @JvmStatic
        val apiService: API
            get() = retrofit.create(API::class.java)
    }
}