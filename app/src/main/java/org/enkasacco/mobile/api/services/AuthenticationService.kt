package org.enkasacco.mobile.api.services

import org.enkasacco.mobile.api.ApiEndPoints
import org.enkasacco.mobile.models.User
import org.enkasacco.mobile.models.payload.LoginPayload

import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * @author Vishwajeet
 * @since 09/06/16
 */

interface AuthenticationService {

    @POST(ApiEndPoints.AUTHENTICATION)
    fun authenticate(@Body loginPayload: LoginPayload?): Observable<User?>?
}