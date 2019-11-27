package nl.otters.elbho

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_scrolling.*
import nl.otters.elbho.models.Adviser
import nl.otters.elbho.models.Vehicle.Car
import nl.otters.elbho.models.viewModels.VehicleViewModel
import nl.otters.elbho.repositories.AdviserRepository
import nl.otters.elbho.repositories.VehicleRepository
import nl.otters.elbho.utils.SharedPreferences

class ScrollingActivity : AppCompatActivity() {
    // Repositories
    private val adviserRepository: AdviserRepository = AdviserRepository(this)
    private val vehicleRepository: VehicleRepository = VehicleRepository()

    // ViewModels
    private val vehicleViewModel: VehicleViewModel = VehicleViewModel(vehicleRepository, this)
    private val loginCredentials: Adviser.Login = Adviser.Login("582297@student.inholland.nl", "lol")


    override fun onCreate(savedInstanceState: Bundle? ) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        Log.e("ADVISER LOGIN", adviserRepository.adviserLogin(loginCredentials).toString())
        val authToken: String? = SharedPreferences(this).getValueString("auth-token")
        Log.e("authToken activity", authToken!!)

        vehicleViewModel.getAllVehicles()!!.observe(this, Observer {
            //Update UI....
            Log.e("ArrayList<Vehicle.Car>" ,it.toString())
        })

        vehicleViewModel.getVehicle("abc je mams")!!.observe(this, Observer {
            //Update UI....
            Log.e("<Vehicle.Car>" , it.toString())
        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
