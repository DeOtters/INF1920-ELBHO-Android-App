package nl.otters.elbho.repositories

import android.content.Context
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

    fun getAllAvailabilities(): LiveData<ArrayList<Availability.Slot>>? {
        val availabilities = MutableLiveData<ArrayList<Availability.Slot>>()
        availabilityService.getAllAvailabilities(getAuthToken())
            .enqueue(object : Callback<ArrayList<Availability.Slot>> {

                override fun onFailure(call: Call<ArrayList<Availability.Slot>>, t: Throwable) {
                    // TODO: not implemented
                }

                override fun onResponse(
                    call: Call<ArrayList<Availability.Slot>>,
                    response: Response<ArrayList<Availability.Slot>>
                ) {
                    availabilities.value = response.body()
                }
            })

        return availabilities
    }

    fun getAvailability(id: Int): LiveData<Availability> {
        val availability = MutableLiveData<Availability>()
        availabilityService.getAvailability(getAuthToken(), id)
            .enqueue(object : Callback<Availability> {

                override fun onFailure(call: Call<Availability>, t: Throwable) {
                    // TODO: not implemented
                }

                override fun onResponse(
                    call: Call<Availability>,
                    response: Response<Availability>
                ) {
                    availability.value = response.body()
                }

            })

        return availability
    }

    fun createAvailability(availability: Availability) {
        availabilityService.createAvailability(getAuthToken(), availability)
            .enqueue(object : Callback<Unit> {
                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    // TODO: not implemented
                }

                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    // TODO: not implemented
                }

            })
    }

    fun updateAvailability(id: Int, availability: Availability) {
        availabilityService.updateAvailability(getAuthToken(), id, availability)
            .enqueue(object : Callback<Unit> {
                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    // TODO: not implemented
                }

                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    // TODO: not implemented
                }

            })
    }

    fun deleteAvailability(id: Int) {
        availabilityService.deleteAvailability(getAuthToken(), id).enqueue(object : Callback<Unit> {
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                // TODO: not implemented
            }

            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                // TODO: not implemented
            }

        })
    }

    private fun getAuthToken(): String {
        val sharedPreferences = SharedPreferences(context)
        return sharedPreferences.getValueString("auth-token") ?: ""
    }
}