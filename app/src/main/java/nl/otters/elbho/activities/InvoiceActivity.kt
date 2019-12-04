package nl.otters.elbho.activities

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_overview.*
import nl.otters.elbho.R

class InvoiceActivity : NavigationActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invoice)
        navigation.setNavigationItemSelectedListener(this)
    }
}