package org.lspl.mobile.api.services

import io.reactivex.Observable
import okhttp3.ResponseBody
import org.lspl.mobile.api.ApiEndPoints
import org.lspl.mobile.models.notification.NotificationRegisterPayload
import org.lspl.mobile.models.notification.NotificationUserDetail
import retrofit2.http.*

/**
 * Created by dilpreet on 26/09/17.
 */
interface NotificationService {
    @GET(ApiEndPoints.DEVICE + "/registration/client/{clientId}")
    fun getUserNotificationId(@Path("clientId") clientId: Long): Observable<NotificationUserDetail?>?

    @POST(ApiEndPoints.DEVICE + "/registration")
    fun registerNotification(@Body payload: NotificationRegisterPayload?): Observable<ResponseBody?>?

    @PUT(ApiEndPoints.DEVICE + "/registration/{id}")
    fun updateRegisterNotification(
            @Path("id") id: Long,
            @Body payload: NotificationRegisterPayload?
    ): Observable<ResponseBody?>?
}