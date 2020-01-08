package nl.otters.elbho.views.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_vehicle.*
import kotlinx.android.synthetic.main.fragment_vehicle_reservation.*
import kotlinx.android.synthetic.main.fragment_vehicle_reservation.recyclerView
import nl.otters.elbho.R
import nl.otters.elbho.adapters.VehicleCarListAdapter
import nl.otters.elbho.adapters.VehicleReservedListAdapter
import nl.otters.elbho.models.Vehicle
import nl.otters.elbho.repositories.VehicleRepository
import nl.otters.elbho.utils.DateParser
import nl.otters.elbho.utils.SharedPreferences
import nl.otters.elbho.viewModels.VehicleViewModel
import nl.otters.elbho.views.activities.LoginActivity
import nl.otters.elbho.views.activities.NavigationActivity
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

class VehicleReservationFragment : DetailFragment() {
    private var vehicleCarList: ArrayList<Vehicle.CarWithReservations> = ArrayList()
    private val dateParser: DateParser = DateParser()

    private var startReservationTime: String = " "
    private var endReservationTime: String = " "
    private var reservationDate: String = SimpleDateFormat("yyyy-MM-dd", Locale("nl")).format(Calendar.getInstance().time)

    private val calStart = Calendar.getInstance()
    private val calEnd = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vehicle_reservation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val vehicleRepository = VehicleRepository(activity!!.applicationContext)
        val vehicleViewModel = VehicleViewModel(vehicleRepository)

        val sharedPreferences = SharedPreferences(activity!!.applicationContext)
        val authToken: String? = sharedPreferences.getValueString("auth-token")

        if (authToken == null){
            startLoginActivity()
        }

        vehicleViewModel.getAllVehicleReservations(Vehicle.CarReservationOptions(date = reservationDate)).observe(this, androidx.lifecycle.Observer {
            vehicleCarList.addAll(it)
            setupRecyclerView(vehicleViewModel)
        })

