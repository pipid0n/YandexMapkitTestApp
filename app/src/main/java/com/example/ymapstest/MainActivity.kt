package com.example.ymapstest

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private var launcher: ActivityResultLauncher<Intent>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textView: TextView = findViewById(R.id.tv_message)
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val latitude = result.data?.getDoubleExtra("latitude", 0.0).toString()
                val longitude = result.data?.getDoubleExtra("longitude", 0.0).toString()
                textView.text = "Address ${latitude.dropLast(7)}, ${longitude.dropLast(7)}"
            }
        }

    }

    fun onClickToMap(view: View) {
        launcher?.launch(Intent(this, MapActivity::class.java))
    }

}