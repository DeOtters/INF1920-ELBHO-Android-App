package nl.otters.elbho.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import nl.otters.elbho.models.Vehicle
import nl.otters.elbho.repositories.VehicleRepository

class VehicleViewModel(private val vehicleRepository: VehicleRepository): ViewModel() {
//    fun getAllVehiclesClaims(): LiveData<ArrayList<Vehicle.Reservation>>? {
//        return vehicleRepository.getAllVehicleReservations()
//    }
//
//    fun getVehicle(id: String): LiveData<Vehicle.Car>? {
//        return vehicleRepository.getAllVehicles(id)
//    }
//
//    fun getAllVehicles(): LiveData<ArrayList<Vehicle.Car>>? {
//        return vehicleRepository.getAllVehicles()
//    }
//
//    fun createClaim(reservation: Vehicle.CreateReservation) {
//        return vehicleRepository.createClaim(reservation)
//    }
//
//    fun deleteClaim(claimId: String) {
//        return vehicleRepository.deleteClaim(claimId)
//    }
}