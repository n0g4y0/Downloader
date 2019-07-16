package com.nogas.downloader

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class DownloadService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if (intent !=null){
            if (intent.action == "download"){

                val url = intent.getStringExtra("url")
                Log.d("DownloadService","ellos quieren que descargue desde esta URL: $url")

                val thread = Thread{
                    doWork(url)
                }
                thread.start()

            }else if (intent.action == "bark"){
                Log.d("DownloadService","ruff")
            }

         //   Thread.sleep(5000)
         //   Log.d("DownloaderService","el SERVICE sigue AQUI!!")
        }

        return Service.START_STICKY
    }

    private fun doWork(url:String){

        Downloader().downloaderFake(url)

        // si..!! ya descarga ya termino:
        val doneIntent = Intent()
        doneIntent.action = "downloadcomplete"
        doneIntent.putExtra("url",url)
        //se envia el BRODASCAST
        sendBroadcast(doneIntent)

        /*
        * AQUI es donde realmente finaliza la descarga, por eso, to-do este codigo,debe ir aqui adentro, del hilo, que es una FUNCION LAMBDA.
        * */

    }


    /*
    * Este metodo es usado para los servicios "bound" (enlazados)
    * nuestro servicio, no usa el modo BOUND, por eso retornamos null
    * */


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
