package com.quickdev.projectdashboard.viewmodels.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.quickdev.projectdashboard.databinding.ListitemProjectBinding
import com.quickdev.projectdashboard.models.domain.Project
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProjectAdapter(private val clickListener: ProjectItemClickListener):
    ListAdapter<ProjectAdapter.DataItem, RecyclerView.ViewHolder>(ProjectDiffCallback()) {

    companion object {
        private const val ITEM_VIEW_TYPE_ITEM = 1
    }

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun setList(list: List<Project>?) {
        adapterScope.launch {
            val items = list?.map {DataItem.ReservatieItem(it) }

            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val item = getItem(position) as DataItem.ReservatieItem
                holder.bind(clickListener, item.project)
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
        return when (getItem(position)) {
            is DataItem.ReservatieItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    class ViewHolder private constructor(private val binding: ListitemProjectBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: ProjectItemClickListener, item: Project) {
            binding.project = item
            binding.clickListener = clickListener

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListitemProjectBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }

    class ProjectDiffCallback : DiffUtil.ItemCallback<DataItem>() {

        override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem == newItem
        }
    }

    sealed class DataItem {
        data class ReservatieItem(val project: Project) : DataItem() {
            override val id = project.id
        }

        abstract val id: Int
    }
}

class ProjectItemClickListener(val clickListener: (projectId: Int) -> Unit) {
    fun onClick(project: Project) = clickListener(project.id)
}
