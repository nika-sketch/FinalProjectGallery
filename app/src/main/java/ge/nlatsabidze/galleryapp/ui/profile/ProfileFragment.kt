package ge.nlatsabidze.galleryapp.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import ge.nlatsabidze.galleryapp.R


class ProfileFragment : Fragment() {

    private var mAuth: FirebaseAuth? = null

    private var userEmail: TextView? = null
    private var logoutBtn: Button? = null
    private var emailFieldWrapper: TextInputLayout? = null
    private var emailField: TextInputEditText? = null
    private var passwordFieldWrapper: TextInputLayout? = null
    private var passwordField: TextInputEditText? = null
    private var loginBtn: Button? = null
    private var registerBtn: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        userEmail = root.findViewById(R.id.userEmail)
        logoutBtn = root.findViewById(R.id.logoutBtn)
        emailFieldWrapper = root.findViewById(R.id.emailFieldWrapper)
        emailField = root.findViewById(R.id.emailField)
        passwordFieldWrapper = root.findViewById(R.id.passwordFieldWrapper)
        passwordField = root.findViewById(R.id.passwordField)
        loginBtn = root.findViewById(R.id.loginBtn)
        registerBtn = root.findViewById(R.id.registerBtn)

        mAuth = FirebaseAuth.getInstance()
        mAuth!!.addAuthStateListener { state ->
            run {
                val currentUser = state.currentUser
                if (currentUser != null) {
                    userEmail!!.text = currentUser.email

                    logoutBtn!!.visibility = View.VISIBLE
                    emailFieldWrapper!!.visibility = View.INVISIBLE
                    passwordFieldWrapper!!.visibility = View.INVISIBLE
                    loginBtn!!.visibility = View.INVISIBLE
                    registerBtn!!.visibility = View.INVISIBLE
                } else {
                    userEmail!!.text = getString(R.string.sign_in)

                    logoutBtn!!.visibility = View.INVISIBLE
                    emailFieldWrapper!!.visibility = View.VISIBLE
                    passwordFieldWrapper!!.visibility = View.VISIBLE
                    loginBtn!!.visibility = View.VISIBLE
                    registerBtn!!.visibility = View.VISIBLE
                }
            }
        }

        root.findViewById<Button>(R.id.registerBtn).setOnClickListener {
            val email = emailField!!.text
            val password = passwordField!!.text

            mAuth!!.createUserWithEmailAndPassword(email.toString().trim(), password.toString().trim())
        }

        root.findViewById<Button>(R.id.loginBtn).setOnClickListener {
            val email = emailField!!.text
            val password = passwordField!!.text

            mAuth!!.signInWithEmailAndPassword(email.toString().trim(), password.toString().trim())
        }

        root.findViewById<Button>(R.id.logoutBtn).setOnClickListener {
            mAuth!!.signOut()
        }

        return root
    }
}