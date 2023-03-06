package org.lspl.mobile.api.services

import io.reactivex.Observable
import okhttp3.ResponseBody
import org.lspl.mobile.api.ApiEndPoints
import org.lspl.mobile.models.UpdatePasswordPayload
import org.lspl.mobile.models.passwordreset.NewpasswordPayload
import org.lspl.mobile.models.passwordreset.ResetPayload
import org.lspl.mobile.models.passwordreset.TokenPayload
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

/*
* Created by saksham on 13/July/2018
*/   interface UserDetailsService {
    @PUT(ApiEndPoints.USER)
    fun updateAccountPassword(@Body payload: UpdatePasswordPayload?): Observable<ResponseBody?>?

    @POST(ApiEndPoints.USER+"/resetPassword")
    fun requestToken(@Body payload: ResetPayload): Observable<ResponseBody?>?

    @POST(ApiEndPoints.FORGOT+"/reset-password")
    fun newPassword(@Body payload: NewpasswordPayload): Observable<ResponseBody?>?


}