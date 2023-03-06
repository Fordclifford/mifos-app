package org.lspl.mobile.presenters

import android.content.Context
import android.util.Log

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers

import org.lspl.mobile.R
import org.lspl.mobile.api.BaseApiManager
import org.lspl.mobile.api.DataManager
import org.lspl.mobile.api.local.PreferencesHelper
import org.lspl.mobile.injection.ApplicationContext
import org.lspl.mobile.models.Page
import org.lspl.mobile.models.Question
import org.lspl.mobile.models.User
import org.lspl.mobile.models.client.Client
import org.lspl.mobile.models.payload.LoginPayload
import org.lspl.mobile.models.payload.QuestionPayload
import org.lspl.mobile.models.templates.client.SecurityQuestionOptions
import org.lspl.mobile.presenters.base.BasePresenter
import org.lspl.mobile.ui.views.LoginView
import org.lspl.mobile.utils.Constants
import org.lspl.mobile.utils.MFErrorParser

import retrofit2.HttpException

import javax.inject.Inject

/**
 * @author Vishwajeet
 * @since 05/06/16
 */
class LoginPresenter @Inject constructor(private val dataManager: DataManager?, @ApplicationContext context: Context?) :
        BasePresenter<LoginView?>(context) {

    private val preferencesHelper: PreferencesHelper? = dataManager?.preferencesHelper
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun detachView() {
        super.detachView()
        compositeDisposable.clear()
    }

    fun loadQuestions() {
        checkViewAttached()
        dataManager?.loadQuestions()
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribeWith(object : DisposableObserver<List<SecurityQuestionOptions>?>() {
                override fun onError(e: Throwable) {
                    val errorMessage: String
                    try {
                        if (e is HttpException) {
                            if (e.code() == 503) {
                                mvpView?.showMessage(context?.getString(R.string.error_server_down))
                            } else {
                                errorMessage = e.response().errorBody().string()
                                mvpView
                                    ?.showMessage(MFErrorParser.parseError(errorMessage)
                                        .developerMessage)
                            }
                        }
                    } catch (throwable: Throwable) {
                        RxJavaPlugins.getErrorHandler()
                    }
                }

                override fun onComplete() {
                }

                override fun onNext(t: List<SecurityQuestionOptions>) {
                    Log.d("Options Fetched",t.toString())
                    mvpView!!.showQuestions(t)
                }
            })?.let { compositeDisposable.add(it) }
    }

    /**
     * This method validates the username and password entered by the user
     * and reports any errors that might exists in any of the inputs.
     * If there are no errors, then we attempt to authenticate the user from
     * the server and then persist the authentication data if we successfully
     * authenticate the credentials or notify the view about any errors.
     */
    fun login(loginPayload: LoginPayload?) {
        checkViewAttached()
        if (isCredentialsValid(loginPayload)) {
            mvpView?.showProgress()
            compositeDisposable.add(dataManager?.login(loginPayload)
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribeOn(Schedulers.io())!!
                    .subscribeWith(object : DisposableObserver<User?>() {
                        override fun onComplete() {}
                        override fun onError(e: Throwable) {
                            mvpView?.hideProgress()
                            val errorMessage: String
                            try {
                                if (e is HttpException) {
                                    if (e.code() == 503) {
                                        mvpView?.showMessage(context?.getString(R.string.error_server_down))
                                    } else {
                                        errorMessage = e.response().errorBody().string()
                                        mvpView
                                                ?.showMessage(MFErrorParser.parseError(errorMessage)
                                                        .developerMessage)
                                    }
                                }
                            } catch (throwable: Throwable) {
                                RxJavaPlugins.getErrorHandler()
                            }
                        }

                        override fun onNext(user: User) {
                            val userName = user.username
                            val userID = user.userId
                            val authToken = Constants.BASIC +
                                    user.base64EncodedAuthenticationKey
                            saveAuthenticationTokenForSession(userName, userID, authToken)
                            mvpView?.onLoginSuccess()
                        }
                    })
            )
        }
    }

    fun addQuestion(questionPayload: QuestionPayload) {
        checkViewAttached()
            mvpView?.showProgress()
            compositeDisposable.add(dataManager?.addQuestion(questionPayload)
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())!!
                .subscribeWith(object : DisposableObserver<Question?>() {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                        mvpView?.hideProgress()
                        val errorMessage: String
                        try {
                            if (e is HttpException) {
                                if (e.code() == 503) {
                                    mvpView?.showMessage(context?.getString(R.string.error_server_down))
                                } else {
                                    errorMessage = e.response().errorBody().string()
                                    mvpView
                                        ?.showMessage(MFErrorParser.parseError(errorMessage)
                                            .developerMessage)
                                }
                            }
                        } catch (throwable: Throwable) {
                            RxJavaPlugins.getErrorHandler()
                        }
                    }

                    override fun onNext(question: Question) {
                        mvpView?.onLoginSuccess()
                    }
                })
            )
    }

    /**
     * This method fetching the Client, associated with current Access Token.
     */
    fun loadClient() {
        checkViewAttached()
        dataManager?.clients
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribeWith(object : DisposableObserver<Page<Client?>?>() {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                        mvpView?.hideProgress()
                        if ((e as HttpException).code() == 401) {
                            mvpView?.showMessage(context?.getString(R.string.unauthorized_client))
                        } else {
                            mvpView?.showMessage(context?.getString(R.string.error_fetching_client))
                        }
                        preferencesHelper?.clear()
                        reInitializeService()
                    }

                    override fun onNext(clientPage: Page<Client?>) {
                        mvpView?.hideProgress()
                        if (clientPage.pageItems.isNotEmpty()) {
                            val clientId = clientPage.pageItems[0]?.id?.toLong()
                            val clientName = clientPage.pageItems[0]?.displayName
                            preferencesHelper?.clientId = clientId
                            dataManager.clientId = clientId
                            reInitializeService()
                            mvpView?.showPassCodeActivity(clientName)
                        } else {
                            mvpView?.showMessage(context
                                    ?.getString(R.string.error_client_not_found))
                        }
                    }
                })?.let {
                    compositeDisposable.add(it
                    )
                }
    }

    private fun isCredentialsValid(loginPayload: LoginPayload?): Boolean {
        val username: String = loginPayload?.username.toString()
        val password: String = loginPayload?.password.toString()
        var credentialValid = true
        val resources = context?.resources
        val correctUsername = username.replaceFirst("\\s++$".toRegex(), "").trim { it <= ' ' }
        when {
            username.isEmpty() -> {
                mvpView?.showUsernameError(context?.getString(R.string.error_validation_blank,
                        context?.getString(R.string.username)))
                credentialValid = false
            }
            username.length < 5 -> {
                mvpView?.showUsernameError(context?.getString(R.string.error_validation_minimum_chars,
                        resources?.getString(R.string.username),
                        resources?.getInteger(R.integer.username_minimum_length)))
                credentialValid = false
            }
            correctUsername.contains(" ") -> {
                mvpView?.showUsernameError(context?.getString(
                        R.string.error_validation_cannot_contain_spaces,
                        resources?.getString(R.string.username),
                        context?.getString(R.string.not_contain_username)))
                credentialValid = false
            }
            else -> {
                mvpView?.clearUsernameError()
            }
        }
        when {
            password.isEmpty() -> {
                mvpView?.showPasswordError(context?.getString(R.string.error_validation_blank,
                        context?.getString(R.string.password)))
                credentialValid = false
            }
            password.length < 6 -> {
                mvpView?.showPasswordError(context?.getString(R.string.error_validation_minimum_chars,
                        resources?.getString(R.string.password),
                        resources?.getInteger(R.integer.password_minimum_length)))
                credentialValid = false
            }
            else -> {
                mvpView?.clearPasswordError()
            }
        }
        return credentialValid
    }

    /**
     * Save the authentication token from the server and the user ID.
     * The authentication token would be used for accessing the authenticated
     * APIs.
     *
     * @param userID    - The userID of the user to be saved.
     * @param authToken - The authentication token to be saved.
     */
    private fun saveAuthenticationTokenForSession(userName: String?, userID: Long, authToken: String) {
        preferencesHelper?.userName = userName
        preferencesHelper?.userId = userID
        preferencesHelper?.saveToken(authToken)
        reInitializeService()
    }

    private fun reInitializeService() {
        BaseApiManager.createService(preferencesHelper?.baseUrl, preferencesHelper?.tenant,
                preferencesHelper?.token)
    }

}