        setupDateComponents(vehicleViewModel)
    }

    private fun setupDateComponents(vehicleViewModel: VehicleViewModel) {
        calendarReservationView.minDate = (System.currentTimeMillis() - 1000)

        calendarReservationView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val cal= Calendar.getInstance()
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.YEAR, year)

            reservationDate = SimpleDateFormat("yyyy-MM-dd", Locale("nl")).format(cal.time)

            vehicleCarList.clear()
            vehicleViewModel.getAllVehicleReservations(Vehicle.CarReservationOptions(date = reservationDate)).observe(this, androidx.lifecycle.Observer {
                vehicleCarList.addAll(it)
                setupRecyclerView(vehicleViewModel)
            })
        }

        startTime.setOnClickListener {

            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                calStart.set(Calendar.HOUR_OF_DAY, hour)
                calStart.set(Calendar.MINUTE, minute)

                calEnd.set(Calendar.HOUR_OF_DAY, hour + 2)
                calEnd.set(Calendar.MINUTE, minute)

                startTime.setText(SimpleDateFormat("HH:mm", Locale("nl")).format(calStart.time))
                startReservationTime = SimpleDateFormat("HH:mm", Locale("nl")).format(calStart.time)

                endTime.setText(SimpleDateFormat("HH:mm", Locale("nl")).format(calEnd.time))
                endReservationTime = SimpleDateFormat("HH:mm", Locale("nl")).format(calEnd.time)

                setupRecyclerView(vehicleViewModel)
            }
            TimePickerDialog(context, timeSetListener, calStart.get(Calendar.HOUR_OF_DAY), calStart.get(Calendar.MINUTE), true).show()
        }

        endTime.setOnClickListener {

            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                calEnd.set(Calendar.HOUR_OF_DAY, hour)
                calEnd.set(Calendar.MINUTE, minute)

                if(startReservationTime.equals(" ")) {
                    calStart.set(Calendar.HOUR_OF_DAY, hour - 2)
                    calStart.set(Calendar.MINUTE, minute)

                    startTime.setText(SimpleDateFormat("HH:mm", Locale("nl")).format(calStart.time))
                    startReservationTime = SimpleDateFormat("HH:mm", Locale("nl")).format(calStart.time)

                    endTime.setText(SimpleDateFormat("HH:mm", Locale("nl")).format(calEnd.time))
                    endReservationTime = SimpleDateFormat("HH:mm", Locale("nl")).format(calEnd.time)

                    setupRecyclerView(vehicleViewModel)
                } else if(calEnd.after(calStart)){
                    endTime.setText(SimpleDateFormat("HH:mm", Locale("nl")).format(calEnd.time))
                    endReservationTime = SimpleDateFormat("HH:mm", Locale("nl")).format(calEnd.time)

                    setupRecyclerView(vehicleViewModel)
                } else {
                    Toast.makeText(context,R.string.toast_end_after,Toast.LENGTH_SHORT).show()
                }
            }
            TimePickerDialog(context, timeSetListener, calEnd.get(Calendar.HOUR_OF_DAY), calEnd.get(Calendar.MINUTE), true).show()
        }
    }

    // TODO: Use Synchronize instead of Thread.Sleep, and show Toast on previous page when new reservation
    private fun setupRecyclerView(vehicleViewModel: VehicleViewModel) {
        val viewManager = LinearLayoutManager(activity!!.applicationContext)
        val vehicleListAdapter = VehicleCarListAdapter(
            activity!!.applicationContext,
            vehicleCarList,
            reservationDate,
            startReservationTime,
            endReservationTime,
            object : VehicleCarListAdapter.OnClickItemListener {
                @SuppressLint("NewApi")
                override fun onItemClick(position: Int, view: View) {

                    val car : Vehicle.CarWithReservations = vehicleCarList[position]

                    if(!reservationDate.equals(" ") && !startReservationTime.equals(" ") && !endReservationTime.equals(" ")) {
                        val sharedPreferences = SharedPreferences(activity!!.applicationContext)
                        val adviserId: String = sharedPreferences.getValueString("adviser-id")!!
                        val carId: String = car.id

                        val dateFormatted: String = dateParser.toFormattedNLdate(reservationDate)

                        val fromTime: String = formatDateTime(reservationDate, startReservationTime)
                        val toTime: String = formatDateTime(reservationDate, endReservationTime)

                        MaterialAlertDialogBuilder(context)
                            .setTitle(getString(R.string.confirm_reservation))
                            .setMessage(formatAlertDialog(
                                brand = car.brand,
                                model = car.model,
                                transmission = car.transmission,
                                date = dateFormatted,
                                startTime = startReservationTime,
                                endTime = endReservationTime))
                            .setPositiveButton(getString(R.string.vehicle_delete_message_true)) { _, _ ->

                                vehicleViewModel.createVehicleReservation(Vehicle.CreateReservation(
                                    advisor = adviserId,
                                    vehicle = carId,
                                    date = reservationDate,
                                    start = fromTime,
                                    end = toTime))
                                Thread.sleep(500)
                                findNavController().navigate(R.id.action_global_vehicleFragment)



//                                val toast: Toast = Toast.makeText(context,R.string.toast_vehicle_reserved,Toast.LENGTH_SHORT)
//
//                                val view: View = toast.view
//                                view.setBackgroundColor(resources.getColor(R.color.green_button))
//
//                                val text: TextView = view.findViewById(android.R.id.message)
//                                text.setTextColor(Color.WHITE)
//                                toast.show()

                            }.setNegativeButton(getString(R.string.vehicle_delete_message_false), null)
                            .show()
                    } else {
                        Toast.makeText(context,R.string.toast_select_all_inputs,Toast.LENGTH_LONG).show()
                    }
                }
            })

        recyclerView.apply {
            this.layoutManager = viewManager
            this.adapter = vehicleListAdapter
        }

        val viewManager2 = LinearLayoutManager(activity!!.applicationContext)
        val reservationsList: ArrayList<Vehicle.CarWithReservations> = ArrayList()
        for (reservation in vehicleCarList) {
            if (reservation.reservations.isEmpty()) {
                continue
            } else {
                reservationsList.add(reservation)
            }
        }

        val vehicleReservedListAdapter = VehicleReservedListAdapter(
            activity!!.applicationContext,
            reservationsList,
            object : VehicleReservedListAdapter.OnClickItemListener {
                override fun onItemClick(position: Int, view: View) {

                }
            })

        recyclerViewReservations.apply {
            this.layoutManager = viewManager2
            this.adapter = vehicleReservedListAdapter
        }
    }

    private fun startLoginActivity() {
        val intent = Intent(activity!!.applicationContext, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun setTitle() {
        val appTitle = activity!!.findViewById<View>(R.id.app_title) as TextView
        appTitle.setText(R.string.navigation_vehicle)
    }

    override fun onResume() {
        setTitle()
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        (activity as NavigationActivity).setProgressBarVisible(false)
    }

    private fun formatDateTime(date: String, time: String): String {
        return date
            .plus("T")
            .plus(time)
    }

    private fun formatAlertDialog(brand: String, model: String, transmission: String, date: String, startTime: String, endTime: String): String {
        return getString(R.string.alert_car_info)
            .plus(" ")
            .plus(brand)
            .plus(" ")
            .plus(model)
            .plus(" ")
            .plus(transmission)
            .plus("\n")
            .plus(getString(R.string.alert_date_info))
            .plus(" ")
            .plus(date)
            .plus("\n")
            .plus(getString(R.string.alert_time_info))
            .plus(" ")
            .plus(startTime)
            .plus(" - ")
            .plus(endTime)
    }
}