package com.example.tiketsfm

import android.content.DialogInterface
import android.content.DialogInterface.OnShowListener
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.rengwuxian.materialedittext.MaterialEditText


class MainActivity : AppCompatActivity() {
    private lateinit var signInBtn: Button
    private lateinit var signUpBtn: Button
    private lateinit var root: ConstraintLayout
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var users: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        signInBtn = findViewById(R.id.button_sign_in)
        signUpBtn = findViewById(R.id.button_sign_up)
        root = findViewById(R.id.root_element)
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        users = db.getReference("USERS")

        signInBtn.setOnClickListener{showSignInWindow()}
        signUpBtn.setOnClickListener{showSignUpWindow()}
    }

    private fun showSignInWindow(){

        val inflater: LayoutInflater = LayoutInflater.from(this)
        val signInWindow: View = inflater.inflate(R.layout.sign_in_window, null)

        val email: MaterialEditText = signInWindow.findViewById(R.id.emailField)
        val password: MaterialEditText = signInWindow.findViewById(R.id.Password)

        val signInDialog = AlertDialog.Builder(this)
                .setView(signInWindow)
                .setTitle("Вход")
                .setPositiveButton("Войти", null)
                .setNegativeButton("Отмена", null)
                .create()

        signInDialog.setOnShowListener {
            val buttonNegative = signInDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            val buttonPositive = signInDialog.getButton(AlertDialog.BUTTON_POSITIVE)

            buttonNegative.setOnClickListener {signInDialog.dismiss()}

            buttonPositive.setOnClickListener {
                if(TextUtils.isEmpty(email.text.toString())){
                    Snackbar.make(root, "Введите вашу почту", Snackbar.LENGTH_SHORT).show()
                } else if (password.text.toString().length < 5) {
                    Snackbar.make(root, "Введите пароль, длиной больше 5 символов", Snackbar.LENGTH_SHORT).show()
                } else {
                    auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                            .addOnSuccessListener {
                                Snackbar.make(root, "Authentication succeed.", Snackbar.LENGTH_SHORT).show()
                                val intent = Intent(this, TicketsActivity::class.java)
                                startActivity(intent)
                            }.addOnFailureListener { e: Exception ->
                                Snackbar.make(root, "Authentication failed: " + e.message, Snackbar.LENGTH_SHORT).show()
                            }
                }
            }

        }
        signInDialog.show()
    }

    private fun showSignUpWindow() {

        val inflater: LayoutInflater = LayoutInflater.from(this)
        val signUpWindow: View = inflater.inflate(R.layout.sign_up_window, null)

        val name: MaterialEditText = signUpWindow.findViewById(R.id.name)
        val lastName: MaterialEditText = signUpWindow.findViewById(R.id.LastNameField)
        val email: MaterialEditText = signUpWindow.findViewById(R.id.emailField)
        val password: MaterialEditText = signUpWindow.findViewById(R.id.Password)

        val signUpDialog = AlertDialog.Builder(this)
                .setView(signUpWindow)
                .setTitle("Регистрация")
                .setPositiveButton("Зарегистрироваться", null)
                .setNegativeButton("Отмена", null)
                .create()

        signUpDialog.setOnShowListener {
            val buttonNegative = signUpDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            val buttonPositive = signUpDialog.getButton(AlertDialog.BUTTON_POSITIVE)

            buttonNegative.setOnClickListener {signUpDialog.dismiss()}

            buttonPositive.setOnClickListener {
                when {
                    TextUtils.isEmpty(name.text.toString()) -> {
                        Snackbar.make(root, "Введите ваше имя", Snackbar.LENGTH_SHORT).show()
                    }
                    TextUtils.isEmpty(lastName.text.toString()) -> {
                        Snackbar.make(root, "Введите ваш фамилию", Snackbar.LENGTH_SHORT).show()
                    }
                    TextUtils.isEmpty(email.text.toString()) -> {
                        Snackbar.make(root, "Введите вашу почту", Snackbar.LENGTH_SHORT).show()
                    }
                    password.text.toString().length < 5 -> {
                        Snackbar.make(root, "Придумайте пароль минимум 5 символов", Snackbar.LENGTH_SHORT).show()
                    }
                    else -> auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                            .addOnSuccessListener {
                                val user = User(name.text.toString() + " " + lastName.text.toString(),
                                        email.text.toString(), password.text.toString())

                                users.child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(user)
                                val intent = Intent(this, TicketsActivity::class.java)
                                startActivity(intent)
                                Snackbar.make(root, "Регистрация выполнена успешно", Snackbar.LENGTH_SHORT).show()
                                signUpDialog.dismiss()
                            }
                }
            }

        }
        signUpDialog.show()
    }

}