package com.drawer.platform.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

object FileHelper {

    fun copyImageFromUri(context: Context, uri: Uri, dirName: String = Constants.IMAGES_DIR): String? {
        return try {
            val dir = File(context.filesDir, dirName)
            if (!dir.exists()) dir.mkdirs()
            val destFile = File(dir, "IMG_${System.currentTimeMillis()}.jpg")
            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(destFile).use { output -> input.copyTo(output) }
            }
            destFile.absolutePath
        } catch (e: Exception) {
            e.printStackTrace(); null
        }
    }

    fun copyVideoFromUri(context: Context, uri: Uri): String? {
        return try {
            val dir = File(context.filesDir, Constants.VIDEOS_DIR)
            if (!dir.exists()) dir.mkdirs()
            val destFile = File(dir, "VID_${System.currentTimeMillis()}.mp4")
            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(destFile).use { output -> input.copyTo(output) }
            }
            destFile.absolutePath
        } catch (e: Exception) {
            e.printStackTrace(); null
        }
    }

    fun createTempImageFile(context: Context): File {
        val dir = File(context.filesDir, "temp")
        if (!dir.exists()) dir.mkdirs()
        return File(dir, "TEMP_${System.currentTimeMillis()}.jpg")
    }

    fun parseImagePaths(imagePaths: String): List<String> =
        if (imagePaths.isBlank()) emptyList()
        else imagePaths.split(",").filter { it.isNotBlank() }

    fun joinImagePaths(paths: List<String>): String = paths.joinToString(",")

    fun getFirstImagePath(imagePaths: String): String? =
        parseImagePaths(imagePaths).firstOrNull()
}
