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
import nl.otters.elbho.models.Vehicle
import nl.otters.elbho.utils.DateParser
import java.util.*


class VehicleListAdapter(
    private val context: Context,
    private val items: ArrayList<Vehicle.Reservation>,
    private val listener: OnClickItemListener
//    private val bottomReachedListener: OnBottomReachedListener
) : RecyclerView.Adapter<VehicleListAdapter.ViewHolder>() {
    private val dateParser = DateParser()

    interface OnClickItemListener {
        fun onItemClick(position: Int, view: View)
    }
//    interface OnBottomReachedListener {
//        fun onBottomReached(position: Int)
//    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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

        holder.titleView.text = "bla auto"
        holder.descriptionView.text = formatDescription(
            dateParser.toFormattedTime(item.dateTimeSlot.startDateTime),
            dateParser.toFormattedTime((item.dateTimeSlot.endDateTime)),
            "bla loc"
        )
        holder.dateView.text = dateParser.toFormattedDate(item.dateTimeSlot.startDateTime)
        holder.dayView.text = dateParser.toFormattedDay(item.dateTimeSlot.startDateTime)
        holder.icon.setImageResource(R.drawable.ic_chevron_right_24dp)
        holder.itemView.setOnClickListener {
            listener.onItemClick(holder.adapterPosition, it)
        }
    }

    // TODO: endtime
    private fun formatDescription(startTime: String, endTime: String, address: String): String {
        return startTime
            .plus(" - ")
            .plus(endTime)
            .plus(", ")
            .plus(
                address.removeRange(address.indexOf(','), address.length)
            )
    }
}