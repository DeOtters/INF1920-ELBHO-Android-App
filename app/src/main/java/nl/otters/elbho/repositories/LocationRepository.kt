package nl.otters.elbho.repositories

import android.content.Context
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import nl.otters.elbho.R
import nl.otters.elbho.factories.RetrofitFactory
import nl.otters.elbho.models.Location
import nl.otters.elbho.services.LocationService
import nl.otters.elbho.utils.ResponseHandler
import nl.otters.elbho.utils.SharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LocationRepository(private val context: Context, view: View) {
    private val locationService = RetrofitFactory.get().create(LocationService::class.java)
    private val responseHandler: ResponseHandler = ResponseHandler(context, view)

    fun putLocation(location: Location.Properties): LiveData<Boolean> {
        val success: MutableLiveData<Boolean> = MutableLiveData(false)
        locationService.putLocation(getAuthToken(), location)
            .enqueue(object : Callback<Location.Properties> {
                override fun onResponse(
                    call: Call<Location.Properties>,
                    response: Response<Location.Properties>
                ) {
                    if (response.body() != null && response.isSuccessful) {
                        success.value = true
                    } else {
                        responseHandler.errorMessage(R.string.error_api_vehicle)
                    }
                }

                override fun onFailure(call: Call<Location.Properties>, t: Throwable) {
                    responseHandler.errorMessage(R.string.error_api_vehicle)
                }
            })
        return success
    }

    private fun getAuthToken(): String {
        val sharedPreferences = SharedPreferences(context)
        return "Bearer " + (sharedPreferences.getValueString("auth-token") ?: "")
    }
}
