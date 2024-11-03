package com.example.lr4_second

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.lr4_second.adapter.ExpenseAdapter
import com.example.lr4_second.model.ExpenseModel
import com.google.android.material.bottomnavigation.BottomNavigationView

import android.app.AlertDialog
import android.widget.EditText


class Expenses : AppCompatActivity() {

    lateinit var adapter: ExpenseAdapter
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_expenses)

        val intent: Intent = intent

        /*
        val bundle: Bundle? = intent.extras

        if (bundle != null)
        {
            var list: ArrayList<ExpenseModel> = ArrayList()
            list.add(ExpenseModel("" + bundle.getString("name"), "" + bundle.getString("sum")))

            initial(list)
        }
        */

        var list: ArrayList<ExpenseModel>? = intent.extras?.getParcelableArrayList<ExpenseModel>("list")

        if (list != null)
        {
            initial(list)
        }

        /*
        val bundle: Bundle? = intent.extras

        if (bundle != null)
        {
            text.text = "Расходы:\n" + bundle.getString("values")
        }
         */

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigationView.selectedItemId = R.id.expenses

        bottomNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId)
            {
                R.id.adding -> {
                    finish()

                    list = adapter.getList()

                    val intent: Intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("list", list)

                    startActivity(intent)
                    true
                }
                /*
                R.id.expenses -> {
                    finish()
                    startActivity(Intent(this, MainActivity::class.java))
                    //bottomNavigationView.selectedItemId = R.id.expenses
                    true
                }

                 */
                else -> false
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId)
        {
            100 -> {
                showUpdateDialog(item.groupId)
                true
            }
            101 -> {
                adapter.deleteItem(item.groupId)
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    // Метод для отображения диалога обновления
    private fun showUpdateDialog(position: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Обновить расход")

        val input = EditText(this)
        input.hint = "Название,сумма"

        builder.setView(input)

        builder.setPositiveButton("Обновить") { dialog, _ ->

            if (input.text.toString().isNotEmpty() && input.text.toString().contains(',') && input.text.toString().last() != ',') {

                var newExpenseName = input.text.toString().split(",")[0]
                var newExpenseValue = input.text.toString().split(",")[1]

                if (newExpenseValue.contains(' '))
                {
                    newExpenseValue = newExpenseValue.replace(" ", "")
                }

                if (newExpenseValue.all { it.isDigit() })
                {
                    if (newExpenseName.isNotEmpty() && newExpenseValue.isNotEmpty()) {
                        adapter.updateItem(position, newExpenseName, newExpenseValue)
                    }
                }
            }
            dialog.dismiss()
        }
        builder.setNegativeButton("Закрыть") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun initial(list: ArrayList<ExpenseModel>)
    {
        recyclerView = findViewById(R.id.rv_expenses)
        adapter = ExpenseAdapter()
        recyclerView.adapter = adapter

        adapter.setList(list)
    }

    private fun expensesToString(list: ArrayList<ExpenseModel>): String
    {
        var result: String = ""
        for (item: ExpenseModel in list)
        {
            result += item.expName + ": " + item.expValue + "; "
        }
        return result
    }

    private fun makeMyExpenses(): ArrayList<ExpenseModel>
    {
        var expensesList = ArrayList<ExpenseModel>()

        val firstExp = ExpenseModel("Магазин", "500")
        val secondExp = ExpenseModel("Кинотеатр", "800")
        val thirdExp = ExpenseModel("Кафе", "1500")

        expensesList.add(firstExp)
        expensesList.add(secondExp)
        expensesList.add(thirdExp)

        return expensesList
    }
}