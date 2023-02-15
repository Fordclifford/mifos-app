package org.lspl.mobile.api.services

import io.reactivex.Observable
import okhttp3.ResponseBody
import org.lspl.mobile.api.ApiEndPoints
import org.lspl.mobile.models.client.ClientResp
import org.lspl.mobile.models.register.ClientUserRegisterPayload
import org.lspl.mobile.models.register.RegisterPayload
import org.lspl.mobile.models.register.UserVerify
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by dilpreet on 31/7/17.
 */
interface RegistrationService {
    @POST(ApiEndPoints.REGISTRATION)
    fun registerUser(@Body registerPayload: RegisterPayload?): Observable<ResponseBody?>?

    @POST(ApiEndPoints.REGISTRATION + "/user")
    fun verifyUser(@Body userVerify: UserVerify?): Observable<ResponseBody?>?

    @POST(ApiEndPoints.CLIENT_USER_REGISTRATION)
    fun registerClientUser(@Body clientUserRegisterPayload: ClientUserRegisterPayload?): Observable<ClientResp?>?
}