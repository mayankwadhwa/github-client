package com.mayankwadhwa.github_client.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mayankwadhwa.github_client.R
import com.mayankwadhwa.github_client.model.RepoModel
import com.squareup.picasso.Picasso

class TrendingListAdapter(private val trendingList: List<RepoModel>) :
    RecyclerView.Adapter<TrendingListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_trending_list, parent, false)
        return TrendingListViewHolder(view)
    }

    override fun getItemCount() = trendingList.size

    override fun onBindViewHolder(holder: TrendingListViewHolder, position: Int) {
        val currentRepo = trendingList[position]
        Picasso.get().load(currentRepo.avatar).into(holder.avatarImageView)
        holder.authorTextView.text = currentRepo.author
        holder.repoTextView.text = currentRepo.name
        holder.starsTextView.text = currentRepo.stars.toString()
        holder.forksTextView.text = currentRepo.forks.toString()
        holder.descriptionTextView.text = currentRepo.description
    }


}

class TrendingListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var avatarImageView: ImageView = itemView.findViewById(R.id.iv_avatar)
    var authorTextView: TextView = itemView.findViewById(R.id.text_username)
    var repoTextView: TextView = itemView.findViewById(R.id.text_repo_name)
    var descriptionTextView: TextView = itemView.findViewById(R.id.text_description)
    var starsTextView: TextView = itemView.findViewById(R.id.text_stars)
    var forksTextView: TextView = itemView.findViewById(R.id.text_forks)
    var languageTextView: TextView = itemView.findViewById(R.id.text_language)
}
