package com.example.lastversion

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.lastversion.databinding.FragmentTaskDetialsBinding

class Task_detialsFragment : Fragment() {
    lateinit var binding: FragmentTaskDetialsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTaskDetialsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button.setOnClickListener {
            val taskName = binding.taskName.text.toString()
            val taskDetails = binding.taskDetails.text.toString()

            if (taskName.isBlank() || taskDetails.isBlank()) {
                // Show a message if any of the fields are empty
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                // If both fields are filled, navigate to the listFragment
                val bundle = Bundle().apply {
                    putString("TaskName", taskName)
                    putString("TaskDescription", taskDetails)
                }
                findNavController().previousBackStackEntry?.savedStateHandle?.set("newTask", bundle)

                // Navigate back to the previous fragment or activity
                findNavController().popBackStack()

                findNavController().navigate(R.id.listFragment, bundle)
                Toast.makeText(requireContext(), "the new task is saved  ", Toast.LENGTH_SHORT).show()
            }
        }
    }
}