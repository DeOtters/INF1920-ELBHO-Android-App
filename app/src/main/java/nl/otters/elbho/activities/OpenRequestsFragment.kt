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
import kotlinx.android.synthetic.main.fragment_open_requests.*
import nl.otters.elbho.R
import nl.otters.elbho.adapters.ListAdapter
import nl.otters.elbho.models.Request
import nl.otters.elbho.repositories.RequestRepository
import nl.otters.elbho.viewModels.OpenRequestsViewModel

class OpenRequestsFragment : Fragment() {
    private var requests: ArrayList<Request.Properties> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) =
        inflater.inflate(R.layout.fragment_open_requests, container, false)!!

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val requestRepository = RequestRepository(activity!!.applicationContext)
        val openRequestsViewModel = OpenRequestsViewModel(requestRepository)
        setupRecyclerView()

        openRequestsViewModel.getAllRequests().observe(this, Observer {
            updateRequestData(it)
        })
    }

    override fun onResume() {
        val appTitle = activity!!.findViewById<View>(R.id.app_title) as TextView
        appTitle.setText(R.string.navigation_open_requests)
        super.onResume()
    }

    private fun updateRequestData(newRequests: ArrayList<Request.Properties>) {
        requests.clear()
        requests.addAll(newRequests)
        recyclerView.adapter!!.notifyDataSetChanged()
    }

    private fun setupRecyclerView() {
        val viewManager = LinearLayoutManager(activity!!.applicationContext)
        val listAdapter = ListAdapter(
            activity!!.applicationContext,
            requests,
            object : ListAdapter.OnClickItemListener {
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