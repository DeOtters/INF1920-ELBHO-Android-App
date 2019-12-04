package nl.otters.elbho.activities

import android.os.Bundle
import nl.otters.elbho.R

class TrackingActivity : NavigationActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracking)
    }
}