package nl.otters.elbho.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import nl.otters.elbho.models.Vehicle
import nl.otters.elbho.repositories.VehicleRepository

class VehicleViewModel(private val vehicleRepository: VehicleRepository): ViewModel() {

    fun getAllVehicles(options: Vehicle.CarOptions?): LiveData<ArrayList<Vehicle.Car>> {
        return vehicleRepository.getAllVehicles(options)
    }

    fun getAllVehicleReservations(options: Vehicle.CarReservationOptions?): LiveData<ArrayList<Vehicle.CarWithReservations>> {
        return vehicleRepository.getAllVehicleReservations(options)
    }

    fun getAllVehicleReservationsByAdviser(options: Vehicle.ReservationOptions?): LiveData<ArrayList<Vehicle.Reservation>> {
        return vehicleRepository.getAllVehicleReservationsByAdviser(options)
    }

    fun createVehicleReservation(vehicleReservation: Vehicle.CreateReservation) {
        return vehicleRepository.createVehicleReservation(vehicleReservation)
    }

    fun removeVehicleReservation(reservationId: String) {
        return vehicleRepository.removeVehicleReservation(reservationId)
    }
}