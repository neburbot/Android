@file:Suppress("UNUSED_PARAMETER", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.example.afinal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class DetailsActivity : AppCompatActivity() {
    private val dbHandler = DBHelper(this, null)
    lateinit var nombreEditText:EditText
    lateinit var telefonoEditText:EditText
    lateinit var direccionEditText:EditText
    lateinit var emailEditText:EditText
    lateinit var modifyId:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        nombreEditText = findViewById(R.id.nombre)
        telefonoEditText = findViewById(R.id.telefono)
        direccionEditText = findViewById(R.id.direccion)
        emailEditText = findViewById(R.id.email)

        /* Check  if activity opened from List Item Click */
        if(intent.hasExtra("id")) {
            getActionBar()?.setTitle("Modificar persona");
            getSupportActionBar()?.setTitle("Modificar persona");
            modifyId = intent.getStringExtra("id")
            nombreEditText.setText(intent.getStringExtra("nombre"))
            telefonoEditText.setText(intent.getStringExtra("telefono"))
            direccionEditText.setText(intent.getStringExtra("direccion"))
            emailEditText.setText(intent.getStringExtra("email"))
            findViewById<Button>(R.id.btnAdd).visibility = View.GONE
        }
        else {
            getActionBar()?.setTitle("Agregar persona");
            getSupportActionBar()?.setTitle("Agregar persona");
            findViewById<Button>(R.id.btnModificar).visibility = View.GONE
            findViewById<Button>(R.id.btnEliminar).visibility = View.GONE
        }

    }

    fun add(v:View) {
        val nombre = nombreEditText.text.toString()
        if (nombre.isNotEmpty()) {
            val telefono = telefonoEditText.text.toString()
            val direccion = direccionEditText.text.toString()
            val email = emailEditText.text.toString()
            if (telefono.isEmpty() && direccion.isEmpty() && email.isEmpty()) {
                Toast.makeText(this, "Ingrese al menos un dato de contacto", Toast.LENGTH_SHORT).show()
            }
            else {
                dbHandler.insertRow(nombre, telefono, direccion, email)
                Toast.makeText(this, "Persona agregada", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
        else {
            Toast.makeText(this, "Ingrese un nombre", Toast.LENGTH_SHORT).show()
        }
    }

    fun modificar(v:View) {
        val nombre = nombreEditText.text.toString()
        if (nombre.isNotEmpty()) {
            val telefono = telefonoEditText.text.toString()
            val direccion = direccionEditText.text.toString()
            val email = emailEditText.text.toString()
            if (telefono.isEmpty() && direccion.isEmpty() && email.isEmpty()) {
                Toast.makeText(this, "Ingrese al menos un dato de contacto", Toast.LENGTH_SHORT).show()
            }
            else {
                dbHandler.updateRow(modifyId, nombre, telefono, direccion, email)
                Toast.makeText(this, "Persona modificada", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
        else {
            Toast.makeText(this, "Ingrese un nombre", Toast.LENGTH_SHORT).show()
        }
    }

    fun eliminar(v:View) {
        dbHandler.deleteRow(modifyId)
        Toast.makeText(this, "Persona eliminada", Toast.LENGTH_SHORT).show()
        finish()
    }

    fun cancelar(v:View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}