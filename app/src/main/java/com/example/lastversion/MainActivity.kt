package com.example.lastversion

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import com.example.lastversion.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Set up NavController for navigation
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        // Set OnClickListener for the FloatingActionButton
        binding.floatingActionButton.setOnClickListener {
            binding.welcomeText.visibility = View.INVISIBLE
            binding.starttext.visibility = View.INVISIBLE
            binding.floatingActionButton.visibility = View.INVISIBLE
            navController.navigate(R.id.taskDetailsFragment)
            Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show()
        }

        // Apply window insets to main layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onResume() {
        super.onResume()
        updateUIBasedOnTaskList()
    }

     fun updateUIBasedOnTaskList() {
        if (areTasksAvailable()) {
            // Show the ListFragment
            val navController = findNavController(R.id.fragmentContainerView)
            navController.navigate(R.id.listFragment)
            binding.welcomeText.visibility = View.INVISIBLE
            binding.starttext.visibility = View.INVISIBLE
            binding.floatingActionButton.visibility = View.VISIBLE
        } else {
            // Show welcome screen if there are no tasks
            binding.welcomeText.visibility = View.VISIBLE
            binding.starttext.visibility = View.VISIBLE
            binding.floatingActionButton.visibility = View.VISIBLE
        }
    }

    private fun areTasksAvailable(): Boolean {
        val sharedPreferences = getSharedPreferences("com.example.lastversion.PREFS", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("task_list", null)
        val type = object : TypeToken<MutableList<TaskData>>() {}.type
        val taskList: MutableList<TaskData> = gson.fromJson(json, type) ?: mutableListOf()
        return taskList.isNotEmpty()
    }
}
