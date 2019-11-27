package nl.otters.elbho.models.viewModels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import nl.otters.elbho.models.Vehicle
import nl.otters.elbho.repositories.VehicleRepository
import nl.otters.elbho.utils.SharedPreferences

class VehicleViewModel(private val vehicleRepository: VehicleRepository, context: Context): ViewModel() {
    private val sharedPreference: SharedPreferences = SharedPreferences(context)
    private val authToken: String? = sharedPreference.getValueString("auth-token")

    fun getAllVehicles(): LiveData<ArrayList<Vehicle.Car>>? = when(authToken!=null){
        true -> vehicleRepository.getAllVehicles(authToken)
        false -> null
    }

    fun getVehicle(vehicleId: String): LiveData<Vehicle.Car>? = when(authToken!=null){
        true -> vehicleRepository.getVehicle(vehicleId, authToken)
        false -> null
    }
}

