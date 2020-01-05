package nl.otters.elbho.adapters

import android.annotation.SuppressLint
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

class VehicleCarListAdapter(
    private val context: Context,
    private val vehicleCarList: ArrayList<Vehicle.Car>,
    private val reservationDate: String,
    private val reservationStartdate: String,
    private val reservationEndDate: String,
    private val listener: OnClickItemListener
//    private val bottomReachedListener: OnBottomReachedListener
) : RecyclerView.Adapter<VehicleCarListAdapter.ViewHolder>() {
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
        val view = LayoutInflater.from(context).inflate(R.layout.component_listitem, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = vehicleCarList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val car = vehicleCarList[position]

        holder.titleView.text = formatCarTitle(
            car.brand,
            car.model,
            car.transmission
        )

        if(reservationStartdate.equals(" ") && reservationEndDate.equals(" ")){
            holder.descriptionView.text = formatDescription(
                reservationStartdate,
                reservationEndDate,
                car.location)
            holder.dateView.text = dateParser.dateToFormattedDate(reservationDate)
            holder.dayView.text = dateParser.dateToFormattedDay(reservationDate)
        } else {
            holder.descriptionView.text = formatDescription(
                reservationStartdate,
                reservationEndDate,
                car.location)
            holder.dateView.text = dateParser.dateToFormattedDate(reservationDate)
            holder.dayView.text = dateParser.dateToFormattedDay(reservationDate)
        }

        holder.icon.setImageResource(R.drawable.ic_chevron_right_24dp)
        holder.itemView.setOnClickListener {
            listener.onItemClick(holder.adapterPosition, it)
        }
    }

    private fun formatCarTitle(brand: String, model: String, transmission: String): String {
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
    }

    // TODO: when car location is seperated by comma, split by comma and use City name
    private fun formatDescription(startTime: String, endTime: String, address: String): String {
        return startTime
            .plus(" - ")
            .plus(endTime)
            .plus(", ")
            .plus(address)
    }
}