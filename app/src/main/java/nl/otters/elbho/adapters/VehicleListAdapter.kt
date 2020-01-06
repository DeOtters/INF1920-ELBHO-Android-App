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
import nl.otters.elbho.models.Vehicle
import nl.otters.elbho.utils.DateParser
import kotlin.collections.ArrayList

class VehicleListAdapter(
    private val context: Context,
    private val vehicleClaim: ArrayList<Vehicle.Reservation>,
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
        val view = LayoutInflater.from(context).inflate(R.layout.component_vehicle_listitem, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = vehicleClaim.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val claim : Vehicle.Reservation = vehicleClaim[position]

        holder.titleView.text = formatCarTitle(
            claim.vehicle.brand,
            claim.vehicle.model,
            claim.vehicle.transmission,
            claim.vehicle.licensePlate)
        holder.descriptionView.text = formatDescription(
            dateParser.toFormattedTime(claim.start),
            dateParser.toFormattedTime((claim.end)),
            claim.vehicle.location)
        holder.dateView.text = dateParser.toFormattedDate(claim.start)
        holder.dayView.text = dateParser.toFormattedDay(claim.start)
        holder.icon.setImageResource(R.drawable.ic_chevron_right_24dp)
        holder.itemView.setOnClickListener {
            listener.onItemClick(holder.adapterPosition, it)
        }
    }

    private fun formatCarTitle(brand: String, model: String, transmission: String, licensePlate: String): String {
        val trans: String
        if(transmission.equals("Automaat")){
            trans = context.resources.getString(R.string.vehicle_transmission_true)
        }else{
            trans = context.resources.getString(R.string.vehicle_transmission_false)
        }

        return brand
            .plus(" ")
            .plus(model)
            .plus(" ")
            .plus(trans)
            .plus(" ")
            .plus(licensePlate)
    }

    // TODO: endtime
    private fun formatDescription(startTime: String, endTime: String, address: String): String {
        return startTime
            .plus(" - ")
            .plus(endTime)
            .plus(", ")
            .plus(address.substringAfterLast(","))
    }
}