package nl.otters.elbho.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_navigation.*
import nl.otters.elbho.R
import nl.otters.elbho.models.Adviser
import nl.otters.elbho.repositories.AdviserRepository
import nl.otters.elbho.utils.SharedPreferences


class NavigationActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var navController: NavController
    private val adviserRepository = AdviserRepository(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPreferences = SharedPreferences(this)
        val adviser = adviserRepository.getAdvisor()

        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_navigation)
        setupNavigationDrawer()
        setLoggedInName(adviser)

        logout.setOnClickListener {
            sharedPreferences.clear()
            startLoginActivity()
        }
    }

    private fun setLoggedInName(adviser : LiveData<Adviser.Properties>) {
        adviser.observe(this, Observer{
            logged_in_user.setText(String.format(getResources().getString(R.string.logged_in_as),
                it.firstName, it.lastName))
        })
    }

    private fun startLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun setupNavigationDrawer() {
        app_menu_title.setText(R.string.app_name)

        navController = findNavController(R.id.nav_host_fragment)

        navigation.setNavigationItemSelectedListener(this)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val drawerToggle = object : ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.navigation_open,
            R.string.navigation_close
        ) {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                app_menu_title.alpha = slideOffset
                app_title.alpha = 1 - slideOffset
                super.onDrawerSlide(drawerView, slideOffset)
            }
        }

        drawerToggle.isDrawerIndicatorEnabled = true
        drawer_layout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
    }

    private fun closeMenu() {
        drawer_layout.closeDrawer(GravityCompat.START)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.open_requests -> {
                // TODO: Deep link to tabs
                navController.navigate(R.id.overviewFragment)
                app_title.setText(R.string.navigation_overview)
                closeMenu()
                true
            }
            R.id.upcoming_requests -> {
                // TODO: Deep link to tabs
                navController.navigate(R.id.overviewFragment)
                app_title.setText(R.string.navigation_overview)
                closeMenu()
                true
            }
            R.id.done_requests -> {
                // TODO: Deep link to tabs
                navController.navigate(R.id.overviewFragment)
                app_title.setText(R.string.navigation_overview)
                closeMenu()
                true
            }
            R.id.availability -> {
                navController.navigate(R.id.availabilityFragment)
                app_title.setText(R.string.navigation_availability)
                closeMenu()
                true
            }
            R.id.vehicle -> {
                navController.navigate(R.id.vehicleFragment)
                app_title.setText(R.string.navigation_vehicle)
                closeMenu()
                true
            }
            R.id.invoice -> {
                navController.navigate(R.id.invoiceFragment)
                app_title.setText(R.string.navigation_invoice)
                closeMenu()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}