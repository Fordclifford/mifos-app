package org.lspl.mobile.viewModels

import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import okhttp3.ResponseBody
import org.lspl.mobile.api.DataManager
import org.lspl.mobile.models.payload.LoansPayload
import org.lspl.mobile.ui.enums.LoanState
import javax.inject.Inject

class ReviewLoanApplicationViewModel @Inject constructor(var dataManager: DataManager?) : ViewModel() {

    private lateinit var loansPayload: LoansPayload
    private lateinit var loanState: LoanState
    private lateinit var loanName: String
    private lateinit var accountNo: String
    private var loanId: Long = 0

    fun insertData(loanState: LoanState, loansPayload: LoansPayload, loanName: String, accountNo: String) {
        this.loanState = loanState
        this.loansPayload = loansPayload
        this.loanName = loanName
        this.accountNo = accountNo
    }

    fun insertData(loanState: LoanState, loanId: Long, loansPayload: LoansPayload, loanName: String, accountNo: String) {
        this.loanState = loanState
        this.loanId = loanId
        this.loansPayload = loansPayload
        this.loanName = loanName
        this.accountNo = accountNo
    }

    fun getLoanProduct() = loansPayload.productName

    fun getLoanPurpose() = loansPayload.loanPurpose

    fun getPrincipal() = loansPayload.principal

    fun getCurrency() = loansPayload.currency

    fun getSubmissionDate() = loansPayload.submittedOnDate

    fun getDisbursementDate() = loansPayload.expectedDisbursementDate

    fun getLoanName() = loanName

    fun getAccountNo() = accountNo

    fun getLoanState() = loanState

    fun submitLoan(): Observable<ResponseBody?>? {
        loansPayload.productName = null
        loansPayload.loanPurpose = null
        loansPayload.currency = null
        return if (loanState == LoanState.CREATE)
            dataManager?.createLoansAccount(loansPayload)
        else
            dataManager?.updateLoanAccount(loanId, loansPayload)
    }
}
