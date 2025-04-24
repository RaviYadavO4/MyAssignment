package com.example.myassignment.di

import android.content.Context
import android.util.Log
import com.example.myassignment.BuildConfig
import com.example.myassignment.app.AppExecutors
import com.example.myassignment.network.AssignmentApiServices
import com.example.myassignment.persistence.Prefs
import com.example.myassignment.utils.LiveDataCallAdapterFactory
import com.hadiyarajesh.flower.calladpater.FlowCallAdapterFactory
import com.squareup.moshi.Moshi
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

import java.io.File
import java.util.concurrent.TimeUnit

private const val CACHE_STALE_IN_DAYS = 60 * 60 * 24 * 7 // seven day
private const val CACHE_MAX_AGE_IN_SECONDS = 60  // 1 minute

val retrofitModule = module {


    single<AssignmentApiServices>(named("default")) {
        apiServices(okHttpClient = get(qualifier = named("default")))
    }

    single<OkHttpClient>(named("default")) {
        getOkHttpClient(
            cache = get(),
            httpLoggingInterceptor = getHttpLoggingInterceptor(),
            prefs = get()
        )
    }

    single {
        AppExecutors()
    }

    single {
        provideCacheFile(get())
    }
}

val moshi = Moshi.Builder().build()

private fun apiServices(okHttpClient: OkHttpClient): AssignmentApiServices {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create().asLenient())
        .addCallAdapterFactory(FlowCallAdapterFactory())
        .addCallAdapterFactory(LiveDataCallAdapterFactory())
        .client(okHttpClient)
        .build()
        .create(AssignmentApiServices::class.java)
}

private fun getOkHttpClient(
    cache: Cache,
    httpLoggingInterceptor: HttpLoggingInterceptor,
    prefs: Prefs
): OkHttpClient {
    val builder = getOkHttpClientBuilder()

    return builder
        .cache(cache)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(httpLoggingInterceptor)
        .build()
}

fun getHttpLoggingInterceptor(): HttpLoggingInterceptor {

    val httpLoggingInterceptor = HttpLoggingInterceptor { message ->
        // Custom Logcat tag for easier filtering
        Log.d("MyNetworkLogger", message)
    }

    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

    return httpLoggingInterceptor
}

private fun provideCacheFile(context: Context): Cache {
    val cacheSize = (5 * 1024 * 1024).toLong()
    val cacheFile = File(context.cacheDir, "api-cache")
    return Cache(cacheFile, cacheSize)
}

private val builder = OkHttpClient.Builder()

fun getOkHttpClientBuilder(): OkHttpClient.Builder {
    return builder
}
