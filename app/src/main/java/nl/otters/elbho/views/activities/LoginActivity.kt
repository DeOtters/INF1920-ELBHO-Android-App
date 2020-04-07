package nl.otters.elbho.views.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
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
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val sharedPreferences = SharedPreferences(this)
        val authToken: String? = sharedPreferences.getValueString("auth-token")
        val view: View = findViewById(R.id.container)
        val adviserRepository = AdviserRepository(this, view)
        val loginViewModel = LoginViewModel(adviserRepository)

        if (authToken != null) {
            startOverviewActivity()
        }

        setupTextFieldListeners(textWatcher)

        loginButton.setOnClickListener {
            updateUI(loginViewModel)
        }
    }

    private fun updateUI(loginViewModel: LoginViewModel) {
        progressBar.isVisible = true
        loginButton.isEnabled = false
        val loginCredentials: Adviser.Login =
            Adviser.Login(emailTextInputEdit.text.toString(), passwordTextInputEdit.text.toString())
        loginViewModel.adviserLogin(loginCredentials).observe(this, Observer {
            if (it) {
                startOverviewActivity()
            } else {
                progressBar.isVisible = false
                loginButton.isEnabled = true
                Snackbar.make(
                    container,
                    R.string.login_snackbar_message_wrongCredentials,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun startOverviewActivity() {
        val intent = Intent(this, NavigationActivity::class.java)
        startActivity(intent)
    }

    private fun setupTextFieldListeners(textWatcher: TextWatcher) {
        emailTextInputEdit.addTextChangedListener(textWatcher)
        passwordTextInputEdit.addTextChangedListener(textWatcher)
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(editable: Editable) {
            loginButton.isEnabled =
                !(TextUtils.isEmpty(emailTextInputEdit.text) || TextUtils.isEmpty(
                    passwordTextInputEdit.text
                ))
        }
    }
}
