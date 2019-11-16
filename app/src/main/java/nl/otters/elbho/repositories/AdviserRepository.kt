package nl.otters.elbho.repositories

import android.content.Context
import android.util.Log
import nl.otters.elbho.factories.RetrofitFactory
import nl.otters.elbho.models.Adviser
import nl.otters.elbho.services.AdviserService
import nl.otters.elbho.utils.SharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdviserRepository(private val context: Context) {
    private val adviserService = RetrofitFactory.get().create(AdviserService::class.java)

    fun adviserLogin(loginCredentials: Adviser.Login) {
        adviserService.login(loginCredentials).enqueue(object : Callback<Adviser.Authentication> {
            override fun onResponse(
                call: Call<Adviser.Authentication>,
                response: Response<Adviser.Authentication>
            ) {
                if (response.isSuccessful && response.body() != null){
                    val authentication: Adviser.Authentication? = response.body()
                    val sharedPreferences = SharedPreferences(context)
                    sharedPreferences.save("auth-token", authentication!!.authToken)

                }
                //TODO: wrong credentials seem to give back incorrect response?
                Log.e("HTTP", response.toString())
            }

            override fun onFailure(call: Call<Adviser.Authentication>, t: Throwable) {
                //TODO: implement error handling
                Log.e("HTTP: ", "Could not fetch data" , t)
            }
        })
    }
}