package org.mifos.mobile.ui.activities.mpesa

import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class MainRepository @Inject constructor(private val retrofitInterface: RetrofitInterface) {


    suspend fun stkpush(mpesa: Mpesa): Resource<Resp> {
        return try {
           val resp = retrofitInterface.simulate(mpesa)
            println("Response received $resp")
            Resource.Success(resp)
        }catch (e: Exception){
            Resource.Error("An error occurred, Please try again $e")
        }
    }



}

