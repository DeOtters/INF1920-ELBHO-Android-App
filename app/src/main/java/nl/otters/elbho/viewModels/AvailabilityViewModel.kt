package nl.otters.elbho.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import nl.otters.elbho.models.Availability
import nl.otters.elbho.repositories.AvailabilityRepository

class AvailabilityViewModel(private val availabilityRepository: AvailabilityRepository) : ViewModel() {
    fun getAllAvailabilities(timePeriod: Availability.TimePeriod?): LiveData<ArrayList<Availability.Slot>>? {
        return availabilityRepository.getAvailabilities(timePeriod)
    }
}