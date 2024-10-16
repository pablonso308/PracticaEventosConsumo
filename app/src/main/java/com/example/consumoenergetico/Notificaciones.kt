package com.example.consumoenergetico

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.provider.Settings.Global.getString
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService

class Notificaciones(private val context: Context) {

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.channel_name)
            val descriptionText = context.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("Consumo", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun checkValuesAndNotify(value1: Int, value2: Int, value3: Int) {
        val thresholdCocina = 100
        val thresholdBano = 200
        val thresholdSalon = 300

        if (value1 > thresholdCocina) {
            val builder = NotificationCompat.Builder(context, "Consumo")
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Alto consumo !")
                .setContentText("i Consumo alto en la cocina, piense en apagar la cocina !")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build()
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(1, builder)
        }

        if (value2 > thresholdBano) {
            val builder = NotificationCompat.Builder(context, "Consumo")
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Alto consumo !")
                .setContentText("i Consumo alto en el baño, piense en apagar el secador de pelo !")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build()
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(1, builder)
        }

        if (value3 > thresholdSalon) {
            val builder = NotificationCompat.Builder(context, "Consumo")
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Alto consumo !")
                .setContentText("i Consumo alto en el salon, piense en apagar la television !")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build()
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(1, builder)
        }
    }
}