package com.example.consumoenergetico

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.firebase.database.*

class Grafica : AppCompatActivity() {

    private lateinit var barChart: BarChart
    private val TAG = "Grafica"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grafica)

        barChart = findViewById(R.id.barChart)

        // Botón para regresar a la pantalla principal
        val buttonToScreen1: Button = findViewById(R.id.buttonToScreen1)
        buttonToScreen1.setOnClickListener {
            val intent = Intent(this@Grafica, MainActivity::class.java)
            startActivity(intent)
        }

        // Obtener referencia de la base de datos "consumos"
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("consumos")

        // Escuchar cambios en la base de datos
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val consumoData = ArrayList<BarEntry>()
                val dias = ArrayList<String>()

                var index = 0
                for (snapshot in dataSnapshot.children) {
                    // Usar la clase Consumo para obtener los datos directamente
                    val consumoObj = snapshot.getValue(Consumo::class.java)

                    // Asegurarse de que los datos no sean nulos y asignarlos a las variables
                    if (consumoObj != null && consumoObj.dia != null && consumoObj.consumo != null) {
                        consumoData.add(BarEntry(index.toFloat(), consumoObj.consumo.toFloat()))
                        dias.add(consumoObj.dia)
                        index++
                    } else {
                        Log.e(TAG, "Datos nulos o incompletos para el objeto Consumo")
                    }
                }

                // Mostrar el gráfico solo si se recuperaron datos
                if (consumoData.isNotEmpty()) {
                    mostrarGrafico(consumoData, dias)
                } else {
                    Log.e(TAG, "No se encontraron datos de consumo para mostrar en el gráfico")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "Error al leer los datos de Firebase", databaseError.toException())
            }
        })
    }

    // Método para mostrar el gráfico
    private fun mostrarGrafico(consumoData: ArrayList<BarEntry>, dias: ArrayList<String>) {
        val barDataSet = BarDataSet(consumoData, "Consumo por Día")
        val barData = BarData(barDataSet)

        barChart.data = barData
        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(dias)  // Mostrar los días en el eje X
        barChart.xAxis.granularity = 1f  // Asegurar que se muestre cada día
        barChart.invalidate()  // Refrescar el gráfico
    }
}
