package ui.teams.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jetpack.demo.R
import data.response.Team
import di.GlideUtils


internal class TeamsAdapter (private var context: Context)
    : ListAdapter<Team, TeamsAdapter.TeamViewHolder>(DIFF_CALLBACK) {
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.team_item, parent, false)
        return TeamViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    internal inner class TeamViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        private var tvName: AppCompatTextView = view.findViewById(R.id.text_name)
        private var ivIcon : AppCompatImageView = view.findViewById(R.id.imv_team_icon)

        fun bind(item: Team) {
            tvName.text = "${item.name}"
            GlideUtils().loadImage(context, item.logo, ivIcon)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Team>() {

            override fun areItemsTheSame(
                oldItem: Team,
                newItem: Team
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Team,
                newItem: Team
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}