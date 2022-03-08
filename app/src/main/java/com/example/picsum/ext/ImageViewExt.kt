package com.example.picsum.ext

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.picsum.BuildConfig
import com.example.picsum.R
import com.example.picsum.data.vo.Image

@BindingAdapter("bind:loadImage")
fun ImageView.loadImage(image: Image?) {
    image?.let {
        val deviceWidth = (context.resources.displayMetrics.widthPixels)
        val size = (deviceWidth) / 4
        val imageUrl = (BuildConfig.SERVER_URL + "id/" + image.id + "/$size/$size").formatPhotoUrl(
            image.grayScale,
            image.blur
        )
        Glide.with(this).load(imageUrl)
            .apply(
                RequestOptions().placeholder(R.drawable.ic_no_image)
                    .error(R.drawable.ic_no_image)
                    .override(size)
            )
            .into(this)
    }
}

@BindingAdapter(
    "bind:loadImageDetail",
    "bind:onResourceReady",
    "bind:onLoadFailed"
)
fun ImageView.loadImageDetail(
    image: Image,
    onResourceReady: () -> Unit,
    onLoadFailed: () -> Unit
) {
    val calcWidth = (context.resources.displayMetrics.widthPixels) / 2
    val calcHeight = (image.height * calcWidth) / image.width
    val imageUrl =
        (BuildConfig.SERVER_URL + "id/" + image.id + "/$calcWidth/$calcHeight").formatPhotoUrl(
            image.grayScale,
            image.blur
        )
    Glide.with(this)
        .load(imageUrl)
        .apply(
            RequestOptions().override(calcWidth, calcHeight)
        ).addListener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                onLoadFailed()
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                onResourceReady()
                return false
            }
        })
        .into(this)
}