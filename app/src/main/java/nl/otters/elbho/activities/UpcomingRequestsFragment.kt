package nl.otters.elbho.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import nl.otters.elbho.R
import nl.otters.elbho.repositories.RequestRepository
import nl.otters.elbho.viewModels.UpcomingRequestsViewModel

class UpcomingRequestsFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.fragment_upcoming_requests, container, false)!!

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        val requestRepository = RequestRepository(activity!!.applicationContext)
        val aanstaandViewModel = UpcomingRequestsViewModel(requestRepository)
        super.onActivityCreated(savedInstanceState)
    }
}