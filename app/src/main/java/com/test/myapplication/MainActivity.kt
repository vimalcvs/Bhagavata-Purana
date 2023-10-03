package com.test.myapplication

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.test.myapplication.activity.AboutGitaActivity
import com.test.myapplication.adapter.ChapterAdapter
import com.test.myapplication.adapter.SliderVerseAdapter
import com.test.myapplication.data.Chapter
import com.test.myapplication.data.Verse
import com.test.myapplication.databinding.ActivityMainBinding
import com.test.myapplication.fragment.AboutAppFragment
import com.test.myapplication.fragment.ThemeFragment
import org.json.JSONArray
import java.io.IOException
import java.nio.charset.Charset
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var chapterList: List<Chapter>
    private lateinit var verseList: List<Verse>
    private lateinit var viewPager: ViewPager2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)


        verseList = loadVersesFromJson()
        verseList = verseList.shuffled(Random(System.currentTimeMillis()))


        val jsonString = applicationContext.assets.open("chapters.json").bufferedReader().use {
            it.readText()
        }
        chapterList = parseJson(jsonString)
        val adapterC = ChapterAdapter(chapterList, this)
        binding.recyclerView.adapter = adapterC
        binding.recyclerView.layoutManager = LinearLayoutManager(this)


        viewPager = binding.viewPager
        val adapter = SliderVerseAdapter(verseList)
        binding.viewPager.adapter = adapter

        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                binding.viewPager.currentItem = (binding.viewPager.currentItem + 1) % verseList.size
                handler.postDelayed(this, 10000)
            }
        }
        handler.postDelayed(runnable, 10000)


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_about_gita -> {
                intent.setClass(this, AboutGitaActivity::class.java)
                startActivity(intent)
            }



            R.id.nav_theme -> {
                val themeDialog = ThemeFragment()
                themeDialog.show(supportFragmentManager, "theme_dialog")
                return true
            }

            R.id.nav_about -> {
                val aboutDialog = AboutAppFragment()
                aboutDialog.show(supportFragmentManager, "AboutAppFragment")

            }

        }
        return super.onOptionsItemSelected(item)
    }


    private fun loadVersesFromJson(): List<Verse> {
        val json: String?
        try {
            val inputStream = assets.open("verse.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, Charset.defaultCharset())
        } catch (e: IOException) {
            e.printStackTrace()
            return emptyList()
        }

        val listType = object : TypeToken<List<Verse>>() {}.type
        return Gson().fromJson(json, listType)
    }


    private fun parseJson(jsonString: String): List<Chapter> {
        val chapterList = mutableListOf<Chapter>()
        val jsonArray = JSONArray(jsonString)

        for (i in 0 until jsonArray.length()) {
            val chapterJson = jsonArray.getJSONObject(i)
            val chapter = Chapter(
                chapterJson.getInt("chapter_number"),
                chapterJson.getString("chapter_summary"),
                chapterJson.getString("chapter_summary_hindi"),
                chapterJson.getInt("id"),
                chapterJson.getString("image_name"),
                chapterJson.getString("name"),
                chapterJson.getString("name_meaning"),
                chapterJson.getString("name_translation"),
                chapterJson.getString("name_transliterated"),
                chapterJson.getInt("verses_count")
            )
            chapterList.add(chapter)
        }

        return chapterList
    }
}