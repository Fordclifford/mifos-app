package org.lspl.mobile.api

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Function
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import org.lspl.mobile.api.local.DatabaseHelper
import org.lspl.mobile.api.local.PreferencesHelper
import org.lspl.mobile.models.*
import org.lspl.mobile.models.accounts.loan.LoanAccount
import org.lspl.mobile.models.accounts.loan.LoanWithAssociations
import org.lspl.mobile.models.accounts.loan.LoanWithdraw
import org.lspl.mobile.models.accounts.savings.SavingsAccountApplicationPayload
import org.lspl.mobile.models.accounts.savings.SavingsAccountUpdatePayload
import org.lspl.mobile.models.accounts.savings.SavingsAccountWithdrawPayload
import org.lspl.mobile.models.accounts.savings.SavingsWithAssociations
import org.lspl.mobile.models.beneficiary.Beneficiary
import org.lspl.mobile.models.beneficiary.BeneficiaryPayload
import org.lspl.mobile.models.beneficiary.BeneficiaryUpdatePayload
import org.lspl.mobile.models.client.Client
import org.lspl.mobile.models.client.ClientAccounts
import org.lspl.mobile.models.client.ClientResp
import org.lspl.mobile.models.client.NextOfKinPayload
import org.lspl.mobile.models.guarantor.GuarantorApplicationPayload
import org.lspl.mobile.models.guarantor.GuarantorPayload
import org.lspl.mobile.models.guarantor.GuarantorTemplatePayload
import org.lspl.mobile.models.notification.MifosNotification
import org.lspl.mobile.models.notification.NotificationRegisterPayload
import org.lspl.mobile.models.notification.NotificationUserDetail
import org.lspl.mobile.models.passwordreset.NewpasswordPayload
import org.lspl.mobile.models.passwordreset.ResetPayload
import org.lspl.mobile.models.passwordreset.TokenPayload
import org.lspl.mobile.models.payload.LoansPayload
import org.lspl.mobile.models.payload.LoginPayload
import org.lspl.mobile.models.payload.QuestionPayload
import org.lspl.mobile.models.payload.TransferPayload
import org.lspl.mobile.models.register.ClientUserRegisterPayload
import org.lspl.mobile.models.register.IdentifierPayload
import org.lspl.mobile.models.register.RegisterPayload
import org.lspl.mobile.models.register.UserVerify
import org.lspl.mobile.models.stkpush.StkpushRequestPayload
import org.lspl.mobile.models.stkpush.StkpushResponse
import org.lspl.mobile.models.stkpush.StkpushStatusRequest
import org.lspl.mobile.models.stkpush.StkpushStatusResponse
import org.lspl.mobile.models.templates.account.AccountOptionsTemplate
import org.lspl.mobile.models.templates.beneficiary.BeneficiaryTemplate
import org.lspl.mobile.models.templates.client.FamilyMemberOptions
import org.lspl.mobile.models.templates.client.SecurityQuestionOptions
import org.lspl.mobile.models.templates.loans.LoanTemplate
import org.lspl.mobile.models.templates.savings.SavingsAccountTemplate
import org.mifos.mobile.FakeRemoteDataSource
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author Vishwajeet
 * @since 13/6/16.
 */
