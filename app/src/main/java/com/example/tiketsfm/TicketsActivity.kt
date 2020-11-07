package com.example.tiketsfm

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class TicketsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    private val testList = listOf(
        Ticket("День Первака", "Клуб завод", Date(2020,10,30), 100, 300, false),
        Ticket("Новый год", "Правая набережная", Date(2020,12,29), 200, 300, true)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tickets)

        recyclerView = findViewById(R.id.recycler_tickets)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = TicketsAdapter(testList)
        }

    }

}