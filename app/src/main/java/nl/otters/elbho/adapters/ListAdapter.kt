package nl.otters.elbho.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_listitem.view.*
import nl.otters.elbho.R
import nl.otters.elbho.models.Request

class ListAdapter(
    private val context: Context,
    private val items: ArrayList<Request.Properties>,
    private val listener: OnClickItemListener
//    private val bottomReachedListener: OnBottomReachedListener
) : RecyclerView.Adapter<ListAdapter.ViewHolder>(){

    interface OnClickItemListener{
        fun onItemClick(position: Int, view: View)
    }
//    interface OnBottomReachedListener {
//        fun onBottomReached(position: Int)
//    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val dayView: TextView = itemView.listItem_dayTextView
        val dateView: TextView = itemView.listItem_dateTextView
        val titleView: TextView = itemView.listItem_titleTextView
        val descriptionView: TextView = itemView.lisItem_descriptionTextView
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_listitem, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.titleView.text = item.cocName
        holder.descriptionView.text = item.address
        holder.dateView.text = item.appointmentDateTime
        holder.dayView.text = item.appointmentDateTime

        holder.itemView.setOnClickListener{
            listener.onItemClick(holder.adapterPosition, it)
        }

//        if (position == itemCount - 1){
//            bottomReachedListener.onBottomReached(position)
//        }
    }
}

