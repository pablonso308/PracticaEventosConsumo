package com.example.consumoenergetico;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Grafica extends AppCompatActivity {
    private static final String TAG = "Grafica";
    private BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafica);

        // Referencia al gráfico en el layout
        barChart = findViewById(R.id.barChart);

        // Referencia a la base de datos de Firebase
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("consumo");

        // Leer los datos de la base de datos
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Procesar los datos de Firebase
                ArrayList<BarEntry> consumoData = new ArrayList<>();
                ArrayList<String> dias = new ArrayList<>();

                int index = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String dia = snapshot.child("dia").getValue(String.class);
                    Float consumo = snapshot.child("consumo").getValue(Float.class);

                    if (dia != null && consumo != null) {
                        consumoData.add(new BarEntry(index, consumo));
                        dias.add(dia);
                        index++;
                    }
                }

                // Configurar y mostrar el gráfico
                mostrarGrafico(consumoData, dias);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar errores
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }

    // Método para configurar y mostrar el gráfico
    private void mostrarGrafico(ArrayList<BarEntry> consumoData, ArrayList<String> dias) {
        BarDataSet barDataSet = new BarDataSet(consumoData, "Consumo por Día");
        BarData barData = new BarData(barDataSet);

        barChart.setData(barData);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(dias));
        barChart.getXAxis().setGranularity(1f); // Para que las etiquetas se alineen correctamente
        barChart.invalidate(); // Refrescar el gráfico
    }
}
