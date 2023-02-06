package org.enkasacco.mobile.api.services

import io.reactivex.Observable
import okhttp3.ResponseBody
import org.enkasacco.mobile.api.ApiEndPoints
import org.enkasacco.mobile.models.payload.TransferPayload
import org.enkasacco.mobile.models.templates.account.AccountOptionsTemplate
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Created by dilpreet on 21/6/17.
 */
interface ThirdPartyTransferService {
    @get:GET(ApiEndPoints.ACCOUNT_TRANSFER + "/template?type=tpt")
    val accountTransferTemplate: Observable<AccountOptionsTemplate?>?

    @POST(ApiEndPoints.ACCOUNT_TRANSFER + "?type=\"tpt\"")
    fun makeTransfer(@Body transferPayload: TransferPayload?): Observable<ResponseBody?>?
}