package nl.otters.elbho.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import nl.otters.elbho.factories.RetrofitFactory
import nl.otters.elbho.models.Vehicle
import nl.otters.elbho.services.VehicleService
import nl.otters.elbho.utils.SharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VehicleRepository (private val context: Context) {
    private val vehicleService: VehicleService = RetrofitFactory.get().create(VehicleService::class.java)

    fun getAllVehicles(): LiveData<ArrayList<Vehicle.Car>> {
        val vehicles: MutableLiveData<ArrayList<Vehicle.Car>> = MutableLiveData()

        vehicleService.getAllVehicles(getAuthToken()).enqueue(object : Callback<ArrayList<Vehicle.Car>> {
            override fun onResponse(
                call: Call<ArrayList<Vehicle.Car>>,
                response: Response<ArrayList<Vehicle.Car>>
            ) {
                if (response.message() == "OK" && response.body() != null){
                    vehicles.value = response.body()!!
                }
            }

            override fun onFailure(call: Call<ArrayList<Vehicle.Car>>, t: Throwable) {
                //TODO: implement error handling
                Log.e("HTTP Vehicles: ", "Could not fetch data" , t)
            }
        })
        return vehicles
    }

    fun getVehicle(vehicleId: String): LiveData<Vehicle.Car>{
        val vehicle: MutableLiveData<Vehicle.Car> = MutableLiveData()

        vehicleService.getVehicle(getAuthToken(), vehicleId).enqueue(object : Callback<Vehicle.Car> {
            override fun onResponse(call: Call<Vehicle.Car>, response: Response<Vehicle.Car>) {
                if (response.message() == "OK"  && response.body() != null){
                    vehicle.value = response.body()!!
                    Log.e("vehicle", response.body().toString())
                }
            }

            override fun onFailure(call: Call<Vehicle.Car>, t: Throwable) {
                Log.e("HTTP Vehicle: ", "Could not fetch Vehicle data" , t)
            }
        })
        return vehicle
    }

    // We should only update the location of the Vehicle
    fun updateVehicle(vehicleId: String, vehicle: Vehicle){
        vehicleService.updateVehicle(getAuthToken(), vehicleId).enqueue(object : Callback<Vehicle.Car> {
            override fun onResponse(call: Call<Vehicle.Car>, response: Response<Vehicle.Car>) {
                //TODO: Not implemented
            }

            override fun onFailure(call: Call<Vehicle.Car>, t: Throwable) {
                Log.e("HTTP Vehicle: ", "Could not update Vehicle data" , t)
            }
        })
    }

    private fun getAuthToken(): String {
        val sharedPreferences = SharedPreferences(context)
        return sharedPreferences.getValueString("auth-token") ?: ""
    }
}