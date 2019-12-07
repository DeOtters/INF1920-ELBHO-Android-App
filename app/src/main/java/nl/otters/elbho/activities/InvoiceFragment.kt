package nl.otters.elbho.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_open_requests.*
import nl.otters.elbho.R
import nl.otters.elbho.adapters.InvoiceListAdapter
import nl.otters.elbho.models.Invoice
import nl.otters.elbho.repositories.InvoiceRepository
import nl.otters.elbho.viewModels.InvoiceViewModel

class InvoiceFragment : Fragment() {
    //Deze lijst moet je vullen met "neppe" data
    private var invoices: ArrayList<Invoice.File> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        invoices.add(
            Invoice.File(
                id = 1,
                adviserId = "fff",
                month = "APRIL",
                fileName = "Factuur van je moeder.pdf",
                filePath = "/troep/",
                document = "Random string"
            )
        )
        return inflater.inflate(R.layout.fragment_invoice, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val invoiceRepository = InvoiceRepository(activity!!.applicationContext)
        val invoicesViewModel = InvoiceViewModel(invoiceRepository)
        setupRecyclerView()

        invoicesViewModel.getAllInvoices()?.observe(this, Observer {
            if (it != null) {
                updateInvoiceData(it)
            }
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
                    showAlert()
                }
            })

        recyclerView.apply {
            this.layoutManager = viewManager
            this.adapter = invoiceListAdapter
        }
    }

    private fun showAlert() {
        MaterialAlertDialogBuilder(context)
            .setTitle(getString(R.string.invoice_download_message))
            .setPositiveButton(getString(R.string.invoice_download)) { _, _ ->
                // TODO: Download file to device
            }.setNegativeButton(getString(R.string.invoice_cancel), null)
            .show()
    }

    override fun onResume() {
        setTitle()
        super.onResume()
    }

    private fun setTitle() {
        val appTitle = activity!!.findViewById<View>(R.id.app_title) as TextView
        appTitle.setText(R.string.navigation_invoice)
    }
}