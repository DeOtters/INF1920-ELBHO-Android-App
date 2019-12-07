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
import nl.otters.elbho.models.Request
import java.text.SimpleDateFormat
import java.util.*


class RequestListAdapter(
    private val context: Context,
    private val items: ArrayList<Request.Properties>,
    private val listener: OnClickItemListener
//    private val bottomReachedListener: OnBottomReachedListener
) : RecyclerView.Adapter<RequestListAdapter.ViewHolder>() {

    interface OnClickItemListener{
        fun onItemClick(position: Int, view: View)
    }
//    interface OnBottomReachedListener {
//        fun onBottomReached(position: Int)
//    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val dayView: TextView = itemView.invoice_listItem_monthTextView
        val dateView: TextView = itemView.listItem_dateTextView
        val titleView: TextView = itemView.invoice_listItem_fileNameTextView
        val descriptionView: TextView = itemView.invoice_lisItem_uploadedDateTextView
        val icon: ImageView = itemView.listItem_iconImageView
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_listitem, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.titleView.text = item.cocName
        holder.descriptionView.text = formatDescription(parseToTime(item.appointmentDatetime), parseToTime((item.appointmentDatetime)), item.address)
        holder.dateView.text = parseToDate(item.appointmentDatetime)
        holder.dayView.text = parseToDay(item.appointmentDatetime)
        holder.icon.setImageResource(R.drawable.ic_chevron_right_24dp)
        holder.itemView.setOnClickListener{
            listener.onItemClick(holder.adapterPosition, it)
        }
    }

    // TODO: put this in a util class
    // TODO: do something with the Locale, like a factory or safe it in the sharedpref
    private fun parseToDay(dateTime: String) : String{
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale("nl"))
        val formatter = SimpleDateFormat("EE",  Locale("nl"))
        return formatter.format(parser.parse(dateTime)).toUpperCase()
    }

    private fun parseToDate(dateTime: String) : String{
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss",  Locale("nl"))
        val formatter = SimpleDateFormat("dd-MM",  Locale("nl"))
        return formatter.format(parser.parse(dateTime))
    }

    private fun parseToTime(dateTime: String) : String{
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss",  Locale("nl"))
        val formatter = SimpleDateFormat("HH:mm",  Locale("nl"))
        return formatter.format(parser.parse(dateTime))
    }

    // TODO: endtime
    private fun formatDescription(startTime: String, endTime: String, address: String): String{
        return startTime
            .plus( " - ")
            .plus(endTime)
            .plus( ", ")
            .plus(address.removeRange(address.indexOf(','), address.length)
        )
    }
}

