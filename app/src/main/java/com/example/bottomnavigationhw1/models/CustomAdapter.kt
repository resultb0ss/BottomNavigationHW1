package com.example.bottomnavigationhw1.models

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.example.bottomnavigationhw1.R

class CustomAdapter(private var notes: MutableList<Note>):
    RecyclerView.Adapter<CustomAdapter.NoteViewHolder>() {

        private var onNoteClickListener: OnNoteClickListener? = null

    interface OnNoteClickListener {
        fun onNoteClick(note: Note, position: Int)
    }

    class NoteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val noteText: TextView = itemView.findViewById(R.id.nameNote)
        val date: TextView = itemView.findViewById(R.id.createDateNote)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note_recycler_view, parent,false)
        return NoteViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.noteText.text = note.note
        holder.date.text = note.date
        holder.itemView.setOnClickListener{
            if (onNoteClickListener != null){
                onNoteClickListener!!.onNoteClick(note,position)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(newList: MutableList<Note>){
        notes = newList
        notifyDataSetChanged()
    }

    fun setOnNoteClickListener(onNoteClickListener: OnNoteClickListener) {
        this.onNoteClickListener = onNoteClickListener
    }
}