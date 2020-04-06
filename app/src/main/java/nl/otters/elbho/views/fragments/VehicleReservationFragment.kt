package nl.otters.elbho.views.fragments

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_vehicle_reservation.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import nl.otters.elbho.R
import nl.otters.elbho.adapters.VehicleCarListAdapter
import nl.otters.elbho.models.Vehicle
import nl.otters.elbho.repositories.VehicleRepository
import nl.otters.elbho.utils.DateParser
import nl.otters.elbho.utils.SharedPreferences
import nl.otters.elbho.viewModels.VehicleViewModel
import nl.otters.elbho.views.activities.NavigationActivity
import java.util.*
import kotlin.collections.ArrayList

class VehicleReservationFragment : DetailFragment() {
    private var vehicleCarList: ArrayList<Vehicle.CarWithReservations> = ArrayList()
    private val dateParser: DateParser = DateParser()

    private var startReservationTime: String = " "
    private var endReservationTime: String = " "
    private var reservationDate: String = dateParser.getDateStampToday()

    private val calStart = Calendar.getInstance()
    private val calEnd = Calendar.getInstance()

    private var carReservation: Vehicle.CreateReservation? = null
    private var itemSelected: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vehicle_reservation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val vehicleRepository = VehicleRepository(activity!!.applicationContext, this.view!!)
        val vehicleViewModel = VehicleViewModel(vehicleRepository)

        setupVehicleCarList(vehicleViewModel)
        setupDateComponents(vehicleViewModel)

        vehicle_reservation_btn.setOnClickListener {
            setupMakeCarReservation(vehicleViewModel)
        }
    }

    private fun setupVehicleCarList(vehicleViewModel : VehicleViewModel) {
        vehicleViewModel.getAllVehicleReservations(Vehicle.CarReservationOptions(date = reservationDate))
            .observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                vehicleCarList.addAll(it)
                setupRecyclerView(vehicleViewModel)
            })
    }

    private fun setupDateComponents(vehicleViewModel: VehicleViewModel) {
        calendarReservationView.minDate = System.currentTimeMillis()

        calendarReservationView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val cal = Calendar.getInstance()
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.YEAR, year)

            reservationDate = dateParser.getDateYMD(cal.time)

            itemSelected = -1
            carReservation = null

            vehicleCarList.clear()
            vehicleViewModel.getAllVehicleReservations(Vehicle.CarReservationOptions(date = reservationDate))
                .observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                    vehicleCarList.addAll(it)
                    setupRecyclerView(vehicleViewModel)
                })
        }

        startTime.setOnClickListener {
            val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                calStart.set(Calendar.HOUR_OF_DAY, hour)
                calStart.set(Calendar.MINUTE, minute)

                startTime.setText(dateParser.toFormatTime(calStart.time))
                startReservationTime = dateParser.toFormatTime(calStart.time)

                itemSelected = -1
                carReservation = null

                setupRecyclerView(vehicleViewModel)
            }
            TimePickerDialog(
                context,
                timeSetListener,
                calStart.get(Calendar.HOUR_OF_DAY),
                calStart.get(Calendar.MINUTE),
                true
            ).show()
        }

        endTime.setOnClickListener {

            val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                calEnd.set(Calendar.HOUR_OF_DAY, hour)
                calEnd.set(Calendar.MINUTE, minute)

                endTime.setText(dateParser.toFormatTime(calEnd.time))
                endReservationTime = dateParser.toFormatTime(calEnd.time)

                itemSelected = -1
                carReservation = null

                setupRecyclerView(vehicleViewModel)
            }
            TimePickerDialog(
                context,
                timeSetListener,
                calEnd.get(Calendar.HOUR_OF_DAY),
                calEnd.get(Calendar.MINUTE),
                true
            ).show()
        }
    }

    private fun setupRecyclerView(vehicleViewModel: VehicleViewModel) {
        checkIfTimeSelected()
        val viewManager = LinearLayoutManager(activity!!.applicationContext)

        val vehicleListAdapter = VehicleCarListAdapter(
            activity!!.applicationContext,
            vehicleCarList,
            reservationDate,
            startReservationTime,
            endReservationTime,
            itemSelected,
            object : VehicleCarListAdapter.OnClickItemListener {
                override fun onItemClick(position: Int, view: View) {
                    if (reservationDate != " " &&
                        startReservationTime != " " &&
                        endReservationTime != " ") {
                        if (startReservationTime != endReservationTime && calEnd.after(calStart)) {
                            val car: Vehicle.CarWithReservations = vehicleCarList[position]

                            itemSelected = position

                            val sharedPreferences = SharedPreferences(activity!!.applicationContext)
                            val adviserId: String = sharedPreferences.getValueString("adviser-id")!!
                            val carId: String = car.id
                            val fromTime: String =
                                formatDateTime(reservationDate, startReservationTime)
                            val toTime: String = formatDateTime(reservationDate, endReservationTime)

                            carReservation = Vehicle.CreateReservation(
                                adviser = adviserId,
                                vehicle = carId,
                                date = reservationDate,
                                start = fromTime,
                                end = toTime
                            )
                            setupRecyclerView(vehicleViewModel)
                        } else {
                            errorMsg(R.string.toast_end_after)
                        }
                    } else {
                        errorMsg(R.string.toast_select_all_inputs)
                    }
                }
            })

        recyclerView.apply {
            this.layoutManager = viewManager
            this.adapter = vehicleListAdapter
        }
    }

    private fun setupMakeCarReservation(vehicleViewModel: VehicleViewModel) = runBlocking {
        if (reservationDate != " " &&
            startReservationTime != " " &&
            endReservationTime != " ") {
            if (startReservationTime != endReservationTime && calEnd.after(calStart)) {
                if (carReservation != null) {
                    vehicleViewModel.createVehicleReservation(carReservation!!)
                    val job = GlobalScope.launch {
                        delay(500L)
                        super.getFragmentManager()?.popBackStack()
                    }
                    job.join()
                } else {
                    errorMsg(R.string.toast_select_car)
                }
            } else {
                errorMsg(R.string.toast_end_after)
            }
        } else {
            errorMsg(R.string.toast_select_all_inputs)
        }
    }

    private fun checkIfTimeSelected() {
        if (startReservationTime == " " && endReservationTime == " ") {
            recyclerView.visibility = View.INVISIBLE
            empty_view.visibility = View.VISIBLE
            vehicle_reservation_btn.visibility = View.INVISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            empty_view.visibility = View.INVISIBLE
            vehicle_reservation_btn.visibility = View.VISIBLE
        }
    }

    private fun errorMsg(error_string : Int) {
        Toast.makeText(
            context,
            error_string,
            Toast.LENGTH_LONG
        ).show()
    }

    private fun setTitle() {
        val appTitle = activity!!.findViewById<View>(R.id.app_title) as TextView
        appTitle.setText(R.string.navigation_vehicle_reserve)
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
}