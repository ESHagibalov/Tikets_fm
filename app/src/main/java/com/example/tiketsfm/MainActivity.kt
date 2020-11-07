package com.example.tiketsfm

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
import com.google.firebase.auth.FirebaseAuth
import com.rengwuxian.materialedittext.MaterialEditText

class MainActivity : AppCompatActivity() {
    private lateinit var signInBtn: Button
    private lateinit var root: ConstraintLayout
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        signInBtn = findViewById(R.id.button_sign_in)
        root = findViewById(R.id.root_element)

        auth = FirebaseAuth.getInstance()

        signInBtn.setOnClickListener{showSignInWindow()}
    }

    private fun showSignInWindow(){
        val dialog: AlertDialog.Builder = AlertDialog.Builder(this)
        dialog.setTitle("Войти")
        dialog.setMessage("Введите данные для входа")

        val inflater: LayoutInflater = LayoutInflater.from(this)
        val signInWindow: View = inflater.inflate(R.layout.sign_in_window, null)
        dialog.setView(signInWindow)

        val email: MaterialEditText = signInWindow.findViewById(R.id.emailField)
        val password: MaterialEditText = signInWindow.findViewById(R.id.Password)

        dialog.setNegativeButton("Отменить") { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.dismiss()
        }

        dialog.setPositiveButton("Войти") { _: DialogInterface, _: Int ->
            if(TextUtils.isEmpty(email.text.toString())){
                Snackbar.make(root, "Введите вашу почту", Snackbar.LENGTH_SHORT).show()
            }

            if (password.text.toString().length < 5) {
                Snackbar.make(root, "Введите пароль, длиной больше 5 символов", Snackbar.LENGTH_SHORT).show()
            }

            auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Snackbar.make(root, "Registration succeed.", Snackbar.LENGTH_SHORT).show()
                    }
                }

            auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnSuccessListener {
                    Snackbar.make(root, "Authentication succeed.", Snackbar.LENGTH_SHORT).show()
                    val intent = Intent(this, TicketsActivity::class.java)
                    startActivity(intent)
                }.addOnFailureListener { e: Exception ->
                    Snackbar.make(root, "Authentication failed: " + e.message, Snackbar.LENGTH_SHORT).show()
                }
        }.show()
    }
}