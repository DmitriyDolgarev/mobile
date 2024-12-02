package com.example.lr4_second

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.asLiveData
import com.example.lr4_second.db.ExpenseItem
import com.example.lr4_second.db.MainDB
import com.example.lr4_second.model.ExpenseModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.File
import java.io.FileInputStream

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        var btn = findViewById<Button>(R.id.button)
        var name = findViewById<EditText>(R.id.nameText)
        var sum = findViewById<EditText>(R.id.sumText)

        val intent: Intent = intent
        /*db
        var listText: String = ""
        var listFromDB: ArrayList<ExpenseModel> = ArrayList()
        val db = MainDB.getDB(this)
        db.getDao().getAllItems().asLiveData().observe(this)
        { itList ->
            listFromDB = ArrayList()
            itList.forEach{
                var expense: ExpenseModel = ExpenseModel(it.expenseName, it.expenseValue)
                listFromDB.add(expense)
            }
        }
         */

        /* получение через интент
        var list: ArrayList<ExpenseModel>? = intent.extras?.getParcelableArrayList<ExpenseModel>("list")
        if (list == null)
        {
            list = ArrayList<ExpenseModel>()
        }

         */

        /*
        val folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val fileXls = File(folder, "table.xlsx")

        var listFromXls: ArrayList<ExpenseModel>? = getListFromXls(fileXls)

         */

        var list: ArrayList<ExpenseModel> = ArrayList()

        val db = MainDB.getDB(this)
        db.getDao().getAllItems().asLiveData().observe(this)
        { itList ->
            list = ArrayList()
            itList.forEach{
                var expense: ExpenseModel = ExpenseModel(it.expenseName, it.expenseValue)
                list.add(expense)
            }
        }

        /*
        if (listFromXls != null)
        {
            list = listFromXls
        }
        else
        {
            list = ArrayList()
        }

         */

        btn.setOnClickListener {
            var text = makeText(name.text.toString(), sum.text.toString())
            var expense = ExpenseModel(name.text.toString(), sum.text.toString())

            /*db

             */
            val item = ExpenseItem(null, name.text.toString(), sum.text.toString())
            Thread{
                db.getDao().insertItem(item)
            }.start()

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
                R.id.expenses -> {
                    finish()
                    val intent: Intent = Intent(this, Expenses::class.java)
                    intent.putExtra("list", list)

                    startActivity(intent)
                    true
                }
                R.id.table -> {
                    finish()
                    startActivity(Intent(this, TableActivity::class.java))
                    true
                }
                R.id.camera -> {
                    finish()
                    startActivity(Intent(this, CameraActivity::class.java))
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

    private fun getListFromXls(myFile: File): ArrayList<ExpenseModel>?
    {
        var list: ArrayList<ExpenseModel> = ArrayList()

        return try {
            val fis = FileInputStream(myFile)
            val workbook = WorkbookFactory.create(fis)
            val sheet = workbook.getSheetAt(0) // Получаем первый лист

            for (row in sheet)
            {
                var expense: ExpenseModel = ExpenseModel(row.getCell(0).stringCellValue, row.getCell(1).stringCellValue)
                list.add(expense)
            }

            workbook.close()
            fis.close()
            list
        }
        catch (e: Exception)
        {
            e.printStackTrace()
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
            list
        }
    }
}