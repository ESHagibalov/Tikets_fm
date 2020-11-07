package com.example.tiketsfm

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TicketViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.card_ticket, parent, false)) {
    private var nameView: TextView? = null
    private var placeView: TextView? = null
    private var dateView: TextView? = null
    private var costView: TextView? = null
    private var remainView: TextView? = null
    private var boughtView: TextView? = null

    init {
        nameView = itemView.findViewById(R.id.text_view_name)
        placeView = itemView.findViewById(R.id.text_view_place)
        dateView = itemView.findViewById(R.id.text_view_date_and_time)
        costView = itemView.findViewById(R.id.text_view_cost)
        remainView = itemView.findViewById(R.id.text_view_remain)
        boughtView = itemView.findViewById(R.id.text_view_bought)
    }

    fun bind(movie: Ticket) {
        nameView?.text = movie.name
        placeView?.text = movie.place
        dateView?.text = movie.date.toString()
        costView?.text = "Цена: " + movie.cost.toString()
        remainView?.text = "Осталось: " + movie.remain.toString() + " билетов"
        boughtView?.text = if(movie.bought) "КУПЛЕН" else "НЕ КУПЛЕН"
    }

}