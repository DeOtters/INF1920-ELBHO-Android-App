package nl.otters.elbho.views.activities

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.text.HtmlCompat
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
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
import java.util.*

class NavigationActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var navController: NavController
    private lateinit var drawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_navigation)

        val view: View = findViewById(R.id.container)
        val adviserRepository = AdviserRepository(this, view)
        val sharedPreferences = SharedPreferences(this)
        val authToken: String? = sharedPreferences.getValueString("auth-token")

        if (authToken == null) {
            startLoginActivity()
        }

        val adviser = adviserRepository.getAdviser()

        navController = this.findNavController(R.id.nav_host_fragment)
        setupNavigationDrawer()
        setLoggedInName(adviser)

        val configuration: Configuration = resources.configuration
        configuration.setLocale(Locale("nl"))

        logout.setOnClickListener {
            sharedPreferences.clear()
            startLoginActivity()
        }

        // Receive pdf-files to create a new invoice
        if (intent?.action == Intent.ACTION_SEND)
            if ("application/pdf" == intent.type)
                (intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as? Uri)?.let {
                    handlePDF(it)
                }
    }

    // send pdf to CreateInvoiceFragment
    private fun handlePDF(uri: Uri) {
        val bundle = bundleOf("Uri" to uri)
        if (isTablet())
            navController.navigate(R.id.invoiceFragment, bundle)
        else
            navController.navigate(R.id.createInvoiceFragment, bundle)
    }

    override fun onBackPressed() {
        if (!isTablet() && drawer_layout.isDrawerOpen(GravityCompat.START)) {
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
            logged_in_user.text = HtmlCompat.fromHtml(
                getString(R.string.logged_in_as, it.firstName, it.lastName),
                HtmlCompat.FROM_HTML_MODE_COMPACT
            )
        })
    }

    private fun startLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun setupNavigationDrawer() {
        app_menu_title.setText(R.string.navigation_app_title)
        navigation.setNavigationItemSelectedListener(this)

        setSupportActionBar(toolbar)

        if (!isTablet()) {

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
                navController.navigateUp()
            }
        } else {
            drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN)
            drawer_layout.setScrimColor(ContextCompat.getColor(this, R.color.drawerNoShadow))
        }
    }

    fun setDrawerEnabled(visible: Boolean) {
        if(!isTablet()){
            if (visible) {
                drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, GravityCompat.END)
            } else {
                drawer_layout.setDrawerLockMode(
                    DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
                    GravityCompat.END
                )
            }

            drawerToggle.isDrawerIndicatorEnabled = visible
            drawerToggle.syncState()
        } else {
            if (visible) {
                drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            } else {
                drawer_layout.setDrawerLockMode(
                    DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
                    GravityCompat.END
                )
            }
        }
    }

    fun setProgressBarVisible(visible: Boolean) {
        progressBar.isVisible = visible
    }

    private fun closeMenu() {
        drawer_layout.closeDrawer(GravityCompat.START)
        drawerToggle.syncState()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.open_requests -> {
                val bundle = bundleOf("tabId" to 0)
                navController.navigate(R.id.action_global_overviewFragment, bundle)
                if (!isTablet())
                    closeMenu()
                true
            }
            R.id.upcoming_requests -> {
                val bundle = bundleOf("tabId" to 1)
                navController.navigate(R.id.action_global_overviewFragment, bundle)
                if (!isTablet())
                    closeMenu()
                true
            }
            R.id.done_requests -> {
                val bundle = bundleOf("tabId" to 2)
                navController.navigate(R.id.action_global_overviewFragment, bundle)
                if (!isTablet())
                    closeMenu()
                true
            }
            R.id.availability -> {
                navController.navigate(R.id.action_global_availabilityFragment)
                if (!isTablet())
                    closeMenu()
                true
            }
            R.id.vehicle -> {
                navController.navigate(R.id.action_global_vehicleFragment)
                if (!isTablet())
                    closeMenu()
                true
            }
            R.id.invoice -> {
                navController.navigate(R.id.action_global_invoiceFragment)
                if (!isTablet())
                    closeMenu()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun isTablet(): Boolean {
        return resources.getBoolean(R.bool.isTablet)
    }
}