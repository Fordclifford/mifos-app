package org.lspl.mobile.ui.activities.mpesa

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class RetrofitInstance {

    @Singleton
    @Provides
    fun getMainRepository(
        api: RetrofitInterface
    ) = MainRepository(api)

    @Singleton
    @Provides
    fun getRetrofit(): RetrofitInterface {
        val okHttpClient: OkHttpClient?
        val logging = HttpLoggingInterceptor()
// set your desired log level
// set your desired log level
        logging.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
// add your other interceptors …

// add logging as last interceptor
// add your other interceptors …

// add logging as last interceptor
        httpClient.addInterceptor(logging) //

        okHttpClient = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl(Constants.ROUTE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(SynchronousCallAdapterFactory.create())
            .client(okHttpClient)
            .build()
            .create(RetrofitInterface::class.java)
    }
}