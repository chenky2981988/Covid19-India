package developer.chirag.covid19.di

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import developer.chirag.covid19.api.Covid19IndiaApiService
import developer.chirag.covid19.repositories.MainRepository
import developer.chirag.covid19.utils.isInternetAvailable
import okhttp3.Cache
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


/**
 * Created by Chirag Sidhiwala on 29/5/20.
 */

val retrofitModule = module {
    single {
        Retrofit.Builder()
            .baseUrl(Covid19IndiaApiService.BASE_URL)
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                )
            )
            .client(getOkHttpClient(androidContext()))
            .build()
            .create(Covid19IndiaApiService::class.java)
    }

    single {
        MainRepository(get())
    }
}

fun getOkHttpClient(context: Context): OkHttpClient {
    val cacheSize = (5 * 1024 * 1024).toLong()
    val localCache = Cache(context.cacheDir, cacheSize)
    return OkHttpClient.Builder()
        .cache(localCache)
        .addInterceptor { chain ->
            var request = chain.request()
            request = if (isInternetAvailable(context)) {
                request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
            } else {
                request.newBuilder().header(
                    "Cache-Control",
                    "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
                ).build()
            }
            chain.proceed(request)
        }.build()
}