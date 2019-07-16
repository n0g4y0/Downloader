package com.nogas.downloader

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
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

        // Creando una Notificaion:
        makeNotification(url)

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

    private fun makeNotification(url:String){

        // creando una canal de NOTIFICACION, para las nuevas versiones de ANDROID ( >=8.0 ): (codigo base, copy-paste)
        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            val channel = NotificationChannel(
                id,"name",
                NotificationManager.IMPORTANCE_DEFAULT)
            val manager = getSystemService(NOTIFICATION_CHANNEL_ID)
                as NotificationManager
            manager.createNotificationChannel(channel)
            val builder = Notification.Builder(this,id)


        }
        */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_ID,
                NotificationManager.IMPORTANCE_DEFAULT)
            val manager = getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager
            manager.createNotificationChannel(channel)

            // ESTE ES EL CODIGO base para crear una notificacion

            val builder = Notification.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setContentTitle("Tu descarga esta completa")
                .setContentTitle(url)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.icon_download)

            // cuando el usuario hace click a la notificacion, lanza el activity: DOWNLOADERACTIVITY:

            val intent = Intent(this,DownloaderActivity::class.java)
            intent.action = "downloadcomplete"
            intent.putExtra("url",url)

            val pending = PendingIntent.getActivity(
                this,0,intent,0
            )
            builder.setContentIntent(pending)


            val notification = builder.build()

            manager.notify(NOTIFICATION_ID,notification)

        }
        else{

            // ESTE ES EL CODIGO base para crear una notificacion

            val builder = Notification.Builder(this)
                .setContentTitle("Tu descarga esta completa")
                .setContentTitle(url)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.icon_download)

            // cuando el usuario hace click a la notificacion, lanza el activity: DOWNLOADERACTIVITY:

            val intent = Intent(this,DownloaderActivity::class.java)
            intent.action = "downloadcomplete"
            intent.putExtra("url",url)

            val pending = PendingIntent.getActivity(
                this,0,intent,0
            )
            builder.setContentIntent(pending)




            val notification = builder.build()

            val manager = getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager
            manager.notify(NOTIFICATION_ID,notification)

        }


        /*
        * este codigo servia para versiones anterioes a android 8:
        *

        // ESTE ES EL CODIGO base para crear una notificacion

        val builder = Notification.Builder(this)
            .setContentTitle("Tu descarga esta completa")
            .setContentTitle(url)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.icon_download)

        // cuando el usuario hace click a la notificacion, lanza el activity: DOWNLOADERACTIVITY:

        val intent = Intent(this,DownloaderActivity::class.java)
        intent.action = "downloadcomplete"
        intent.putExtra("url",url)

        val pending = PendingIntent.getActivity(
            this,0,intent,0
        )
        builder.setContentIntent(pending)




        val notification = builder.build()

        val manager = getSystemService(Context.NOTIFICATION_SERVICE)
            as NotificationManager
        manager.notify(NOTIFICATION_ID,notification)

        * */

    }


    /*
    * Este metodo es usado para los servicios "bound" (enlazados)
    * nuestro servicio, no usa el modo BOUND, por eso retornamos null
    * */


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    /*
    * las variables que se necesitan para hacer las NOTIFICACIONES
    * */
    companion object {
        // ID code that is used to lunch the download notifications
        private const val NOTIFICATION_CHANNEL_ID = "nogayoDownloadService"
        private const val NOTIFICATION_ID = 1234
    }
}
