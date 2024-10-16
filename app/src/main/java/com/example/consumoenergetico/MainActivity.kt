package com.example.consumoenergetico

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
import com.google.firebase.firestore.auth.User
import java.util.*

class MainActivity : AppCompatActivity() {

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

        val buttonToScreen2: Button = findViewById(R.id.buttonToScreen2)
        buttonToScreen2.setOnClickListener {
            val intent = Intent(this, RegistroManual::class.java)
            startActivity(intent)
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

    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("users")

    private fun fetchDataFromFirebase() {
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val user = postSnapshot.getValue(User::class.java)
                    Log.d("MainActivity", user.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("MainActivity", "Failed to read value.", error.toException())
            }
        })
    }
}