@Singleton
class DataManager @Inject constructor(
    val preferencesHelper: PreferencesHelper, private val baseApiManager: BaseApiManager,
    private val databaseHelper: DatabaseHelper
) {
    var clientId: Long? = preferencesHelper.clientId
    fun login(loginPayload: LoginPayload?): Observable<User?>? {
        return baseApiManager.authenticationApi?.authenticate(loginPayload)
    }

    fun addQuestion(questionPayload: QuestionPayload?): Observable<Question?>? {
        return baseApiManager.authenticationApi?.addQuestion(questionPayload)
    }

    val clients: Observable<Page<Client?>?>?
        get() = baseApiManager.clientsApi?.clients
    val currentClient: Observable<Client?>?
        get() = baseApiManager.clientsApi?.getClientForId(clientId)
    val clientImage: Observable<ResponseBody?>?
        get() = baseApiManager.clientsApi?.getClientImage(clientId)
    val clientAccounts: Observable<ClientAccounts?>?
        get() = baseApiManager.clientsApi?.getClientAccounts(clientId)

    fun getAccounts(accountType: String?): Observable<ClientAccounts?>? {
        return baseApiManager.clientsApi?.getAccounts(clientId, accountType)
    }

    fun getRecentTransactions(offset: Int, limit: Int): Observable<Page<Transaction?>?>? {
        return baseApiManager.recentTransactionsApi
            ?.getRecentTransactionsList(clientId, offset, limit)
    }

    fun getClientCharges(clientId: Long): Observable<Page<Charge?>?>? {
        return baseApiManager.clientChargeApi?.getClientChargeList(clientId)
            ?.concatMap { chargePage -> databaseHelper.syncCharges(chargePage) }
    }

    fun getLoanCharges(loanId: Long): Observable<List<Charge?>?>? {
        return baseApiManager.clientChargeApi?.getLoanAccountChargeList(loanId)
    }

    fun getSavingsCharges(savingsId: Long): Observable<List<Charge?>?>? {
        return baseApiManager.clientChargeApi?.getSavingsAccountChargeList(savingsId)
    }

    fun getSavingsWithAssociations(
        accountId: Long?,
        associationType: String?
    ): Observable<SavingsWithAssociations?>? {
        return baseApiManager
            .savingAccountsListApi?.getSavingsWithAssociations(accountId, associationType)
    }

    val accountTransferTemplate: Observable<AccountOptionsTemplate?>?
        get() = baseApiManager.savingAccountsListApi?.accountTransferTemplate

    fun makeTransfer(transferPayload: TransferPayload?): Observable<ResponseBody?>? {
        return baseApiManager.savingAccountsListApi?.makeTransfer(transferPayload)
    }

    fun getSavingAccountApplicationTemplate(client: Long?): Observable<SavingsAccountTemplate?>? {
        return baseApiManager.savingAccountsListApi
            ?.getSavingsAccountApplicationTemplate(client)
    }

    fun submitSavingAccountApplication(
        payload: SavingsAccountApplicationPayload?
    ): Observable<ResponseBody?>? {
        return baseApiManager.savingAccountsListApi?.submitSavingAccountApplication(payload)
    }

    fun updateSavingsAccount(
        accountId: Long?, payload: SavingsAccountUpdatePayload?
    ): Observable<ResponseBody?>? {
        return baseApiManager.savingAccountsListApi
            ?.updateSavingsAccountUpdate(accountId, payload)
    }

    fun submitWithdrawSavingsAccount(
        accountId: String?, payload: SavingsAccountWithdrawPayload?
    ): Observable<ResponseBody?>? {
        return baseApiManager.savingAccountsListApi
            ?.submitWithdrawSavingsAccount(accountId, payload)
            ?.onErrorResumeNext(Function<Throwable?, ObservableSource<out ResponseBody>> {
                Observable.just(
                    ResponseBody.create(
                        MediaType.parse("text/parse"),
                        "Saving Account Withdrawn Successfully"
                    )
                )
            })
    }

    fun getLoanAccountDetails(loanId: Long): Observable<LoanAccount?>? {
        return baseApiManager.loanAccountsListApi?.getLoanAccountsDetail(loanId)
    }

    fun getLoanWithAssociations(
        associationType: String?,
        loanId: Long?
    ): Observable<LoanWithAssociations?>? {
        return baseApiManager.loanAccountsListApi
            ?.getLoanWithAssociations(loanId, associationType)
    }

    val loanTemplate: Observable<LoanTemplate?>?
        get() = baseApiManager.loanAccountsListApi?.getLoanTemplate(clientId)

    fun getLoanTemplateByProduct(productId: Int?): Observable<LoanTemplate?>? {
        return baseApiManager.loanAccountsListApi
            ?.getLoanTemplateByProduct(clientId, productId)
    }

    fun createLoansAccount(loansPayload: LoansPayload?): Observable<ResponseBody?>? {
        return baseApiManager.loanAccountsListApi?.createLoansAccount(loansPayload)
    }

    fun updateLoanAccount(loanId: Long, loansPayload: LoansPayload?): Observable<ResponseBody?>? {
        return baseApiManager.loanAccountsListApi?.updateLoanAccount(loanId, loansPayload)
    }

    fun withdrawLoanAccount(
        loanId: Long?,
        loanWithdraw: LoanWithdraw?
    ): Observable<ResponseBody?>? {
        return baseApiManager.loanAccountsListApi?.withdrawLoanAccount(loanId, loanWithdraw)
    }

    val beneficiaryList: Observable<List<Beneficiary?>?>?
        get() = baseApiManager.beneficiaryApi?.beneficiaryList
    val beneficiaryTemplate: Observable<BeneficiaryTemplate?>?
        get() = baseApiManager.beneficiaryApi?.beneficiaryTemplate

    fun createBeneficiary(beneficiaryPayload: BeneficiaryPayload?): Observable<ResponseBody?>? {
        return baseApiManager.beneficiaryApi?.createBeneficiary(beneficiaryPayload)
    }

    fun updateBeneficiary(
        beneficiaryId: Long?,
        payload: BeneficiaryUpdatePayload?
    ): Observable<ResponseBody?>? {
        return baseApiManager.beneficiaryApi?.updateBeneficiary(beneficiaryId, payload)
    }

    fun deleteBeneficiary(beneficiaryId: Long?): Observable<ResponseBody?>? {
        return baseApiManager.beneficiaryApi?.deleteBeneficiary(beneficiaryId)
    }

    val thirdPartyTransferTemplate: Observable<AccountOptionsTemplate?>?
        get() = baseApiManager.thirdPartyTransferApi?.accountTransferTemplate

    fun makeThirdPartyTransfer(transferPayload: TransferPayload?): Observable<ResponseBody?>? {
        return baseApiManager.thirdPartyTransferApi?.makeTransfer(transferPayload)
    }

    fun registerUser(registerPayload: RegisterPayload?): Observable<ResponseBody?>? {
        return baseApiManager.registrationApi?.registerUser(registerPayload)
    }


    fun registerClientUser(registerPayload: ClientUserRegisterPayload?): Observable<ClientResp?>? {
        return baseApiManager.registrationApi?.registerClientUser(registerPayload)
    }

    fun createIdentifier(
        clientIdentifier: Long?,
        identifierPayload: ArrayList<IdentifierPayload?>
    ): Observable<ResponseBody?>? {
        return baseApiManager.clientsApi?.createIdentifier(clientIdentifier, identifierPayload)
    }

    fun verifyUser(userVerify: UserVerify?): Observable<ResponseBody?>? {
        return baseApiManager.registrationApi?.verifyUser(userVerify)
    }

    val clientLocalCharges: Observable<Page<Charge?>?>?
        get() = databaseHelper.clientCharges
    val notifications: Observable<List<MifosNotification?>?>?
        get() = databaseHelper.notifications
    val unreadNotificationsCount: Observable<Int>
        get() = databaseHelper.unreadNotificationsCount

    fun registerNotification(payload: NotificationRegisterPayload?): Observable<ResponseBody?>? {
        return baseApiManager.notificationApi?.registerNotification(payload)
    }

    fun updateRegisterNotification(
        id: Long,
        payload: NotificationRegisterPayload?
    ): Observable<ResponseBody?>? {
        return baseApiManager.notificationApi?.updateRegisterNotification(id, payload)
    }

    fun getUserNotificationId(id: Long): Observable<NotificationUserDetail?>? {
        return baseApiManager.notificationApi?.getUserNotificationId(id)
    }

    fun updateAccountPassword(payload: UpdatePasswordPayload?): Observable<ResponseBody?>? {
        return baseApiManager.userDetailsService?.updateAccountPassword(payload)
    }

    fun getGuarantorTemplate(loanId: Long?): Observable<GuarantorTemplatePayload?>? {
        return baseApiManager.guarantorApi?.getGuarantorTemplate(loanId)
            ?.onErrorResumeNext(Function<Throwable?, ObservableSource<out GuarantorTemplatePayload>> {
                Observable.just(
                    FakeRemoteDataSource.guarantorTemplatePayload
                )
            })
    }

    fun getGuarantorList(loanId: Long): Observable<List<GuarantorPayload?>?>? {
        return baseApiManager.guarantorApi?.getGuarantorList(loanId)
            ?.onErrorResumeNext(Function<Throwable?, ObservableSource<out List<GuarantorPayload>>> {
                Observable.just(
                    FakeRemoteDataSource.guarantorsList
                )
            })
    }

    fun createGuarantor(
        loanId: Long?,
        payload: GuarantorApplicationPayload?
    ): Observable<ResponseBody?>? {
        return baseApiManager.guarantorApi?.createGuarantor(loanId, payload)
            ?.onErrorResumeNext(Function<Throwable?, ObservableSource<out ResponseBody>> {
                val responseBody = ResponseBody.create(
                    MediaType
                        .parse("text/plain"), "Guarantor Added Successfully"
                )
                Observable.just(responseBody)
            })
    }

    fun updateGuarantor(
        payload: GuarantorApplicationPayload?,
        loanId: Long?, guarantorId: Long?
    ): Observable<ResponseBody?>? {
        return baseApiManager.guarantorApi?.updateGuarantor(payload, loanId, guarantorId)
            ?.onErrorResumeNext(Function<Throwable?, ObservableSource<out ResponseBody>> {
                Observable.just(
                    ResponseBody.create(
                        MediaType
                            .parse("plain/text"), "Guarantor Updated Successfully"
                    )
                )
            })
    }

    fun deleteGuarantor(loanId: Long?, guarantorId: Long?): Observable<ResponseBody?>? {
        return baseApiManager.guarantorApi?.deleteGuarantor(loanId, guarantorId)
            ?.onErrorResumeNext(Function<Throwable?, ObservableSource<out ResponseBody>> {
                Observable.just(
                    ResponseBody.create(
                        MediaType
                            .parse("plain/text"), "Guarantor Deleted Successfully"
                    )
                )
            })
    }

    fun createImage(
        clientId: Long,
        requestFileBody: MultipartBody.Part
    ): Observable<ResponseBody?>? {
        return baseApiManager.clientsApi?.createImage(clientId, requestFileBody)
    }

    fun createDocument(
        entityId: Long, name: String?, desc: String?,
        file: MultipartBody.Part?
    ): Observable<ResponseBody?>? {
        return baseApiManager.clientsApi!!.createDocument(entityId, name, desc, file)
    }

    fun loadFamilyTemplate(clientId: Long): Observable<FamilyMemberOptions?>? {
        return baseApiManager.clientsApi?.getClientTemplate(clientId)
    }

    fun loadQuestions(): Observable<List<SecurityQuestionOptions>?>? {
        return baseApiManager.clientsApi?.loadQuestions()
    }

    fun createNok(payload: NextOfKinPayload, clientId: Long?): Observable<ResponseBody?>? {
        return baseApiManager.clientsApi!!.createNok(clientId, payload)

    }

    fun stkPush(payload: StkpushRequestPayload): Observable<StkpushResponse?>? {
        return baseApiManager.clientsApi!!.stkPush(payload)
    }

    fun stkPushStatus(payload: StkpushStatusRequest): Observable<StkpushStatusResponse?>? {
        return baseApiManager.clientsApi!!.stkPushStatus(payload)
    }

    fun requestToken(payload: ResetPayload): Observable<ResponseBody?>? {
        return baseApiManager.userDetailsService!!.requestToken(payload)
    }

    fun token(payload: TokenPayload): Observable<ResponseBody?>? {
        return this.baseApiManager.clientsApi!!.token(payload)
    }

    fun newPassword(payload: NewpasswordPayload): Observable<ResponseBody?>? {
        return this.baseApiManager.userDetailsService!!.newPassword(payload)
    }


}