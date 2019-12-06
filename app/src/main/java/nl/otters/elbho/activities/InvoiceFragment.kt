package nl.otters.elbho.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_open_requests.*
import nl.otters.elbho.R
import nl.otters.elbho.adapters.InvoiceListAdapter
import nl.otters.elbho.models.Invoice
import nl.otters.elbho.repositories.InvoiceRepository
import nl.otters.elbho.viewModels.InvoiceViewModel

class InvoiceFragment : Fragment() {
    private var invoices: ArrayList<Invoice.File> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_invoice, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val invoiceRepository = InvoiceRepository(activity!!.applicationContext)
        val invoicesViewModel = InvoiceViewModel(invoiceRepository)
        setupRecyclerView()

        invoicesViewModel.getAllInvoices().observe(this, Observer {
            updateInvoiceData(it)
        })
    }

    private fun updateInvoiceData(newRequests: ArrayList<Invoice.File>) {
        invoices.clear()
        invoices.addAll(newRequests)
        recyclerView.adapter!!.notifyDataSetChanged()
    }

    private fun setupRecyclerView() {
        val viewManager = LinearLayoutManager(activity!!.applicationContext)
        val invoiceListAdapter = InvoiceListAdapter(
            activity!!.applicationContext,
            invoices,
            object : InvoiceListAdapter.OnClickItemListener {
                override fun onItemClick(position: Int, view: View) {
                    //findNavController().navigate(R.id.action_global_createInvoiceFragment)
                }
            })

        recyclerView.apply {
            this.layoutManager = viewManager
            this.adapter = invoiceListAdapter
        }
    }
}