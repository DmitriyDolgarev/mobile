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

class Expenses : AppCompatActivity() {

    lateinit var adapter: ExpenseAdapter
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_expenses)

        var text = findViewById<TextView>(R.id.expensesList)
        text.setText("Расходы:\n")

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
        else
        {
            text.text = "не получило"
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
                    startActivity(Intent(this, MainActivity::class.java))
                    //bottomNavigationView.selectedItemId = R.id.adding
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

    /*
    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId)
        {
            100 -> {
                //showUpdateDialog(item.groupId)
                true
            }
            101 -> {
                true
            }
        }
    }

     */

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