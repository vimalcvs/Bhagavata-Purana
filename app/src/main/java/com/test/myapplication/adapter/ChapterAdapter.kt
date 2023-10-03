package com.test.myapplication.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test.myapplication.activity.ChapterDetailActivity
import com.test.myapplication.data.Chapter
import com.test.myapplication.databinding.ChapterCardviewItemBinding

class ChapterAdapter(private val chapters: List<Chapter>, private val context: Context) :
    RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChapterViewHolder {
        return ChapterViewHolder(
            ChapterCardviewItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ChapterViewHolder, position: Int) {
        val chapter = chapters[position]

        holder.binding.chapterNumberTextView.text = chapter.chapter_number.toString()
        holder.binding.chapterNameTextView.text = chapter.name
        holder.binding.chapterNameMeaningTextView.text = chapter.name_meaning

        holder.binding.cvCard.setOnClickListener {
            val intent = Intent(context, ChapterDetailActivity::class.java)
            intent.putExtra("chapter_number", chapter.chapter_number)
            intent.putExtra("chapter_name", chapter.name)
            intent.putExtra("name_meaning", chapter.name_meaning)
            intent.putExtra("chapter_summary", chapter.chapter_summary)
            intent.putExtra("chapter_summary_hindi", chapter.chapter_summary_hindi)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return chapters.size
    }

    class ChapterViewHolder(val binding: ChapterCardviewItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}

