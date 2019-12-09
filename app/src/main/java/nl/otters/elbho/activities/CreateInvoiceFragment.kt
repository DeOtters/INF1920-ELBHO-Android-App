package nl.otters.elbho.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_create_invoice.*
import nl.otters.elbho.R

class CreateInvoiceFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_invoice, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        create_invoice.setOnClickListener {
            createInvoice(view!!)
        }
    }

    private fun createInvoice(view: View) {
        //TODO: Get data from text fields and send to API

        Snackbar.make(
            view,
            R.string.create_invoice_uploaded,
            Snackbar.LENGTH_SHORT
        ).show()
        findNavController().navigateUp()
    }

    override fun onResume() {
        setTitle()
        (activity as NavigationActivity).setDrawerEnabled(false)
        super.onResume()
    }

    override fun onPause() {
        (activity as NavigationActivity).setDrawerEnabled(true)
        super.onPause()
    }

    private fun setTitle() {
        val appTitle = activity!!.findViewById<View>(R.id.app_title) as TextView
        appTitle.setText(R.string.add_new_invoice_title)
    }
}