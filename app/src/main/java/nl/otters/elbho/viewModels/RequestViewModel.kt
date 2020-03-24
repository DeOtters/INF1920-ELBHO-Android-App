package nl.otters.elbho.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import nl.otters.elbho.models.Location
import nl.otters.elbho.models.Request
import nl.otters.elbho.repositories.LocationRepository
import nl.otters.elbho.repositories.RequestRepository

class RequestViewModel(
    private val requestRepository: RequestRepository,
    private val locationRepository: LocationRepository
) : ViewModel() {
    fun respondToRequest(requestId: String, accept: Request.Respond) {
        return requestRepository.respondToRequest(requestId, accept)
    }

    fun putLocation(location: Location.Properties): LiveData<Boolean> {
        return locationRepository.putLocation(location)
    }
}