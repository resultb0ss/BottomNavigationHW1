package com.example.bottomnavigationhw1.ui.notes

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bottomnavigationhw1.R
import com.example.bottomnavigationhw1.databinding.FragmentNotesBinding
import com.example.bottomnavigationhw1.models.CustomAdapter
import com.example.bottomnavigationhw1.models.Note
import com.example.bottomnavigationhw1.ui.profile.PersonViewModel
import java.sql.Date


class NotesFragment : Fragment() {

    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapterNote: CustomAdapter
    lateinit var viewModel: NoteViewModel

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNotesBinding.inflate(inflater,container,false)

        viewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        binding.recyclerViewNoteFragment.layoutManager = LinearLayoutManager(requireContext())
        adapterNote = CustomAdapter(viewModel.noteList)
        binding.recyclerViewNoteFragment.adapter = adapterNote

        binding.saveNoteButtonBTN.setOnClickListener {
            val noteText = binding.mainNoteEditText.text.toString()
            if (noteText.isEmpty()){
                Toast.makeText(requireContext(),"Заметка пустая",Toast.LENGTH_SHORT).show()
            } else {
                val date = java.util.Date().toString()
                val note = Note(viewModel.noteList.size + 1,noteText,date)
                viewModel.noteList.add(note)
                adapterNote.notifyDataSetChanged()
                binding.mainNoteEditText.text.clear()
            }
        }

        adapterNote.setOnNoteClickListener(object: CustomAdapter.OnNoteClickListener {
            override fun onNoteClick(note: Note, position: Int) {

                val (dialog, editName: android.widget.EditText) = dialogInit()
                dialog.setPositiveButton("Обновить") { _, _ ->

                    val newName = editName.text.toString()
                    val newDate = java.util.Date().toString()
                    val newNote = Note(viewModel.noteList[position].id,newName,newDate)

                    val index = search(viewModel.noteList,note)
                    swap(viewModel.noteList,index,newNote)
                    adapterNote.notifyDataSetChanged()
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

                viewModel.noteList.removeAt(viewHolder.adapterPosition)
                adapterNote.notifyItemRemoved(viewHolder.adapterPosition)

            }

        }).attachToRecyclerView(binding.recyclerViewNoteFragment)

        return binding.root
        }


    private fun dialogInit(): Pair<AlertDialog.Builder, EditText> {
        val dialog = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.update_note, null)
        dialog.setView(dialogView)
        val editName: EditText = dialogView.findViewById(R.id.updateNoteText)

        dialog.setTitle("Обновить запись")
        dialog.setMessage("Введите данные ниже")
        return Pair(dialog, editName)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()


    }

    private fun swap(articles: MutableList<Note>, index: Int, newArticle: Note){
        articles.add(index + 1, newArticle)
        articles.removeAt(index)
    }

    private fun search(articles: MutableList<Note>, oldArticle: Note): Int {
        var result = -1
        for (i in articles.indices) {
            if ( oldArticle.id == articles[i].id ) result = i
        }
        return result
    }




}