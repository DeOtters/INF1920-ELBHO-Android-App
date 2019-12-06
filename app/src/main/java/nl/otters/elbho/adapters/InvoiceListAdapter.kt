package nl.otters.elbho.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_listitem.view.*
import nl.otters.elbho.R
import nl.otters.elbho.models.Invoice


class InvoiceListAdapter(
    private val context: Context,
    private val items: ArrayList<Invoice.File>,
    private val listener: OnClickItemListener
//    private val bottomReachedListener: OnBottomReachedListener
) : RecyclerView.Adapter<InvoiceListAdapter.ViewHolder>() {

    interface OnClickItemListener {
        fun onItemClick(position: Int, view: View)
    }
//    interface OnBottomReachedListener {
//        fun onBottomReached(position: Int)
//    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayView: TextView = itemView.listItem_dayTextView
        val dateView: TextView = itemView.listItem_dateTextView
        val titleView: TextView = itemView.listItem_titleTextView
        val descriptionView: TextView = itemView.lisItem_descriptionTextView
        val icon: ImageView = itemView.listItem_iconImageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_listitem, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: Invoice.File = items[position]

        holder.titleView.text = item.fileName
        holder.descriptionView.text = context.getString(R.string.invoice_uploaded_on)
        holder.dateView.text = item.month
        holder.dayView.text = "placeholder"
        holder.icon.setImageResource(R.drawable.ic_file_download_gray_24dp)
        holder.itemView.setOnClickListener {
            listener.onItemClick(holder.adapterPosition, it)
        }
    }
}

