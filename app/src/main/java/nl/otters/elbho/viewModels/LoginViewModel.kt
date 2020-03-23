package nl.otters.elbho.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import nl.otters.elbho.models.Adviser
import nl.otters.elbho.repositories.AdviserRepository

class LoginViewModel(private val adviserRepository: AdviserRepository) : ViewModel() {
    fun adviserLogin(loginCredentials: Adviser.Login): LiveData<Boolean> {
        return adviserRepository.adviserLogin(loginCredentials)
    }
}