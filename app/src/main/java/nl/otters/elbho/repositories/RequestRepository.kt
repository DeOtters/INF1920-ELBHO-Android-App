package nl.otters.elbho.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import nl.otters.elbho.factories.RetrofitFactory
import nl.otters.elbho.models.Request
import nl.otters.elbho.services.RequestService
import nl.otters.elbho.utils.SharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RequestRepository(private val context: Context) {
    private val requestService = RetrofitFactory.get().create(RequestService::class.java)

    fun getAllRequests(): LiveData<ArrayList<Request.Properties>> {
        val requests = MutableLiveData<ArrayList<Request.Properties>>()
        requestService.getAllRequests(getAuthToken()).enqueue(object : Callback<ArrayList<Request.Properties>> {
            override fun onResponse(
                call: Call<ArrayList<Request.Properties>>,
                response: Response<ArrayList<Request.Properties>>
            ) {
                if (response.isSuccessful && response.body() != null){
                    requests.value = response.body()
                }
            }

            override fun onFailure(call: Call<ArrayList<Request.Properties>>, t: Throwable) {
                //TODO: implement error handling
                //TODO: with current api state, show message like: couldn't establish network connection
                Log.e("HTTP: ", "Could not fetch data" , t)
            }
        })
        return requests
    }

    fun respondToRequest(appointmentId: String, accept: Boolean){
        val requests = MutableLiveData<ArrayList<Request.Properties>>()
        requestService.respondToRequest(getAuthToken(),appointmentId, accept).enqueue(object : Callback<Unit> {
            override fun onResponse(
                call: Call<Unit>,
                response: Response<Unit>
            ) {
                if (response.isSuccessful && response.body() != null){
                    // TODO: not implemented
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                //TODO: implement error handling
                //TODO: with current api state, show message like: couldn't establish network connection
                Log.e("HTTP: ", "Could not fetch data" , t)
            }
        })
    }

    private fun getAuthToken(): String {
        val sharedPreferences = SharedPreferences(context)
        return "Bearer " + (sharedPreferences.getValueString("auth-token") ?: "")
    }
}
