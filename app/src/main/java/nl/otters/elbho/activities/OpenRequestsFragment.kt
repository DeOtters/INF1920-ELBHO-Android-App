package nl.otters.elbho.activities

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_open_requests.*
import nl.otters.elbho.R
import nl.otters.elbho.adapters.ListAdapter
import nl.otters.elbho.models.Request
import nl.otters.elbho.repositories.RequestRepository
import nl.otters.elbho.viewModels.OpenRequestsViewModel
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

    private fun updateRequestData(newRequests: ArrayList<Request.Properties>){
        requests.clear()
        requests.addAll(newRequests)
        recyclerView.adapter!!.notifyDataSetChanged()
    }

    private fun setupRecyclerView(){
        val viewManager = LinearLayoutManager(activity!!.applicationContext)
        val listAdapter = ListAdapter(activity!!.applicationContext, requests, object: ListAdapter.OnClickItemListener {
            override fun onItemClick(position: Int, view: View) {
                // startDetailActivity()
            }
        })

        recyclerView.apply{
            this.layoutManager = viewManager
            this.adapter = listAdapter
        }
    }
}