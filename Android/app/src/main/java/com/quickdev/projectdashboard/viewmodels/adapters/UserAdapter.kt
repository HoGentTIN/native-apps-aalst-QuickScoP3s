package com.quickdev.projectdashboard.viewmodels.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.quickdev.projectdashboard.databinding.ListitemUserBinding
import com.quickdev.projectdashboard.models.domain.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserAdapter(clickListener: (Int) -> Unit) : ListAdapter<User, RecyclerView.ViewHolder>(UserDiffCallback()) {

    companion object {
        private const val ITEM_VIEW_TYPE_ITEM = 1
    }

    private val clickListener = ClickListener(clickListener)
    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun setList(list: List<User>?) {
        adapterScope.launch {
            withContext(Dispatchers.Main) {
                submitList(list)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val item = getItem(position) as User
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

    class ViewHolder private constructor(private val binding: ListitemUserBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: ClickListener, item: User) {
            binding.user = item
            binding.clickListener = clickListener

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListitemUserBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }

    class ClickListener(val clickListener: (Int) -> Unit) {
        fun onClick(item: User) = clickListener(item.id)
    }

    private class UserDiffCallback : DiffUtil.ItemCallback<User>() {

        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }
}
