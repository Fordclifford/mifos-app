package org.enkasacco.mobile.api.services

import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import org.enkasacco.mobile.api.ApiEndPoints
import org.enkasacco.mobile.models.Page
import org.enkasacco.mobile.models.stkpush.StkpushResponse
import org.enkasacco.mobile.models.stkpush.StkpushStatusResponse
import org.enkasacco.mobile.models.client.Client
import org.enkasacco.mobile.models.client.ClientAccounts
import org.enkasacco.mobile.models.client.NextOfKinPayload
import org.enkasacco.mobile.models.register.IdentifierPayload
import org.enkasacco.mobile.models.stkpush.StkpushRequestPayload
import org.enkasacco.mobile.models.stkpush.StkpushStatusRequest
import org.enkasacco.mobile.models.templates.client.FamilyMemberOptions
import retrofit2.http.*

/**
 * @author Vishwajeet
 * @since 20/6/16.
 */
interface ClientService {
    //This is a default call and Loads client from 0 to 200
    @get:GET(ApiEndPoints.CLIENTS)
    val clients: Observable<Page<Client?>?>?

    @GET(ApiEndPoints.CLIENTS + "/{clientId}")
    fun getClientForId(@Path(CLIENT_ID) clientId: Long?): Observable<Client?>?

    @GET(ApiEndPoints.CLIENTS + "/{clientId}/images")
    fun getClientImage(@Path(CLIENT_ID) clientId: Long?): Observable<ResponseBody?>?

    @GET(ApiEndPoints.CLIENTS + "/{clientId}/accounts")
    fun getClientAccounts(@Path(CLIENT_ID) clientId: Long?): Observable<ClientAccounts?>?

    @GET(ApiEndPoints.CLIENTS + "/{clientId}/accounts")
    fun getAccounts(
        @Path(CLIENT_ID) clientId: Long?,
        @Query("fields") accountType: String?
    ): Observable<ClientAccounts?>?

    @POST(ApiEndPoints.CLIENTS + "/{clientId}/createidentifier")
    fun createIdentifier(
        @Path(CLIENT_ID) clientId: Long?,
        @Body payload: ArrayList<IdentifierPayload?>
    ): Observable<ResponseBody?>?

    @Multipart
    @POST(ApiEndPoints.CLIENTS + "/{clientId}/createimage")
    fun createImage(
        @Path(CLIENT_ID) clientId: Long?,
        @Part body: MultipartBody.Part
    ): Observable<ResponseBody?>?

    @POST(ApiEndPoints.CLIENTS + "/{entityId}/createdocument")
    @Multipart
    fun createDocument(
        @Path("entityId") entityId: Long,
        @Part("name") nameOfDocument: String?,
        @Part("description") description: String?,
        @Part typedFile: MultipartBody.Part?
    ): Observable<ResponseBody?>?

    @GET(ApiEndPoints.CLIENTS + "/{clientId}/gettemplate")
    fun getClientTemplate( @Path(CLIENT_ID) clientId: Long?): Observable<FamilyMemberOptions?>?

    @POST(ApiEndPoints.CLIENTS + "/{clientId}/createnok")
    fun createNok(
        @Path(CLIENT_ID) clientId: Long?,
        @Body payload: NextOfKinPayload?
    ): Observable<ResponseBody?>?

    @POST(ApiEndPoints.CLIENTS + "/stkpush")
    fun stkPush(
        @Body payload: StkpushRequestPayload?
    ): Observable<StkpushResponse?>?

    @POST(ApiEndPoints.CLIENTS + "/stkpushstatus")
    fun stkPushStatus(
        @Body payload: StkpushStatusRequest
    ): Observable<StkpushStatusResponse?>?

    companion object {
        const val CLIENT_ID = "clientId"
    }
}