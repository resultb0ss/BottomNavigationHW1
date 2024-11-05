package com.example.bottomnavigationhw1.ui.notes

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bottomnavigationhw1.R
import com.example.bottomnavigationhw1.databinding.FragmentNotesBinding
import com.example.bottomnavigationhw1.models.CustomAdapter
import com.example.bottomnavigationhw1.models.Note
import java.sql.Date


class NotesFragment : Fragment() {

    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!
    private var notes: MutableList<Note> = mutableListOf()
    val adapter = CustomAdapter(notes)

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNotesBinding.inflate(inflater,container,false)

        binding.recyclerViewNoteFragment.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewNoteFragment.adapter = adapter

        binding.saveNoteButtonBTN.setOnClickListener {
            val noteText = binding.mainNoteEditText.text.toString()
            if (noteText.isEmpty()){
                Toast.makeText(requireContext(),"Заметка пустая",Toast.LENGTH_SHORT).show()
            } else {
                val date = java.util.Date().toString()
                val note = Note(notes.size + 1,noteText,date)
                notes.add(note)
                adapter.notifyDataSetChanged()
            }
        }

        adapter.setOnNoteClickListener(object: CustomAdapter.OnNoteClickListener {
            override fun onNoteClick(note: Note, position: Int) {

                val dialog = AlertDialog.Builder(requireContext())
                val inflater = requireActivity().layoutInflater
                val dialogView = inflater.inflate(R.layout.update_note, null)
                dialog.setView(dialogView)
                val editName: EditText = dialogView.findViewById(R.id.updateNoteText)

                dialog.setTitle("Обновить запись")
                dialog.setMessage("Введите данные ниже")
                dialog.setPositiveButton("Обновить") { _, _ ->

                    val newNoteText = binding.mainNoteEditText.text.toString()
                    val newDate = java.util.Date().toString()
                    val newNote = Note(notes[position].id,newNoteText, newDate)

                    val index = search(notes, note)
                    swap(notes, index, newNote)


                }
                dialog.setNegativeButton("Отмена") { _, _ -> }
                dialog.create().show()
            }
        }
        )

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                notes.removeAt(viewHolder.adapterPosition)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)

            }

        }).attachToRecyclerView(binding.recyclerViewNoteFragment)

        return binding.root
        }

    private fun swap(articles: MutableList<Note>, index: Int, newNote: Note){
        articles.add(index + 1, newNote)
        articles.removeAt(index)
    }

    private fun search(articles: MutableList<Note>, oldNote: Note): Int {
        var result = -1
        for (i in articles.indices) {
            if ( oldNote.id == notes[i].id ) result = i
        }
        return result
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()


    }




}