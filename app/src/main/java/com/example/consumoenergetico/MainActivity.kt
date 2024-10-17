package com.example.consumoenergetico

import RegistroManual
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("consumos")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fetchDataFromFirebase()

        val greetingTextView: TextView = findViewById(R.id.greetingTextView)
        val greetingMessage = getGreetingMessage()
        greetingTextView.text = greetingMessage

        val buttonToScreen1: Button = findViewById(R.id.buttonToScreen1)
        buttonToScreen1.setOnClickListener {
            val intent = Intent(this, Grafica::class.java)
            startActivity(intent)
        }

        // Boton para simular una semana
        val buttonSimulateWeek: Button = findViewById(R.id.buttonSimulateWeek)
        buttonSimulateWeek.setOnClickListener {
            RegistroManual { success ->
                if (success) {
                    Log.d("MainActivity", "Exitoso")
                    fetchDataFromFirebase() // Récupérer les données après l'envoi
                } else {
                    Log.e("MainActivity", "Fracaso")
                }
            }.execute() // Exécuter la tâche asynchrone
        }
    }

    private fun getGreetingMessage(): String {
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (currentHour) {
            in 5..11 -> "¡Buenos días!"
            in 12..17 -> "¡Buenas tardes!"
            in 18..21 -> "¡Buenas noches!"
            else -> "¡Hola!"
        }
    }

    private fun fetchDataFromFirebase() {
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val consumo = postSnapshot.getValue(Consumo::class.java)
                    Log.d("MainActivity", consumo.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("MainActivity", "Failed to read value.", error.toException())
            }
        })
    }
}


public data class Consumo(
    val dia: String = "",
    val cuarto: String = "",
    val consumo: Double = 0.0
)