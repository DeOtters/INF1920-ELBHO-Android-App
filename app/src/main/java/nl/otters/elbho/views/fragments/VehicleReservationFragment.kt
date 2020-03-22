package nl.otters.elbho.views.fragments

import android.annotation.SuppressLint
import android.app.ProgressDialog.show
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_vehicle_reservation.*
import nl.otters.elbho.R
import nl.otters.elbho.adapters.VehicleCarListAdapter
import nl.otters.elbho.models.Vehicle
import nl.otters.elbho.repositories.VehicleRepository
import nl.otters.elbho.utils.DateParser
import nl.otters.elbho.utils.SharedPreferences
import nl.otters.elbho.viewModels.VehicleViewModel
import nl.otters.elbho.views.activities.LoginActivity
import nl.otters.elbho.views.activities.NavigationActivity
import java.text.SimpleDateFormat
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
        val vehicleRepository = VehicleRepository(activity!!.applicationContext)
        val vehicleViewModel = VehicleViewModel(vehicleRepository)

        val sharedPreferences = SharedPreferences(activity!!.applicationContext)
        val authToken: String? = sharedPreferences.getValueString("auth-token")

        if (authToken == null){
            startLoginActivity()
        }

        vehicleViewModel.getAllVehicleReservations(Vehicle.CarReservationOptions(date = reservationDate))
            .observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            vehicleCarList.addAll(it)
            setupRecyclerView(vehicleViewModel)
        })

        setupDateComponents(vehicleViewModel)
        setupMakeCarReservation(vehicleViewModel)
    }

    private fun setupDateComponents(vehicleViewModel: VehicleViewModel) {
        calendarReservationView.minDate = (System.currentTimeMillis() - 1000)

        calendarReservationView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val cal= Calendar.getInstance()
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.YEAR, year)

            reservationDate = SimpleDateFormat("yyyy-MM-dd", Locale("nl")).format(cal.time)

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

            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                calStart.set(Calendar.HOUR_OF_DAY, hour)
                calStart.set(Calendar.MINUTE, minute)

                calEnd.set(Calendar.HOUR_OF_DAY, hour)
                calEnd.set(Calendar.MINUTE, minute)

                startTime.setText(SimpleDateFormat("HH:mm", Locale("nl")).format(calStart.time))
                startReservationTime = SimpleDateFormat("HH:mm", Locale("nl")).format(calStart.time)

                endTime.setText(SimpleDateFormat("HH:mm", Locale("nl")).format(calEnd.time))
                endReservationTime = SimpleDateFormat("HH:mm", Locale("nl")).format(calEnd.time)

                itemSelected = -1
                carReservation = null

                setupRecyclerView(vehicleViewModel)
            }
            TimePickerDialog(context, timeSetListener, calStart.get(Calendar.HOUR_OF_DAY), calStart.get(Calendar.MINUTE), true).show()
        }

        endTime.setOnClickListener {

            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                calEnd.set(Calendar.HOUR_OF_DAY, hour)
                calEnd.set(Calendar.MINUTE, minute)

                if(startReservationTime.equals(" ")) {
                    calStart.set(Calendar.HOUR_OF_DAY, hour)
                    calStart.set(Calendar.MINUTE, minute)

                    startTime.setText(SimpleDateFormat("HH:mm", Locale("nl")).format(calStart.time))
                    startReservationTime = SimpleDateFormat("HH:mm", Locale("nl")).format(calStart.time)

                    endTime.setText(SimpleDateFormat("HH:mm", Locale("nl")).format(calEnd.time))
                    endReservationTime = SimpleDateFormat("HH:mm", Locale("nl")).format(calEnd.time)

                    itemSelected = -1
                    carReservation = null

                    setupRecyclerView(vehicleViewModel)
                } else if(calEnd.after(calStart)){
                    endTime.setText(SimpleDateFormat("HH:mm", Locale("nl")).format(calEnd.time))
                    endReservationTime = SimpleDateFormat("HH:mm", Locale("nl")).format(calEnd.time)

                    itemSelected = -1
                    carReservation = null

                    setupRecyclerView(vehicleViewModel)
                } else {
                    val toast = Toast.makeText(context,R.string.toast_end_after,Toast.LENGTH_SHORT)
                    toast.view.setBackgroundResource(R.color.colorSecondary)
                    toast.show()
                }
            }
            TimePickerDialog(context, timeSetListener, calEnd.get(Calendar.HOUR_OF_DAY), calEnd.get(Calendar.MINUTE), true).show()
        }
    }

    // TODO: Use Synchronize instead of Thread.Sleep, and show Toast on previous page when new reservation
    private fun setupRecyclerView(vehicleViewModel: VehicleViewModel) {
        checkTimeSelected()
        val viewManager = LinearLayoutManager(activity!!.applicationContext)

        val vehicleListAdapter = VehicleCarListAdapter(
            activity!!.applicationContext,
            vehicleCarList,
            reservationDate,
            startReservationTime,
            endReservationTime,
            itemSelected,
            object : VehicleCarListAdapter.OnClickItemListener {
                @SuppressLint("NewApi")
                override fun onItemClick(position: Int, view: View) {

                    if(!reservationDate.equals(" ") && !startReservationTime.equals(" ") && !endReservationTime.equals(" ")) {
                        if (!startReservationTime.equals(endReservationTime)) {

                            val car: Vehicle.CarWithReservations = vehicleCarList[position]

                            itemSelected = position

                            val sharedPreferences = SharedPreferences(activity!!.applicationContext)
                            val adviserId: String = sharedPreferences.getValueString("adviser-id")!!
                            val carId: String = car.id
                            val fromTime: String =
                                formatDateTime(reservationDate, startReservationTime)
                            val toTime: String = formatDateTime(reservationDate, endReservationTime)


                            carReservation = Vehicle.CreateReservation(
                                advisor = adviserId,
                                vehicle = carId,
                                date = reservationDate,
                                start = fromTime,
                                end = toTime
                            )

                            setupRecyclerView(vehicleViewModel)
                        } else {
                            val toast = Toast.makeText(context, R.string.toast_end_after, Toast.LENGTH_SHORT)
                            toast.view.setBackgroundResource(R.color.colorSecondary)
                            toast.show()

                        }
                    } else {
                        val toast : Toast = Toast.makeText(context, R.string.toast_select_all_inputs, Toast.LENGTH_LONG)
                        toast.view.setBackgroundResource(R.color.colorSecondary)
                        toast.show()
                    }
                }
            })

        recyclerView.apply {
            this.layoutManager = viewManager
            this.adapter = vehicleListAdapter
        }
    }

    private fun setupMakeCarReservation(vehicleViewModel: VehicleViewModel) {
        vehicle_reservation_btn.setOnClickListener {
            if (!reservationDate.equals(" ") && !startReservationTime.equals(" ") && !endReservationTime.equals(" ")) {
                if (!startReservationTime.equals(endReservationTime)) {
                    if (carReservation != null) {

                        vehicleViewModel.createVehicleReservation(carReservation!!)
                        Thread.sleep(500)
                        super.getFragmentManager()?.popBackStack()

                        val snackbarDialog = Snackbar.make(
                            it,
                            getString(R.string.snackbar_vehicle_reserved),
                            Snackbar.LENGTH_LONG
                        )
                        val snackbarView = snackbarDialog.view
                        snackbarView.setBackgroundColor(
                            ContextCompat.getColor(
                                activity!!.applicationContext,
                                R.color.vehicle_snackBar_bg_col
                            )
                        )
                        val snackbarTextView =
                            snackbarView.findViewById<TextView>(R.id.snackbar_text)
                        snackbarTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            R.drawable.ic_check_circle_24dp,
                            0,
                            0,
                            0
                        )
                        snackbarTextView.compoundDrawablePadding = 75
                        snackbarDialog.show()

                    } else {
                        val toast = Toast.makeText(context, R.string.toast_select_car, Toast.LENGTH_SHORT)
                        toast.view.setBackgroundResource(R.color.colorSecondary)
                        toast.show()
                    }

                } else {
                    val toast = Toast.makeText(context, R.string.toast_end_after, Toast.LENGTH_SHORT)
                    toast.view.setBackgroundResource(R.color.colorSecondary)
                    toast.show()
                }

            } else {
                val toast = Toast.makeText(context, R.string.toast_select_all_inputs, Toast.LENGTH_LONG)
                toast.view.setBackgroundResource(R.color.colorSecondary)
                toast.show()
            }
        }
    }

    private fun checkTimeSelected() {
        if (startReservationTime.equals(" ") && endReservationTime.equals(" ")) {
            recyclerView.visibility = View.INVISIBLE
            empty_view.visibility = View.VISIBLE
            vehicle_reservation_btn.visibility = View.INVISIBLE
        }
        else {
            recyclerView.visibility = View.VISIBLE
            empty_view.visibility = View.INVISIBLE
            vehicle_reservation_btn.visibility = View.VISIBLE
        }
    }

    private fun startLoginActivity() {
        val intent = Intent(activity!!.applicationContext, LoginActivity::class.java)
        startActivity(intent)
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