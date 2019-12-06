package nl.otters.elbho.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import nl.otters.elbho.R
import nl.otters.elbho.repositories.RequestRepository
import nl.otters.elbho.viewModels.UpcomingRequestsViewModel

class UpcomingRequestsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.fragment_upcoming_requests, container, false)!!

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val requestRepository = RequestRepository(activity!!.applicationContext)
        val upcomingViewModel = UpcomingRequestsViewModel(requestRepository)

    }

    override fun onResume() {
        val appTitle = activity!!.findViewById<View>(R.id.app_title) as TextView
        appTitle.setText(R.string.navigation_upcoming_requests)
        super.onResume()
    }
}