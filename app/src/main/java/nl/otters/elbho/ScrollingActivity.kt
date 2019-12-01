package nl.otters.elbho

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_scrolling.*
import nl.otters.elbho.models.Adviser
import nl.otters.elbho.models.Invoice
import nl.otters.elbho.repositories.AdviserRepository
import nl.otters.elbho.repositories.AvailabilityRepository
import nl.otters.elbho.repositories.InvoiceRepository

class ScrollingActivity : AppCompatActivity() {
    private val adviserRepository = AdviserRepository(this)
    private val invoiceRepository = InvoiceRepository(this)
    private val availabilityRepository = AvailabilityRepository(this)
    private val loginCredentials: Adviser.Login =
        Adviser.Login("582297@student.inholland.nl", "lol")

    override fun onCreate(savedInstanceState: Bundle? ) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        val invoice: LiveData<Invoice> = invoiceRepository.getInvoice(1)
        Log.e("TEST", invoice.toString())
        adviserRepository.adviserLogin(loginCredentials)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
