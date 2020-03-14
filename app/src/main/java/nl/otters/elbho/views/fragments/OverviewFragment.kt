package nl.otters.elbho.views.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_overview.*
import nl.otters.elbho.R
import nl.otters.elbho.adapters.ViewPagerAdapter
import nl.otters.elbho.models.Request
import nl.otters.elbho.repositories.AppointmentRepository
import nl.otters.elbho.repositories.RequestRepository
import nl.otters.elbho.utils.DateParser
import nl.otters.elbho.utils.SharedPreferences
import nl.otters.elbho.utils.ZoomOutPageTransformer
import nl.otters.elbho.viewModels.OverviewViewModel
import nl.otters.elbho.views.activities.LoginActivity
import nl.otters.elbho.views.activities.NavigationActivity


class OverviewFragment : BaseFragment() {
    private var requests: ArrayList<Request.Properties> = ArrayList()
    private var todaysAppointments: ArrayList<Request.Properties> = ArrayList()
    private val dateParser: DateParser = DateParser()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_overview, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        val requestRepository = RequestRepository(activity!!.applicationContext)
        val appointmentRepository = AppointmentRepository(activity!!.applicationContext)
        val overviewViewModel = OverviewViewModel(requestRepository, appointmentRepository)
        (activity as NavigationActivity).setProgressBarVisible(true)
        overviewViewModel.getAllRequests().observe(viewLifecycleOwner, Observer {
            requests = it
            (activity as NavigationActivity).setProgressBarVisible(false)
            updateNotificationBadge(0, requests.count())
        })

        overviewViewModel.getTodaysAppointments().observe(viewLifecycleOwner, Observer {
            todaysAppointments = it
            (activity as NavigationActivity).setProgressBarVisible(false)
            updateNotificationBadge(1, todaysAppointments.count())
        })

        super.onActivityCreated(savedInstanceState)
        val sharedPreferences = SharedPreferences(activity!!.applicationContext)
        val authToken: String? = sharedPreferences.getValueString("auth-token")

        if (authToken == null) {
            startLoginActivity()
        }

        setupViewPager()
        todayTextView.text = dateParser.getDateToday()
    }

    private fun updateNotificationBadge(tabIndex: Int, badgeCount: Int) {
        tabs.getTabAt(tabIndex)!!.removeBadge()
        if (badgeCount >= 1) {
            tabs.getTabAt(tabIndex)!!.orCreateBadge.number = badgeCount
        }
    }
    private fun setupViewPager() {
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(OpenRequestsFragment(), resources.getString(R.string.overview_tab_left_label))
        adapter.addFragment(UpcomingRequestsFragment(), resources.getString(R.string.overview_tab_middle_label))
        adapter.addFragment(DoneRequestsFragment(), resources.getString(R.string.overview_tab_right_label))
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)
        tabs.getTabAt(0)!!.setIcon(R.drawable.ic_event_available_24dp)
        tabs.getTabAt(1)!!.setIcon(R.drawable.ic_event_24dp)
        tabs.getTabAt(2)!!.setIcon(R.drawable.ic_event_done_24dp)
        tabs.getTabAt(arguments!!.get("tabId") as Int)!!.select()
        viewPager.setPageTransformer(true, ZoomOutPageTransformer())
    }

    private fun startLoginActivity() {
        val intent = Intent(activity!!.applicationContext, LoginActivity::class.java)
        startActivity(intent)
    }
}