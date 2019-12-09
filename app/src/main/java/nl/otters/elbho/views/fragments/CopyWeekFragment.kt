package nl.otters.elbho.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.component_copy_week.view.*
import kotlinx.android.synthetic.main.fragment_copy_week.*
import nl.otters.elbho.R

class CopyWeekFragment : DetailFragment() {

    private lateinit var weekList: ArrayList<View>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_copy_week, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        weekList = arrayListOf(
            copy_week_A,
            copy_week_B,
            copy_week_C,
            copy_week_D,
            copy_week_E,
            copy_week_F,
            copy_week_G,
            copy_week_H,
            copy_week_I
        )
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        copy_week_confirm.setOnClickListener { copyWeek(view!!) }

        for (item in weekList) {
            item.copy_checkbox.setOnClickListener {
                // TODO: Save checkbox?
            }
        }
    }

    private fun copyWeek(view: View) {
        // TODO: Get selected checkboxes and send to API

        Snackbar.make(
            view,
            getString(R.string.copy_week_saved),
            Snackbar.LENGTH_SHORT
        ).show()
        findNavController().navigateUp()
    }

    override fun onResume() {
        setTitle()
        super.onResume()
    }

    private fun setTitle() {
        val appTitle = activity!!.findViewById<View>(R.id.app_title) as TextView
        appTitle.setText(R.string.copy_week_title_bar)
    }
}