package com.example.tiketsfm

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TicketsAdapter(private val list: List<Ticket>) : RecyclerView.Adapter<TicketViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return TicketViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        val ticket: Ticket = list[position]
        holder.bind(ticket)
    }

    override fun getItemCount(): Int = list.size

}