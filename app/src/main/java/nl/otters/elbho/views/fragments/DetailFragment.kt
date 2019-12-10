package nl.otters.elbho.views.fragments

import androidx.fragment.app.Fragment
import nl.otters.elbho.views.activities.NavigationActivity

abstract class DetailFragment : Fragment() {

    override fun onStart() {
        (activity as NavigationActivity).setDrawerEnabled(false)
        super.onStart()
    }
}