package nl.otters.elbho.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import nl.otters.elbho.models.Vehicle
import nl.otters.elbho.repositories.VehicleRepository

class VehicleViewModel(private val vehicleRepository: VehicleRepository): ViewModel() {
    fun getAllVehiclesClaims(): LiveData<ArrayList<Vehicle.Reservation>>? {
        return vehicleRepository.getAllVehicleClaims()
    }

    fun getVehicle(id: String): LiveData<Vehicle.Car>? {
        return vehicleRepository.getVehicle(id)
    }

    fun deleteClaim(claimId: String) {
        return vehicleRepository.deleteClaim(claimId)
    }
}