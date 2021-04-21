package com.example.gbgithubuser2.Adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.gbgithubuser2.Data.dataUser
import com.example.gbgithubuser2.databinding.ItemUserBinding
import java.lang.Exception
import java.net.URL

class ListUserAdapter(private val listUser: ArrayList<dataUser>) : RecyclerView.Adapter<ListUserAdapter.ListViewHolder> () {

    private var onItemClickCallback: OnItemClickCallback? = null
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: dataUser)
    }

    inner class ListViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dataUser: dataUser) {
            with(binding){
                tvUsername.text = dataUser.username
                val imageUrl = dataUser.avatar
                Glide.with(itemView.context)
                    .load(imageUrl)
                    .apply(RequestOptions().override(55,55))
                    .into(avatarUser)

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(dataUser)  }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int {
        return listUser.size
    }
}