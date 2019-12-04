package nl.otters.elbho.activities

import android.content.Intent
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationView
import nl.otters.elbho.R

abstract class NavigationActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.navigation_items, menu)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.open_requests -> {
                val intent = Intent(this, OverviewActivity::class.java)
                Toast.makeText(this, "Overview", Toast.LENGTH_SHORT).show()
                this.startActivity(intent)
                true
            }
            R.id.upcoming_requests -> {
                val intent = Intent(this, OverviewActivity::class.java)
                this.startActivity(intent)
                true
            }
            R.id.done_requests -> {
                val intent = Intent(this, OverviewActivity::class.java)
                this.startActivity(intent)
                true
            }
            R.id.availability -> {
                val intent = Intent(this, AvailabilityActivity::class.java)
                this.startActivity(intent)
                true
            }
            R.id.vehicle -> {
                val intent = Intent(this, VehicleActivity::class.java)
                this.startActivity(intent)
                true
            }
            R.id.invoice -> {
                val intent = Intent(this, LoginActivity::class.java)
                this.startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}