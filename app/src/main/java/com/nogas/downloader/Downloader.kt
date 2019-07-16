package com.nogas.downloader

import android.os.Environment
import android.util.Log
import java.io.File

class Downloader{

    companion object {
        /** Default amount of time to pause during downloads to test long delays. */

        const val DEFAULT_DELAY = 3000
    }

    /**
     * Downloads the file found at the given URL
     * into the Android device's Downloads folder in its external storage.
     * and returns the file name it was saved to.
     *
     */

    fun download(urlString: String):String{
        return ""
    }
    /*
    * Pretends to Download the file found at the given URL.
    * and returns the file name it would have been seved to.
    * delays for the given number of ms before finishing.
    *
    * */


    fun downloaderFake(urlString:String, delayMS:Int = DEFAULT_DELAY):String{
        //Log.d("Downloader class","se descargo el archivo llamado: $url")
        val folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val urlFile = File(urlString)
        val filename = urlFile.name
        val outFile = File(folder,filename)
        Log.d("Downloader","downloading $urlString to $outFile")
        Thread.sleep(delayMS.toLong())
        return filename

    }




}