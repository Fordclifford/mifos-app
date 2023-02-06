package org.enkasacco.mobile.api.services

import io.reactivex.Observable
import okhttp3.ResponseBody
import org.enkasacco.mobile.api.ApiEndPoints
import org.enkasacco.mobile.models.UpdatePasswordPayload
import org.enkasacco.mobile.models.passwordreset.NewpasswordPayload
import org.enkasacco.mobile.models.passwordreset.ResetPayload
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

    @POST(ApiEndPoints.USER+"/savePassword")
    fun newPassword(@Body payload: NewpasswordPayload): Observable<ResponseBody?>?


}