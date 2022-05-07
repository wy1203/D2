package com.example.hack
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImageResult(val id: Int, val image_url: String, val tags: MutableList<String>)