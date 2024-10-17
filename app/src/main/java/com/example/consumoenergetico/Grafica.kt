package com.example.consumoenergetico;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

        barChart = findViewById(R.id.barChart);

        Button buttonToScreen1 = findViewById(R.id.buttonToScreen1);

        buttonToScreen1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Grafica.this, MainActivity.class);
                startActivity(intent);
            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("consumo");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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
                mostrarGrafico(consumoData, dias);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Échec de la lecture de la valeur.", databaseError.toException());
            }
        });
    }
    
    private void mostrarGrafico(ArrayList<BarEntry> consumoData, ArrayList<String> dias) {
        BarDataSet barDataSet = new BarDataSet(consumoData, "Consumo por Día");
        BarData barData = new BarData(barDataSet);

        barChart.setData(barData);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(dias));
        barChart.getXAxis().setGranularity(1f);
        barChart.invalidate();
    }
}
