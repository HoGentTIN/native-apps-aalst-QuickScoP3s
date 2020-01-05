package com.quickdev.projectdashboard.viewmodels.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.quickdev.projectdashboard.databinding.ListitemProjectBinding
import com.quickdev.projectdashboard.models.domain.Project
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProjectAdapter(clickListener: (Project, projectName: TextView, teamName: TextView) -> Unit): ListAdapter<Project, RecyclerView.ViewHolder>(ProjectDiffCallback()) {

    companion object {
        private const val ITEM_VIEW_TYPE_ITEM = 1
    }

    private val clickListener = ClickListener(clickListener)
    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun setList(list: List<Project>?) {
        adapterScope.launch {
            withContext(Dispatchers.Main) {
                submitList(list)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val item = getItem(position) as Project
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

    class ViewHolder private constructor(private val binding: ListitemProjectBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: ClickListener, item: Project) {
            clickListener.projectName = binding.txtProjectName
            clickListener.teamName = binding.txtProjectTeam

            binding.project = item
            binding.clickListener = clickListener

            binding.txtProjectName.transitionName = item.name
            binding.txtProjectTeam.transitionName = "Team ${item.name}"

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

    class ClickListener(val clickListener: (Project, projectName: TextView, teamName: TextView) -> Unit) {
        internal lateinit var projectName: TextView
        internal lateinit var teamName: TextView

        fun onClick(project: Project) = clickListener(project, projectName, teamName)
    }

    private class ProjectDiffCallback : DiffUtil.ItemCallback<Project>() {

        override fun areItemsTheSame(oldItem: Project, newItem: Project): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Project, newItem: Project): Boolean {
            return oldItem == newItem
        }
    }
}
