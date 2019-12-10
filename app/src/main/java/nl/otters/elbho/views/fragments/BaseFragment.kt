package nl.otters.elbho.views.fragments

import androidx.fragment.app.Fragment
import nl.otters.elbho.views.activities.NavigationActivity

abstract class BaseFragment : Fragment() {

    override fun onStart() {
        (activity as NavigationActivity).setDrawerEnabled(true)
        super.onStart()
    }
}