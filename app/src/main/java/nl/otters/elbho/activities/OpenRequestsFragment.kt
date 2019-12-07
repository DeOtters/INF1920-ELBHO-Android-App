package nl.otters.elbho.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.fragment_open_requests.*
import nl.otters.elbho.R
import nl.otters.elbho.adapters.RequestListAdapter
import nl.otters.elbho.models.Request
import nl.otters.elbho.repositories.RequestRepository
import nl.otters.elbho.viewModels.OverviewViewModel

class OpenRequestsFragment : Fragment() {
    private var requests: ArrayList<Request.Properties> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.fragment_open_requests, container, false)!!

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val requestRepository = RequestRepository(activity!!.applicationContext)
        val overviewViewModel = OverviewViewModel(requestRepository)

        setupRecyclerView()

        overviewViewModel.getAllRequests().observe( this, Observer<ArrayList<Request.Properties>> {
            updateRequestData(it)
        })
    }

    override fun onResume() {
        setTitle()
        super.onResume()
    }

    private fun setTitle() {
        val appTitle = activity!!.findViewById<View>(R.id.app_title) as TextView
        val navigation = activity!!.findViewById<View>(R.id.navigation) as NavigationView
        navigation.setCheckedItem(R.id.open_requests)
        appTitle.setText(R.string.navigation_open_requests)
    }

    private fun updateRequestData(newRequests: ArrayList<Request.Properties>) {
        requests.clear()
        requests.addAll(newRequests)
        recyclerView.adapter!!.notifyDataSetChanged()
    }

    private fun setupRecyclerView() {
        val viewManager = LinearLayoutManager(activity!!.applicationContext)
        val listAdapter = RequestListAdapter(
            activity!!.applicationContext,
            requests,
            object : RequestListAdapter.OnClickItemListener {
                override fun onItemClick(position: Int, view: View) {
                    findNavController().navigate(R.id.action_global_requestFragment)
                }
            })

        recyclerView.apply {
            this.layoutManager = viewManager
            this.adapter = listAdapter
        }
    }
}