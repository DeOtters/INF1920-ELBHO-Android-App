package nl.otters.elbho.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import nl.otters.elbho.factories.RetrofitFactory
import nl.otters.elbho.models.Invoice
import nl.otters.elbho.services.InvoiceService
import nl.otters.elbho.utils.SharedPreferences
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

    fun createInvoice(invoice: Invoice.Upload) {
        val fileBody: RequestBody =
            RequestBody.create(MediaType.parse("application/pdf"), invoice.file)
        val file = MultipartBody.Part.createFormData("file", invoice.file.name, fileBody)
        invoiceService.createInvoice(getAuthToken(), file, invoice.date)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    // TODO: not implemented
                    Log.d(
                        "upload", "result: "
                                + response.message() + "\n"
                                + response.raw() + "\n"
                    )
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    // TODO: not implemented
                    Log.d("upload", call.toString() + "\n" + t.message + "\n" + invoice.file)
                }
            })
    }

    private fun getAuthToken(): String {
        val sharedPreferences = SharedPreferences(context)
        return "Bearer " + (sharedPreferences.getValueString("auth-token") ?: "")
    }
}
