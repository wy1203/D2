package com.example.hack

import java.util.*
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)

data class ImageObject(val image_data: String, val tags: MutableList<String>)

