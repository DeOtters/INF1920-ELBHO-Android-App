package nl.otters.elbho.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.*
import nl.otters.elbho.R
import nl.otters.elbho.models.Adviser
import nl.otters.elbho.repositories.AdviserRepository
import nl.otters.elbho.utils.SharedPreferences
import nl.otters.elbho.viewModels.LoginViewModel

class LoginActivity : AppCompatActivity() {
    private val adviserRepository = AdviserRepository(this)

    override fun onCreate(savedInstanceState: Bundle? ) {
        val sharedPreferences = SharedPreferences(this)
        val authToken: String? = sharedPreferences.getValueString("auth-token")
        val loginViewModel = LoginViewModel(adviserRepository)

        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreferences.clear()
        if (authToken != null){
            startOverviewActivity()
        }

        setupTextFieldListeners(textWatcher)

        loginButton.setOnClickListener {
            updateUI(loginViewModel)
        }
    }

    private fun updateUI(loginViewModel: LoginViewModel){
        progressBar.isVisible = true
        val loginCredentials: Adviser.Login = Adviser.Login(emailTextInputEdit.text.toString(), passwordTextInputEdit.text.toString())
        loginViewModel.adviserLogin(loginCredentials).observe(this, Observer {
            //val success: Boolean
            if(it){
                startOverviewActivity()
            }else{
                progressBar.isVisible = false
                Snackbar.make(container, R.string.login_snackbar_message_wrongCredentials, Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    private fun startOverviewActivity() {
        val intent = Intent(this, NavigationActivity::class.java)
        startActivity(intent)
    }

    private fun setupTextFieldListeners(textWatcher: TextWatcher){
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
