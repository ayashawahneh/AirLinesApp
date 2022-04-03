package com.example.airlinesapp.binding

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.airlinesapp.R

@BindingAdapter("android:loadImage")
fun loadImage(imageView: ImageView, url: String?) {
    Glide.with(imageView)
        .load(url)
        .apply(RequestOptions().fitCenter().placeholder(R.drawable.im_image_not_found))
        .into(imageView)
}

@BindingAdapter("android:setText")
fun setText(textView: TextView, stringId: Int) {
    textView.setText(stringId)
}

@BindingAdapter("visibleGone")
fun showHide(view: View, show: Boolean) {
    view.visibility = if (show) View.VISIBLE else View.GONE
}