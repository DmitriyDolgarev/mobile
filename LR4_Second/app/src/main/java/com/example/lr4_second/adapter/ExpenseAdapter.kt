package com.example.lr4_second.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.recyclerview.widget.RecyclerView
import com.example.lr4_second.R
import com.example.lr4_second.model.ExpenseModel

class ExpenseAdapter: RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    private var expensesList = emptyList<ExpenseModel>()

    class ExpenseViewHolder(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_expense_layout, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun getItemCount(): Int {
        return expensesList.size
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.expenseName).text = expensesList[position].name
        holder.itemView.findViewById<TextView>(R.id.expenseValue).text = expensesList[position].expenseValue
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<ExpenseModel>)
    {
        expensesList = list
        notifyDataSetChanged()
    }
}