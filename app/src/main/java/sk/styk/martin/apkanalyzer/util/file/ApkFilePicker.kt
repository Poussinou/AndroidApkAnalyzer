package sk.styk.martin.apkanalyzer.util.file

import android.content.Intent
import android.net.Uri
import sk.styk.martin.apkanalyzer.ApkAnalyzer.Companion.context
import java.io.File

/**
 * @author Martin Styk
 * @version 09.10.2017.
 */

object ApkFilePicker {

    const val REQUEST_PICK_APK = 1324

    val filePickerIntent: Intent
        get() {
            val mediaIntent = Intent(Intent.ACTION_GET_CONTENT)
            mediaIntent.type = "application/vnd.android.package-archive"
            return mediaIntent
        }

    fun getPathFromIntentData(apkUri: Uri?): String? {
        if (apkUri == null)
            return null

        val fileFromPath = File(apkUri.path)
        if (fileFromPath.exists())
            return fileFromPath.absolutePath

        val pathFromRealPathUtils = RealPathUtils.getPathFromUri(context, apkUri)
        if (pathFromRealPathUtils != null) {
            try {
                val fileFromRealPathUtils = File(pathFromRealPathUtils)
                if (fileFromRealPathUtils.exists())
                    return fileFromRealPathUtils.absolutePath

            } catch (e: Exception) {
                // sometimes new File call might throw an exception
                //we failed, return anything
                return apkUri.toString()
            }

        }
        //we failed, return anything
        return apkUri.toString()
    }


}
