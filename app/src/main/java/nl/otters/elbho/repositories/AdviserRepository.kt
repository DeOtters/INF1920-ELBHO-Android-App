package nl.otters.elbho.repositories

import android.content.Context
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import nl.otters.elbho.R
import nl.otters.elbho.factories.RetrofitFactory
import nl.otters.elbho.models.Adviser
import nl.otters.elbho.services.AdviserService
import nl.otters.elbho.utils.ResponseHandler
import nl.otters.elbho.utils.SharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdviserRepository(private val context: Context, view: View) {
    private val adviserService = RetrofitFactory.get().create(AdviserService::class.java)
    private val responseHandler: ResponseHandler = ResponseHandler(context, view)


    fun adviserLogin(loginCredentials: Adviser.Login): LiveData<Boolean> {
        val success = MutableLiveData<Boolean>()
        adviserService.login(loginCredentials).enqueue(object : Callback<Adviser.Authentication> {
            override fun onResponse(
                call: Call<Adviser.Authentication>,
                response: Response<Adviser.Authentication>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val authentication: Adviser.Authentication? = response.body()
                    val sharedPreferences = SharedPreferences(context)
                    sharedPreferences.save("auth-token", authentication!!.token)
                    success.value = true
                } else {
                    success.value = false
                    responseHandler.errorMessage(R.string.error_api)
                }
            }

            override fun onFailure(call: Call<Adviser.Authentication>, t: Throwable) {
                success.value = false
                responseHandler.errorMessage(R.string.error_api)
            }
        })
        return success
    }

    fun getAdviser(): LiveData<Adviser.Properties> {
        val adviser = MutableLiveData<Adviser.Properties>()
        adviserService.getAdviser(getAuthToken()).enqueue(object : Callback<Adviser.Properties> {

            override fun onResponse(
                call: Call<Adviser.Properties>,
                response: Response<Adviser.Properties>
            ) {
                val loggedInAdviser: Adviser.Properties? = response.body()
                val sharedPreferences = SharedPreferences(context)
                sharedPreferences.save("adviser-id", loggedInAdviser!!.id)
                adviser.value = response.body()
            }

            override fun onFailure(call: Call<Adviser.Properties>, t: Throwable) {
                responseHandler.errorMessage(R.string.error_api)
            }
        })

        return adviser
    }

    private fun getAuthToken(): String {
        val sharedPreferences = SharedPreferences(context)
        return "Bearer " + (sharedPreferences.getValueString("auth-token") ?: "")
    }
}