package com.example.bottomnavigationhw1.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.example.bottomnavigationhw1.R
import com.example.bottomnavigationhw1.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMainBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val animationImage = AnimationUtils.loadAnimation(requireContext(),R.anim.fade)
        binding.imageView.startAnimation(animationImage)

        val animationText = AnimationUtils.loadAnimation(requireContext(),R.anim.fade)
        binding.appNameTextView.startAnimation(animationText)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}