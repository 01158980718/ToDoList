package com.example.lastversion

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.lastversion.databinding.FragmentDetialBinding

class DetialFragment : Fragment() {
    lateinit var binding: FragmentDetialBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetialBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set task details
        binding.TaskName.text = arguments?.getString("TaskName")
        binding.TaskDescription.text = arguments?.getString("TaskDescription")

        // Set up the back button listener

    }
}
