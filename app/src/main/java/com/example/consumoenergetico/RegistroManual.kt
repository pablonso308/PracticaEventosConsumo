import android.os.AsyncTask
import com.example.consumoenergetico.Consumo
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

public class RegistroManual(private val callback: (Boolean) -> Unit) : AsyncTask<Void, Void, Boolean>() {

    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("consumos")

    override fun doInBackground(vararg params: Void?): Boolean {
        val pieces = listOf("Cocina", "Salón", "Baño")
        val calendar = Calendar.getInstance()
        calendar.time = SimpleDateFormat("yyyy-MM-dd").parse("2024-10-15") // Début de la semaine
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        var success = true
        //para una semana completa
        for (i in 0 until 7) {
            val currentDate = dateFormat.format(calendar.time)
            //hacer algo para cada cuarto de la casa
            for (piece in pieces) {
                val consumo = generateRandomConsumption()
                val newConsumo = Consumo(dia = currentDate, cuarto = piece, consumo = consumo)
                //enviar datos a firebase
                try {
                    myRef.push().setValue(newConsumo)

                } catch (e: Exception) {
                    success = false
                    break
                }
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        return success
    }

    private fun generateRandomConsumption(): Double {
        return (0..300).random() / 10.0 // Consumo entre 0.0 y 30.0 kWh
    }

    override fun onPostExecute(result: Boolean) {
        callback(result)
    }
}
