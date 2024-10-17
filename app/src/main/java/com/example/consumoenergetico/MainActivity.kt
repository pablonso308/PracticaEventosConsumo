package com.example.consumoenergetico

import RegistroManual
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.data.BarEntry
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

class MainActivity : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("consumos")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Appel à la méthode pour lire les données Firebase
        fetchDataFromFirebase()

        val greetingTextView: TextView = findViewById(R.id.greetingTextView)
        val greetingMessage = getGreetingMessage()
        greetingTextView.text = greetingMessage

        val buttonToScreen1: Button = findViewById(R.id.buttonToScreen1)
        buttonToScreen1.setOnClickListener {
            val intent = Intent(this, Grafica::class.java)
            startActivity(intent)
        }

        val buttonSimulateWeek: Button = findViewById(R.id.buttonSimulateWeek)
        buttonSimulateWeek.setOnClickListener {
            resetRealtimeDatabase()
            RegistroManual { success ->
                if (success) {
                    Log.d("MainActivity", "Envoi réussi")
                    fetchDataFromFirebase()
                } else {
                    Log.e("MainActivity", "Échec de l'envoi")
                }
            }.execute()
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
        val consumoData = ArrayList<BarEntry>()
        val dias = ArrayList<String>()

        val databaseReference = FirebaseDatabase.getInstance().getReference("consumo")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                consumoData.clear()
                dias.clear()

                var index = 0
                for (snapshot in dataSnapshot.children) {
                    val dia = snapshot.child("dia").getValue(String::class.java)
                    val cuarto = snapshot.child("cuarto").getValue(String::class.java)
                    val consumo = snapshot.child("consumo").getValue(Float::class.java)

                    if (dia != null && consumo != null) {
                        consumoData.add(BarEntry(index.toFloat(), consumo))
                        dias.add("$dia - $cuarto")
                        index++
                    }
                }
                mostrarGrafico(consumoData, dias)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("Firebase", "Échec de la lecture des données.", databaseError.toException())
            }
        })
    }

    private fun mostrarGrafico(consumoData: List<BarEntry>, dias: List<String>) {
    }
}

fun resetRealtimeDatabase() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.reference

    // Pour supprimer tous les nœuds sous la référence racine
    myRef.removeValue()
        .addOnSuccessListener {
            // Suppression réussie
            Log.d("DatabaseSuccess", "Database reset successfully.")
        }
        .addOnFailureListener { e ->
            // Gérer les erreurs
            Log.w("DatabaseError", "Error resetting database: ", e)
        }
}

data class Consumo(
    val dia: String = "",
    val id: String = "",
    val cuarto: String = "",
    val consumo: Double = 0.0
)
