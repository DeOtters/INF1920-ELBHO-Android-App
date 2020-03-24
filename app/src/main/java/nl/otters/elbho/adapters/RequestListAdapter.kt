package nl.otters.elbho.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.component_listitem.view.*
import nl.otters.elbho.R
import nl.otters.elbho.models.Request
import nl.otters.elbho.utils.DateParser
import java.util.*

class RequestListAdapter(
    private val context: Context,
    private val items: ArrayList<Request.Properties>,
    private val listener: OnClickItemListener
) : RecyclerView.Adapter<RequestListAdapter.ViewHolder>() {
    private val dateParser = DateParser()

    interface OnClickItemListener {
        fun onItemClick(position: Int, view: View)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayView: TextView = itemView.invoice_listItem_monthTextView
        val dateView: TextView = itemView.listItem_dateTextView
        val titleView: TextView = itemView.invoice_listItem_fileNameTextView
        val descriptionView: TextView = itemView.invoice_lisItem_uploadedDateTextView
        val icon: ImageView = itemView.listItem_iconImageView
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.component_listitem, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.titleView.text = item.cocName
        holder.descriptionView.text = formatDescription(
            dateParser.toFormattedTimeString(item.startTime),
            dateParser.toFormattedTimeString((item.endTime)),
            item.address
        )
        holder.dateView.text = dateParser.toFormattedDate(item.startTime)
        holder.dayView.text = dateParser.toFormattedDay(item.startTime)
        holder.icon.setImageResource(R.drawable.ic_chevron_right_24dp)
        holder.itemView.setOnClickListener {
            listener.onItemClick(holder.adapterPosition, it)
        }
    }

    private fun formatDescription(startTime: String, endTime: String, address: String): String {
        return startTime
            .plus(" - ")
            .plus(endTime)
            .plus(", ")
            .plus(address)
    }
}

