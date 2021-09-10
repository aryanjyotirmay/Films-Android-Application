package com.example.films

import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class CastAdapter(private val casts: List<Cast>) : RecyclerView.Adapter<CastHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cast_item, parent, false)
        return CastHolder(view)
    }

    override fun onBindViewHolder(holder: CastHolder, position: Int) {
        return holder.bind(casts[position])
    }

    override fun getItemCount(): Int {

        return casts.size
    }


}

class CastHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val bar: ProgressBar = itemView.findViewById(R.id.cast_bar)
    private val actor: ImageView = itemView.findViewById(R.id.cast_image)
    private val name: TextView = itemView.findViewById(R.id.cast_name)
    private val layoutRefer: RelativeLayout = itemView.findViewById(R.id.cast_card)


    fun bind(cast: Cast) {
        bar.visibility = View.VISIBLE
        if (cast.profile_path == null) {

            val draw: Drawable? =
                ResourcesCompat.getDrawable(itemView.resources, R.drawable.images_2, null)
            Glide.with(itemView.context).load(draw).into(actor)
            bar.visibility = View.GONE

        }

        if (cast.profile_path != null) {
            Glide.with(itemView.context).load("https://image.tmdb.org/t/p/w500${cast.profile_path}")
                .listener(object :
                    RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        bar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        bar.visibility = View.GONE
                        return false
                    }
                }).into(actor)
        }

        if (cast.character.count() != 0) {
            name.text = "${cast.name} As ${cast.character}"
        }
        if (cast.character.count() == 0) {
            name.text = cast.name
        }

        layoutRefer.setOnClickListener {
            val castIntent = Intent(itemView.context, ActorActivity::class.java)
            castIntent.putExtra("ActorId", cast.id)
            itemView.context.startActivity(castIntent)


        }


    }


}