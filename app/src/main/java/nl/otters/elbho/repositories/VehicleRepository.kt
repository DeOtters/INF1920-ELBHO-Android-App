package nl.otters.elbho.repositories

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import nl.otters.elbho.R
import nl.otters.elbho.factories.RetrofitFactory
import nl.otters.elbho.models.Vehicle
import nl.otters.elbho.services.VehicleService
import nl.otters.elbho.utils.SharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class VehicleRepository (private val context: Context) {
    private val vehicleService: VehicleService = RetrofitFactory.get().create(VehicleService::class.java)

    fun getAllVehicles(options: Vehicle.CarOptions?): LiveData<ArrayList<Vehicle.Car>> {
        val vehicles: MutableLiveData<ArrayList<Vehicle.Car>> = MutableLiveData()

        vehicleService.getAllVehicles(getAuthToken(), options?.page, options?.limit).enqueue(object : Callback<ArrayList<Vehicle.Car>> {
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

    fun getAllVehicleReservations(options: Vehicle.CarReservationOptions?): LiveData<ArrayList<Vehicle.CarWithReservations>> {
        val vehicleReservations: MutableLiveData<ArrayList<Vehicle.CarWithReservations>> = MutableLiveData()

        vehicleService.getAllVehicleReservations(getAuthToken(), options?.date).enqueue(object : Callback<ArrayList<Vehicle.CarWithReservations>> {
            override fun onResponse(
                call: Call<ArrayList<Vehicle.CarWithReservations>>,
                response: Response<ArrayList<Vehicle.CarWithReservations>>
            ) {
                if (response.message() == "OK" && response.body() != null){
                    vehicleReservations.value = response.body()!!
                }
            }

            override fun onFailure(call: Call<ArrayList<Vehicle.CarWithReservations>>, t: Throwable) {
                //TODO: implement error handling
                Log.e("HTTP Vehicles: ", "Could not fetch data" , t)
            }
        })
        return vehicleReservations
    }

    fun getAllVehicleReservationsByAdviser(options: Vehicle.ReservationOptions?): LiveData<ArrayList<Vehicle.Reservation>> {
        val vehicleReservations: MutableLiveData<ArrayList<Vehicle.Reservation>> = MutableLiveData()

        vehicleService.getAllVehicleReservationsByAdviser(getAuthToken(), options?.after, options?.sort).enqueue(object : Callback<ArrayList<Vehicle.Reservation>> {
            override fun onResponse(
                call: Call<ArrayList<Vehicle.Reservation>>,
                response: Response<ArrayList<Vehicle.Reservation>>
            ) {
                if (response.message() == "OK" && response.body() != null){
                    vehicleReservations.value = response.body()!!
                }
            }

            override fun onFailure(call: Call<ArrayList<Vehicle.Reservation>>, t: Throwable) {
                //TODO: implement error handling
                Log.e("HTTP Vehicles: ", "Could not fetch data" , t)
            }
        })
        return vehicleReservations
    }

    fun createVehicleReservation(vehicleReservation: Vehicle.CreateReservation){
        vehicleService.createVehicleReservation(getAuthToken(), vehicleReservation).enqueue(object : Callback<Unit> {
            override fun onResponse(
                call: Call<Unit>,
                response: Response<Unit>
            ) {
                if (response.code() == 201 && response.body() != null){
                    //TODO: not implemented

//                    Toast.makeText(context, R.string.toast_vehicle_reserved,Toast.LENGTH_LONG).show()
                } else if (response.code() == 409) {
                    Toast.makeText(context,R.string.toast_vehicle_already_reserved,Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                //TODO: implement error handling
                Log.e("HTTP Vehicles: ", "Could not fetch data" , t)
            }
        })
    }

    fun removeVehicleReservation(vehicleReservationId: String){
        vehicleService.removeVehicleReservation(getAuthToken(), vehicleReservationId).enqueue(object : Callback<Unit> {
            override fun onResponse(
                call: Call<Unit>,
                response: Response<Unit>
            ) {
                if (response.message() == "OK" && response.body() != null){
                    //TODO: not implemented
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                //TODO: implement error handling
                Log.e("HTTP Vehicles: ", "Could not fetch data" , t)
            }
        })
    }

    private fun getAuthToken(): String {
        val sharedPreferences = SharedPreferences(context)
        return "Bearer " + (sharedPreferences.getValueString("auth-token") ?: "")
    }
}