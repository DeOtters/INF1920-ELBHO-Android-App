package nl.otters.elbho.activities

import android.os.Bundle
import nl.otters.elbho.R

class OverviewActivity : NavigationActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)
    }
}