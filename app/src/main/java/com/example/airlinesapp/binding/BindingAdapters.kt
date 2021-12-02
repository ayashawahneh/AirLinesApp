package com.example.airlinesapp.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.airlinesapp.R

@BindingAdapter("android:loadImage")
fun loadImage(imageView: ImageView, url: String) {
    Glide.with(imageView)
        .load(url)
        .apply(RequestOptions().fitCenter().placeholder(R.drawable.im_image_not_found))
        .into(imageView)
}



