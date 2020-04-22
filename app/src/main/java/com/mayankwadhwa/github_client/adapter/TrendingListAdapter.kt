package com.mayankwadhwa.github_client.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.mayankwadhwa.github_client.R
import com.mayankwadhwa.github_client.model.RepoModel
import com.squareup.picasso.Picasso


class TrendingListAdapter(private val trendingList: List<RepoModel>) :
    RecyclerView.Adapter<TrendingListViewHolder>() {
    lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_trending_list, parent, false)
        context = parent.context
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
        if (!currentRepo.language.isNullOrEmpty()) {
            holder.languageTextView.text = currentRepo.language
            val drawable = ContextCompat.getDrawable(context, R.drawable.drawable_dot)
            val wrappedDrawable = DrawableCompat.wrap(drawable!!)
            DrawableCompat.setTint(wrappedDrawable, Color.parseColor(currentRepo.languageColor))
            holder.languageColorImageView.setImageDrawable(drawable)
        } else {
            holder.languageTextView.visibility = View.GONE
            holder.languageColorImageView.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            if (holder.layoutDescription.visibility == View.GONE)
                holder.layoutDescription.visibility = View.VISIBLE
            else
                holder.layoutDescription.visibility = View.GONE

            notifyItemChanged(position)
        }
    }
}

class TrendingListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val avatarImageView: ImageView = itemView.findViewById(R.id.iv_avatar)
    val authorTextView: TextView = itemView.findViewById(R.id.text_username)
    val repoTextView: TextView = itemView.findViewById(R.id.text_repo_name)
    val descriptionTextView: TextView = itemView.findViewById(R.id.text_description)
    val starsTextView: TextView = itemView.findViewById(R.id.text_stars)
    val forksTextView: TextView = itemView.findViewById(R.id.text_forks)
    val languageTextView: TextView = itemView.findViewById(R.id.text_language)
    val languageColorImageView: ImageView = itemView.findViewById(R.id.iv_language)
    val layoutDescription: View = itemView.findViewById(R.id.layout_description)
}

