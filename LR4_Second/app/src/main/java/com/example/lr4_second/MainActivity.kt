package com.example.lr4_second

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.lr4_second.model.ExpenseModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        var btn = findViewById<Button>(R.id.button)
        var name = findViewById<EditText>(R.id.nameText)
        var sum = findViewById<EditText>(R.id.sumText)
        var values: String = ""

        var nameStr: String = ""
        var sumStr: String = ""

        var list: ArrayList<ExpenseModel> = ArrayList<ExpenseModel>()

        btn.setOnClickListener {
            var text = makeText(name.text.toString(), sum.text.toString())

            //values = name.text.toString() + ", " + sum.text
            //nameStr = name.text.toString()
            //sumStr = sum.text.toString()
            var expense = ExpenseModel(name.text.toString(), sum.text.toString())
            list.add(expense)

            name.setText("")
            sum.setText("")

            Toast.makeText(this, text, Toast.LENGTH_LONG).show()
        }

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigationView.selectedItemId = R.id.adding

        bottomNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId)
            {
                /*
                R.id.adding -> {
                    finish()
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                */
                R.id.expenses -> {
                    finish()
                    val intent: Intent = Intent(this, Expenses::class.java)
                    //intent.putExtra("values", values)
                    intent.putExtra("list", list)
                    //intent.putExtra("name", nameStr)
                    //intent.putExtra("sum", sumStr)


                    startActivity(intent)
                    true
                }
                else -> false
            }

        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun makeText(name: String, sum: String): String
    {
        var text = ""
        var sumArr = sum.toCharArray()

        if ((name == "") || (sum == ""))
        {
            text = "Заполните поля ввода!"
        }
        else
        {
            if (sumArr[0] == '-')
            {
                text = "Сумма должна быть положительной!"
            }
            else
            {
                text = "Добавлено: " + name + ", " + sum
            }
        }
        return text
    }
}