package com.example.appdetectordecolores

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.hardware.camera2.*
import android.os.Bundle
import android.view.Surface
import android.view.TextureView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class ColorDetectionActivity : AppCompatActivity() {

    private lateinit var vistaCamara: TextureView
    private lateinit var textoColorDetectado: TextView
    private lateinit var btnCapturarColor: Button
    private lateinit var btnRegresarMenu: Button
    private lateinit var previsualizacionColor: android.view.View

    private var camaraDevice: CameraDevice? = null
    private var captureSession: CameraCaptureSession? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_detection)

        vistaCamara = findViewById(R.id.vista_camara)
        textoColorDetectado = findViewById(R.id.texto_color_detectado)
        btnCapturarColor = findViewById(R.id.btn_capturar_color)
        btnRegresarMenu = findViewById(R.id.btn_regresar_menu)
        previsualizacionColor = findViewById(R.id.previsualizacion_color)

        if (!checkCameraPermission()) {
            requestCameraPermission()
        } else {
            inicializarCamara()
        }

        btnCapturarColor.setOnClickListener {
            val bitmap = vistaCamara.bitmap ?: return@setOnClickListener
            val color = capturarColor(bitmap)
            textoColorDetectado.text = "Color detectado: ${colorToHex(color)}"
            previsualizacionColor.setBackgroundColor(color)
            HistorialColores.agregarColor(color)
        }

        btnRegresarMenu.setOnClickListener {
            camaraDevice?.close() // Cerramos la cámara antes de regresar
            startActivity(Intent(this, MainActivity::class.java))
            finish() // Finalizamos esta actividad para no dejarla en el stack
        }
    }

    private fun checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 100)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            inicializarCamara()
        } else {
            Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun inicializarCamara() {
        val cameraManager = getSystemService(CameraManager::class.java)
        try {
            val cameraId = cameraManager.cameraIdList[0]
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) return
            cameraManager.openCamera(cameraId, object : CameraDevice.StateCallback() {
                override fun onOpened(camera: CameraDevice) {
                    camaraDevice = camera
                    iniciarVistaCamara()
                }

                override fun onDisconnected(camera: CameraDevice) {
                    camaraDevice?.close()
                }

                override fun onError(camera: CameraDevice, error: Int) {
                    camaraDevice?.close()
                    Toast.makeText(this@ColorDetectionActivity, "Error al abrir la cámara", Toast.LENGTH_SHORT).show()
                }
            }, null)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun iniciarVistaCamara() {
        val surfaceTexture = vistaCamara.surfaceTexture ?: return
        surfaceTexture.setDefaultBufferSize(640, 480)
        val surface = Surface(surfaceTexture)
        camaraDevice?.createCaptureSession(listOf(surface), object : CameraCaptureSession.StateCallback() {
            override fun onConfigured(session: CameraCaptureSession) {
                captureSession = session
                val request = camaraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)?.apply {
                    addTarget(surface)
                }
                captureSession?.setRepeatingRequest(request?.build()!!, null, null)
            }

            override fun onConfigureFailed(session: CameraCaptureSession) {
                Toast.makeText(this@ColorDetectionActivity, "Error al configurar la cámara", Toast.LENGTH_SHORT).show()
            }
        }, null)
    }

    private fun capturarColor(bitmap: Bitmap): Int {
        val x = bitmap.width / 2
        val y = bitmap.height / 2
        return bitmap.getPixel(x, y)
    }

    private fun colorToHex(color: Int): String {
        return String.format("#%06X", 0xFFFFFF and color)
    }

    override fun onDestroy() {
        super.onDestroy()
        camaraDevice?.close()
        captureSession?.close()
    }
}



