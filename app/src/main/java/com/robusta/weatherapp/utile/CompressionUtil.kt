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
import android.widget.Toast
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.io.ByteStreams
import java.io.*

class CompressionUtil {
//    fun captureView( filename: String?,view :View):Uri {
//        //Find the view we are after
//        //Create a Bitmap with the same dimensions
//        val image = Bitmap.createBitmap(
//            view.width,
//            view.height,
//            Bitmap.Config.RGB_565
//        )
//        //Draw the view inside the Bitmap
//        view.draw(Canvas(image))
//
//        //Store to sdcard
//        try {
//            val path: String = Environment.getExternalStorageDirectory().toString()
//            val myFile = File(path, filename.toString())
//            val out = FileOutputStream(myFile)
//            image.compress(Bitmap.CompressFormat.PNG, 90, out) //Output
//            return Uri.fromFile(myFile)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return Uri.EMPTY
//    }

    fun getBitmapFromView(context: Context,view: View): String{
        //Define a bitmap with the same size as the view
        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        //Bind a canvas to it
        val canvas = Canvas(returnedBitmap)
        //Get the view's background
        val bgDrawable = view.background
        if (bgDrawable != null) //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas) else  //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE)
        // draw the view on the canvas
        view.draw(canvas)
        //return the bitmap
        return convertBitmapToUri(context,returnedBitmap)
    }


    fun convertBitmapToUri(context:Context , bitmap: Bitmap):String{
        val imageFileName = "JPEG_" + System.currentTimeMillis() + ".jpg"
        val file: File
        // fix creating File Directory bug in Android 10 (API 29)
        file = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + "/Weather")
        } else {
            File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/Weather")
        }
        //Get Access to a local file.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values: ContentValues = getContentValues()!!
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, imageFileName)
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Weather")
            values.put(MediaStore.Images.Media.IS_PENDING, true)
            val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            if (uri != null) {
                try {

                    saveImageToStream(bitmap, context.contentResolver.openOutputStream(uri))
                    values.put(MediaStore.Images.Media.IS_PENDING, false)
                    context.contentResolver.update(uri, values, null, null)


                    val parcelFileDescriptor = context.contentResolver.openFileDescriptor(uri, "r", null)
                    parcelFileDescriptor?.let {
                        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)

                        val file = File(context.cacheDir, imageFileName)
                        val outputStream = FileOutputStream(file)
                        ByteStreams.copy(inputStream, outputStream)
                       return file.path
                    }


                } catch (e: FileNotFoundException) {

                    e.printStackTrace()
                }
            }
        }

    return ""
    }

    private fun saveImageToStream(bitmap: Bitmap, fOut: OutputStream?) {
        if (fOut != null) {
            try {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, fOut)
                fOut.flush()
                fOut.close()
                Log.e("TagFileSaved", "ok " + fOut.toString())
                Log.e("TagONGmail", "handle save image bitstream " + " ")

            } catch (e: IOException) {

                e.printStackTrace()
            } finally {
                Log.e("TagONGmail", "handle save image bitstream finally" + " ")

            }
        }
    }

    private fun getContentValues(): ContentValues? {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
        return values
    }

}