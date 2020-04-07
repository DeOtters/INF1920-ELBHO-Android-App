package nl.otters.elbho.repositories

import android.content.Context
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import nl.otters.elbho.R
import nl.otters.elbho.factories.RetrofitFactory
import nl.otters.elbho.models.Request
import nl.otters.elbho.services.RequestService
import nl.otters.elbho.utils.ResponseHandler
import nl.otters.elbho.utils.SharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RequestRepository(private val context: Context, view: View) {
    private val requestService = RetrofitFactory.get().create(RequestService::class.java)
    private val responseHandler: ResponseHandler = ResponseHandler(context, view)


    fun getAllRequests(): LiveData<ArrayList<Request.Properties>> {
        val requests = MutableLiveData<ArrayList<Request.Properties>>()
        requestService.getAllRequests(getAuthToken())
            .enqueue(object : Callback<ArrayList<Request.Properties>> {
                override fun onResponse(
                    call: Call<ArrayList<Request.Properties>>,
                    response: Response<ArrayList<Request.Properties>>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        requests.value = response.body()
                    } else {
                        responseHandler.errorMessage(R.string.error_api_vehicle)
                    }
                }

                override fun onFailure(call: Call<ArrayList<Request.Properties>>, t: Throwable) {
                    responseHandler.errorMessage(R.string.error_api_vehicle)
                }
            })
        return requests
    }

    fun respondToRequest(appointmentId: String, accept: Request.Respond) {
        requestService.respondToRequest(getAuthToken(), appointmentId, accept)
            .enqueue(object : Callback<Unit> {
                override fun onResponse(
                    call: Call<Unit>,
                    response: Response<Unit>
                ) {
                    if (!response.isSuccessful || response.body() == null) {
                        responseHandler.errorMessage(R.string.error_api_vehicle)
                    }
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    responseHandler.errorMessage(R.string.error_api_vehicle)
                }
            })
    }

    private fun getAuthToken(): String {
        val sharedPreferences = SharedPreferences(context)
        return "Bearer " + (sharedPreferences.getValueString("auth-token") ?: "")
    }
}
