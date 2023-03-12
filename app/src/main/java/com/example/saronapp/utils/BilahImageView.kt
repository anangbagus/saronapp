package com.example.saronapp.utils

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.example.saronapp.R

class BilahImageView(
    private val ctx: Context,
    attr: AttributeSet
) : AppCompatImageView(ctx, attr) {

    fun highlight() {
        setBackgroundColor(ContextCompat.getColor(ctx, R.color.purple_200))
    }

    fun removeHighlight() {
        setBackgroundColor(ContextCompat.getColor(ctx, R.color.purple_500))
    }
}