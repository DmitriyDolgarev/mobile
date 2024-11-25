package com.example.lr4_second.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.recyclerview.widget.RecyclerView
import com.example.lr4_second.R
import com.example.lr4_second.db.MainDB
import com.example.lr4_second.model.ExpenseModel

class ExpenseAdapter: RecyclerView.Adapter<ExpensesViewHolder>() {

    private var expensesList = ArrayList<ExpenseModel>()

    class ExpenseViewHolder(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpensesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_expense_layout, parent, false)
        return ExpensesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return expensesList.size
    }

    override fun onBindViewHolder(holder: ExpensesViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.expenseName).text = expensesList[position].name
        holder.itemView.findViewById<TextView>(R.id.expenseValue).text = expensesList[position].expenseValue
    }

    fun updateItem(position: Int, newName: String, newValue: String, context: Context)
    {
        val db = MainDB.getDB(context)
        var name = expensesList.get(position).expName
        Thread{
            db.getDao().updateItem(name, newName, newValue)
        }.start()

        expensesList[position].name = newName
        expensesList[position].expenseValue = newValue
        notifyItemChanged(position)
    }

    fun deleteItem(position: Int, context: Context)
    {
        val db = MainDB.getDB(context)
        var name = expensesList.get(position).expName
        Thread{
            db.getDao().deleteItemByName(name)
        }.start()

        expensesList.removeAt(position)

        notifyItemRemoved(position)
    }

    fun getList(): ArrayList<ExpenseModel>
    {
        return expensesList
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: ArrayList<ExpenseModel>)
    {
        expensesList = list
        notifyDataSetChanged()
    }
}