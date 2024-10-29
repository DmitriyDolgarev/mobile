package com.example.lr4_second.adapter

import android.view.ContextMenu
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ExpensesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener {
    var currentPosition: Int = 0

    init {
        itemView.setOnCreateContextMenuListener(this)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menu?.add(adapterPosition, 100, 0, "Обновить")
        menu?.add(adapterPosition, 101, 1, "Удалить")
        currentPosition = adapterPosition
    }
}