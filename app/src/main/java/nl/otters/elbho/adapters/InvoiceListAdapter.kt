package nl.otters.elbho.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.component_listitem.view.invoice_lisItem_uploadedDateTextView
import kotlinx.android.synthetic.main.component_listitem.view.invoice_listItem_fileNameTextView
import kotlinx.android.synthetic.main.component_listitem.view.invoice_listItem_monthTextView
import kotlinx.android.synthetic.main.component_listitem.view.listItem_iconImageView
import kotlinx.android.synthetic.main.fragment_invoice_listitem.view.*
import nl.otters.elbho.R
import nl.otters.elbho.models.Invoice
import nl.otters.elbho.utils.DateParser
import java.util.*


class InvoiceListAdapter(
    private val context: Context,
    private val items: ArrayList<Invoice.File>,
    private val listener: OnClickItemListener
) : RecyclerView.Adapter<InvoiceListAdapter.ViewHolder>() {

    private val dateParser = DateParser()

    interface OnClickItemListener {
        fun onItemClick(position: Int, view: View)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val monthView: TextView = itemView.invoice_listItem_monthTextView
        val yearView: TextView = itemView.invoice_listItem_yearTextView
        val fileNameView: TextView = itemView.invoice_listItem_fileNameTextView
        val uploadedDateView: TextView = itemView.invoice_lisItem_uploadedDateTextView
        val icon: ImageView = itemView.listItem_iconImageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.fragment_invoice_listitem, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: Invoice.File = items[position]

        holder.fileNameView.text = item.fileName
        holder.uploadedDateView.text = context.resources.getString(
            R.string.invoices_creation_date,
            dateParser.toFormattedDateWithYear(item.createdAt)
        )
        holder.monthView.text = dateParser
            .toFormattedUploadMonth(item.invoiceMonth)
            .substring(0, 3)
            .toUpperCase(Locale("nl"))
        holder.yearView.text = dateParser
            .toFormattedYear(item.invoiceMonth)
        holder.icon.setImageResource(R.drawable.ic_file_download_gray_24dp)
        holder.itemView.setOnClickListener {
            listener.onItemClick(holder.adapterPosition, it)
        }
    }
}

