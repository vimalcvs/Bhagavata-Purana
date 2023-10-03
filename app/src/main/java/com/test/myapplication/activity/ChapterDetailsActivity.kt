package com.test.myapplication.activity

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.test.myapplication.adapter.VerseAdapter
import com.test.myapplication.data.Chapter
import com.test.myapplication.data.Verse
import com.test.myapplication.databinding.ActivityChapterDetailBinding
import kotlinx.coroutines.*
import java.io.IOException

class ChapterDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChapterDetailBinding
    private var verseList: List<Verse> = emptyList()
    private var isSummaryExpanded = false
    private var isSummaryHindiExpanded = false
    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChapterDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { onBackPressed() }

        progressBar = binding.progressBar


        val chapterNumber = intent.getIntExtra("chapter_number", 0)
        val chapterName = intent.getStringExtra("chapter_name")
        val chapterNameMeaning = intent.getStringExtra("name_meaning")
        val chapterSummary = intent.getStringExtra("chapter_summary")
        val chapterSummaryHindi = intent.getStringExtra("chapter_summary_hindi")


        progressBar.visibility = View.VISIBLE

        val verse = loadVersesForChapter(chapterNumber)
        val adapter = VerseAdapter(verse , this)
        binding.verseRecyclerView.adapter = adapter
        binding.verseRecyclerView.layoutManager = LinearLayoutManager(this)

        GlobalScope.launch(Dispatchers.IO) {
            verseList = loadVersesForChapter(chapterNumber)

            withContext(Dispatchers.Main) {
                binding.toolbar.title = "Chapter $chapterNumber"
                binding.chapterNameTextView.text = chapterName
                binding.chapterNameMeaningTextView.text = chapterNameMeaning
                binding.verseRecyclerView.layoutManager = LinearLayoutManager(this@ChapterDetailActivity)
                binding.verseRecyclerView.adapter = VerseAdapter(verseList, this@ChapterDetailActivity)
                progressBar.visibility = View.GONE
            }
        }

        binding.chapterSummaryTextView.text = getEllipsizedText(chapterSummary ?: "", 2, 40)
        binding.seeMoreTextView.setOnClickListener {
            isSummaryExpanded = !isSummaryExpanded
            if (isSummaryExpanded) {
                binding.chapterSummaryTextView.text = chapterSummary
                binding.seeMoreTextView.text = "See Less"
            } else {
                binding.chapterSummaryTextView.text = getEllipsizedText(chapterSummary ?: "", 2, 40)
                binding.seeMoreTextView.text = "See More"
            }
        }

        binding.chapterSummaryHindiTextView.text =
            getEllipsizedText(chapterSummaryHindi ?: "", 2, 40)
        binding.seeMoreHindiTextView.setOnClickListener {
            isSummaryHindiExpanded = !isSummaryHindiExpanded
            if (isSummaryHindiExpanded) {
                binding.chapterSummaryHindiTextView.text = chapterSummaryHindi
                binding.seeMoreHindiTextView.text = "छोटा करें"
            } else {
                binding.chapterSummaryHindiTextView.text =
                    getEllipsizedText(chapterSummaryHindi ?: "", 2, 40)
                binding.seeMoreHindiTextView.text = "और देखें"
            }
        }

        binding.verseRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.verseRecyclerView.adapter = VerseAdapter(verseList, this)
    }

    private fun getEllipsizedText(text: String, maxLines: Int, maxCharactersPerLine: Int): String {
        val maxCharacters = maxLines * maxCharactersPerLine
        return if (text.length > maxCharacters) {
            "${text.substring(0, maxCharacters)}..."
        } else {
            text
        }
    }

    private fun loadJsonFromAsset(fileName: String): String? {
        return try {
            applicationContext.assets.open(fileName).bufferedReader().use {
                it.readText()
            }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            null
        }
    }



    private fun loadVersesForChapter(chapterNumber: Int): List<Verse> {
        val jsonString = loadJsonFromAsset("verse.json")
        val verseListType = object : TypeToken<List<Verse>>() {}.type
        val allVerses: List<Verse> = Gson().fromJson(jsonString, verseListType)
        return allVerses.filter { it.chapter_number == chapterNumber }
    }
}

