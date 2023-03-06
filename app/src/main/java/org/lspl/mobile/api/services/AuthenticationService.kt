package org.lspl.mobile.api.services

import org.lspl.mobile.api.ApiEndPoints
import org.lspl.mobile.models.User
import org.lspl.mobile.models.payload.LoginPayload

import io.reactivex.Observable
import org.lspl.mobile.models.Question
import org.lspl.mobile.models.payload.QuestionPayload
import org.lspl.mobile.models.templates.client.SecurityQuestionOptions
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * @author Vishwajeet
 * @since 09/06/16
 */

interface AuthenticationService {

    @POST(ApiEndPoints.AUTHENTICATION)
    fun authenticate(@Body loginPayload: LoginPayload?): Observable<User?>?

    @POST("/fineract-provider/api/v1/self/usersecurityquestions")
    fun addQuestion(@Body questionPayload: QuestionPayload?): Observable<Question?>?
}