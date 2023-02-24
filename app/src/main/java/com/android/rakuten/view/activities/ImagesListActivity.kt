package com.android.rakuten.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.rakuten.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImagesListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
    }
}