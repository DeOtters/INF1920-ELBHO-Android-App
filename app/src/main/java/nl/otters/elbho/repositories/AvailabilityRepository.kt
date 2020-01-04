package nl.otters.elbho.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import nl.otters.elbho.factories.RetrofitFactory
import nl.otters.elbho.models.Availability
import nl.otters.elbho.services.AvailabilityService
import nl.otters.elbho.utils.SharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AvailabilityRepository(private val context: Context) {
    private val availabilityService = RetrofitFactory.get().create(AvailabilityService::class.java)

    fun getAvailabilities(timePeriod: Availability.TimePeriod?): LiveData<ArrayList<Availability.Slot>>? {
        val availabilities = MutableLiveData<ArrayList<Availability.Slot>>()
        availabilityService.getAvailabilities(getAuthToken(), timePeriod)
            .enqueue(object : Callback<ArrayList<Availability.Slot>> {
                override fun onResponse(
                    call: Call<ArrayList<Availability.Slot>>,
                    response: Response<ArrayList<Availability.Slot>>
                ) {
                    availabilities.value = response.body()
                }

                override fun onFailure(call: Call<ArrayList<Availability.Slot>>, t: Throwable) {
                    // TODO: not implemented
                    Log.e("HTTP", "Could not fetch data", t)
                }
            })

        return availabilities
    }

    fun createAvailability(availability: Availability.Slot) {
        availabilityService.createAvailability(getAuthToken(), availability)
            .enqueue(object : Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    // TODO: not implemented
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    // TODO: not implemented
                }
            })
    }

    private fun getAuthToken(): String {
        val sharedPreferences = SharedPreferences(context)
        return "Bearer " + (sharedPreferences.getValueString("auth-token") ?: "")
    }
}