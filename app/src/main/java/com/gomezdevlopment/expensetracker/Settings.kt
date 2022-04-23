package com.gomezdevlopment.expensetracker

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.github.dhaval2404.imagepicker.ImagePicker
import com.gomezdevlopment.expensetracker.MainActivity.Companion.currency
import com.gomezdevlopment.expensetracker.database.ViewModel
import com.gomezdevlopment.expensetracker.databinding.SettingsBinding
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream


class Settings : AppCompatActivity() {

    private lateinit var binding: SettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (File(filesDir, "profileImage.png").exists()) {
            val bitmap = BitmapFactory.decodeFile(File(filesDir, "profileImage.png").toString())
            binding.profileImage.setImageBitmap(bitmap)
        }


        binding.export.setOnClickListener {
            exportCSV()
        }

        binding.homeArrow.setOnClickListener {
            onBackPressed()
        }

        binding.changeNameButton.setOnClickListener {
            changeUsername(this)
        }

        val startForProfileImageResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                val resultCode = result.resultCode
                val data = result.data

                when (resultCode) {
                    Activity.RESULT_OK -> {
                        //Image Uri will not be null for RESULT_OK
                        val fileUri = data?.data!!

                        val bitmap = getContactBitmapFromURI(this, fileUri)
                        if (bitmap != null) {
                            saveImageToInternalStorage(bitmap)
                        }
                        binding.profileImage.setImageURI(fileUri)
                    }
                    ImagePicker.RESULT_ERROR -> {
                        Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        binding.profileImage.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .cropSquare()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
        }

        val preferences = getSharedPreferences("preferences", MODE_PRIVATE)

        val currencies = resources.getStringArray(R.array.currencies)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, currencies)
        binding.currencyDropdownField.setAdapter(arrayAdapter)

        binding.currencyDropdownField.setOnItemClickListener { _, _, i, _ ->
            when (i) {
                0 -> currency = "$"
                1 -> currency = "£"
                2 -> currency = "€"
                3 -> currency = "¥"
                else -> println("Nothing")
            }
            preferences.edit().putString("currency", currency).apply()
        }

        val themes = arrayListOf("Light", "Dark", "System")
        val themeArrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, themes)
        binding.themeDropdownField.setAdapter(themeArrayAdapter)

        binding.themeDropdownField.setOnItemClickListener { _, _, i, _ ->
            when (i) {
                0 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    preferences.edit().putInt("theme", 0).apply()
                }
                1 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    preferences.edit().putInt("theme", 1).apply()
                }
                2 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    preferences.edit().putInt("theme", 2).apply()
                }
                else -> println("Nothing")
            }
            finish()
        }
    }

    private fun exportCSV() {
        val userViewModel = ViewModelProvider(this)[ViewModel::class.java]
        val filename = "TrackMyExpensesData.csv"
        val path = getExternalFilesDir(null)
        val csvFile = File(path, filename)
        csvFile.delete()
        csvFile.createNewFile()
        csvFile.appendText("Title,Amount,Type,Date\n")

        userViewModel.userEntries.observe(this) {
            for (entry in it) {
                csvFile.appendText("${entry.title},${entry.value},${entry.entryType},${entry.date}\n")
            }
            val uri = FileProvider.getUriForFile(
                this,
                applicationContext.packageName.toString() + ".provider",
                csvFile
            )
            val sendIntent = Intent(Intent.ACTION_SEND)
            sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            sendIntent.putExtra(Intent.EXTRA_STREAM, uri)
            sendIntent.type = "text/csv"
            startActivity(Intent.createChooser(sendIntent, "SHARE"))
        }
    }

    private fun getContactBitmapFromURI(context: Context, uri: Uri?): Bitmap? {
        try {
            val input = context.contentResolver.openInputStream(uri!!) ?: return null
            return BitmapFactory.decodeStream(input)
        } catch (e: FileNotFoundException) {
        }
        return null
    }

    private fun saveImageToInternalStorage(image: Bitmap): Boolean {
        return try {
            val fos: FileOutputStream = openFileOutput("profileImage.png", MODE_PRIVATE)

            image.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.close()
            true
        } catch (e: Exception) {
            Log.e("saveToInternalStorage()", e.toString())
            false
        }
    }

    private fun changeUsername(context: Context) {
        val dialog = Dialog(context, R.style.AlertDialog)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.change_username_dialog)
        dialog.show()

        val name: EditText = dialog.findViewById(R.id.nameEditText)
        val submitButton: Button = dialog.findViewById(R.id.submitButton)
        val cancelButton: Button = dialog.findViewById(R.id.cancelButton)

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        fun isLetters(string: String): Boolean {
            return string.all { it.isLetter() }
        }

        fun checkUserName(name: String) {
            if (name.isNotEmpty()) {
                if (isLetters(name)) {
                    val userName = name
                    val preferences = getSharedPreferences("preferences", MODE_PRIVATE)
                    preferences.edit().putString("username", userName).apply()
                    dialog.dismiss()
                    Toast.makeText(context, "Username Changed Successfully", Toast.LENGTH_LONG)
                        .show()
                } else {
                    Toast.makeText(
                        context,
                        "Please enter alphabetic characters only. No spaces.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                Toast.makeText(
                    context,
                    "Please enter a name, it can be a nickname or any username you prefer.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        submitButton.setOnClickListener {
            val submittedName = name.text.toString()
            checkUserName(submittedName)
        }
    }
}