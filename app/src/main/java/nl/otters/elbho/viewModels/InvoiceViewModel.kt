package nl.otters.elbho.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import nl.otters.elbho.models.Invoice
import nl.otters.elbho.repositories.InvoiceRepository

class InvoiceViewModel(private val invoiceRepository: InvoiceRepository) : ViewModel() {
    fun getAllInvoices(): LiveData<ArrayList<Invoice.File>>? {
        return invoiceRepository.getAllInvoices()
    }
}