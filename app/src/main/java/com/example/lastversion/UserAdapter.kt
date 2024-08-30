package com.example.lastversion

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lastversion.databinding.ItemLayoutBinding

class UserAdapter(
    private val onItemDetail: (TaskData) -> Unit,
    private val onItemDelete: (Pair<TaskData, Int>) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    var adapterList: MutableList<TaskData> = mutableListOf()

    fun setTaskList(list: List<TaskData>) {
        adapterList.clear()
        adapterList.addAll(list)
        notifyDataSetChanged() // Notify adapter about the data change
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bindData(adapterList[position], position) // Pass position to the bindData method
    }

    override fun getItemCount() = adapterList.size // Return the size of userList

    inner class UserViewHolder(private val binding: ItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(task: TaskData, position: Int) {
            binding.TaskName.text = task.TaskName
            binding.TaskDescription.text = task.TaskDescription

            binding.root.setOnClickListener {
                onItemDetail(task)
            }
            binding.deleteButton.setOnClickListener {
                onItemDelete(Pair(task, adapterPosition)) // Correctly use the Pair constructor
            }
        }
    }
}
