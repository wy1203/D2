package com.example.hack

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity(), Home.callback {
    lateinit var viewpager2: ViewPager2
    lateinit var tabLayout: TabLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewpager2 = findViewById(R.id.fragment_container)
        tabLayout = findViewById(R.id.tab_layout)
        viewpager2.adapter = Adapter(this)

        TabLayoutMediator(tabLayout, viewpager2) { tab, position ->
            when (position) {
                0 -> tab.text = "Home"
                1 -> tab.text = "Upload"
                2 -> tab.text = "Favorites"
            }
        }.attach()
    }

    private inner class Adapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int {
            return 3
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> Home()
                1 -> Upload()
                2 -> Favorite()
                else -> Home()
            }
        }
    }

    override fun notifyFavorites() {
        viewpager2.adapter?.notifyItemChanged(2)
    }
}


