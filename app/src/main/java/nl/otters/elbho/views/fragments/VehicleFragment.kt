package nl.otters.elbho.views.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_open_requests.*
import kotlinx.android.synthetic.main.fragment_vehicle.*
import kotlinx.android.synthetic.main.fragment_vehicle.recyclerView
import nl.otters.elbho.R
import nl.otters.elbho.adapters.VehicleListAdapter
import nl.otters.elbho.models.Vehicle
import nl.otters.elbho.repositories.VehicleRepository
import nl.otters.elbho.utils.SharedPreferences
import nl.otters.elbho.viewModels.VehicleViewModel

class VehicleFragment : Fragment() {
    private var vehicleReservationsList: ArrayList<Vehicle.Reservation> = ArrayList()
    private var reservedCarsList: ArrayList<Vehicle.Car> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_vehicle, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val vehicleRepository = VehicleRepository(activity!!.applicationContext)
        val vehicleViewModel = VehicleViewModel(vehicleRepository)

        setupRecyclerView()

        val sharedPreferences = SharedPreferences(activity!!.applicationContext)
        val authToken: String? = sharedPreferences.getValueString("auth-token")

        if (authToken == null){
            startLoginActivity()
        }

        vehicleViewModel.getAllVehiclesClaims()?.observe(this, Observer {
            if (it != null) {
                updateVehicleData(it)
                setupCarsList(vehicleViewModel)
            }
        })

        goVehicleReservation.setOnClickListener {
            findNavController().navigate(R.id.action_global_vehicleReservationFragment)
        }
    }

    private fun updateVehicleData(newRequests: ArrayList<Vehicle.Reservation>) {
        vehicleReservationsList.clear()
        vehicleReservationsList.addAll(newRequests)
        recyclerView.adapter!!.notifyDataSetChanged()
    }

    private fun setupCarsList(vehicleViewModel: VehicleViewModel){
//        TODO: dit moet het dus worden...
//        for(claim in vehicleReservationsList) {
//            vehicleViewModel.getVehicle(claim.vehicleId)?.observe(this, Observer {
//                reservedCarsList.add(it)
//            })
//        }

        for(claim in vehicleReservationsList) {
            reservedCarsList.add(Vehicle.Car(
                id = "3345fcc1-f94f-4afd-a05a-7a9091c88b71",
                licensePlate = "BC-56-OP",
                brand = "Tesla",
                model = "Model 3",
                transmission = true,
                color = "Blue",
                location = "Almere",
                picture = "blabla.jpg"))
        }

    }

    private fun setupRecyclerView() {
        val viewManager = LinearLayoutManager(activity!!.applicationContext)
        val vehicleListAdapter = VehicleListAdapter(
            activity!!.applicationContext,
            vehicleReservationsList,
            reservedCarsList,
            object : VehicleListAdapter.OnClickItemListener {
                override fun onItemClick(position: Int, view: View) {
                    findNavController().navigate(R.id.action_global_vehicleReservedFragment)
                }
            })

        recyclerView.apply {
            this.layoutManager = viewManager
            this.adapter = vehicleListAdapter
        }
    }

    private fun startLoginActivity() {
        val intent = Intent(activity!!.applicationContext, LoginActivity::class.java)
        startActivity(intent)
    }

    override fun onResume() {
        setTitle()
        super.onResume()
    }

    private fun setTitle() {
        val appTitle = activity!!.findViewById<View>(R.id.app_title) as TextView
        appTitle.setText(R.string.navigation_vehicle)
    }
}