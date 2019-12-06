package nl.otters.elbho.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.Snackbar
import nl.otters.elbho.R
import nl.otters.elbho.factories.RetrofitFactory
import nl.otters.elbho.models.Adviser
import nl.otters.elbho.services.AdviserService
import nl.otters.elbho.utils.SharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdviserRepository(private val context: Context) {
    private val adviserService = RetrofitFactory.get().create(AdviserService::class.java)

    fun adviserLogin(loginCredentials: Adviser.Login): LiveData<Boolean>{
        val success = MutableLiveData<Boolean>()
        adviserService.login(loginCredentials).enqueue(object : Callback<Adviser.Authentication> {
            override fun onResponse(
                call: Call<Adviser.Authentication>,
                response: Response<Adviser.Authentication>
            ) {
                if (response.isSuccessful && response.body() != null){
                    val authentication: Adviser.Authentication? = response.body()
                    val sharedPreferences = SharedPreferences(context)
                    sharedPreferences.save("auth-token", authentication!!.authToken)
                    success.value = true
                }else{
                    success.value = false
                }
            }

            override fun onFailure(call: Call<Adviser.Authentication>, t: Throwable) {
                //TODO: implement error handling
                //TODO: with current api state, show message like: couldn't establish network connection
                //TODO: with that in mind I think we shouldn't only return LiveData<Boolean>, but something in the line of LiveData<success, message>.
                Log.e("HTTP", "Could not fetch data" , t)
                success.value = false
            }
        })
        return success
    }

    fun getAdvisor() : LiveData<Adviser.Properties> {
        val adviser = MutableLiveData<Adviser.Properties>()
        adviserService.getAdviser(getAuthToken()).enqueue(object : Callback<Adviser.Properties> {

            override fun onResponse(call: Call<Adviser.Properties>, response: Response<Adviser.Properties>) {
                adviser.value = response.body()
            }

            override fun onFailure(call: Call<Adviser.Properties>, t: Throwable) {
                // TODO: not implemented
            }
        })

        return adviser
    }

    private fun getAuthToken(): String {
        val sharedPreferences = SharedPreferences(context)
        return sharedPreferences.getValueString("auth-token") ?: ""
    }
}