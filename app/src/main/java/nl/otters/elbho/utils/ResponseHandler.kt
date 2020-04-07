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

    fun succesMessage(succes_string: Int) {
        val snackbarDialog = Snackbar.make(
            curView,
            succes_string,
            Snackbar.LENGTH_LONG
        )
        val snackbarView = snackbarDialog.view
        snackbarView.setBackgroundColor(
            ContextCompat.getColor(
                context,
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
    }
}