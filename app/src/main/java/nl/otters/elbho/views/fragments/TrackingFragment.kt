package nl.otters.elbho.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import nl.otters.elbho.R

class TrackingFragment : DetailFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tracking, container, false)
    }

    override fun onResume() {
        setTitle()
        super.onResume()
    }

    private fun setTitle() {
        val appTitle = activity!!.findViewById<View>(R.id.app_title) as TextView
        appTitle.setText(R.string.temp_tracking)
    }
}