package com.example.ymapstest

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PointF
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.map.*
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.image.ImageProvider

class MapActivity : AppCompatActivity(),  InputListener, UserLocationObjectListener{

    private lateinit var mapView: MapView
    private val mapAPI = "e932415b-c715-45ad-86c6-ba2b35c46db0"
    private var userInput: Point = Point(0.0, 0.0)
    private lateinit var locationMapKit : UserLocationLayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey(mapAPI)
        MapKitFactory.initialize(this)
        setContentView(R.layout.activity_map)

        mapView = findViewById(R.id.mapview)

        requestLocationPermission()
        val mapKit = MapKitFactory.getInstance()
        mapKit.resetLocationManagerToDefault()

        locationMapKit = mapKit.createUserLocationLayer(mapView.mapWindow)
        locationMapKit.isVisible = true
        locationMapKit.isHeadingEnabled = false
        locationMapKit.setObjectListener(this)

        mapView.map.addInputListener(this)
        val position = CameraPosition(mapView.map.cameraPosition.target, 14f, 0f ,0f)
        mapView.map.move(position)

    }

    fun onClick(view: View) {
        if (userInput.latitude == Point(0.0, 0.0).latitude) {
            Toast.makeText(this, "You didnt place the point", Toast.LENGTH_LONG).show()
        } else {
            val intent = Intent()
            intent.putExtra("latitude", userInput.latitude)
            intent.putExtra("longitude", userInput.longitude)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 0
            )
        }
        return
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onMapTap(map: Map, point: Point) {
        mapView.map.mapObjects.clear()
        mapView.map.mapObjects.addPlacemark(point, ImageProvider.fromResource(this, R.drawable.ic_pinuser))
        userInput = point
    }


    override fun onMapLongTap(p0: Map, p1: Point) {
    }

    override fun onObjectAdded(userLocationView: UserLocationView) {
        locationMapKit.setAnchor(
            PointF(
                (mapView.width * 0.5).toFloat(),
                (mapView.height * 0.5).toFloat()
            ), PointF((mapView.width * 0.5).toFloat(), (mapView.height * 0.83).toFloat())
        )

        userLocationView.arrow.setIcon(
            ImageProvider.fromResource(
                this, R.drawable.ic_clear
            )
        )

        val pinIcon = userLocationView.pin.useCompositeIcon()

        pinIcon.setIcon(
            "pin",
            ImageProvider.fromResource(this, R.drawable.ic_clear),
            IconStyle().setAnchor(PointF(0.5f, 0.5f))
                .setRotationType(RotationType.ROTATE)
                .setZIndex(1f)
                .setScale(0.5f)
        )
        userLocationView.accuracyCircle.fillColor = Color.BLUE and -0x0
    }

    override fun onObjectRemoved(p0: UserLocationView) {
    }

    override fun onObjectUpdated(p0: UserLocationView, p1: ObjectEvent) {
    }
}