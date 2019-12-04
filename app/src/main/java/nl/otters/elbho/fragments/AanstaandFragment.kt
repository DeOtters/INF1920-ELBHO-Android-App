package nl.otters.elbho.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import nl.otters.elbho.R

class AanstaandFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.fragment_aanstaand, container, false)!!

//    overviewViewModel.getAllRequests().observe(this, Observer {
//    })
}