package com.example.films

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ms.square.android.expandabletextview.ExpandableTextView

class ReviewAdapter(private val reviews: List<revResult>) :
    RecyclerView.Adapter<ReviewViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReviewViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.review_item, parent, false)

        return ReviewViewHolder(view)

    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        return holder.bind(reviews[position])
    }

    override fun getItemCount(): Int {
        return reviews.size
    }
}


class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val imageR: ImageView = itemView.findViewById(R.id.review_image)
    private val textR: ExpandableTextView = itemView.findViewById(R.id.expand_text_view)
    private val reviewBy: TextView = itemView.findViewById(R.id.reviewer)

    fun bind(cast: revResult) {
        textR.setText(cast.content)
        reviewBy.text = "Review By ${cast.author} on ${cast.created_at.take(10)}"

//        if (cast.author_details.avatar_path==null){
//
        //       val draw: Drawable?= ResourcesCompat.getDrawable(itemView.resources,R.drawable.images_2,null)
        //      Glide.with(itemView.context).load(draw).into(imageR)
//        }
        //      if(cast.author_details.avatar_path.startsWith("/")) {}

        Glide.with(itemView.context)
            .load("https://image.tmdb.org/t/p/w500${cast.author_details.avatar_path}")
            .error(R.drawable.images_2)
            .into(imageR)

//        if (cast.author_details.avatar_path.startsWith("www")){
//            Glide.with(itemView.context).load(cast.author_details.avatar_path).into(imageR)
//        }
    }
}