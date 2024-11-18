package com.example.lr4_second

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.lr4_second.adapter.ExpenseAdapter
import com.example.lr4_second.model.ExpenseModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader


class Expenses : AppCompatActivity() {

    lateinit var adapter: ExpenseAdapter
    lateinit var recyclerView: RecyclerView
    private val EXTERNAL_STORAGE_PERMISSION_CODE = 23

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_expenses)

        var makeXlsxBtn = findViewById<Button>(R.id.makeXlsx)

        val intent: Intent = intent

        var list: ArrayList<ExpenseModel>? = intent.extras?.getParcelableArrayList<ExpenseModel>("list")
        var listStr: String = ""

        if (list != null)
        {
            initial(list)
            listStr = listToString(list)
        }
        else
        {
            val folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val fileXls = File(folder, "table.xlsx")
            list = getListFromXls(fileXls)

            if (list == null)
            {
                list = ArrayList()
            }
        }

        makeXlsxBtn.setOnClickListener{
            // Проверяем наличие разрешения
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // Запрашиваем разрешение у пользователя
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), EXTERNAL_STORAGE_PERMISSION_CODE)
            } else {

                //запись в бинарный файл
                val folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val file = File(folder, "binary.txt")

                writeTextData(file, listStr)

                //запись в xls файл
                val folderXls = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val fileXls = File(folderXls, "table.xlsx")

                writeListToXlsx(fileXls, list)
                Toast.makeText(this, "Список сохранен!", Toast.LENGTH_SHORT).show()
            }
        }

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

    private fun writeTextData(file: File, data: String) {
        var fileOutputStream: FileOutputStream? = null
        try {
            fileOutputStream = FileOutputStream(file)
            fileOutputStream.write(data.toByteArray())
            //Toast.makeText(this, "Файл сохранен: " + file.absolutePath, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            //Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun writeListToXlsx(file: File, list: ArrayList<ExpenseModel>?)
    {
        if (list != null) {
            var fileOutputStream: FileOutputStream? = null
            try {
                fileOutputStream = FileOutputStream(file)

                val workbook = HSSFWorkbook()
                val sheet = workbook.createSheet("Расходы")

                // Запись данных из массива
                for ((rowIndex, expense) in list.withIndex()) {
                    val row = sheet.createRow(rowIndex)

                    row.createCell(0).setCellValue(expense.name)
                    row.createCell(1).setCellValue(expense.expenseValue)
                }

                workbook.write(fileOutputStream)

                fileOutputStream.close()
                workbook.close()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
            }
        }
        else
        {
            Toast.makeText(this, "Список пуст!!!", Toast.LENGTH_SHORT).show()
        }
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


    //*******************************************************************************


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