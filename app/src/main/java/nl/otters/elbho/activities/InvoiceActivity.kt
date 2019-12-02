package nl.otters.elbho.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import nl.otters.elbho.R

class InvoiceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invoice)
    }
}