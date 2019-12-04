package nl.otters.elbho.activities

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle

import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_navigation.*
import nl.otters.elbho.R


class NavigationActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme_NoActionBar)
        setContentView(R.layout.activity_navigation)
        setupNavigationDrawer()
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