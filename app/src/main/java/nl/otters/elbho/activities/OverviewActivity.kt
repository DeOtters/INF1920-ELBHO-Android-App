package nl.otters.elbho.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_overview.*
import nl.otters.elbho.R
import nl.otters.elbho.adapters.ViewPagerAdapter
import nl.otters.elbho.fragments.AanstaandFragment
import nl.otters.elbho.fragments.AfgerondFragment
import nl.otters.elbho.fragments.OpenstaandFragment
import nl.otters.elbho.utils.SharedPreferences

class OverviewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPreferences = SharedPreferences(this)
        val authToken: String? = sharedPreferences.getValueString("auth-token")

        if (authToken == null){
            startLoginActivity()
        }

        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)
        setupViewPager()
    }

    private fun setupViewPager(){
        val adapter = ViewPagerAdapter(supportFragmentManager)
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
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}