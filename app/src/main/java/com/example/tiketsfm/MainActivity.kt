package com.example.tiketsfm

import android.content.Context
import android.content.DialogInterface
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
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.rengwuxian.materialedittext.MaterialEditText


class MainActivity : AppCompatActivity() {
    lateinit var signInBtn: Button
    lateinit var root: ConstraintLayout
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        signInBtn = findViewById(R.id.signIn)
        root = findViewById(R.id.root_element)
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()

       // signInBtn.setOnClickListener{showSignInWindow()}

    }

    fun showSignInWindow(){
        val dialog: AlertDialog.Builder = AlertDialog.Builder(this)
        dialog.setTitle("Войти")
        dialog.setMessage("ВВедите данные для входа")

        val inflater: LayoutInflater = LayoutInflater.from(this)
        val signInWindow: View = inflater.inflate(R.layout.sign_in_window, null)
        dialog.setView(signInWindow)

        val email: MaterialEditText = signInWindow.findViewById(R.id.emailField)
        val password: MaterialEditText = signInWindow.findViewById(R.id.Password)

        dialog.setNegativeButton("Отменить") { dialogInterface: DialogInterface, i: Int ->
            dialogInterface.dismiss()
        }

        dialog.setPositiveButton("Войти") {dialogInterface: DialogInterface, i: Int ->
            if(TextUtils.isEmpty(email.text.toString())){
                Snackbar.make(root, "ВВедите вашу почту", Snackbar.LENGTH_SHORT).show()
                return@setPositiveButton
            }

            if (password.getText().toString().length < 5) {
                Snackbar.make(root, "ВВедите пароль, который больше 5 символов", Snackbar.LENGTH_SHORT).show()
                return@setPositiveButton
            }

            auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnSuccessListener {authResult: AuthResult? ->
                    //переход в другую активити?
                    //startActivity(Intent(this@MainActivity, MapActivity::class.java))
                    finish()
                }.addOnFailureListener { e: Exception ->
                    Snackbar.make(root, "Ошибка авторизации" + e.message, Snackbar.LENGTH_SHORT).show()
                }
        }
        dialog.show()
    }
}