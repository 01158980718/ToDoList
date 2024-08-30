package com.example.lastversion

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lastversion.databinding.FragmentListBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListFragment : Fragment() {
    private lateinit var binding: FragmentListBinding
    private lateinit var adapter: UserAdapter

    // Shared Preferences key
    private val PREFS_NAME = "com.example.lastversion.PREFS"
    private val KEY_TASK_LIST = "task_list"

    companion object {
        val list: MutableList<TaskData> = mutableListOf()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the adapter
        adapter = UserAdapter(
            onItemDetail = { task ->
                findNavController().navigate(R.id.detailFragment, bundleOf(
                    "TaskName" to task.TaskName,
                    "TaskDescription" to task.TaskDescription
                ))
            },
            onItemDelete = { adapterPair ->
                // Remove item from the list
                list.remove(adapterPair.first)
                // Update the adapter and RecyclerView
                adapter.setTaskList(list)
                adapter.notifyItemRemoved(adapterPair.second)

                // Save updated list to SharedPreferences
                saveTaskListToPreferences()

                // Check if list is empty and notify MainActivity
                if (list.isEmpty()) {
                    val mainActivity = activity as? MainActivity
                    mainActivity?.updateUIBasedOnTaskList()
                }
            }
        )

        // Setup RecyclerView
        binding.recyler.layoutManager = LinearLayoutManager(requireContext())
        binding.recyler.adapter = adapter

        // Load the task list from SharedPreferences
        loadTaskListFromPreferences()

        // Add item to the list if arguments are present
        val taskName = arguments?.getString("TaskName")
        val taskDescription = arguments?.getString("TaskDescription")
        if (taskName != null && taskDescription != null) {
            addItemToList(taskName, taskDescription)
        }
    }

    private fun addItemToList(taskName: String, taskDescription: String) {
        val existingItem = list.find { it.TaskName == taskName && it.TaskDescription == taskDescription }
        if (existingItem == null) {
            list.add(TaskData(taskName, taskDescription))
            adapter.setTaskList(list)
            adapter.notifyItemInserted(list.size - 1)
            saveTaskListToPreferences()

            // Notify MainActivity to update UI
            val mainActivity = activity as? MainActivity
            mainActivity?.updateUIBasedOnTaskList()
        } else {
            Log.d("ListFragment", "Item is already in the list, not adding again")
        }
    }

    private fun saveTaskListToPreferences() {
        val sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(list)
        editor.putString(KEY_TASK_LIST, json)
        editor.apply()
    }

    private fun loadTaskListFromPreferences() {
        val sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString(KEY_TASK_LIST, null)
        val type = object : TypeToken<MutableList<TaskData>>() {}.type
        list.clear()
        list.addAll(gson.fromJson(json, type) ?: mutableListOf())
        adapter.setTaskList(list)  // Ensure adapter is updated with loaded data
        adapter.notifyDataSetChanged()  // Refresh RecyclerView
    }
}
