package data.remote.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.jetpack.demo.R
import utils.constant.*

class UpcomingWorker(
    private val context: Context,
    private val workerParameters: WorkerParameters
) : Worker(context, workerParameters) {

    private val notificationManager =
        applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override fun doWork(): Result {

        val title = workerParameters.inputData.getString(KEY_INPUT_NOTIFY_UPCOMING_MATCH_TITLE)
        val content = workerParameters.inputData.getString(KEY_INPUT_NOTIFY_UPCOMING_MATCH_MESSAGE)
        val millisecond = workerParameters.inputData.getLong(KEY_INPUT_NOTIFY_UPCOMING_MATCH_MILLS, UPCOMING_TIME_IN_MILLIS_DEFAULT) //1 hour
        try {
            displayNotification(title, content, millisecond)
            notificationManager.cancel(context.resources.getInteger(R.integer.notificationID))
            val outPutData = Data.Builder()
                .putString(KEY_OUTPUT_NOTIFY_UPCOMING_MATCH_MESSAGE, content)
                .build()
            Result.success(outPutData)
        } catch (e: Exception) {
            val outPutData = Data.Builder()
                .putString(
                    KEY_OUTPUT_NOTIFY_UPCOMING_MATCH_MESSAGE,
                    "Causing Exception : ${e.localizedMessage}"
                )
                .build()
            Result.failure(outPutData)
        }

        return Result.failure()
    }

    private fun displayNotification(title: String?, content: String?, milliseconds: Long?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                context.getString(R.string.channel_upcoming_id),
                context.getString(R.string.channel_upcoming_match),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.enableVibration(false)
            notificationManager.createNotificationChannel(channel)
        }
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder =
            NotificationCompat.Builder(context, context.getString(R.string.channel_upcoming_id))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)

        notificationManager.notify(
            context.resources.getInteger(R.integer.notification_upcoming_id),
            notificationBuilder.build()
        )
    }
}
