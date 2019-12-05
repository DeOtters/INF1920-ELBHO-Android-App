package nl.otters.elbho.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import nl.otters.elbho.models.Request
import nl.otters.elbho.repositories.RequestRepository

class OpenRequestsViewModel(private val requestRepository: RequestRepository) : ViewModel() {
    fun getAllRequests(): LiveData<ArrayList<Request.Properties>> {
        return requestRepository.getAllRequests()
    }
}