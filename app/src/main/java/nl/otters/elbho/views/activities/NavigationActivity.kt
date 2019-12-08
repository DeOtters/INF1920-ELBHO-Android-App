package nl.otters.elbho.views.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
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
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private val adviserRepository = AdviserRepository(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPreferences = SharedPreferences(this)
        val adviser = adviserRepository.getAdvisor()
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_navigation)
        navController = this.findNavController(R.id.nav_host_fragment)
        setupNavigationDrawer()
        setLoggedInName(adviser)

        logout.setOnClickListener {
            sharedPreferences.clear()
            startLoginActivity()
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            closeMenu()
        } else {
            navController.navigateUp()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    private fun setLoggedInName(adviser: LiveData<Adviser.Properties>) {
        adviser.observe(this, Observer {
            logged_in_user.text = String.format(
                resources.getString(R.string.logged_in_as),
                it.firstName, it.lastName
            )
        })
    }

    private fun startLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun setupNavigationDrawer() {
        app_menu_title.setText(R.string.app_name)
        navigation.setNavigationItemSelectedListener(this)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        drawerToggle = object : ActionBarDrawerToggle(
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
        drawerToggle.setToolbarNavigationClickListener {
            // TODO: Ask if user wants to leave screen
            navController.navigateUp()
        }
    }

    fun setDrawerEnabled(visible: Boolean) {
        drawerToggle.isDrawerIndicatorEnabled = visible
        if (visible) {
            drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, GravityCompat.END)
        } else {
            drawer_layout.setDrawerLockMode(
                DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
                GravityCompat.END
            )
        }
    }

    private fun closeMenu() {
        drawer_layout.closeDrawer(GravityCompat.START)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.open_requests -> {
                val bundle = bundleOf("tabId" to 0)
                navController.navigate(R.id.action_global_overviewFragment, bundle)
                closeMenu()
                true
            }
            R.id.upcoming_requests -> {
                val bundle = bundleOf("tabId" to 1)
                navController.navigate(R.id.action_global_overviewFragment, bundle)
                closeMenu()
                true
            }
            R.id.done_requests -> {
                val bundle = bundleOf("tabId" to 2)
                navController.navigate(R.id.action_global_overviewFragment, bundle)
                closeMenu()
                true
            }
            R.id.availability -> {
                navController.navigate(R.id.action_global_availabilityFragment)
                closeMenu()
                true
            }
            R.id.vehicle -> {
                navController.navigate(R.id.action_global_vehicleFragment)
                closeMenu()
                true
            }
            R.id.invoice -> {
                navController.navigate(R.id.action_global_invoiceFragment)
                closeMenu()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
}