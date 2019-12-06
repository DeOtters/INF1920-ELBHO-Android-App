package nl.otters.elbho.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_listitem.view.*
import nl.otters.elbho.R
import nl.otters.elbho.models.Request
import nl.otters.elbho.utils.DateParser
import kotlin.collections.ArrayList


class ListAdapter(
    private val context: Context,
    private val items: ArrayList<Request.Properties>,
    private val listener: OnClickItemListener
//    private val bottomReachedListener: OnBottomReachedListener
) : RecyclerView.Adapter<ListAdapter.ViewHolder>(){
    private val dateParser = DateParser()

    interface OnClickItemListener{
        fun onItemClick(position: Int, view: View)
    }
//    interface OnBottomReachedListener {
//        fun onBottomReached(position: Int)
//    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
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
        val item = items[position]

        holder.titleView.text = item.cocName
        holder.descriptionView.text = formatDescription(item.appointmentDatetime, item.address)
        holder.dateView.text = dateParser.toFormattedDate(item.appointmentDatetime)
        holder.dayView.text = dateParser.toFormattedDay(item.appointmentDatetime)
        holder.icon.setImageResource(R.drawable.ic_chevron_right_24dp)
        holder.itemView.setOnClickListener{
            listener.onItemClick(holder.adapterPosition, it)
        }
    }

    private fun formatDescription(startTime: String, address: String): String{
        val parsedStartTime: String = dateParser.toFormattedTime(startTime)
        val endTime: String = dateParser.addHours(startTime, 1)
        val parsedEndTime: String = dateParser.toFormattedTime(endTime)

        return parsedStartTime
            .plus( " - ")
            .plus(parsedEndTime)
            .plus( ", ")
            .plus(address.removeRange(address.indexOf(','), address.length)
        )
    }
}

