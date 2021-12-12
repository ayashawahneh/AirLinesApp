package com.example.airlinesapp.models

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.airlinesapp.R
import java.math.BigDecimal

data class AirLine(
    val country: String,
    val established: String,
    val head_quaters: String,
    val id: BigDecimal,
    val logo: String,
    val name: String,
    val slogan: String,
    val website: String
) {
    companion object {
        @BindingAdapter("android:loadImage")
        @JvmStatic
        fun loadImage(imageView: ImageView, url: String) {
            Glide.with(imageView)
                .load(url)
                .apply(RequestOptions().fitCenter().placeholder(R.drawable.im_image_not_found))
                .into(imageView)
        }
    }


}