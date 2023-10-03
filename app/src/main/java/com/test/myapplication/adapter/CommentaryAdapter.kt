package com.test.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.test.myapplication.R
import com.test.myapplication.data.Commentary

class CommentaryAdapter(private val commentary: List<Commentary>) :
    RecyclerView.Adapter<CommentaryAdapter.CommentaryViewHolder>() {

    inner class CommentaryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val authorNameTextView: TextView = itemView.findViewById(R.id.authorNameTextView)
        val commentaryTextView: TextView = itemView.findViewById(R.id.tversedescriptionTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentaryViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.commentary_cardview_item, parent, false)
        return CommentaryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CommentaryViewHolder, position: Int) {
        val commentaryItem = commentary[position]

        // Bind data to the views
        holder.authorNameTextView.text = commentaryItem.authorName
        holder.commentaryTextView.text = commentaryItem.description
    }

    override fun getItemCount(): Int {
        return commentary.size
    }


    fun getAllCommentaryText(): String {
        var allCommentaryText = ""
        for (commentaryItem in commentary) {
            allCommentaryText += commentaryItem.description + "\n\n"
        }
        return allCommentaryText
    }
}

