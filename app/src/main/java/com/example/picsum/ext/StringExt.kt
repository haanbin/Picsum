package com.example.picsum.ext

fun String.formatPhotoUrl(grayscale: Boolean, blur: Int): String {
    val stringBuilder = StringBuilder(this)
    if (grayscale) {
        stringBuilder.append("&grayscale")
    }
    if (blur != 0) {
        stringBuilder.append("&blur=$blur")
    }
    return stringBuilder.toString().replaceFirst("&", "?")
}
