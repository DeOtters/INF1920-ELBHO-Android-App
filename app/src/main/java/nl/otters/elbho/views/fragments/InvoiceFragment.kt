package nl.otters.elbho.views.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_invoice.*
import nl.otters.elbho.R
import nl.otters.elbho.adapters.InvoiceListAdapter
import nl.otters.elbho.models.Invoice
import nl.otters.elbho.repositories.InvoiceRepository
import nl.otters.elbho.viewModels.InvoiceViewModel
import nl.otters.elbho.views.activities.NavigationActivity

class InvoiceFragment : BaseFragment() {
    private lateinit var invoiceRepository: InvoiceRepository
    private lateinit var invoicesViewModel: InvoiceViewModel
    private var invoices: ArrayList<Invoice.File> = ArrayList()
    private var selectedFileUrl: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_invoice, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        invoiceRepository = InvoiceRepository(activity!!.applicationContext)
        invoicesViewModel = InvoiceViewModel(invoiceRepository)
        setupRecyclerView()
        setupPullDownToRefresh()
        setupButtonListener()

        // direct share intent to child fragment when in tablet mode
        if (isTablet() && arguments?.get("Uri") != null) {
            (childFragmentManager.fragments[0] as CreateInvoiceFragment).selectedFileUri =
                arguments?.get("Uri") as Uri
            (childFragmentManager.fragments[0] as CreateInvoiceFragment).fileChosen()
        }

        (activity as NavigationActivity).setProgressBarVisible(true)
        invoicesViewModel.getAllInvoices()?.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                updateInvoiceData(it)
            }
            (activity as NavigationActivity).setProgressBarVisible(false)
        })
    }

    private fun setupButtonListener() {
        add_new_invoice.setOnClickListener {
            findNavController().navigate(R.id.action_invoiceFragment_to_createInvoiceFragment)
        }
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
                    selectedFileUrl = invoices[position].filePath
                    if (selectedFileUrl == "Not yet saved")
                        Snackbar.make(
                            activity!!.findViewById(android.R.id.content),
                            R.string.invoice_server_not_yet_saved,
                            Snackbar.LENGTH_SHORT
                        ).show()
                    else
                        showAlert()
                }
            })

        recyclerView.apply {
            this.layoutManager = viewManager
            this.adapter = invoiceListAdapter
        }
    }

    private fun setupPullDownToRefresh() {
        swipe_to_refresh.setOnRefreshListener {
            swipe_to_refresh.isRefreshing = true
            invoicesViewModel.getAllInvoices()?.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    updateInvoiceData(it)
                    swipe_to_refresh.isRefreshing = false
                }
            })
        }
    }

    private fun showAlert() {
        MaterialAlertDialogBuilder(context)
            .setTitle(getString(R.string.invoice_download_message))
            .setPositiveButton(getString(R.string.invoice_download)) { _, _ ->
                val intent = Intent()
                    .setAction(Intent.ACTION_VIEW)
                    .setData(Uri.parse(selectedFileUrl))
                startActivity(intent)
            }.setNegativeButton(getString(R.string.invoice_cancel), null)
            .show()
    }

    override fun onResume() {
        setTitle()
        super.onResume()
    }

    private fun setTitle() {
        val appTitle = activity!!.findViewById<View>(R.id.app_title) as TextView
        val navigation = activity!!.findViewById<View>(R.id.navigation) as NavigationView
        navigation.setCheckedItem(R.id.invoice)
        appTitle.setText(R.string.navigation_invoice)
    }

    private fun isTablet(): Boolean {
        return resources.getBoolean(R.bool.isTablet)
    }
}