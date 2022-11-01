package com.example.to_do

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.View.OnClickListener
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class SignIn : AppCompatActivity() {

    private lateinit var inputEmail: EditText
    private lateinit var inputPassword:EditText
    private lateinit var buttonSignIn: MaterialButton
    private lateinit var pbar2: ProgressBar
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var preferenceManager: PreferenceManager

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this@SignIn, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        preferenceManager = PreferenceManager(this@SignIn)

        setContentView(R.layout.activity_sign_in)

        inputEmail = findViewById(R.id.inputEmail)
        inputPassword = findViewById(R.id.inputPassword)
        buttonSignIn = findViewById(R.id.signinbtn)
        pbar2 = findViewById(R.id.signinbar)

        findViewById<TextView>(R.id.signup).setOnClickListener(object: OnClickListener{
            override fun onClick(p0: View?) {
                val intent = Intent(this@SignIn, SignUp::class.java)
                startActivity(intent)
            }
        })

        buttonSignIn.setOnClickListener(object: OnClickListener {
            override fun onClick(p0: View?) {
                if (inputEmail.text.toString().trim { it <= ' ' }.isEmpty()) {
                    Toast.makeText(this@SignIn, "Enter Email", Toast.LENGTH_SHORT).show()
                } else if (inputPassword.text.toString().trim { it <= ' ' }.isEmpty()) {
                    Toast.makeText(this@SignIn, "Enter Password", Toast.LENGTH_SHORT).show()
                } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.text.toString()).matches()) {
                    Toast.makeText(this@SignIn, "Enter Valid Email", Toast.LENGTH_SHORT).show()
                } else if (inputPassword.text.toString().length < 6) {
                    Toast.makeText(this@SignIn, "password must have 6 letters" , Toast.LENGTH_SHORT).show()
                }
                else {
                    signIn()
                }
            }
        })
    }

//    override fun onStart() {
//        super.onStart()
//        val currentUser = auth.currentUser
//        if (currentUser != null) {
//            val intent = Intent(this@SignIn, MainActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
//    }

    private fun signIn() {
        buttonSignIn.visibility = View.INVISIBLE
        pbar2.visibility = View.VISIBLE

        val email = inputEmail.text.toString()
        val pass = inputPassword.text.toString()

        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                db.collection(Constants.KEY_COLLECTION_USERS)
                    .whereEqualTo(Constants.KEY_EMAIL, inputEmail.text.toString())
                    .whereEqualTo(Constants.KEY_PASSWORD, inputPassword.text.toString())
                    .get()
                    .addOnCompleteListener {
                        if (it.isSuccessful && it.result != null && it.result.documents.size > 0) {
                            val documentSnapshot: DocumentSnapshot = it.result.documents[0]
                            preferenceManager.putString(Constants.KEY_ID, documentSnapshot.id)
                            preferenceManager.putString(Constants.KEY_FIRST_NAME, documentSnapshot.getString(Constants.KEY_FIRST_NAME))
                            preferenceManager.putString(Constants.KEY_LAST_NAME, documentSnapshot.getString(Constants.KEY_LAST_NAME))

                            val intent = Intent(this@SignIn, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)
                        } else {
                            pbar2.visibility = View.INVISIBLE
                            buttonSignIn.visibility = View.VISIBLE
                            Toast.makeText(this@SignIn, "Unable To Sign In", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                pbar2.visibility = View.INVISIBLE
                buttonSignIn.visibility = View.VISIBLE
                Toast.makeText(
                    this@SignIn, task.exception!!.localizedMessage, Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}