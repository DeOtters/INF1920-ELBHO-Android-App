package nl.otters.elbho.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nl.otters.elbho.models.Request
import nl.otters.elbho.repositories.RequestRepository

class OverviewViewModel(private val requestRepository: RequestRepository) : ViewModel() {
    private var requests: LiveData<ArrayList<Request.Properties>> = MutableLiveData()

    init {
        loadAllRequests()
    }

    private fun loadAllRequests(){
        requests = requestRepository.getAllRequests()
    }

    fun getAllRequests(): LiveData<ArrayList<Request.Properties>> {
        return requests
    }

//    TODO: getAllRequestAssignments()
}