package nl.otters.elbho.activities

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_overview.*
import nl.otters.elbho.R
import nl.otters.elbho.adapters.ViewPagerAdapter
import nl.otters.elbho.fragments.AanstaandFragment
import nl.otters.elbho.fragments.AfgerondFragment
import nl.otters.elbho.fragments.OpenstaandFragment
import nl.otters.elbho.utils.SharedPreferences

class OverviewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_overview, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val sharedPreferences = SharedPreferences(activity!!.applicationContext)
        val authToken: String? = sharedPreferences.getValueString("auth-token")

        if (authToken == null){
            startLoginActivity()
        }

        setupViewPager()
    }

    private fun setupViewPager(){
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(OpenstaandFragment(), resources.getString(R.string.overview_tab_left_label))
        adapter.addFragment(AanstaandFragment(),  resources.getString(R.string.overview_tab_middle_label))
        adapter.addFragment(AfgerondFragment(),  resources.getString(R.string.overview_tab_right_label))
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)
        tabs.getTabAt(0)!!.setIcon(R.drawable.ic_event_available_24dp)
        tabs.getTabAt(1)!!.setIcon(R.drawable.ic_event_24dp)
        tabs.getTabAt(2)!!.setIcon(R.drawable.ic_event_done_24dp)
    }

    private fun startLoginActivity() {
        val intent = Intent(activity!!.applicationContext, LoginActivity::class.java)
        startActivity(intent)
    }
}