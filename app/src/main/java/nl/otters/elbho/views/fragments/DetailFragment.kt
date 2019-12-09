package nl.otters.elbho.views.fragments

import androidx.fragment.app.Fragment
import nl.otters.elbho.views.activities.NavigationActivity

abstract class DetailFragment : Fragment() {

    override fun onResume() {
        (activity as NavigationActivity).setDrawerEnabled(false)
        super.onResume()
    }

    override fun onPause() {
        (activity as NavigationActivity).setDrawerEnabled(true)
        super.onPause()
    }

}