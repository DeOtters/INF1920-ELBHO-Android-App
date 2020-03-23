package nl.otters.elbho.views.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.whiteelephant.monthpicker.MonthPickerDialog
import kotlinx.android.synthetic.main.fragment_create_invoice.*
import nl.otters.elbho.R
import nl.otters.elbho.models.Invoice
import nl.otters.elbho.repositories.InvoiceRepository
import nl.otters.elbho.views.activities.NavigationActivity
import java.io.*
import java.text.DateFormatSymbols
import java.util.*


class CreateInvoiceFragment : Fragment(), MonthPickerDialog.OnDateSetListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_invoice, container, false)
    }

    private var selectedFileUri: Uri? = null
    private var fileChosen: Boolean = false
    private var fileName: String = ""
    private var chosenMonth: String = ""

    companion object {
        private const val PDF_REQUEST_CODE = 42069
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setOnClickListeners()

        // receive pdf from share intent
        if (arguments?.get("Uri") != null) {
            selectedFileUri = arguments?.get("Uri") as Uri
            fileChosen()
        }
//        test
    }

    private fun setOnClickListeners() {
        invoiceMonthTextView.setOnClickListener { showDatePickerDialog() }
        invoiceFileTextView.setOnClickListener { showFileChooserDialog() }
        create_invoice.setOnClickListener {
            if (selectedFileUri != null && fileChosen && chosenMonth != "") {
                create_invoice.isEnabled = false
                createInvoice()
            } else
                Snackbar.make(
                    activity!!.findViewById(android.R.id.content),
                    R.string.create_invoice_fields_empty,
                    Snackbar.LENGTH_SHORT
                ).show()
        }
    }

    private fun showFileChooserDialog() {
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

    private fun showDatePickerDialog() {
        val yearSelected: Int
        val monthSelected: Int

        // set default values
        val calendar: Calendar = Calendar.getInstance()
        val yearNow = calendar.get(Calendar.YEAR)
        val monthNow = calendar.get(Calendar.MONTH)
        yearSelected = yearNow
        monthSelected = monthNow

        val dialogBuilder: MonthPickerDialog.Builder =
            MonthPickerDialog.Builder(this.context, this, yearSelected, monthSelected)
        dialogBuilder
            .setMaxYear(yearNow)
            .setTitle(getString(R.string.create_invoice_select_month))
            .build()
            .show()
    }


    private fun createInvoice() {
        val invoiceRepository = InvoiceRepository(activity!!.applicationContext)
        val inputStream: InputStream =
            context!!.contentResolver.openInputStream(this.selectedFileUri!!)!!
        val file = File(
            context!!.getExternalFilesDir(null)!!.absolutePath
                    + "/" + fileName
        )
        val outputStream: OutputStream = FileOutputStream(file)
        val buffer = ByteArray(1024)
        var length: Int

        while (inputStream.read(buffer).also { length = it } > 0) {
            outputStream.write(buffer, 0, length)
        }
        outputStream.close()
        inputStream.close()

        (activity as NavigationActivity).setProgressBarVisible(true)
        invoiceRepository.createInvoice(Invoice.Upload(chosenMonth, file), this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PDF_REQUEST_CODE && resultCode == RESULT_OK) {

            data.let {
                try {
                    selectedFileUri = it!!.data!!
                    fileChosen()
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

    private fun fileChosen() {
        fileName = DocumentFile.fromSingleUri(context!!, this.selectedFileUri!!)!!.name!!
        invoiceFileTextView.setText(fileName)
        fileChosen = true
    }

    override fun onResume() {
        setTitle()
        super.onResume()
    }

    private fun setTitle() {
        val appTitle = activity!!.findViewById<View>(R.id.app_title) as TextView
        appTitle.setText(R.string.add_new_invoice_title)
    }

    override fun onDateSet(month: Int, year: Int) {
        chosenMonth = ""
            .plus(year)
            .plus("-")
            .plus("%02d".format(month + 1))
            .plus("-01T16:20:00.000Z")
        val date: String = DateFormatSymbols().months[month] + " " + year
        invoiceMonthTextView.setText(date)
    }

    override fun onStart() {
        if(isTablet()){
            (activity as NavigationActivity).setDrawerEnabled(true)
        } else {
            (activity as NavigationActivity).setDrawerEnabled(false)
        }

        super.onStart()
    }

    private fun isTablet(): Boolean {
        return resources.getBoolean(R.bool.isTablet)
    }
}