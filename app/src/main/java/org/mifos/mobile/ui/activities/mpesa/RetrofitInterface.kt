package org.mifos.mobile.ui.activities.mpesa

import retrofit2.http.*
import javax.inject.Singleton

@Singleton
interface RetrofitInterface {

    @POST("laravel-mpesa/public/api/v1/mpesa/stkpush")
    suspend fun simulate(
        @Body model: Mpesa,
    ): Resp
}