package nl.otters.elbho.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import nl.otters.elbho.models.Vehicle
import nl.otters.elbho.repositories.VehicleRepository

class VehicleViewModel(private val vehicleRepository: VehicleRepository): ViewModel() {
    fun getAllVehicleReservationsByAdviser(options: Vehicle.ReservationOptions?): LiveData<ArrayList<Vehicle.Reservation>>? {
        return vehicleRepository.getAllVehicleReservationsByAdviser(options)
    }
//
//    fun getVehicle(id: String): LiveData<Vehicle.Car>? {
//        return vehicleRepository.getAllVehicles(id)
//    }
//
    fun getAllVehicles(options: Vehicle.CarOptions?): LiveData<ArrayList<Vehicle.Car>>? {
        return vehicleRepository.getAllVehicles(options)
    }
//
    fun createVehicleReservation(vehicleReservation: Vehicle.CreateReservation) {
        return vehicleRepository.createVehicleReservation(vehicleReservation)
    }
//
//    fun deleteClaim(claimId: String) {
//        return vehicleRepository.deleteClaim(claimId)
//    }
}