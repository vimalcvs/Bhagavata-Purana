package com.test.myapplication.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.test.myapplication.adapter.TranslationAdapter
import com.test.myapplication.data.Translation
import com.test.myapplication.databinding.ActivityVerseTranslationBinding
import java.io.IOException


class VerseTranslationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerseTranslationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerseTranslationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { onBackPressed() }

        val verseNumber = intent.getIntExtra("verse_id", 0)
        val translations = getTranslationsForVerse(verseNumber)
        val adapter = TranslationAdapter(translations)
        binding.translationRecyclerView.adapter = adapter
        binding.translationRecyclerView.layoutManager = LinearLayoutManager(this)

    }

    private fun getTranslationsForVerse(verseNumber: Int): List<Translation> {
        val jsonString = getJsonDataFromAsset("translation.json")
        val gson = Gson()
        val listTranslationType = object : TypeToken<List<Translation>>() {}.type
        val translations: List<Translation> = gson.fromJson(jsonString, listTranslationType)

        return translations.filter { it.verse_id == verseNumber }
    }

    private fun getJsonDataFromAsset(fileName: String): String? {
        return try {
            applicationContext.assets.open(fileName).bufferedReader().use {
                it.readText()
            }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            null
        }
    }
}
