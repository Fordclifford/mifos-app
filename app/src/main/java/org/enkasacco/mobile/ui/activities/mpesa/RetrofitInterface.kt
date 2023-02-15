package org.enkasacco.mobile.ui.activities.mpesa

import retrofit2.Call
import retrofit2.http.*
import javax.inject.Singleton

@Singleton
interface RetrofitInterface {

    @POST("laravel-mpesa/public/api/v1/mpesa/stkpush")
    @JvmSuppressWildcards
    fun simulate(
        @Body model: Mpesa,
    ): Call<Resp>
}