package com.novoda.androidstoreexample.utilities

import android.content.Context

class ImageHelper {
    fun getResourceIdForImage(context: Context, imageName: String): Int {
        return context.resources.getIdentifier(imageName,
                "drawable", context.packageName)
    }
}