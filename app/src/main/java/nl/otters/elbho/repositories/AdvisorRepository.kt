package nl.otters.elbho.repositories

import android.util.Log
import nl.otters.elbho.factories.RetrofitFactory
import nl.otters.elbho.models.Authentication
import nl.otters.elbho.models.LoginViewModel
import nl.otters.elbho.services.AdvisorService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdvisorRepository{
    private val advisorService = RetrofitFactory.get().create(AdvisorService::class.java)

    fun advisorLogin(loginCredentials: LoginViewModel){
        advisorService.login(loginCredentials).enqueue(object: Callback<Authentication> {
            override fun onResponse(call: Call<Authentication>, response: Response<Authentication>) {
                if (response.isSuccessful && response.body() != null){
                    val authentication: Authentication? = response.body()
                    Log.e("HTTP", authentication.toString())
                    //TODO: add authtoken to sharedpreferences
                }
                //TODO: wrong credentials seem to give back incorrect response?
                Log.e("HTTP", response.toString())
            }
            override fun onFailure(call: Call<Authentication>, t: Throwable) {
                //TODO: implement error handling
                Log.e("HTTP: ", "Could not fetch data" , t)
            }
        })
    }
}