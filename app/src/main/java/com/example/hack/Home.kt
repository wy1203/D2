package com.example.hack

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.GenericTransitionOptions.with
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.with
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.with
import com.bumptech.glide.request.target.Target
import com.github.dhaval2404.imagepicker.ImagePicker.Companion.with
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.*
import okhttp3.*
import java.io.ByteArrayOutputStream
import java.io.IOException


/**
 * A simple [Fragment] subclass.
 * Use the [Home.newInstance] factory method to
 * create an instance of this fragment.
 */
class Home : Fragment() {
    val BASE_URL = "http://34.86.86.57/"
    lateinit var b: ByteArray
    private val client = OkHttpClient()

    interface callback{
        fun notifyFavorites()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val callback = activity as callback
        var rootview = inflater.inflate(R.layout.fragment_home, container, false)
        val button = rootview.findViewById<View>(R.id.button_add)
        val imageView = rootview.findViewById<ImageView>(R.id.image)
        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val queryAdapter = moshi.adapter(ImageGet::class.java)
        val request = Request.Builder().url(BASE_URL+"api/ootds/").get().build()
        Log.d("request", request.toString())
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: java.io.IOException) {
                e.printStackTrace()
            }
            @SuppressLint("MutatingSharedPrefs")
            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    throw IOException("Unexpected code $response")
                } else {
                    var body = response.body
                    Log.d("body", body.toString())
                    val res = queryAdapter.fromJson(body!!.source())
                    Log.d("images", res.toString())
                    val size = res?.images?.size
                    Log.d("size", size.toString())
                    if (res != null) {
                        if (size != null) {
                            var i = (0 until size - 1).random()
                            var image = res.images.get(i).image_url
                            Log.d("imageurl", image.toString())
                            runBlocking {
                                withContext(Dispatchers.Main) {
                                    Glide.with(requireActivity()).load(image).into(imageView)
                                    button.setOnClickListener{
                                        callback.notifyFavorites()
                                        val sharedprefs = requireActivity().getSharedPreferences("favorites",
                                            Context.MODE_PRIVATE
                                        )
                                        val set = sharedprefs.getStringSet("Favorite", MyFav.list.toMutableSet())
                                            ?.toMutableSet()
                                        with (sharedprefs.edit()) {
                                            if (set != null) {
                                                set.add(image)
                                            }
                                            putStringSet("Favorite", set)
                                            apply()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }
        })


        return rootview
    }
}