package nl.otters.elbho.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
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
import nl.otters.elbho.views.activities.NavigationActivity


class OverviewFragment : BaseFragment(), TabLayout.OnTabSelectedListener {
    private lateinit var requestRepository: RequestRepository
    private lateinit var appointmentRepository: AppointmentRepository
    private lateinit var overviewViewModel: OverviewViewModel

    private var requests: ArrayList<Request.Properties> = ArrayList()
    private var todaysAppointments: ArrayList<Request.Properties> = ArrayList()
    private val dateParser: DateParser = DateParser()
    private var tabIndex: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_overview, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        requestRepository = RequestRepository(activity!!.applicationContext)
        appointmentRepository = AppointmentRepository(activity!!.applicationContext)
        overviewViewModel = OverviewViewModel(requestRepository, appointmentRepository)

        setNotificationBadges()
        setupViewPager()
        todayTextView.text = dateParser.getDateToday()
    }

    private fun saveTodaysAppointment() {
        val sharedPreferences = SharedPreferences(activity!!.applicationContext)
        val todaysAppointmentIds = ArrayList<String?>()

        for (appointment in todaysAppointments) {
            todaysAppointmentIds.add(appointment.id)
        }

        sharedPreferences.setArrayPrefs("todaysAppointments", todaysAppointmentIds)
    }

    private fun setNotificationBadges() {
        (activity as NavigationActivity).setProgressBarVisible(true)
        overviewViewModel.getAllRequests().observe(viewLifecycleOwner, Observer {
            requests = it
            (activity as NavigationActivity).setProgressBarVisible(false)
            updateNotificationBadge(0, requests.count())
        })

        overviewViewModel.getTodaysAppointments().observe(viewLifecycleOwner, Observer {
            todaysAppointments = it
            saveTodaysAppointment()
            (activity as NavigationActivity).setProgressBarVisible(false)
            updateNotificationBadge(1, todaysAppointments.count())
        })
    }

    private fun updateNotificationBadge(tabIndex: Int, badgeCount: Int) {
        tabs.getTabAt(tabIndex)!!.removeBadge()
        if (badgeCount >= 1) {
            tabs.getTabAt(tabIndex)!!.orCreateBadge.number = badgeCount
        }
    }

    private fun setupViewPager() {
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(
            OpenRequestsFragment(),
            resources.getString(R.string.overview_tab_left_label)
        )
        adapter.addFragment(
            UpcomingRequestsFragment(),
            resources.getString(R.string.overview_tab_middle_label)
        )
        adapter.addFragment(
            DoneRequestsFragment(),
            resources.getString(R.string.overview_tab_right_label)
        )
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)
        tabs.getTabAt(0)!!.setIcon(R.drawable.ic_event_available_24dp)
        tabs.getTabAt(1)!!.setIcon(R.drawable.ic_event_24dp)
        tabs.getTabAt(2)!!.setIcon(R.drawable.ic_event_done_24dp)
        val tabIndex = arguments!!.get("tabId") as Int
        tabs.getTabAt(tabIndex)!!.select()
        selectNavigationItem()
        viewPager.setPageTransformer(true, ZoomOutPageTransformer())
    }

    private fun selectNavigationItem() {
        val navigation = activity!!.findViewById<View>(R.id.navigation) as NavigationView
        when (tabIndex) {
            0 -> {
                navigation.setCheckedItem(R.id.open_requests)
            }
            1 -> {
                navigation.setCheckedItem(R.id.upcoming_requests)
            }
            2 -> {
                navigation.setCheckedItem(R.id.done_requests)
            }
        }
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        tabIndex = tab?.position ?: 0
    }
}