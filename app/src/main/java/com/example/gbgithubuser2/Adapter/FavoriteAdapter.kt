package com.example.gbgithubuser2.Adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.gbgithubuser2.Data.dataUser
import com.example.gbgithubuser2.R
import com.example.gbgithubuser2.databinding.ItemUserBinding

class FavoriteAdapter(private val activity: Activity) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: dataUser)
    }

    var listUser = ArrayList<dataUser>()
        set(listUser){
            if (listUser.size > 0){
                this.listUser.clear()
            }
            this.listUser.addAll(listUser)

            notifyDataSetChanged()
        }

    fun addItem(dataUser: dataUser){
        this.listUser.add(dataUser)
        notifyItemInserted(this.listUser.size-1)
    }

    fun updateItem(position: Int, dataUser: dataUser){
        this.listUser[position] = dataUser
        notifyItemChanged(position,dataUser)
    }

    fun removeItem(position: Int){
        this.listUser.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listUser.size)
    }

    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemUserBinding.bind(itemView)
        fun bind(dataUser: dataUser) {
            binding.tvUsername.text = dataUser.username
            Glide.with(itemView.context)
                .load(dataUser.avatar)
                .apply(RequestOptions().override(55,55))
                .into(binding.avatarUser)
            itemView.setOnClickListener { onItemClickCallback?.onItemClicked(dataUser)  }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int {
        return listUser.size
    }
}