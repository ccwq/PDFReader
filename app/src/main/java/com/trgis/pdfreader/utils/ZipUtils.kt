package com.trgis.pdfreader.utils

import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

class ZipUtils {
    companion object {
        @Throws(IOException::class)
        fun unzip(inputStream: InputStream, outputDir: File) {
            val zipInputStream = ZipInputStream(inputStream)

            try {
                var entry: ZipEntry?
                val buffer = ByteArray(4096)
                while (zipInputStream.nextEntry.also { entry = it } != null) {
                    val file = File(outputDir, entry!!.name)

                    // 解压的对象是目录
                    if (entry!!.isDirectory) {
                        file.mkdirs()
                    }

                    // 解压的对象是文件
                    else {
                        var fileOutputStream: FileOutputStream? = null
                        var count: Int
                        try{
                            fileOutputStream = FileOutputStream(file)
                            while (zipInputStream.read(buffer).also { count = it } != -1) {
                                fileOutputStream.write(buffer, 0, count)
                            }
                        }catch (e:Exception){
                            e.printStackTrace()
                            Log.i("ZipUtils", "unzip: ${e.message}")
                        }finally {
                            fileOutputStream?.close()
                            zipInputStream.closeEntry()
                        }
                    }
                }
            } finally {
                zipInputStream.close()
            }
        }
    }
}
