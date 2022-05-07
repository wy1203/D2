package com.example.hack

import android.content.SharedPreferences

object MyFav {
    var list = mutableListOf<String>()
    init{
      list = mutableListOf<String>()
    }
    fun addImage(b: String){
        list.add(b)

    }
}

//object MyFav {
//    var list = mutableListOf<ByteArray>()
//        // Only allows retrival of the counter,
//        // setting restricted to only within Singleton
//        private set
//
//    // Optional init block if you need some initalization
//    init {
//        list = mutableListOf<ByteArray>()
//    }
//
//    // Increments the counter and returns the new number after incrementing.
//    fun incrementCounter(): Int {
//        return ++counter
//    }
//
//    // Can define whatever member functions you want!
//}
