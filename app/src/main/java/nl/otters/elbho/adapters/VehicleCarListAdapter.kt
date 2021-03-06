package nl.otters.elbho.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.component_vehicle_listitem.view.*
import nl.otters.elbho.R
import nl.otters.elbho.models.Vehicle
import nl.otters.elbho.utils.DateParser
import java.text.SimpleDateFormat
import java.util.*

class VehicleCarListAdapter(
    private val context: Context,
    private val vehicleCarList: ArrayList<Vehicle.CarWithReservations>,
    private val reservationDate: String,
    private val reservationStartDate: String,
    private val reservationEndDate: String,
    private val itemSelected: Int,
    private val listener: OnClickItemListener
) : RecyclerView.Adapter<VehicleCarListAdapter.ViewHolder>() {
    private val dateParser = DateParser()

    interface OnClickItemListener {
        fun onItemClick(position: Int, view: View)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayView: TextView = itemView.vehicle_listItem_monthTextView
        val dateView: TextView = itemView.vehicle_listItem_dateTextView
        val titleView: TextView = itemView.vehicle_listItem_fileNameTextView
        val descriptionView: TextView = itemView.vehicle_lisItem_uploadedDateTextView
        val icon: ImageView = itemView.vehicle_listItem_iconImageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.component_vehicle_listitem, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = vehicleCarList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val car: Vehicle.CarWithReservations = vehicleCarList[position]

        if (position == itemSelected) {
            holder.itemView.vehicle_constraintLayout.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorSecondary
                )
            )
            holder.itemView.vehicle_listItem_iconImageView.setImageResource(R.drawable.ic_radio_button_checked_orange_24dp)
        } else {
            holder.itemView.vehicle_constraintLayout.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorPrimary
                )
            )
            holder.itemView.vehicle_listItem_iconImageView.setImageResource(R.drawable.ic_radio_button_unchecked_black_24dp)
        }

        holder.titleView.text = formatCarTitle(
            car.brand,
            car.model,
            car.transmission
        )

        holder.descriptionView.text = formatDescription(
            reservationStartDate,
            reservationEndDate,
            holder,
            car
        )

        holder.dateView.text = dateParser.dateToFormattedDate(reservationDate)
        holder.dayView.text = dateParser.dateToFormattedDay(reservationDate)

        holder.itemView.setOnClickListener {
            listener.onItemClick(holder.adapterPosition, it)
        }
    }

    private fun formatCarTitle(brand: String, model: String, transmission: String): String {
        return brand
            .plus(" ")
            .plus(model)
            .plus(" ")
            .plus(transmission)
    }

    private fun formatDescription(
        startTime: String,
        endTime: String,
        holder: ViewHolder,
        car: Vehicle.CarWithReservations
    ): String {
        val builder = StringBuilder()
        builder.append(startTime)
        builder.append(" - ")
        builder.append(endTime)
        builder.append(", ")
        builder.append(car.location.substringAfterLast(" "))

        if (car.reservations.isNullOrEmpty()) {

            return builder.toString()
        } else {

            builder.append("\n\n")
            builder.append(context.resources.getString(R.string.reserved_txt))

            for (res in car.reservations) {

                if (startTime != " " && endTime != " ") {
                    val parser = SimpleDateFormat("HH:mm", Locale("nl"))
                    val parser2 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale("nl"))

                    val calStart = Calendar.getInstance()
                    calStart.time = parser.parse(startTime)!!

                    val calEnd = Calendar.getInstance()
                    calEnd.time = parser.parse(endTime)!!

                    val resStart = Calendar.getInstance()
                    resStart.time = parser.parse(parser.format(parser2.parse(res.start)!!))!!

                    val resEnd = Calendar.getInstance()
                    resEnd.time = parser.parse(parser.format(parser2.parse(res.end)!!))!!

                    if (calStart == resStart || calEnd == resEnd ||
                        calStart.after(resStart) && calStart.before(resEnd) ||
                        calEnd.after(resStart) && calEnd.before(resEnd) ||
                        calStart.before(resStart) && calEnd.after(resEnd)
                    ) {
                        holder.itemView.isClickable = false
                        holder.itemView.isActivated = false
                        holder.itemView.isEnabled = false
                        holder.itemView.isFocusable = false
                        holder.itemView.alpha = 0.8F
                        holder.itemView.setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.colorDisabledButton
                            )
                        )
                        holder.itemView.vehicle_constraintLayout.setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.colorDisabledButton
                            )
                        )
                        holder.itemView.vehicle_constraintLayout2.setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.colorDisabledButton
                            )
                        )
                        holder.itemView.vehicle_listItem_monthTextView.setTextColor(Color.BLACK)
                        holder.itemView.vehicle_listItem_dateTextView.setTextColor(Color.BLACK)
                    }
                }

                builder.append("\n")
                builder.append("\t\t\t")
                builder.append(dateParser.toFormattedTime(res.start))
                builder.append(" - ")
                builder.append(dateParser.toFormattedTime(res.end))
            }

            return builder.toString()
        }
    }
}