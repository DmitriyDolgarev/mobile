package com.example.lr4_second

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.lr4_second.model.ExpenseModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class TableActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_table)

        var text = findViewById<TextView>(R.id.textView3)

        val folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(folder, "binary.txt")

        val data: String? = getdata(file)
        if (data != null) {
            text.text = "Данные из бинарного файла: " + data
        } else {
            text.text = "Файл пустой((("
        }

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigationView.selectedItemId = R.id.expenses

        bottomNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId)
            {
                R.id.adding -> {
                    finish()

                    val intent: Intent = Intent(this, MainActivity::class.java)

                    startActivity(intent)
                    true
                }
                R.id.expenses -> {
                    finish()
                    startActivity(Intent(this, Expenses::class.java))
                    //bottomNavigationView.selectedItemId = R.id.expenses
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

    private fun getdata(myfile: File): String? {
        var fileInputStream: FileInputStream? = null
        try {
            fileInputStream = FileInputStream(myfile)
            var i = -1
            val buffer = StringBuffer()
            while (fileInputStream.read().also { i = it } != -1) {
                buffer.append(i.toChar())
            }
            return buffer.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return null
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

    private fun listToString(list: ArrayList<ExpenseModel>): String
    {
        var result = ""

        for (expense: ExpenseModel in list)
        {
            result += expense.name + ": " + expense.expenseValue + ", "
        }
        return result
    }
}