package com.trgis.pdfreader

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class FunctionListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_function_list)

        //pdfOpen button click
        val pdfOpenButton = findViewById(R.id.pdfOpen) as Button
        pdfOpenButton.setOnClickListener {
            val intent = Intent(this, PdfReadActiviry::class.java)
            startActivity(intent)
        }

        //zipTest
        val zipTestButton = findViewById(R.id.zipTest) as Button
        zipTestButton.setOnClickListener {
            val intent = Intent(this, ZipTestActivity::class.java)
            startActivity(intent)
        }
    }
}