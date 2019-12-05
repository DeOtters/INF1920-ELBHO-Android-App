package nl.otters.elbho.viewModels

import androidx.lifecycle.ViewModel
import nl.otters.elbho.repositories.RequestRepository

class DoneRequestsViewModel(private val requestRepository: RequestRepository) : ViewModel() {
    //TODO: GetAllRequestAssignments where date < date.now
}