package nl.otters.elbho.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import nl.otters.elbho.models.Request
import nl.otters.elbho.repositories.RequestRepository

class AfgerondViewModel(private val requestRepository: RequestRepository): ViewModel() {
    //TODO: GetAllRequestAssignments where date < date.now
}