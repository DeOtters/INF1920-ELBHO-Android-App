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

class VehicleReservedListAdapter (
    private val context: Context,
    private val vehicleReservations: ArrayList<Vehicle.Car>,
    private val listener: OnClickItemListener
//    private val bottomReachedListener: OnBottomReachedListener
) : RecyclerView.Adapter<VehicleReservedListAdapter.ViewHolder>() {
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
        val view = LayoutInflater.from(context).inflate(R.layout.component_vehicle_reserved_listitem, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = vehicleReservations.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val car : Vehicle.Car = vehicleReservations[position]

        holder.titleView.text = formatCarTitle(
            car.brand,
            car.model,
            car.transmission,
            car.licensePlate)
        holder.descriptionView.text = formatDescription(car.reservations, car.location)
        holder.dateView.text = dateParser.toFormattedDate(car.reservations[0].start)
        holder.dayView.text = dateParser.toFormattedDay(car.reservations[0].start)
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

    private fun formatDescription(reservation: ArrayList<Vehicle.ReservationWithVehicleId>, address: String): String {
        val builder = StringBuilder()
        builder.append(context.resources.getString(R.string.reserved_txt))

        for (res in reservation) {
            builder.append("\n")
            builder.append("\t\t\t")
            builder.append(dateParser.toFormattedTime(res.start))
            builder.append(" - ")
            builder.append(dateParser.toFormattedTime(res.end))
            builder.append(", ")
            builder.append(address.substringAfterLast(","))
        }

        return builder.toString()
    }
}