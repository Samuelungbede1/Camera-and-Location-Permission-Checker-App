package com.example.permissioncheckerapp

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    val CAMERA_RQ = 100
    val LOCATION_RQ = 111
    private lateinit var locationButton : Button
    private lateinit var cameraButton : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        locationButton = findViewById(R.id.btn_location)
        cameraButton = findViewById(R.id.btn_camera)
        buttonTap()
    }



private fun buttonTap(){
    locationButton.setOnClickListener{
        checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, "Location", LOCATION_RQ)
    }
    cameraButton.setOnClickListener{
        checkPermission(android.Manifest.permission.CAMERA, "Camera", CAMERA_RQ)

    }
}

    private fun checkPermission( permission: String, name: String, requestCode: Int){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            when{
                ContextCompat.checkSelfPermission(applicationContext,permission)==PackageManager.PERMISSION_GRANTED-> {
                    Toast.makeText(applicationContext, "$name permission granted", Toast.LENGTH_SHORT).show()
                }
                shouldShowRequestPermissionRationale(permission)-> showDialog(permission,name,requestCode)
                else -> ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        fun innerCheck(name: String){
    if (grantResults.isEmpty()|| grantResults[0]!=PackageManager.PERMISSION_GRANTED){
        Toast.makeText(applicationContext, "$name permission refused", Toast.LENGTH_SHORT).show()
    } else{
        Toast.makeText(applicationContext, "$name permission granted", Toast.LENGTH_SHORT).show()
    }
}
      when(requestCode){
          LOCATION_RQ-> innerCheck("Location")
          CAMERA_RQ-> innerCheck("Camera")
      }
    }

    private fun showDialog(permission: String, name: String, requestCode: Int){
val builder = AlertDialog.Builder(this)
        builder.apply {
            setMessage("permission to access your $name is required to use this App")
            setTitle("permission required")
            setPositiveButton("OK") {dialog, which ->
                ActivityCompat.requestPermissions( this@MainActivity, arrayOf(permission), requestCode)
            }
        }

        builder.create().show()
    }
}