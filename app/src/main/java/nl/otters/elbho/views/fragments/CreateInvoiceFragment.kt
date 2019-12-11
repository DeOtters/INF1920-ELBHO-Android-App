package nl.otters.elbho.views.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_create_invoice.*
import nl.otters.elbho.R

class CreateInvoiceFragment : DetailFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_invoice, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        invoiceFileTextView.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val intent = Intent()
                    .setType("application/pdf")
                    .setAction(Intent.ACTION_GET_CONTENT)

                startActivityForResult(Intent.createChooser(intent, "Select a file"), 42069)
            }
        }
        create_invoice.setOnClickListener { createInvoice(view!!) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 42069 && resultCode == RESULT_OK) {
            val selectedFile = data?.data //The uri with the location of the file
            val fileName = selectedFile?.path.toString().substringBeforeLast(".")

            invoiceFileTextView.setText(fileName)
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
        super.onResume()
    }

    private fun setTitle() {
        val appTitle = activity!!.findViewById<View>(R.id.app_title) as TextView
        appTitle.setText(R.string.add_new_invoice_title)
    }
}