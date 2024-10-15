package com.example.consumoenergetico

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Referencia al TextView donde aparecerá el saludo
        val greetingTextView: TextView = findViewById(R.id.greetingTextView)

        // Configura el saludo según la hora del día
        val greetingMessage = getGreetingMessage()
        greetingTextView.text = greetingMessage

        // Referencia al primer botón para navegar a la pantalla 1
        val buttonToScreen1: Button = findViewById(R.id.buttonToScreen1)
        buttonToScreen1.setOnClickListener {
            // Navega a la pantalla 1 (Screen1Activity)
            val intent = Intent(this, Grafica::class.java)
            startActivity(intent)
        }

        // Referencia al segundo botón para navegar a la pantalla 2
        val buttonToScreen2: Button = findViewById(R.id.buttonToScreen2)
        buttonToScreen2.setOnClickListener {
            // Navega a la pantalla 2 (Screen2Activity)
            val intent = Intent(this, RegistroManual::class.java)
            startActivity(intent)
        }
    }

    // Función para determinar el saludo según la hora
    private fun getGreetingMessage(): String {
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (currentHour) {
            in 5..11 -> "¡Buenos días!"
            in 12..17 -> "¡Buenas tardes!"
            in 18..21 -> "¡Buenas noches!"
            else -> "¡Hola!"
        }
    }
}
