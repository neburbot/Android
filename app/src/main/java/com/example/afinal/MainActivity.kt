@file:Suppress("UNUSED_PARAMETER")

package com.example.afinal

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val dbHandler = DBHelper(this, null)
    var dataList = ArrayList<HashMap<String, String>>()
    var order = 0
    private var notificationManager: NotificationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getActionBar()?.setTitle("Agenda");
        getSupportActionBar()?.setTitle("Agenda");

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(
            "com.example.notification.news",
            "Notification News",
            "Example News Channel"
        )

        buscar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                loadIntoList()
            }
        })
    }

    private fun createNotificationChannel(id: String, name: String, description: String) {
        val importance = NotificationManager.IMPORTANCE_LOW
        val channel = NotificationChannel(id, name, importance)

        channel.description = description
        channel.enableLights(true)
        channel.lightColor = Color.RED
        channel.enableVibration(true)
        channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
        notificationManager?.createNotificationChannel(channel)
    }

    fun loadIntoList() {
        dataList.clear()
        val cursor = when (order) {
            1 -> dbHandler.getAllRowDesc(buscar.text.toString())
            2 -> dbHandler.getAllRowAsc(buscar.text.toString())
            else -> dbHandler.getAllRow(buscar.text.toString())
        }
        cursor!!.moveToFirst()

        while (!cursor.isAfterLast) {
            val map = HashMap<String, String>()
            map["id"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ID))
            map["nombre"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NOMBRE))
            map["telefono"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TEL))
            map["direccion"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DIR))
            map["email"] = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_EMAIL))
            dataList.add(map)

            cursor.moveToNext()
        }
        findViewById<ListView>(R.id.listView).adapter = CustomAdapter(this@MainActivity, dataList)
        findViewById<ListView>(R.id.listView).setOnItemClickListener { _, _, i, _ ->
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra("id", dataList[+i]["id"])
            intent.putExtra("nombre", dataList[+i]["nombre"])
            intent.putExtra("telefono", dataList[+i]["telefono"])
            intent.putExtra("direccion", dataList[+i]["direccion"])
            intent.putExtra("email", dataList[+i]["email"])
            startActivity(intent)
        }
    }

    fun fabClicked(v:View) {
        val intent = Intent(this, DetailsActivity::class.java)
        startActivity(intent)
    }

    fun descOrder(v:View) {
        order = 1
        loadIntoList()
    }

    fun ascOrder(v:View) {
        order = 2
        loadIntoList()
    }

    fun clearOrder(v:View) {
        order = 0
        loadIntoList()
    }

    fun aiuda(v:View) {
        val notificationID = 99
        val openURL = Intent(android.content.Intent.ACTION_VIEW)
        openURL.data = Uri.parse("https://i.pinimg.com/originals/f9/df/9c/f9df9c62a90e954231766c7e831df629.png")

        val pendingIntent = PendingIntent.getActivity(
            this, 0, openURL, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val channelID = "com.example.notification.news"

        val icon: Icon = Icon.createWithResource(this, android.R.drawable.ic_dialog_alert)

        val action: Notification.Action = Notification.Action.Builder(icon, "Open", pendingIntent).build()

        val notification = Notification.Builder(this@MainActivity, channelID)
            .setContentTitle("AIUDA")
            .setContentText("Todos necesitamos ayuda.")
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setChannelId(channelID)
            .setContentIntent(pendingIntent)
            .setActions(action)
            .setLargeIcon(icon)
            .build()

        notificationManager?.notify(notificationID, notification)
    }

    public override fun onResume() {
        super.onResume()
        loadIntoList()
    }
}