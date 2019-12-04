package nl.otters.elbho.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import nl.otters.elbho.R

class ListAdapter(val context: Context, val items: ArrayList<Article>, val listener: OnClickItemListener, val bottomReachedListener: OnBottomReachedListener) : RecyclerView.Adapter<ListAdapter.ViewHolder>(){
    interface OnClickItemListener{
        fun onItemClick(position: Int, view: View)
    }
    interface OnBottomReachedListener {
        fun onBottomReached(position: Int)
    }
    interface OnBookmarkClickListener {
        fun onBookmarkClick(position: Int)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
//        val titleView: TextView = itemView.fragment_listitem
//        val imageView: ImageView = itemView.component_list_item_image_imageView
//        val bookmarkButton: ImageButton = itemView.component_list_item_bookmark_button
//        val dayView: TextView =
//        val dateView: TextView =
//        val titleView: TextView =
//        val descriptionView: TextView =

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.component_list_item_article, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        Glide.with(context)
            .load(item.Image)
            .placeholder(R.drawable.img_placeholder_background)
            .transform(RoundedCorners(10))
            .transition(DrawableTransitionOptions.withCrossFade())
            .error(R.drawable.img_placeholder_background)
            .into(holder.imageView)
        holder.titleView.text = item.Title
        holder.itemView.setOnClickListener{
            listener.onItemClick(holder.adapterPosition, it)
        }
        holder.bookmarkButton.setOnClickListener{
            bookmarkClickListener.onBookmarkClick(holder.adapterPosition)
        }
        if(item.IsLiked) { holder.bookmarkButton.setBackgroundResource(R.drawable.ic_bookmark_black_24dp) }
        else{ holder.bookmarkButton.setBackgroundResource(R.drawable.ic_bookmark_border_black_24dp)}

        if (position == itemCount - 1){
            bottomReachedListener.onBottomReached(position)
        }
    }
}

