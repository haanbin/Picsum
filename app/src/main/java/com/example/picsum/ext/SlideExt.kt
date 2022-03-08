package com.example.picsum.ext

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.google.android.material.slider.Slider

@InverseBindingAdapter(attribute = "android:value")
fun Slider.getSliderValue() = value

@BindingAdapter("android:valueAttrChanged")
fun Slider.setSliderListeners(attrChange: InverseBindingListener) {
    addOnChangeListener { _, _, _ ->
        attrChange.onChange()
    }
}
