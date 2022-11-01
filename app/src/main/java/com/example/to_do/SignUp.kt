package com.example.to_do

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUp : AppCompatActivity() {

    private lateinit var inputFirst: EditText
    private lateinit var inputLast: EditText
    private lateinit var inputEmail: EditText
    private lateinit var inputPass: EditText
    private lateinit var inputConfirm: EditText
    private lateinit var buttonSignUp: MaterialButton
    private lateinit var pbar1: ProgressBar
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
            val intent = Intent(this@SignUp, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        preferenceManager = PreferenceManager(this@SignUp)

        setContentView(R.layout.activity_sign_up)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        title = "SIGN UP"

        //auth = FirebaseAuth.getInstance()

        inputFirst = findViewById(R.id.InputFirstName)
        inputLast = findViewById(R.id.InputLastName)
        inputEmail = findViewById(R.id.InputEmail)
        inputPass = findViewById(R.id.InputPassword)
        inputConfirm = findViewById(R.id.InputConfirmPassword)
        buttonSignUp = findViewById(R.id.signupbtn)
        pbar1 = findViewById(R.id.signupbar)

        findViewById<TextView>(R.id.signin).setOnClickListener(object : OnClickListener{
            override fun onClick(p0: View?) {
                onBackPressed()
            }
        })

        buttonSignUp.setOnClickListener(object: OnClickListener{
            override fun onClick(p0: View?) {
                if (inputFirst.text.toString().trim { it <= ' ' }.isEmpty()) {
                    Toast.makeText(this@SignUp, "Enter First Name", Toast.LENGTH_SHORT).show()
                } else if (inputLast.text.toString().trim { it <= ' ' }.isEmpty()) {
                    Toast.makeText(this@SignUp, "Enter Last Name", Toast.LENGTH_SHORT).show()
                } else if (inputEmail.text.toString().trim { it <= ' ' }.isEmpty()) {
                    Toast.makeText(this@SignUp, "Enter Email Address", Toast.LENGTH_SHORT).show()
                } else if (inputPass.text.toString().trim { it <= ' ' }.isEmpty()) {
                    Toast.makeText(this@SignUp, "Enter Password", Toast.LENGTH_SHORT).show()
                } else if (inputPass.text.toString() != inputConfirm.text.toString()) {
                    Toast.makeText(this@SignUp, "Enter Valid Password", Toast.LENGTH_SHORT).show()
                } else if (inputConfirm.text.toString().trim { it <= ' ' }.isEmpty()) {
                    Toast.makeText(this@SignUp, "Confirm Your Password", Toast.LENGTH_SHORT).show()
                } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.text.toString()).matches()) {
                    Toast.makeText(this@SignUp, "Enter Valid Email", Toast.LENGTH_SHORT).show()
                } else if (inputPass.text.toString().length < 6) {
                    Toast.makeText(this@SignUp, "Password must be 6 letters", Toast.LENGTH_SHORT).show()
                }
                else {
                    signUp()
                }
            }
        })
    }

//    override fun onStart() {
//        super.onStart()
//        val currentUser = auth.currentUser
//        if (currentUser != null) {
//            val intent = Intent(this@SignUp, MainActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)

        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return true
    }

    private fun signUp() {
        buttonSignUp.visibility = View.INVISIBLE
        pbar1.visibility = View.VISIBLE
        val email = inputEmail.text.toString()
        val pass = inputPass.text.toString()

        val user = HashMap<String, Any>()
        user[Constants.KEY_FIRST_NAME] = inputFirst.text.toString()
        user[Constants.KEY_LAST_NAME] = inputLast.text.toString()
        user[Constants.KEY_EMAIL] = inputEmail.text.toString()
        user[Constants.KEY_PASSWORD] = inputPass.text.toString()

        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                db.collection(Constants.KEY_COLLECTION_USERS)
                    .add(user)
                    .addOnSuccessListener {
                        preferenceManager.putString(Constants.KEY_ID, it.id)
                        preferenceManager.putString(Constants.KEY_FIRST_NAME, inputFirst.text.toString())
                        preferenceManager.putString(Constants.KEY_LAST_NAME, inputLast.text.toString())

                        val intent = Intent(this@SignUp, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                    }.addOnFailureListener {
                        pbar1.visibility = View.INVISIBLE
                        buttonSignUp.visibility = View.VISIBLE
                        Toast.makeText(this@SignUp, "Error: " + it.message, Toast.LENGTH_SHORT).show()
                    }
            } else {
                pbar1.visibility = View.INVISIBLE
                buttonSignUp.visibility = View.VISIBLE
                Toast.makeText(this@SignUp, task.exception!!.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }
}