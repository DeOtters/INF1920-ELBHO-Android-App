package nl.otters.elbho.activities

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import kotlinx.android.synthetic.main.fragment_overview.*
import nl.otters.elbho.R
import nl.otters.elbho.adapters.ViewPagerAdapter
import nl.otters.elbho.utils.SharedPreferences
import java.text.SimpleDateFormat
import java.util.*

class OverviewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_overview, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        //TODO: GetAllRequests + GetAllRequestAssignments here
        //TODO: Pass data to child fragments
        //TODO: assign requests.length and requestsAssignments to badge.number

        super.onActivityCreated(savedInstanceState)
        val sharedPreferences = SharedPreferences(activity!!.applicationContext)
        val authToken: String? = sharedPreferences.getValueString("auth-token")

        if (authToken == null){
            startLoginActivity()
        }

        setupViewPager()
        todayTextView.text = getDateToday()
    }

    private fun getDateToday(): String{
        //Couldnt achieve this with one simpleDateFormat because we need the month to be uppercase
        val dayFormat = SimpleDateFormat("dd", Locale("nl"))
        val monthFormat = SimpleDateFormat("MMMM", Locale("nl"))
        val yearFormat = SimpleDateFormat("yy", Locale("nl"))
        val day: String = dayFormat.format(Date())
        val month: String = monthFormat.format(Date()).toUpperCase()
        val year: String = yearFormat.format(Date())

        return "$day $month $year"
    }

    private fun setupViewPager(){
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(OpenRequestsFragment(), resources.getString(R.string.overview_tab_left_label))
        adapter.addFragment(UpcomingRequestsFragment(), resources.getString(R.string.overview_tab_middle_label))
        adapter.addFragment(DoneRequestsFragment(), resources.getString(R.string.overview_tab_right_label))
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)
        tabs.getTabAt(0)!!.orCreateBadge.number = 3
        tabs.getTabAt(1)!!.orCreateBadge.number = 1
        tabs.getTabAt(0)!!.setIcon(R.drawable.ic_event_available_24dp)
        tabs.getTabAt(1)!!.setIcon(R.drawable.ic_event_24dp)
        tabs.getTabAt(2)!!.setIcon(R.drawable.ic_event_done_24dp)
    }

    private fun startLoginActivity() {
        val intent = Intent(activity!!.applicationContext, LoginActivity::class.java)
        startActivity(intent)
    }
}