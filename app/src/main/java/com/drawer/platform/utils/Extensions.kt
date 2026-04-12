package com.drawer.platform.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.drawer.platform.R
import java.io.File
import java.text.NumberFormat
import java.util.Locale

fun Context.showToast(message: String) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun View.show() { visibility = View.VISIBLE }
fun View.hide() { visibility = View.GONE }
fun View.invisible() { visibility = View.INVISIBLE }

fun Activity.hideKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    currentFocus?.let { imm.hideSoftInputFromWindow(it.windowToken, 0) }
}

fun ImageView.loadFromPath(path: String?) {
    if (path.isNullOrBlank()) {
        setImageResource(R.drawable.ic_placeholder)
        return
    }
    Glide.with(context)
        .load(File(path))
        .transition(DrawableTransitionOptions.withCrossFade())
        .placeholder(R.drawable.ic_placeholder)
        .error(R.drawable.ic_placeholder)
        .centerCrop()
        .into(this)
}

fun ImageView.loadFromUri(uri: android.net.Uri?) {
    if (uri == null) { setImageResource(R.drawable.ic_placeholder); return }
    Glide.with(context)
        .load(uri)
        .transition(DrawableTransitionOptions.withCrossFade())
        .placeholder(R.drawable.ic_placeholder)
        .centerCrop()
        .into(this)
}

fun Double.toPrice(): String =
    NumberFormat.getCurrencyInstance(Locale.US).format(this)

fun String.toStatusDisplay(): String = when (this) {
    "PENDING" -> "Pending"
    "ACCEPTED" -> "Accepted"
    "PICKED_UP" -> "Picked Up"
    "DELIVERED" -> "Delivered"
    "CANCELLED" -> "Cancelled"
    else -> this
}
