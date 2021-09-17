package com.aryanjyotirmay.films

import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class NewsAdapter(private val news: NewsHeadlinesData): RecyclerView.Adapter<NewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news,parent,false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(news.articles[position])
    }

    override fun getItemCount(): Int {
        return if (news.totalResults<10) {
            news.totalResults
        } else {
            10
        }
    }
}

class NewsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val newsTitle: TextView = itemView.findViewById(R.id.news_title)
    private val newsBy: TextView = itemView.findViewById(R.id.news_by)
    private val newsImage: ImageView = itemView.findViewById(R.id.news_image)
    private val layoutNews: ConstraintLayout = itemView.findViewById(R.id.layout_news_parent)
    private val newsBar: ProgressBar = itemView.findViewById(R.id.news_bar)

    fun bind(news: Article) {
        newsBar.isVisible = true
        Glide.with(itemView.context).load(news.urlToImage).listener(object: RequestListener<Drawable>{
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                newsBar.isVisible = false
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                newsBar.isVisible = false
                return false
            }

        }).error(R.drawable.no_image_available).into(newsImage)

        newsBy.text = news.author
        newsTitle.text = news.title

        layoutNews.setOnClickListener {
            val newsIntent = Intent(itemView.context,WebViewActivity::class.java)
            newsIntent.putExtra("urlToArticle",news.url)
            itemView.context.startActivity(newsIntent)
        }
    }
}



