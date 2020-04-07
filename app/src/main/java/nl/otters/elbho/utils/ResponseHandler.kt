package nl.otters.elbho.utils

import android.content.Context
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import nl.otters.elbho.R

class ResponseHandler(private val context: Context, private val curView: View) {

    fun errorMessage(error_string: Int) {
        Toast.makeText(
            context,
            error_string,
            Toast.LENGTH_LONG
        ).show()
    }

    fun successMessage(success_string: Int) {
        val snackBarDialog = Snackbar.make(
            curView,
            success_string,
            Snackbar.LENGTH_LONG
        )
        val snackBarView = snackBarDialog.view
        snackBarView.setBackgroundColor(
            ContextCompat.getColor(
                context,
                R.color.vehicle_snackBar_bg_col
            )
        )
        val snackBarTextView =
            snackBarView.findViewById<TextView>(R.id.snackbar_text)
        snackBarTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(
            R.drawable.ic_check_circle_24dp,
            0,
            0,
            0
        )
        snackBarTextView.compoundDrawablePadding = 75
        snackBarDialog.show()
    }
}