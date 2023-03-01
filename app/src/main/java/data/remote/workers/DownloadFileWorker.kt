package data.remote.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues
import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.jetpack.demo.R
import utils.constant.*
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class DownloadFileWorker(
    private val context: Context,
    private val workerParameters: WorkerParameters
) : Worker(context, workerParameters) {
    private val notificationManager =
        applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override fun doWork(): Result {
        val mimeType = when (workerParameters.inputData.getString(KEY_INPUT_FILE_TYPE)) {
            "pdf" -> "application/pdf"
            else -> ""
        }
        val filename = workerParameters.inputData.getString(KEY_INPUT_FILE_NAME)
        val url = workerParameters.inputData.getString(KEY_INPUT_FILE_URL)
        displayNotification("$filename")
        url?.let {
            return try {
                val uri = downloadFileFromUri(url, mimeType, filename)
                notificationManager.cancel(context.resources.getInteger(R.integer.notificationID))
                val outPutData = Data.Builder()
                    .putString(KEY_OUTPUT_FILE_URI, uri.toString())
                    .build()
                Result.success(outPutData)
            } catch (e: Exception) {
                val outPutData = Data.Builder()
                    .putString(KEY_OUTPUT_MESSAGE, "Causing Exception : ${e.localizedMessage}")
                    .build()
                Result.failure(outPutData)
            }
        }

        return Result.failure()
    }


    private fun displayNotification(filename: String?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                context.getString(R.string.channel_id),
                context.getString(R.string.channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.enableVibration(false)
            notificationManager.createNotificationChannel(channel)
        }
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder =
            NotificationCompat.Builder(context, context.getString(R.string.channel_id))
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(context.getString(R.string.channel_name))
                .setContentText(String.format(context.getString(R.string.channel_desc), filename))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
        notificationManager . notify (context.resources.getInteger(R.integer.notificationID), notificationBuilder.build())
    }


    private fun downloadFileFromUri(url: String, mimeType: String, filename: String?): Uri? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }

            val resolver = context.contentResolver
            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
            return if (uri != null) {
                URL(url).openStream().use { input ->
                    resolver.openOutputStream(uri).use { output ->
                        input.copyTo(output!!, DEFAULT_BUFFER_SIZE)
                    }
                }
                uri
            } else {
                null
            }

        } else {

            val target = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                filename.toString()
            )
            URL(url).openStream().use { input ->
                FileOutputStream(target).use { output ->
                    input.copyTo(output)
                }
            }

            return target.toUri()
        }
    }

}
