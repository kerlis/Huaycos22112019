package com.example.huaycos
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Camera
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import android.hardware.Camera.CameraInfo
import android.hardware.Camera.getCameraInfo
import android.hardware.Camera.getNumberOfCameras

import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Environment
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    val REQUEST_IMAGE_CAPTURE = 1

    lateinit var camera: Button

    lateinit var imagen: ImageView

      lateinit var context: Context


//    android.hardware.Camera.open(int cameraId)


    var cameraId: Int = 0
    lateinit var currentPhotoPath: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        camera =  findViewById(R.id.video_camara)

        imagen =  findViewById(R.id.imagen)

        camera.setOnClickListener(View.OnClickListener {
          dispatchTakePictureIntent()
            //getCameraInstance()
        })

        findFrontFacingCamera()

     checkCameraHardware(applicationContext)
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {

                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data!!.extras.get("data") as Bitmap
            imagen.setImageBitmap(imageBitmap)
        }
    }


    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }






    private fun checkCameraHardware(context: Context): Boolean {
        if (context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {

            Toast.makeText(applicationContext, "si", Toast.LENGTH_LONG)
            Log.d("cameracheck", "si")

            var f : Int =  android.hardware.Camera.getNumberOfCameras()
            Log.d("NUMERO", f.toString())


             // this device has a camera
            return true
        } else {
            Toast.makeText(applicationContext, "no", Toast.LENGTH_LONG)
            Log.d("cameracheck", "si")

            // no camera on this device
            return false
        }
    }



    /** A safe way to get an instance of the Camera object. */


     fun getCameraInstance(): android.hardware.Camera? {
        var c: android.hardware.Camera? = null
        try {
            c = android.hardware.Camera.open(android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT)

            Log.d("LLAMADO", c.toString())
        } catch (e: Exception) {
        }

        return c
        Log.d("INSTANCIA", c.toString())
    }

    /*
    fun getCameraInstance(): android.hardware.Camera?  {


        return try {
           /// android.hardware.Camera.open(1)
        android.hardware.Camera.open(3)


            // android.hardware.Camera.getCameraInfo(1,)


            // attempt to get a Camera instance
        } catch (e: Exception) {
            // Camera is not available (in use or does not exist)
           null   // returns null if camera is unavailable
        }


    }
*/

    private fun findFrontFacingCamera(): Int {

        // Search for the front facing camera
        val numberOfCameras = android.hardware.Camera.getNumberOfCameras()
        for (i in 0 until numberOfCameras) {
            val info = CameraInfo()
            android.hardware.Camera.getCameraInfo(i, info)
            if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i
                var cameraFront = true
                break
            }
        }
        return cameraId
    }





}