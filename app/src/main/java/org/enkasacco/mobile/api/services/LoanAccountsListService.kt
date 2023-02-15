package org.enkasacco.mobile.api.services

import io.reactivex.Observable
import okhttp3.ResponseBody
import org.enkasacco.mobile.api.ApiEndPoints
import org.enkasacco.mobile.models.accounts.loan.LoanAccount
import org.enkasacco.mobile.models.accounts.loan.LoanWithAssociations
import org.enkasacco.mobile.models.accounts.loan.LoanWithdraw
import org.enkasacco.mobile.models.payload.LoansPayload
import org.enkasacco.mobile.models.templates.loans.LoanTemplate
import org.enkasacco.mobile.utils.Constants
import retrofit2.http.*

/**
 * @author Vishwajeet
 * @since 23/6/16.
 */
interface LoanAccountsListService {
    @GET(ApiEndPoints.LOANS + "/{loanId}/")
    fun getLoanAccountsDetail(@Path("loanId") loanId: Long): Observable<LoanAccount?>?

    @GET(ApiEndPoints.LOANS + "/{loanId}")
    fun getLoanWithAssociations(
            @Path("loanId") loanId: Long?,
            @Query("associations") associationType: String?
    ): Observable<LoanWithAssociations?>?

    @GET(ApiEndPoints.LOANS + "/template?templateType=individual")
    fun getLoanTemplate(@Query("clientId") clientId: Long?): Observable<LoanTemplate?>?

    @GET(ApiEndPoints.LOANS + "/template?templateType=individual")
    fun getLoanTemplateByProduct(
            @Query("clientId") clientId: Long?,
            @Query("productId") productId: Int?
    ): Observable<LoanTemplate?>?

    @POST(ApiEndPoints.LOANS)
    fun createLoansAccount(@Body loansPayload: LoansPayload?): Observable<ResponseBody?>?

    @PUT(ApiEndPoints.LOANS + "/{loanId}/")
    fun updateLoanAccount(
            @Path("loanId") loanId: Long,
            @Body loansPayload: LoansPayload?
    ): Observable<ResponseBody?>?

    @POST(ApiEndPoints.LOANS + "/{loanId}?command=withdrawnByApplicant")
    fun withdrawLoanAccount(
            @Path(Constants.LOAN_ID) loanId: Long?,
            @Body loanWithdraw: LoanWithdraw?
    ): Observable<ResponseBody?>?
}