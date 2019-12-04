package nl.otters.elbho.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationView
import nl.otters.elbho.R

abstract class NavigationActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme_NoActionBar)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.open_requests -> {
                val intent = Intent(this, OverviewActivity::class.java)
                this.startActivity(intent)
                Toast.makeText(this, "Overview", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.upcoming_requests -> {
                val intent = Intent(this, OverviewActivity::class.java)
                this.startActivity(intent)
                Toast.makeText(this, "Overview", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.done_requests -> {
                val intent = Intent(this, OverviewActivity::class.java)
                this.startActivity(intent)
                Toast.makeText(this, "Overview", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.availability -> {
                val intent = Intent(this, AvailabilityActivity::class.java)
                this.startActivity(intent)
                Toast.makeText(this, "Availability", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.vehicle -> {
                val intent = Intent(this, VehicleActivity::class.java)
                this.startActivity(intent)
                Toast.makeText(this, "Vehicle", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.invoice -> {
                val intent = Intent(this, InvoiceActivity::class.java)
                this.startActivity(intent)
                Toast.makeText(this, "Invoice", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}