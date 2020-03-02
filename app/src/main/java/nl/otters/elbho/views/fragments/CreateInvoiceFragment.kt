package nl.otters.elbho.views.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_create_invoice.*
import nl.otters.elbho.R
import nl.otters.elbho.models.Invoice
import nl.otters.elbho.repositories.InvoiceRepository
import java.io.*


class CreateInvoiceFragment : DetailFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_invoice, container, false)
    }

    private lateinit var selectedFileUri: Uri

    companion object {
        private const val PDF_REQUEST_CODE = 42069
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        invoiceFileTextView.setOnClickListener {
            val intent = Intent()
                .setType("application/pdf")
                .setAction(Intent.ACTION_GET_CONTENT)
                .addCategory(Intent.CATEGORY_OPENABLE)

            startActivityForResult(
                Intent.createChooser(
                    intent,
                    getString(R.string.create_invoice_choose_file)
                ), PDF_REQUEST_CODE
            )
        }
        create_invoice.setOnClickListener { createInvoice(view!!) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PDF_REQUEST_CODE && resultCode == RESULT_OK) {

            data.let {

                try {
                    selectedFileUri = it!!.data!!
                    invoiceFileTextView.setText("Bestand geselecteerd")
                } catch (e: IOException) {
                    Snackbar.make(
                        activity!!.findViewById(android.R.id.content),
                        R.string.create_invoice_read_error,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun createInvoice(view: View) {
        // TODO: Get data from text fields and send to API
        val invoiceRepository = InvoiceRepository(activity!!.applicationContext)
        val inputStream: InputStream = context!!.contentResolver.openInputStream(selectedFileUri)!!
        val file = File(context!!.getExternalFilesDir(null)!!.absolutePath + "/invoice.pdf")
        val outputStream: OutputStream = FileOutputStream(file)
        val buffer = ByteArray(1024)
        var length: Int

        while (inputStream.read(buffer).also { length = it } > 0) {
            outputStream.write(buffer, 0, length)
        }
        outputStream.close()
        inputStream.close()

        // TODO: Get real date
        val date = "2020-03-01T13:20:00.000Z"
        invoiceRepository.createInvoice(Invoice.Upload(date, file))
        Log.d("file", file.path)
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