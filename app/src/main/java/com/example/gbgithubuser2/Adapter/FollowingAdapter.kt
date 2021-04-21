package com.example.gbgithubuser2.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.gbgithubuser2.Data.dataUser
import com.example.gbgithubuser2.databinding.ItemUserBinding

class FollowingAdapter(private val listUser : ArrayList<dataUser>) : RecyclerView.Adapter<FollowingAdapter.FollowingViewHolder>() {

    class FollowingViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dataUser: dataUser) {
            with(binding){
                tvUsername.text = dataUser.username
                val imageUrl = dataUser.avatar
                Glide.with(itemView.context)
                    .load(imageUrl)
                    .apply(RequestOptions().override(55,55))
                    .into(avatarUser)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FollowingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FollowingViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int {
        return listUser.size
    }
}