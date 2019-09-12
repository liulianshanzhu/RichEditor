package com.durian.richeditor

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.os.Environment
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


/**
 * @author Durian
 * @since 2019/9/11 15:02
 */
object FileUtil {

    fun saveFile(c: Context, fileName: String, bitmap: Bitmap): String {
        return saveFile(c, "", fileName, bitmap)
    }

    fun saveFile(c: Context, filePath: String, fileName: String, bitmap: Bitmap): String {
        val bytes = bitmapToBytes(bitmap)
        return saveFile(c, filePath, fileName, bytes)
    }

    fun bitmapToBytes(bm: Bitmap): ByteArray {
        val baos = ByteArrayOutputStream()
        bm.compress(CompressFormat.JPEG, 100, baos)
        return baos.toByteArray()
    }

    fun saveFile(c: Context, filePath: String?, fileName: String, bytes: ByteArray): String {
        var filePath = filePath
        var fileFullName = ""
        var fos: FileOutputStream? = null
        val dateFolder = SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA)
            .format(Date())
        try {
            val suffix = ""
            if (filePath == null || filePath.trim { it <= ' ' }.length == 0) {
                filePath = Environment.getExternalStorageDirectory().path + "/XiaoCao/" + dateFolder + "/"
            }
            val file = File(filePath)
            if (!file.exists()) {
                file.mkdirs()
            }
            val fullFile = File(filePath, fileName + suffix)
            fileFullName = fullFile.getPath()
            fos = FileOutputStream(File(filePath, fileName + suffix))
            fos!!.write(bytes)
        } catch (e: Exception) {
            fileFullName = ""
        } finally {
            if (fos != null) {
                try {
                    fos!!.close()
                } catch (e: IOException) {
                    fileFullName = ""
                }

            }
        }
        return fileFullName
    }

    fun getBitmapSize(url: String): LongArray {
        val op = BitmapFactory.Options()
        op.inJustDecodeBounds = true
        BitmapFactory.decodeFile(url, op)
        return longArrayOf(op.outWidth.toLong(), op.outHeight.toLong())
    }

}