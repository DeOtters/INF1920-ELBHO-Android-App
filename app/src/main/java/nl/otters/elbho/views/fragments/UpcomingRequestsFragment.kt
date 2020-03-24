package nl.otters.elbho.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.fragment_upcoming_requests.*
import nl.otters.elbho.R
import nl.otters.elbho.adapters.RequestListAdapter
import nl.otters.elbho.models.Request
import nl.otters.elbho.repositories.AppointmentRepository
import nl.otters.elbho.repositories.RequestRepository
import nl.otters.elbho.viewModels.OverviewViewModel

class UpcomingRequestsFragment : Fragment() {
    private var appointments: ArrayList<Request.Properties> = ArrayList()

    private lateinit var requestRepository: RequestRepository
    private lateinit var appointmentRepository: AppointmentRepository
    private lateinit var overviewViewModel: OverviewViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) =
        inflater.inflate(R.layout.fragment_open_requests, container, false)!!

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requestRepository = RequestRepository(activity!!.applicationContext)
        appointmentRepository = AppointmentRepository(activity!!.applicationContext)
        overviewViewModel = OverviewViewModel(requestRepository, appointmentRepository)

        setupRecyclerView()
        setupPullDownToRefresh()

        overviewViewModel.getAllUpcomingAppointments()
            .observe(viewLifecycleOwner, Observer {
                updateRecyclerView(it)
            })
    }

    override fun onResume() {
        setTitle()
        super.onResume()
    }

    private fun setTitle() {
        val appTitle = activity!!.findViewById<View>(R.id.app_title) as TextView
        val navigation = activity!!.findViewById<View>(R.id.navigation) as NavigationView
        navigation.setCheckedItem(R.id.upcoming_requests)
        appTitle.setText(R.string.navigation_upcoming_requests)
    }

    private fun updateRecyclerView(newRequests: ArrayList<Request.Properties>) {
        appointments.clear()
        appointments.addAll(newRequests)
        recyclerView.adapter!!.notifyDataSetChanged()
    }

    private fun setupPullDownToRefresh() {
        swipe_to_refresh.setOnRefreshListener {
            swipe_to_refresh.isRefreshing = true
            overviewViewModel.getAllUpcomingAppointments().observe(viewLifecycleOwner, Observer {
                updateRecyclerView(it)
                swipe_to_refresh.isRefreshing = false
            })
        }
    }

    private fun setupRecyclerView() {
        val viewManager = LinearLayoutManager(activity!!.applicationContext)
        val listAdapter = RequestListAdapter(
            activity!!.applicationContext,
            appointments,
            object : RequestListAdapter.OnClickItemListener {
                override fun onItemClick(position: Int, view: View) {
                    val bundle = Bundle()
                    bundle.putParcelable("KEY_REQUEST", appointments[position])
                    bundle.putString(
                        "KEY_APP_TITLE",
                        resources.getString(R.string.navigation_upcoming_requests)
                    )
                    findNavController().navigate(R.id.action_global_requestFragment, bundle)
                }
            })

        recyclerView.apply {
            this.layoutManager = viewManager
            this.adapter = listAdapter
        }
    }
}