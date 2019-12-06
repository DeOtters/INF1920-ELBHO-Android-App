package nl.otters.elbho.models.viewModels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import nl.otters.elbho.models.Vehicle
import nl.otters.elbho.repositories.VehicleRepository
import nl.otters.elbho.utils.SharedPreferences

class VehicleViewModel(private val vehicleRepository: VehicleRepository, private val context: Context): ViewModel() {
    fun getAllVehicles(): LiveData<ArrayList<Vehicle.Car>>? = when(getAuthToken() != ""){
        true -> vehicleRepository.getAllVehicles()
        false -> null
    }

    fun getVehicle(vehicleId: String): LiveData<Vehicle.Car>? = when(getAuthToken() != ""){
        true -> vehicleRepository.getVehicle(vehicleId)
        false -> null
    }

    private fun getAuthToken(): String {
        val sharedPreferences = SharedPreferences(context)
        return sharedPreferences.getValueString("auth-token") ?: ""
    }
}

