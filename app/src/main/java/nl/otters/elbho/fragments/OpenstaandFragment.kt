package nl.otters.elbho.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_openstaand.*
import nl.otters.elbho.R
import nl.otters.elbho.adapters.ListAdapter
import nl.otters.elbho.models.Request
import nl.otters.elbho.repositories.RequestRepository
import nl.otters.elbho.viewModels.OpenstaandViewModel

class OpenstaandFragment : Fragment() {
    private var requests: ArrayList<Request.Properties> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.fragment_openstaand, container, false)!!

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        val requestRepository = RequestRepository(activity!!.applicationContext)
        val openStaandViewModel = OpenstaandViewModel(requestRepository)

        super.onActivityCreated(savedInstanceState)

        val viewManager = LinearLayoutManager(activity!!.applicationContext)
        val listAdapter = ListAdapter(activity!!.applicationContext, requests, object: ListAdapter.OnClickItemListener {
            override fun onItemClick(position: Int, view: View) {
                // startDetailActivity()
            }
        })

        // TODO: setupRecyclerView()
        recyclerView.apply{
            this.hasFixedSize()
            this.layoutManager = viewManager
            this.adapter = listAdapter
        }

        openStaandViewModel.getAllRequests().observe(this, Observer{
            requests = it
            recyclerView.adapter!!.notifyDataSetChanged()
        })
    }
}