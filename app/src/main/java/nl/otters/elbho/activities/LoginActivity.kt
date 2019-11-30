package nl.otters.elbho.activities

import nl.otters.elbho.R
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle? ) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailTextInputEdit.addTextChangedListener(textWatcher)
        passwordTextInputEdit.addTextChangedListener(textWatcher)
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(editable: Editable) {
            // If both input fields have a value, the loginButton will be set to enabled.
            // I know the if else looks weird, but android studio is forcing me to use this syntactic sugar...
            // TODO: Nice have, not checking on string val but if a real email is being entered.
            if (editable == emailTextInputEdit.editableText && editable.toString() != "" && passwordTextInputEdit.editableText.toString() != "") {
                loginButton.isEnabled = true
            } else loginButton.isEnabled = editable == passwordTextInputEdit.editableText && editable.toString() != "" && emailTextInputEdit.editableText.toString() != ""
        }
    }
}
