package nl.otters.elbho.views.fragments

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
import nl.otters.elbho.viewModels.VehicleViewModel
import nl.otters.elbho.views.activities.NavigationActivity
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class VehicleFragment : BaseFragment() {
    private var vehicleReservationList: ArrayList<Vehicle.Reservation> = ArrayList()
    private lateinit var vehicleViewModel: VehicleViewModel

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
        vehicleViewModel = VehicleViewModel(vehicleRepository)

        setupRecyclerView()
        setupPullDownToRefresh()

        setupReservation()

        goVehicleReservation.setOnClickListener {
            findNavController().navigate(R.id.action_global_vehicleReservationFragment)
        }
    }

    private fun setupReservation() {
        (activity as NavigationActivity).setProgressBarVisible(true)
        val newRequests: ArrayList<Vehicle.Reservation> = ArrayList()
        vehicleViewModel.getAllVehicleReservationsByAdviser(
            Vehicle.ReservationOptions(
                after = SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale("nl")
                ).format(Calendar.getInstance().time), sort = "ASC"
            )
        ).observe(viewLifecycleOwner, Observer {
            (activity as NavigationActivity).setProgressBarVisible(false)
            if (it != null) {
                vehicleReservationList.addAll(it)
                newRequests.addAll(it)
                updateRecyclerView(newRequests)
                checkTimeSelected()
            }
        })
    }

    private fun updateRecyclerView(reservations: ArrayList<Vehicle.Reservation>) {
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

    private fun setupPullDownToRefresh() {
        swipe_to_refresh.setOnRefreshListener {
            swipe_to_refresh.isRefreshing = true
            vehicleViewModel.getAllVehicleReservationsByAdviser(
                Vehicle.ReservationOptions(
                    after = SimpleDateFormat(
                        "yyyy-MM-dd",
                        Locale("nl")
                    ).format(Calendar.getInstance().time), sort = "ASC"
                )
            ).observe(viewLifecycleOwner, Observer {
                (activity as NavigationActivity).setProgressBarVisible(false)
                if (it != null) {
                    vehicleReservationList.addAll(it)
                    updateRecyclerView(it)
                    swipe_to_refresh.isRefreshing = false
                }
            })
        }
    }

    private fun checkTimeSelected() {
        if (vehicleReservationList.isEmpty()) {
            recyclerView.visibility = View.INVISIBLE
            empty_view_2.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            empty_view_2.visibility = View.INVISIBLE
        }
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
        appTitle.setText(R.string.navigation_vehicle_all_reservations)
    }
}