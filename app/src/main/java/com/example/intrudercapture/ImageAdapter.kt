package com.example.intrudercapture

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

class ImageAdapter(private val imageList: List<File>, private val itemClickListener: (File) -> Unit) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_imageview, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val currentFile = imageList[position]
        holder.bind(currentFile, itemClickListener)

    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)
        private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)

        fun bind(file: File, itemClickListener: (File) -> Unit) {
            // Load image using Glide or your preferred image loading library
        /*    Glide.with(itemView)
                .load(file)
                .into(imageView)*/

            // Extract date from the file if available or use a default value
            val date = file.lastModified()
            val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)
            dateTextView.text = formattedDate

            // Set a click listener on the item
            itemView.setOnClickListener { itemClickListener(file) }
        }
    }
}