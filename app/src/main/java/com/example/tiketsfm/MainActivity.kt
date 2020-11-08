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
import com.example.tiketsfm.Models.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
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
        users = db.getReference("Users")

        signInBtn.setOnClickListener{showSignInWindow()}
        signUpBtn.setOnClickListener{showRegisterWindow()}
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

            /*auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Snackbar.make(root, "Registration succeed.", Snackbar.LENGTH_SHORT).show()
                    }
                }*/

            auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnSuccessListener {
                    Snackbar.make(root, "Authentication succeed.", Snackbar.LENGTH_SHORT).show()
                    val intent = Intent(this, TicketsActivity::class.java)
                    startActivity(intent)
                }.addOnFailureListener { e: Exception ->
                    Snackbar.make(root, "Authentication failed: " + e.message, Snackbar.LENGTH_SHORT).show()
                }
        }
        dialog.show()
    }

    private fun showRegisterWindow(){
        val dialog: AlertDialog.Builder = AlertDialog.Builder(this)
        dialog.setTitle("Зарегистрироваться")
        dialog.setMessage("Введите свои данные для регистрации")

        val inflater: LayoutInflater = LayoutInflater.from(this)
        val signUpWindow: View = inflater.inflate(R.layout.sign_up_window, null)
        dialog.setView(signUpWindow)

        val name: MaterialEditText = signUpWindow.findViewById(R.id.name)
        val lastName: MaterialEditText = signUpWindow.findViewById(R.id.LastNameField)
        val email: MaterialEditText = signUpWindow.findViewById(R.id.emailField);
        val password: MaterialEditText = signUpWindow.findViewById(R.id.Password);

        dialog.setNegativeButton("Отменить") { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.dismiss()
        }

        dialog.setPositiveButton("Зарегистрироваться") { _: DialogInterface, _: Int ->
            if(TextUtils.isEmpty(name.text.toString())){
                Snackbar.make(root, "Введите ваше имя", Snackbar.LENGTH_SHORT).show()
            }

            if(TextUtils.isEmpty(lastName.text.toString())){
                Snackbar.make(root, "Введите ваш фамилию", Snackbar.LENGTH_SHORT).show()
            }

            if(TextUtils.isEmpty(email.text.toString())){
                Snackbar.make(root, "Введите вашу почту", Snackbar.LENGTH_SHORT).show()
            }

            if(password.text.toString().length < 5){
                Snackbar.make(root, "Придумайте пароль минимум 5 символов", Snackbar.LENGTH_SHORT).show()
            }

            auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                    .addOnSuccessListener { authResult: AuthResult ->
                        val user: User = User(name.text.toString(), lastName.text.toString(), email.text.toString(), password.text.toString())

                        users.child(FirebaseAuth.getInstance().currentUser!!.uid)//НАДО ПРОВЕРИТЬ ПРАВИЛЬНО ЛИ СТОИТ ОПРЕАТОР !!
                                .setValue(user)
                                .addOnSuccessListener {
                                    val intent = Intent(this, TicketsActivity::class.java)
                                    startActivity(intent)
                                }.addOnFailureListener { e: Exception ->
                                    Snackbar.make(root, "Ошибка регистрации" + e.message, Snackbar.LENGTH_SHORT).show()
                                }
                    }
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Snackbar.make(root, "Регистрация выполнена успешно", Snackbar.LENGTH_SHORT).show()

                        }
            }
        }
        dialog.show()
    }
}

//правильно добавляет юзера в бд, но не переходит в другую активити
//возможно и не правильно добавляет. у меня перестало работать.