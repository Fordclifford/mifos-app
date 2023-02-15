package org.lspl.mobile.api.services

import io.reactivex.Observable
import okhttp3.ResponseBody
import org.lspl.mobile.api.ApiEndPoints
import org.lspl.mobile.models.accounts.loan.LoanAccount
import org.lspl.mobile.models.accounts.loan.LoanWithAssociations
import org.lspl.mobile.models.accounts.loan.LoanWithdraw
import org.lspl.mobile.models.payload.LoansPayload
import org.lspl.mobile.models.templates.loans.LoanTemplate
import org.lspl.mobile.utils.Constants
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