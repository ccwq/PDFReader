package com.trgis.pdfreader

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.trgis.pdfreader.utils.ZipUtils
import java.io.File
import java.io.IOException
import java.io.InputStream

private const val REQUEST_CODE = 1

private const val AP_DATA_DIR_NAME = "sdata"

class ZipTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zip_test2)
        val btnRead = findViewById<Button>(R.id.button_choose_file)
        btnRead.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "application/zip"
            startActivityForResult(intent, REQUEST_CODE)
        }

        val btnPdfOpen = findViewById<Button>(R.id.button_pdf_open)
        btnPdfOpen.setOnClickListener {
            previewPdf()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            extractZip(data)
        }
    }

    private fun extractZip(data:Intent?){
        // Get the file URI from the intent
        val fileUri: Uri? = data?.data

        // Unzip the selected file to the app's internal storage
        try {
            val inputStream: InputStream? = fileUri?.let { contentResolver.openInputStream(it) }
            val outputDir: File = File(getExternalFilesDir(null), AP_DATA_DIR_NAME)

            //if no directory, create it
            if (!outputDir.exists()) {
                outputDir.mkdir()
            }
            if (inputStream!=null) {
                ZipUtils.unzip(inputStream, outputDir)
            }else{
                throw IOException("Unable to open file for reading")
            }
            // Display a success message

            Toast.makeText(this, "File unzipped successfully", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    private fun previewPdf() {

        val root = File(getExternalFilesDir(null), AP_DATA_DIR_NAME)
        val files:List<File> = listDirRecursive(root)

        // get first pdfFile
        val pdfFile = files.firstOrNull { it.extension == "pdf" }

        if (pdfFile == null) {
            Toast.makeText(this, "No pdf file found", Toast.LENGTH_SHORT).show()
            return
        }


        Log.i("PDF", "pdfFile: ${pdfFile.absolutePath}")
        // use filePrivoder to get file uri
        val fileUri = FileProvider.getUriForFile(this, "com.trgis.pdfreader.fileprovider", pdfFile)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(fileUri, "application/pdf")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        startActivity(intent)
    }


    //
    private fun listDirRecursive(dir: File): List<File> {
        val files = dir.listFiles()
        val result = mutableListOf<File>()
        for (file in files) {
            if (file.isDirectory) {
                result.addAll(listDirRecursive(file))
            } else {
                result.add(file)
            }
        }
        return result
    }
}