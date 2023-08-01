package com.example.serfinsaapp.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.serfinsaapp.MainActivity
import com.example.serfinsaapp.R
import com.example.serfinsaapp.utils.LoadingDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private val GOOGLE_SIGN_IN = 100
    private val loadingView = LoadingDialog(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnLogin: Button = findViewById(R.id.btnLogin)
        val btnSignInWithGoogle: Button = findViewById(R.id.btnLoginWithGoogle)
        var email: EditText = findViewById(R.id.etEmail)
        var password: EditText = findViewById(R.id.etPassword)

        auth = FirebaseAuth.getInstance()
        btnLogin.setOnClickListener() {
            loadingView.startLoading()
            sigInWithEmail(email.text.toString(), password.text.toString())
        }

        btnSignInWithGoogle.setOnClickListener() {
            loadingView.startLoading()
            signInWithGoogle()
        }
    }

    private fun sigInWithEmail(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    loadingView.dismiss()
                    val user = auth.currentUser
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("key", user.toString())
                    startActivity(intent)
                } else {
                    loadingView.dismiss()
                    Toast.makeText(this, "Error trying to login", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun signInWithGoogle() {
       val googleConf: GoogleSignInOptions =
           GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
               .requestIdToken(getString(R.string.web_client_id))
               .requestEmail()
               .build()

        val googleClient: GoogleSignInClient = GoogleSignIn.getClient(this, googleConf)
        googleClient.signOut()

        startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null )
                    auth.signInWithCredential(credential).addOnCompleteListener() {
                        if (it.isSuccessful)  {
                            loadingView.dismiss()
                            val user = auth.currentUser
                            val intent = Intent(this, MainActivity::class.java)
                            intent.putExtra("key", user.toString())
                            startActivity(intent)
                        } else {
                            loadingView.dismiss()
                            Toast.makeText(this, "Error trying to login", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } catch (e: ApiException) {
                loadingView.dismiss()
                Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }
}