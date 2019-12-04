package nl.otters.elbho.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import nl.otters.elbho.R
import nl.otters.elbho.repositories.RequestRepository
import nl.otters.elbho.viewModels.AanstaandViewModel

class AanstaandFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.fragment_aanstaand, container, false)!!

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        val requestRepository = RequestRepository(activity!!.applicationContext)
        val aanstaandViewModel = AanstaandViewModel(requestRepository)
        super.onActivityCreated(savedInstanceState)
    }
}