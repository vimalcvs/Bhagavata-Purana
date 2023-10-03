package com.test.myapplication.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.test.myapplication.databinding.ActivityAboutGitaBinding

class AboutGitaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutGitaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutGitaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { onBackPressed() }


    }

}