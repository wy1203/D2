package com.example.hack


import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.dhaval2404.imagepicker.ImagePicker
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream



class Upload : Fragment() {
    val BASE_URL = "http://34.86.86.57/"
    private val client = OkHttpClient()
    private val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    private val noteJsonAdapter = moshi.adapter(ImageObject::class.java)
    private val imageResultAdapter = moshi.adapter(ImageResult::class.java)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootview = inflater.inflate(R.layout.fragment_upload, container, false)
        val button = rootview.findViewById<View>(R.id.button_upload)
        button.setOnClickListener {
            ImagePicker.with(this)
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }

        return rootview
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val uri: Uri = data?.data!!
            val bitmap = BitmapFactory.decodeStream(context?.getContentResolver()!!.openInputStream(uri))
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val b: ByteArray = baos.toByteArray()
            val s = Base64.encodeToString(b,Base64.DEFAULT)
            var ls = mutableListOf<String>()
            ls.add("123")
            var image = ImageObject("data:image/jpeg;base64,"+s,ls)
            runBlocking {
                withContext(Dispatchers.IO) { uploadImage(image) }
            }
            Log.i("uniquename", s)


        } else if (resultCode == ImagePicker.RESULT_ERROR) {
           Log.d("errormessage", ImagePicker.getError(data))
        } else {
            Log.d("1","ImagePicker.getError()")
        }
    }


    private fun uploadImage(image: ImageObject ) {

        val requestPost = Request.Builder().url(BASE_URL+"api/ootd/")
            .post(noteJsonAdapter.toJson(image).toRequestBody()).build()
        client.newCall(requestPost).execute().use {
            if(!it.isSuccessful){
                 Log.d("Err",it.toString())
            }
            else{
                Log.d("namee", it.body.toString())
                val imageResult = imageResultAdapter.fromJson(it.body!!.string())!!
                Log.i("image1234", imageResult.image_url.toString())
            }

        }

    }
}



