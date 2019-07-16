package com.nogas.downloader

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.koushikdutta.ion.Ion
import kotlinx.android.synthetic.main.activity_downloader.*
import java.io.File

class DownloaderActivity : AppCompatActivity() {
    private var files:  List<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_downloader)

        // asegurate que tenemos permisos para guardar archivos:
        //askPermission()

        /** escuchando por el BROADCAST de que las descargas, han terminado:*/
        //si o si debemos crear este objeto:
        val filter = IntentFilter()
        filter.addAction("downloadcomplete")
        registerReceiver(DownloadReceiver(),filter)

        //escuchar por clicks en el LIST VIEW:
        list_of_links.setOnItemClickListener{ _,_,index,_ ->
            val filename = files[index]
            val folder = File(the_url.text.toString()).parent
            downloadUrl("$folder/$filename")
        }
    }




    /*
    * Este metodo es llamado, cuando el boton "GO" es clickeado.
    * Descarga la pagina dada.
    * */

    fun getListclick(view:View){
        val webPageUrl = the_url.text.toString()
        Log.d("jhoss", webPageUrl)

        // Descarga la lista de nombres de los archivos, y los pone dentro un LISTVIEW:
        Ion.with(this)
            .load(webPageUrl)
            .asString()
            .setCallback { _, text ->
                files = text.split("\n")
                list_of_links.adapter = ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1,files.toTypedArray())
            }

    }
    /*
    Descargar el archivo, desde la URL dada
    * */

    private fun downloadUrl(url:String){

        val fake = fakebox.isChecked
        val delayMS = Integer.valueOf(delay.text.toString())
        Toast.makeText(this,"Descargando $url ...",Toast.LENGTH_SHORT).show()
        Log.d("DownloaderActivity", "Iniciando descarga del $url ...")

        //TODO descargar usando un servicio

        val intent = Intent(this,DownloadService::class.java)
        intent.action = "download"
        intent.putExtra("url",url)
        startService(intent)

        //        Thread.sleep(5000)
        // Log.d("DownloaderActivity","el activity sigue AQUI!!")

    }



    private inner class DownloadReceiver:BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            // manipular el mensaje recibido del BROADCAST

            val url = intent?.getStringExtra("url")
            Log.d("DownloaderReceiver","this URL is done: $url")
            Toast.makeText(this@DownloaderActivity,"this URL is done $url",Toast.LENGTH_SHORT).show()
        }
    }


}
