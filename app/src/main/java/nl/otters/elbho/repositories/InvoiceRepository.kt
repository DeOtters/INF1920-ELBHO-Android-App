package nl.otters.elbho.repositories

import android.content.Context
import android.util.Log
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.fragment_create_invoice.*
import nl.otters.elbho.R
import nl.otters.elbho.factories.RetrofitFactory
import nl.otters.elbho.models.Invoice
import nl.otters.elbho.services.InvoiceService
import nl.otters.elbho.utils.SharedPreferences
import nl.otters.elbho.views.activities.NavigationActivity
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class InvoiceRepository(private val context: Context) {
    private val invoiceService = RetrofitFactory.get().create(InvoiceService::class.java)

    fun getAllInvoices(): LiveData<ArrayList<Invoice.File>>? {
        val invoices = MutableLiveData<ArrayList<Invoice.File>>()
        invoiceService.getAllInvoices(getAuthToken())
            .enqueue(object : Callback<ArrayList<Invoice.File>> {
                override fun onResponse(
                    call: Call<ArrayList<Invoice.File>>,
                    response: Response<ArrayList<Invoice.File>>
                ) {
                    invoices.value = response.body()
                }

                override fun onFailure(call: Call<ArrayList<Invoice.File>>, t: Throwable) {
                    Log.d("error", t.message + " " + t.cause)
                    // TODO: not implemented
                }
            })

        return invoices
    }

    fun createInvoice(invoice: Invoice.Upload, fragment: Fragment) {
        val fileBody: RequestBody =
            RequestBody.create(MediaType.parse("application/pdf"), invoice.file)
        val file = MultipartBody.Part.createFormData("file", invoice.file.name, fileBody)
        invoiceService.createInvoice(getAuthToken(), file, invoice.date)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    when {
                        response.code() == 406 -> {
                            Snackbar.make(
                                fragment.view!!,
                                R.string.create_invoice_error_not_acceptable,
                                Snackbar.LENGTH_SHORT
                            ).show()
                            (fragment.activity as NavigationActivity).setProgressBarVisible(false)
                            fragment.create_invoice.isEnabled = true
                        }
                        response.code() == 201 -> {
                            Snackbar.make(
                                fragment.view!!,
                                R.string.create_invoice_uploaded,
                                Snackbar.LENGTH_SHORT
                            ).show()
                            (fragment.activity as NavigationActivity).setProgressBarVisible(false)
                            fragment.create_invoice.isEnabled = true
                            findNavController(fragment).navigate(R.id.invoiceFragment)
                        }
                        else -> {
                            Snackbar.make(
                                fragment.view!!,
                                (response.code().toString() + " " + response.message()),
                                Snackbar.LENGTH_SHORT
                            ).show()
                            Log.d(
                                "upload", "result: "
                                        + response.message() + "\n"
                                        + response.raw() + "\n"
                            )
                            (fragment.activity as NavigationActivity).setProgressBarVisible(false)
                            fragment.create_invoice.isEnabled = true
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Snackbar.make(
                        fragment.view!!,
                        t.message.toString(),
                        Snackbar.LENGTH_SHORT
                    ).show()
                    fragment.progressBar.isVisible = false
                    fragment.create_invoice.isEnabled = true
                }
            })
    }

    private fun getAuthToken(): String {
        val sharedPreferences = SharedPreferences(context)
        return "Bearer " + (sharedPreferences.getValueString("auth-token") ?: "")
    }
}
