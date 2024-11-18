package com.example.lr4_second

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.MediaStore.Images
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.Objects


class CameraActivity : AppCompatActivity() {

    private val requestCodeTakePhoto = 1
    private lateinit var photoImageView: ImageView

    private var savedImageUri: Uri? = null
    private val SHARED_PREFS_NAME = "my_prefs"
    private val SAVED_IMAGE_URI_KEY = "saved_image_uri"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_camera)

        photoImageView = findViewById(R.id.photoImageView)

        var takePhotoBtn = findViewById<Button>(R.id.button2)
        takePhotoBtn.setOnClickListener{
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(takePictureIntent, requestCodeTakePhoto)
            }
        }

        // Загрузка URI из SharedPreferences
        val sharedPrefs = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        val savedUriString = sharedPrefs.getString(SAVED_IMAGE_URI_KEY, null)
        savedImageUri = savedUriString?.let { Uri.parse(it) }

        savedImageUri?.let { uri ->
            photoImageView.setImageURI(uri)
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
                R.id.table -> {
                    finish()
                    startActivity(Intent(this, TableActivity::class.java))
                    true
                }
                R.id.expenses -> {
                    finish()
                    val intent: Intent = Intent(this, Expenses::class.java)

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestCodeTakePhoto && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            photoImageView.setImageBitmap(imageBitmap)

            // Сохранение фотографии и обновление пути к файлу
            savePhoto(imageBitmap)
        }
    }

    private fun savePhoto(bitmap: Bitmap) {

        try {
            val values = ContentValues()

            values.put(MediaStore.MediaColumns.DISPLAY_NAME, "Image.jpg")
            values.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + "TestFolder")

            var imageUri: Uri? = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

            val sharedPrefs = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
            val editor = sharedPrefs.edit()
            editor.putString(SAVED_IMAGE_URI_KEY, imageUri.toString())
            editor.apply()

            if (imageUri != null)
            {
                val fos: OutputStream? = contentResolver.openOutputStream(imageUri)

                if (fos != null)
                {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)

                }

                Toast.makeText(this, "Фото сохранено: ", Toast.LENGTH_SHORT).show()
            }
            else
            {
                Toast.makeText(this, "Фото не сохранилось(((", Toast.LENGTH_SHORT).show()
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }
    }

}