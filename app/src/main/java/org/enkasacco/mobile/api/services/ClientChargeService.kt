package org.enkasacco.mobile.api.services

import io.reactivex.Observable

import org.enkasacco.mobile.api.ApiEndPoints
import org.enkasacco.mobile.models.Charge
import org.enkasacco.mobile.models.Page

import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @author Vishwajeet
 * @since 17/8/16.
 */
interface ClientChargeService {

    @GET(ApiEndPoints.CLIENTS + "/{clientId}/charges")
    fun getClientChargeList(@Path("clientId") clientId: Long?): Observable<Page<Charge?>?>?

    @GET(ApiEndPoints.LOANS + "/{loanId}/charges")
    fun getLoanAccountChargeList(@Path("loanId") loanId: Long?): Observable<List<Charge?>?>?

    @GET(ApiEndPoints.SAVINGS_ACCOUNTS + "/{savingsId}/charges")
    fun getSavingsAccountChargeList(@Path("savingsId") savingsId: Long?): Observable<List<Charge?>?>?

}