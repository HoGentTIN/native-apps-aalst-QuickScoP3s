package com.quickdev.projectdashboard.viewmodels.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.quickdev.projectdashboard.databinding.ListitemTaskBinding
import com.quickdev.projectdashboard.models.domain.ProjectTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TaskAdapter(clickListener: (Int) -> Unit): ListAdapter<ProjectTask, RecyclerView.ViewHolder>(TaskDiffCallback()) {

    companion object {
        private const val ITEM_VIEW_TYPE_ITEM = 1
    }

    private val clickListener = ClickListener(clickListener)
    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun setList(list: List<ProjectTask>?) {
        adapterScope.launch {
            withContext(Dispatchers.Main) {
                submitList(list)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val item = getItem(position) as ProjectTask
                holder.bind(clickListener, item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_ITEM -> ViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return ITEM_VIEW_TYPE_ITEM
    }

    class ViewHolder private constructor(private val binding: ListitemTaskBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: ClickListener, item: ProjectTask) {
            binding.task = item
            binding.clickListener = clickListener

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListitemTaskBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }

    class ClickListener(val clickListener: (Int) -> Unit) {
        fun onClick(item: ProjectTask) = clickListener(item.id)
    }

    private class TaskDiffCallback : DiffUtil.ItemCallback<ProjectTask>() {

        override fun areItemsTheSame(oldItem: ProjectTask, newItem: ProjectTask): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProjectTask, newItem: ProjectTask): Boolean {
            return oldItem == newItem
        }
    }
}
