package nl.otters.elbho.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import kotlinx.android.synthetic.main.activity_invoice.*
import nl.otters.elbho.R

class InvoiceActivity : NavigationActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invoice)
        navigation.setNavigationItemSelectedListener(this)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val drawerToggle = object : ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.navigation_open,
            R.string.navigation_close
        ) {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                app_title.alpha = 1 - slideOffset
                super.onDrawerSlide(drawerView, slideOffset)
            }
        }

        drawerToggle.isDrawerIndicatorEnabled = true
        drawer_layout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
    }
}