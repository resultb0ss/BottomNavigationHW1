package com.example.bottomnavigationhw1.ui.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bottomnavigationhw1.R
import com.example.bottomnavigationhw1.databinding.FragmentProfileBinding
import com.example.bottomnavigationhw1.models.Note

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: PersonViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentProfileBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(this).get(PersonViewModel::class.java)
        binding.firstNameTextView.text = viewModel.person.firstName
        binding.lastNameTextView.text = viewModel.person.lastName
        binding.phoneNumberTextView.text = viewModel.person.phoneNumber
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.firstNameTextView.setOnClickListener {
            getDialogFirstName(binding.firstNameTextView)
        }

        binding.lastNameTextView.setOnClickListener {
            getDialogLastName(binding.lastNameTextView)
        }

        binding.phoneNumberTextView.setOnClickListener {
            getDialogPhone(binding.phoneNumberTextView)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getDialogFirstName(view: TextView,){

        val (dialog, editName: EditText) = DialogInit()

        dialog.setPositiveButton("Обновить") { _, _ ->
            view.text = editName.text.toString()
            viewModel.person.firstName = view.text.toString()

        }
        dialog.setNegativeButton("Отмена") { _, _ -> }
        dialog.create().show()
    }

    private fun getDialogLastName(view: TextView,){

        val (dialog, editName: EditText) = DialogInit()

        dialog.setPositiveButton("Обновить") { _, _ ->
            view.text = editName.text.toString()
            viewModel.person.lastName = view.text.toString()

        }
        dialog.setNegativeButton("Отмена") { _, _ -> }
        dialog.create().show()
    }

    private fun getDialogPhone(view: TextView,){

        val (dialog, editName: EditText) = DialogInit()

        dialog.setPositiveButton("Обновить") { _, _ ->
            view.text = editName.text.toString()
            viewModel.person.phoneNumber = view.text.toString()

        }
        dialog.setNegativeButton("Отмена") { _, _ -> }
        dialog.create().show()
    }

    private fun DialogInit(): Pair<AlertDialog.Builder, EditText> {
        val dialog = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.update_note, null)
        dialog.setView(dialogView)
        val editName: EditText = dialogView.findViewById(R.id.updateNoteText)

        dialog.setTitle("Обновить запись")
        dialog.setMessage("Введите данные ниже")
        return Pair(dialog, editName)
    }


}