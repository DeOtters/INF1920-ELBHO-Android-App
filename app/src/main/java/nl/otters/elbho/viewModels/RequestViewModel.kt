package nl.otters.elbho.viewModels

import androidx.lifecycle.ViewModel
import nl.otters.elbho.repositories.RequestRepository

class RequestViewModel(private val requestRepository: RequestRepository) : ViewModel() {
    fun respondToRequest(requestId: String, accept: Boolean) {
        return requestRepository.respondToRequest(requestId, accept)
    }
}