package nl.otters.elbho.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import nl.otters.elbho.factories.RetrofitFactory
import nl.otters.elbho.models.Invoice
import nl.otters.elbho.services.InvoiceService
import nl.otters.elbho.utils.SharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InvoiceRepository(private val context: Context) {
    private val invoiceService = RetrofitFactory.get().create(InvoiceService::class.java)

    fun getAllInvoices(): LiveData<ArrayList<Invoice.File>>? {
        val invoices = MutableLiveData<ArrayList<Invoice.File>>()
        invoiceService.getAllInvoices(getAuthToken())
            .enqueue(object : Callback<ArrayList<Invoice.File>> {

                override fun onFailure(call: Call<ArrayList<Invoice.File>>, t: Throwable) {
                    // TODO: not implemented
                }

                override fun onResponse(
                    call: Call<ArrayList<Invoice.File>>,
                    response: Response<ArrayList<Invoice.File>>
                ) {
                    invoices.value = response.body()
                }
            })

        return invoices
    }

    fun getInvoice(id: Int): LiveData<Invoice.File>? {
        val invoice = MutableLiveData<Invoice.File>()
        invoiceService.getInvoice(getAuthToken(), id).enqueue(object : Callback<Invoice.File> {

            override fun onFailure(call: Call<Invoice.File>, t: Throwable) {
                // TODO: not implemented
            }

            override fun onResponse(call: Call<Invoice.File>, response: Response<Invoice.File>) {
                invoice.value = response.body()
            }

        })

        return invoice
    }

    fun createInvoice(invoice: Invoice.File) {
        invoiceService.createInvoice(getAuthToken(), invoice).enqueue(object : Callback<Unit> {
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                // TODO: not implemented
            }

            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                // TODO: not implemented
            }

        })
    }

    fun updateInvoice(id: Int, invoice: Invoice.File) {
        invoiceService.updateInvoice(getAuthToken(), id, invoice).enqueue(object : Callback<Unit> {
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                // TODO: not implemented
            }

            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                // TODO: not implemented
            }

        })
    }

    private fun getAuthToken(): String {
        val sharedPreferences = SharedPreferences(context)
        return sharedPreferences.getValueString("auth-token") ?: ""
    }
}
