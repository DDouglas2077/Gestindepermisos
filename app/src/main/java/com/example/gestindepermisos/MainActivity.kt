package com.example.gestindepermisos

import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private val TAG = "Practica 04 - Permisos"
    private val CODIGO_SOLICITUD_GRABAR = 101
    private lateinit var estadoTextView: TextView
    private lateinit var btnIniciarGrabacion: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        estadoTextView = findViewById(R.id.estadoTextView)
        btnIniciarGrabacion = findViewById(R.id.btnIniciarGrabacion)

        configurarPermisos()

        btnIniciarGrabacion.setOnClickListener {
            if (tienePermiso()) {
                // Cambiar el estado a "Grabando"
                estadoTextView.text = "Grabando"
            } else {
                solicitarPermiso()
            }
        }
    }

    private fun tienePermiso(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun configurarPermisos() {
        val permiso = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.RECORD_AUDIO
        )

        if (permiso == PackageManager.PERMISSION_GRANTED) {
            // Si el permiso está concedido
            estadoTextView.text = "Permiso concedido"
            btnIniciarGrabacion.isEnabled = true
        } else {
            // Si el permiso no está concedido
            estadoTextView.text = "Esperando permiso"
            btnIniciarGrabacion.isEnabled = false
            solicitarPermiso()
        }
    }

    private fun solicitarPermiso() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.RECORD_AUDIO),
            CODIGO_SOLICITUD_GRABAR
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CODIGO_SOLICITUD_GRABAR) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                // Si el usuario deniega el permiso
                Log.i(TAG, "Permiso denegado por el usuario")
                estadoTextView.text = "Permiso denegado"
                btnIniciarGrabacion.isEnabled = false
            } else {
                // Si el usuario concede el permiso
                Log.i(TAG, "Permiso concedido por el usuario")
                estadoTextView.text = "Permiso concedido"
                btnIniciarGrabacion.isEnabled = true
            }
        }
    }
}
