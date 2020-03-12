package nl.otters.elbho.views.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_vehicle.*
import nl.otters.elbho.R
import nl.otters.elbho.adapters.VehicleListAdapter
import nl.otters.elbho.models.Vehicle
import nl.otters.elbho.repositories.VehicleRepository
import nl.otters.elbho.utils.SharedPreferences
import nl.otters.elbho.viewModels.VehicleViewModel
import nl.otters.elbho.views.activities.LoginActivity
import nl.otters.elbho.views.activities.NavigationActivity
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class VehicleFragment : BaseFragment() {
    private var vehicleReservationList: ArrayList<Vehicle.Reservation> = ArrayList()

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

        setupReservation(vehicleViewModel)

        goVehicleReservation.setOnClickListener {
            findNavController().navigate(R.id.action_global_vehicleReservationFragment)
        }
    }

   private fun setupReservation(vehicleViewModel: VehicleViewModel){
       (activity as NavigationActivity).setProgressBarVisible(true)
       val newRequests: ArrayList<Vehicle.Reservation> = ArrayList()
        vehicleViewModel.getAllVehicleReservationsByAdviser(Vehicle.ReservationOptions(after = SimpleDateFormat("yyyy-MM-dd", Locale("nl")).format(Calendar.getInstance().time), sort = "ASC")).observe(viewLifecycleOwner, Observer {
            (activity as NavigationActivity).setProgressBarVisible(false)
            if (it != null) {
                vehicleReservationList.addAll(it)
                newRequests.addAll(it)
                updateVehicleData(newRequests)
            }
        })
    }

    private fun updateVehicleData(reservations: ArrayList<Vehicle.Reservation>) {
        vehicleReservationList.clear()
        vehicleReservationList.addAll(reservations)
        recyclerView.adapter!!.notifyDataSetChanged()
    }

    private fun setupRecyclerView() {
        val viewManager = LinearLayoutManager(activity!!.applicationContext)
        val vehicleListAdapter = VehicleListAdapter(
            activity!!.applicationContext,
            vehicleReservationList,
            object : VehicleListAdapter.OnClickItemListener {
                override fun onItemClick(position: Int, view: View) {
                    val bundle = Bundle()
                    bundle.putParcelable("KEY_Reservation", vehicleReservationList[position])
                    findNavController().navigate(R.id.action_global_vehicleReservedFragment, bundle)
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

    override fun onPause() {
        super.onPause()
        (activity as NavigationActivity).setProgressBarVisible(false)
    }
    private fun setTitle() {
        val appTitle = activity!!.findViewById<View>(R.id.app_title) as TextView
        appTitle.setText(R.string.navigation_vehicle)
    }
}