package nl.otters.elbho.repositories

import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import nl.otters.elbho.R
import nl.otters.elbho.factories.RetrofitFactory
import nl.otters.elbho.models.Vehicle
import nl.otters.elbho.services.VehicleService
import nl.otters.elbho.utils.ResponseHandler
import nl.otters.elbho.utils.SharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class VehicleRepository(private val context: Context, private val curView: View) {
    private val vehicleService: VehicleService =
        RetrofitFactory.get().create(VehicleService::class.java)
    private val responseHandler: ResponseHandler = ResponseHandler(context, curView)

    fun getAllVehicleReservations(options: Vehicle.CarReservationOptions?): LiveData<ArrayList<Vehicle.CarWithReservations>> {
        val vehicleReservations: MutableLiveData<ArrayList<Vehicle.CarWithReservations>> =
            MutableLiveData()

        vehicleService.getAllVehicleReservations(getAuthToken(), options?.date)
            .enqueue(object : Callback<ArrayList<Vehicle.CarWithReservations>> {
                override fun onResponse(
                    call: Call<ArrayList<Vehicle.CarWithReservations>>,
                    response: Response<ArrayList<Vehicle.CarWithReservations>>
                ) {
                    if (response.code() == 200 && response.body() != null) {
                        vehicleReservations.value = response.body()!!
                    } else {
                        responseHandler.errorMessage(R.string.error_api)
                    }
                }

                override fun onFailure(
                    call: Call<ArrayList<Vehicle.CarWithReservations>>,
                    t: Throwable
                ) {
                    Log.e("HTTP Vehicles: ", "Could not fetch data", t)
                    responseHandler.errorMessage(R.string.error_api)
                }
            })
        return vehicleReservations
    }

    fun getAllVehicleReservationsByAdviser(options: Vehicle.ReservationOptions?): LiveData<ArrayList<Vehicle.Reservation>> {
        val vehicleReservations: MutableLiveData<ArrayList<Vehicle.Reservation>> = MutableLiveData()

        vehicleService.getAllVehicleReservationsByAdviser(
            getAuthToken(),
            options?.after,
            options?.sort
        ).enqueue(object : Callback<ArrayList<Vehicle.Reservation>> {
            override fun onResponse(
                call: Call<ArrayList<Vehicle.Reservation>>,
                response: Response<ArrayList<Vehicle.Reservation>>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    vehicleReservations.value = response.body()!!
                } else {
                    responseHandler.errorMessage(R.string.error_api)
                }
            }

            override fun onFailure(call: Call<ArrayList<Vehicle.Reservation>>, t: Throwable) {
                Log.e("HTTP Vehicles: ", "Could not fetch data", t)
                responseHandler.errorMessage(R.string.error_api)
            }
        })
        return vehicleReservations
    }

    fun createVehicleReservation(vehicleReservation: Vehicle.CreateReservation) {
        vehicleService.createVehicleReservation(getAuthToken(), vehicleReservation)
            .enqueue(object : Callback<Unit> {
                override fun onResponse(
                    call: Call<Unit>,
                    response: Response<Unit>
                ) {
                    if (response.code() == 201 && response.body() != null) {
                        responseHandler.successMessage(R.string.snackbar_vehicle_reserved)
                    } else {
                        responseHandler.errorMessage(R.string.error_api)
                    }
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    Log.e("HTTP Vehicles: ", "Could not fetch data", t)
                    responseHandler.errorMessage(R.string.error_api)
                }
            })
    }

    fun removeVehicleReservation(vehicleReservationId: String) {
        vehicleService.removeVehicleReservation(getAuthToken(), vehicleReservationId)
            .enqueue(object : Callback<Unit> {
                override fun onResponse(
                    call: Call<Unit>,
                    response: Response<Unit>
                ) {
                    if (response.code() == 200 && response.body() != null) {
                        responseHandler.successMessage(R.string.snackbar_vehicle_cancelled)
                    } else {
                        responseHandler.errorMessage(R.string.error_api)
                    }
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    Log.e("HTTP Vehicles: ", "Could not fetch data", t)
                    responseHandler.errorMessage(R.string.error_api)
                }
            })
    }

    private fun getAuthToken(): String {
        val sharedPreferences = SharedPreferences(context)
        return "Bearer " + (sharedPreferences.getValueString("auth-token") ?: "")
    }
}