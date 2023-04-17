package com.trgis.pdfreader

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File


class PdfReadActiviry : AppCompatActivity() {

    private val REQUEST_CODE = 1 // 请求授权的请求码

    override fun onCreate(savedInstanceState: Bundle?) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val builder = VmPolicy.Builder()
            StrictMode.setVmPolicy(builder.build())
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_read)

        val openButton = findViewById(R.id.openButton) as Button

        openButton.setOnClickListener {
            openPdfFile()

            // 检查是否已经授权
            //if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            //    != PackageManager.PERMISSION_GRANTED) {
            //    // 请求授权
            //    ActivityCompat.requestPermissions(
            //        this,
            //        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            //        REQUEST_CODE
            //    )
            //} else {
            //    // 已经授权，打开PDF文件
            //    openPdfFile()
            //}
        }
    }

    // 接收授权结果
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE -> {
                // 如果授权被允许，则打开PDF文件
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openPdfFile()
                } else {
                    Toast.makeText(this, "未授权无法打开文件", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    // 打开PDF文件
    private fun openPdfFile() {
        val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "foo.pdf")
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(Uri.fromFile(file), "application/pdf")
        intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
        startActivity(intent)
    }
}