package nl.otters.elbho.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_availability.*
import nl.otters.elbho.R
import nl.otters.elbho.models.Availability
import nl.otters.elbho.repositories.AvailabilityRepository
import nl.otters.elbho.utils.DateParser
import nl.otters.elbho.viewModels.AvailabilityViewModel

class AvailabilityFragment : Fragment() {
    private var availability: ArrayList<Availability.Slot> = ArrayList()
    private val dateParser: DateParser = DateParser()
    //Eerst moet ik een lijst met de availability ophalen van de DB CHECK
    //TODO: Vervolgens moet ik deze data vullen in de calendar
    //TODO: Ook moet er een OnDateChangeListener toegevoegd
        // Pak de datum
        // Pak de week van deze datum
        // Stuur die mee als bundle naar de volgende pagina

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_availability, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        val availabilityRepository = AvailabilityRepository(activity!!.applicationContext)
        val availabilityViewModel = AvailabilityViewModel(availabilityRepository)

        availabilityViewModel.getAllAvailabilities()?.observe(this, Observer {
            availability = it
            Log.e("it", it.toString())
        })



        super.onActivityCreated(savedInstanceState)
    }

    override fun onResume() {
        setTitle()
        super.onResume()
    }

    private fun setTitle() {
        val appTitle = activity!!.findViewById<View>(R.id.app_title) as TextView
        appTitle.setText(R.string.navigation_availability)
    }
}