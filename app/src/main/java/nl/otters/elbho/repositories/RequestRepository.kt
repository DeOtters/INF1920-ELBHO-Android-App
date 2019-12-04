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

    fun getRequest(id: Int): LiveData<Request.Properties> {
        val request = MutableLiveData<Request.Properties>()
        requestService.getRequest(getAuthToken(), id).enqueue(object : Callback<Request.Properties> {
            override fun onResponse(
                call: Call<Request.Properties>,
                response: Response<Request.Properties>
            ) {
                if (response.isSuccessful && response.body() != null){
                    request.value = response.body()
                }else{
                    Log.e("HTTP: ", "Could not fetch data, id doesn't exist?")
                }
            }

            override fun onFailure(call: Call<Request.Properties>, t: Throwable) {
                //TODO: implement error handling
                //TODO: with current api state, show message like: couldn't establish network connection
                Log.e("HTTP: ", "Could not fetch data" , t)
            }
        })
        return request
    }
    fun getAllRequests(): LiveData<ArrayList<Request.Properties>> {
        val requests = MutableLiveData<ArrayList<Request.Properties>>()
        requestService.getAllRequests(getAuthToken()).enqueue(object : Callback<ArrayList<Request.Properties>> {
            override fun onResponse(
                call: Call<ArrayList<Request.Properties>>,
                response: Response<ArrayList<Request.Properties>>
            ) {
                if (response.isSuccessful && response.body() != null){
                    requests.value = response.body()
                }else{
                    Log.e("Authtoken at getALl", getAuthToken())
                    Log.e("HTTP: ", "Could not fetch data, no authtoken?")
                    Log.e("response ", response.body().toString())
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

    private fun getAuthToken(): String {
        val sharedPreferences = SharedPreferences(context)
        return sharedPreferences.getValueString("auth-token") ?: ""
    }
}
