package ui.matches.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.DialogTitle
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.work.*
import com.jetpack.demo.R
import data.remote.workers.ReminderWorker
import data.remote.workers.UpcomingWorker
import data.response.Match
import utils.constant.*
import java.util.concurrent.TimeUnit

internal class MatchesAdapter(
    private var context: Context,
    private var hightlightsClickListener: HightlightsClickListener,
    private var downloadClickListener: DownloadClickListener
) :
    ListAdapter<Match, MatchesAdapter.MatchViewHolder>(DIFF_CALLBACK) {
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.match_item, parent, false)
        return MatchViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    internal inner class MatchViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        private var tvDate: AppCompatTextView = view.findViewById(R.id.tv_date)
        private var tvDes: AppCompatTextView = view.findViewById(R.id.tv_des)
        private var tvWinner: AppCompatTextView = view.findViewById(R.id.tv_winner)
        private var btnHightlights: AppCompatButton = view.findViewById(R.id.btnViewHighlight)
        private var btnDownload: AppCompatButton = view.findViewById(R.id.btnDownload)
        private var btnSetReminder: AppCompatButton = view.findViewById(R.id.btnSetReminder)

        fun bind(item: Match) {
            tvDate.text = "${item.formatDate()}"
            tvDes.text = "${item.description}"
            if (item.winner != null) {
                tvWinner.text = "Winner : ${item.winner}"
                tvWinner.setTextColor(context.resources.getColor(R.color.colorAccent))
                btnHightlights.isVisible = true
                btnDownload.isVisible = true
                btnSetReminder.isVisible = false
                btnHightlights.setOnClickListener {
                    hightlightsClickListener.onHightLights(item.highlights.toString())
                }
                btnDownload.setOnClickListener {
                    val fileName = item.getDomainName()
                        ?.substringBeforeLast(".").toString() +"_"+ System.currentTimeMillis()
                    val filetype = item.getDomainName()
                        ?.substringAfterLast(".").toString()
                    val fileUrl = item.highlights.toString()
                    downloadClickListener.onDownloadClick(
                        fileName = fileName,
                        fileUrl = fileUrl,
                        fileType = filetype
                    )
                }
            } else {
                tvWinner.text = "---"
                tvWinner.setTextColor(context.resources.getColor(R.color.colorPrimary))
                btnHightlights.isVisible = false
                btnDownload.isVisible = false
                btnSetReminder.isVisible = true
                btnSetReminder.setOnClickListener {
                    notifyReminderMatch(
                        item.description.toString(),
                        item.formatDate().toString(),
                        REMINDER_TIME_IN_MILLIS.toLong()
                    )
                }
                item.toUpcomingTime()
                    ?.let { notifyUpcomingMatch("${item.description}", "${item.formatDate()}", it) }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Match>() {

            override fun areItemsTheSame(
                oldItem: Match,
                newItem: Match
            ): Boolean {
                return oldItem.description == newItem.description
            }

            override fun areContentsTheSame(
                oldItem: Match,
                newItem: Match
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    private fun notifyUpcomingMatch(
        title: String,
        content: String, milliseconds: Long
    ) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()
        val data = Data.Builder()

        data.apply {
            putString(KEY_INPUT_NOTIFY_UPCOMING_MATCH_TITLE, title)
            putString(KEY_INPUT_NOTIFY_UPCOMING_MATCH_MESSAGE, content)
            putLong(KEY_INPUT_NOTIFY_UPCOMING_MATCH_MILLS, milliseconds)
        }

        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(UpcomingWorker::class.java)
            .setConstraints(constraints)
            .setInputData(data.build())
            .setInitialDelay(milliseconds, TimeUnit.MILLISECONDS)
            .addTag(KEY_NOTIFY_UPCOMING_MATCH_TAG)
            .build()

        WorkManager.getInstance(context).enqueue(oneTimeWorkRequest)

    }

    private fun notifyReminderMatch(
        title: String,
        content: String, milliseconds: Long
    ) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()
        val data = Data.Builder()

        data.apply {
            putString(KEY_INPUT_NOTIFY_REMINDER_MATCH_TITLE, title)
            putString(KEY_INPUT_NOTIFY_REMINDER_MATCH_MESSAGE, content)
            putLong(KEY_INPUT_NOTIFY_REMINDER_MATCH_MILLS, milliseconds)
        }

        val repeatingWork = PeriodicWorkRequestBuilder<ReminderWorker>(
            milliseconds,
            TimeUnit.MILLISECONDS
        ).setConstraints(constraints)
            .setInputData(data.build())
            .addTag(KEY_NOTIFY_REMINDER_MATCH_TAG)
            .build()
        WorkManager.getInstance(context).enqueue(repeatingWork)

    }

    internal interface HightlightsClickListener {
        fun onHightLights(url: String)
    }

    internal interface DownloadClickListener {
        fun onDownloadClick(fileName: String, fileUrl: String, fileType: String)
    }

}