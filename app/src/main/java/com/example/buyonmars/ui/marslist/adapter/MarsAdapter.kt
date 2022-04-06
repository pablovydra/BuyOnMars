package com.example.buyonmars.ui.marslist.adapter

import android.animation.Animator
import android.content.Context
import android.os.Build
import android.view.*
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.example.buyonmars.R
import com.example.buyonmars.base.favorites.Favorite
import com.example.buyonmars.models.dto.MarsProperty
import java.lang.reflect.Method

class MarsAdapter(
    private val marsList: List<MarsProperty>,
    private val favorites: MutableList<Favorite>,
    val listener: MarsAdapterActions,
    val context: Context
) :
    RecyclerView.Adapter<MarsAdapter.MarsViewHolder>() {

    interface MarsAdapterActions {
        fun addToFavorite(mars: MarsProperty, callback: (() -> Unit)? = null)
        fun removeFavorite(mars: MarsProperty, callback: (() -> Unit)? = null)
        fun navigatToProperty(id: Int)
    }

    class MarsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val photo: ImageView = itemView.findViewById(R.id.photo)
        val price: TextView = itemView.findViewById(R.id.price)
        val type: TextView = itemView.findViewById(R.id.type)
        val id: TextView = itemView.findViewById(R.id.id)
        val favorite: ImageView = itemView.findViewById(R.id.favorite)
        var lottieLove: LottieAnimationView = itemView.findViewById<View>(R.id.lottie_love) as LottieAnimationView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.mars_item,
            parent, false
        )
        return MarsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MarsViewHolder, position: Int) {
        with(holder) {
            val currentItem = marsList[position]

            holder.price.text = currentItem.price.toString()
            holder.type.text = currentItem.type

            var isFavorite = favorites.filter { it.marsId == currentItem.id }

            val uri: String = currentItem.url

            Glide.with(holder.itemView.context)
                .load(uri)
                .into(holder.photo)

            holder.itemView.setOnClickListener {
                listener.navigatToProperty(currentItem.id.toInt())
            }

            holder.lottieLove.visibility = View.INVISIBLE

            if (isFavorite.isNotEmpty()) {
                holder.favorite.setImageResource(R.drawable.ic_love)
                holder.favorite.tag = R.drawable.ic_love
            } else {
                holder.favorite.setImageResource(R.drawable.ic_love_unmark)
                holder.favorite.tag = R.drawable.ic_love_unmark
            }

            val gd = GestureDetector(itemView.context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onDoubleTap(e: MotionEvent): Boolean {
                    if (holder.favorite.tag == R.drawable.ic_love_unmark) {
                        holder.favorite.setImageResource(R.drawable.ic_love)
                        holder.favorite.tag = R.drawable.ic_love
                        listener.addToFavorite(currentItem) {
                            holder.lottieLove.speed = 1F
                            showLottieAnimation(holder, "new_love.json")
                        }
                        favorites.add(Favorite(currentItem.id, currentItem.url, currentItem.type, currentItem.price))
                    } else {
                        holder.favorite.setImageResource(R.drawable.ic_love_unmark)
                        holder.favorite.tag = R.drawable.ic_love_unmark
                        listener.removeFavorite(currentItem) {
                            holder.lottieLove.speed = -2F
                            showLottieAnimation(holder, "new_love.json")
                        }
                        favorites.remove(Favorite(currentItem.id, currentItem.url, currentItem.type, currentItem.price))
                    }
                    return true
                }
            })

            holder.itemView.setOnTouchListener { _, event -> gd.onTouchEvent(event) }

            holder.favorite.setOnClickListener {
                if (holder.favorite.tag == R.drawable.ic_love_unmark) {
                    holder.favorite.setImageResource(R.drawable.ic_love)
                    holder.favorite.tag = R.drawable.ic_love
                    listener.addToFavorite(currentItem) {
                        holder.lottieLove.speed = 1F
                        showLottieAnimation(this, "new_love.json")
                    }
                    favorites.add(Favorite(currentItem.id, currentItem.url, currentItem.type, currentItem.price))
                } else {
                    holder.favorite.setImageResource(R.drawable.ic_love_unmark)
                    holder.favorite.tag = R.drawable.ic_love_unmark
                    listener.removeFavorite(currentItem) {
                        holder.lottieLove.speed = -2F
                        showLottieAnimation(this, "new_love.json")
                    }
                    favorites.remove(Favorite(currentItem.id, currentItem.url, currentItem.type, currentItem.price))
                }
            }

            when (currentItem.type) {
                "buy" -> holder.type.setBackgroundColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.brand_primary
                    )
                )
                "rent" -> holder.type.setBackgroundColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.brand_secondary
                    )
                )
            }

            holder.itemView.setOnLongClickListener(View.OnLongClickListener {
                showPopupMenu(context, it, currentItem, position)
                return@OnLongClickListener true
            })
        }
    }

    override fun getItemCount() = marsList.size

    private fun showLottieAnimation(marsViewHolder: MarsViewHolder, animationUrl: String) {
        marsViewHolder.lottieLove.apply {
            setAnimation(animationUrl)
            if (!isAnimating) {

                marsViewHolder.lottieLove.visibility = View.VISIBLE

                addAnimatorListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator?) {}
                    override fun onAnimationEnd(animation: Animator?) {
                        marsViewHolder.lottieLove.visibility = View.INVISIBLE
                    }

                    override fun onAnimationCancel(animation: Animator?) {}
                    override fun onAnimationRepeat(animation: Animator?) {}
                })
                playAnimation()
            }
        }
    }

    private fun showPopupMenu(context: Context, v: View, mars: MarsProperty, position: Int) {
        val popup = PopupMenu(context, v)

        popup.apply {
            menuInflater.inflate(R.menu.mars_item_options, menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.favorite -> {
                        listener.addToFavorite(mars)
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


