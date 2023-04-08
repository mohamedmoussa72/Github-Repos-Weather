package com.robusta.weatherapp.utile

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.core.net.toUri
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.io.ByteStreams
import java.io.*

class CompressionUtil {

    fun getScreenShot(context: Context,view: View): Bitmap {
        val screenView = view
        screenView.isDrawingCacheEnabled = true
        val bitmap = Bitmap.createBitmap(screenView.drawingCache)
        screenView.isDrawingCacheEnabled = false
        return bitmap
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(
                inContext.contentResolver,
                inImage,
                "weather Photo",
                null
            )
        return Uri.parse(path)
    }

}