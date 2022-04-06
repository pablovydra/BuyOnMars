package com.example.buyonmars.ui.marslist.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.buyonmars.R
import com.example.buyonmars.models.dto.MarsProperty
import java.lang.reflect.Method

class MarsAdapter(
    private val marsList: List<MarsProperty>,
    val listener: MarsAdapterActions,
    val context: Context
) :
    RecyclerView.Adapter<MarsAdapter.MarsViewHolder>() {

    interface MarsAdapterActions {
        fun addToFavorite(id: Int)
    }

    class MarsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val photo: ImageView = itemView.findViewById(R.id.photo)
        val price: TextView = itemView.findViewById(R.id.price)
        val type: TextView = itemView.findViewById(R.id.type)
        val id: TextView = itemView.findViewById(R.id.id)
        val favorite: ImageView = itemView.findViewById(R.id.favorite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.mars_item,
            parent, false
        )
        return MarsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MarsViewHolder, position: Int) {
        val currentItem = marsList[position]

        holder.id.text = "# ${currentItem.id}"
        holder.price.text = "$ ${currentItem.price}"
        holder.type.text = currentItem.type

        val uri: String = currentItem.url

        Glide.with(holder.itemView.context)
            .load(uri)
            .into(holder.photo)

        holder.itemView.setOnClickListener {
//            listener.navigateToSelectedMole(currentItem.id)
        }

        when (currentItem.type) {
            "buy" -> holder.type.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.brand_primary))
            "rent" -> holder.type.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.brand_secondary))
        }

        holder.itemView.setOnLongClickListener(View.OnLongClickListener {
            showPopupMenu(context, it, currentItem, position)
            return@OnLongClickListener true
        })
    }

    override fun getItemCount() = marsList.size

    private fun showPopupMenu(context: Context, v: View, mars: MarsProperty, position: Int) {
        val popup = PopupMenu(context, v)

        popup.apply {
            menuInflater.inflate(R.menu.mars_item_options, menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.favorite -> {
                        listener.addToFavorite(mars.id.toInt())
                    }
                }
                false
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popup.setForceShowIcon(true)
        } else {
            try {
                val fields = popup.javaClass.declaredFields
                for (field in fields) {
                    if ("mPopup" == field.name) {
                        field.isAccessible = true
                        val menuPopupHelper = field[popup]
                        val classPopupHelper =
                            Class.forName(menuPopupHelper.javaClass.name)
                        val setForceIcons: Method = classPopupHelper.getMethod(
                            "setForceShowIcon",
                            Boolean::class.javaPrimitiveType
                        )
                        setForceIcons.invoke(menuPopupHelper, true)
                        break
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        popup.show()
    }
}


