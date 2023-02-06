package org.enkasacco.mobile.ui.activities.mpesa

import dagger.hilt.android.scopes.ActivityScoped
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@ActivityScoped
class MainRepository @Inject constructor(private val retrofitInterface: RetrofitInterface) {


    fun stkpush(mpesa: Mpesa): Resource<Resp?> {
        val call: Call<Resp> = retrofitInterface.simulate(mpesa)
        var resp: Resp? = null;
        var th: Throwable? = null;
        call.enqueue(object : Callback<Resp?> {
            override fun onResponse(call: Call<Resp?>?, response: Response<Resp?>) {
                resp = response.body()!!
            }

            override fun onFailure(call: Call<Resp?>?, t: Throwable?) {
                th = t
                Resource.Error("An error occurred, Please try again ", t)
            }
        })
        return if (resp == null) {
            Resource.Error("An error occurred, Please try again")
        } else {
            Resource.Success(resp)
        }
    }


}

