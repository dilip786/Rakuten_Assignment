package com.android.rakuten.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.android.rakuten.data.model.Photo
import com.android.rakuten.databinding.ListItemBinding
import com.squareup.picasso.Picasso

class ImageListAdapter(private val context: Context, private var data: ArrayList<Photo> = arrayListOf(), private val onWishListCLickListener: (Photo) -> Unit) : BaseAdapter() {

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    fun refreshList( newData: ArrayList<Photo> = arrayListOf()){
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding: ListItemBinding = ListItemBinding.inflate(LayoutInflater.from(context),parent,false)
        binding.tvTitle.text = data[position].title
        binding.ivImage.setOnClickListener {
            onWishListCLickListener.invoke(data[position])
        }
        Picasso.get()
            .load(getPhotoUrl(data[position]))
            .resize(100, 100)
            .centerCrop()
            .into(binding.ivImage)
        return binding.root
    }

    private fun getPhotoUrl(photo: Photo):String {
        return "https://live.staticflickr.com/${photo.server}/${photo.id}_${photo.secret}_w.jpg"
    }
}
