package com.quickdev.projectdashboard.viewmodels.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.quickdev.projectdashboard.databinding.DropdownItemBinding.inflate
import com.quickdev.projectdashboard.databinding.ListitemTeamBinding
import com.quickdev.projectdashboard.models.domain.Team
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TeamAdapter(private val clickListener: TeamItemClickListener):
    ListAdapter<TeamAdapter.DataItem, RecyclerView.ViewHolder>(TeamDiffCallback()) {

    companion object {
        private const val ITEM_VIEW_TYPE_ITEM = 1
    }

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun setList(list: List<Team>?) {
        adapterScope.launch {
            val items = list?.map { DataItem.Item(it) }

            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val item = getItem(position) as DataItem.Item
                holder.bind(clickListener, item.team)
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
            is DataItem.Item -> ITEM_VIEW_TYPE_ITEM
        }
    }

    class ViewHolder private constructor(private val binding: ListitemTeamBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: TeamItemClickListener, item: Team) {
            binding.team = item
            binding.clickListener = clickListener

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListitemTeamBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }

    class TeamDiffCallback : DiffUtil.ItemCallback<DataItem>() {

        override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem == newItem
        }
    }

    sealed class DataItem {
        data class Item(val team: Team) : DataItem() {
            override val id = team.id
        }

        abstract val id: Int
    }
}

class TeamItemClickListener(val clickListener: (teamId: Int) -> Unit) {
    fun onClick(team: Team) = clickListener(team.id)
}

class TeamDropdownAdapter(context: Context, private var mItems: List<Team>): BaseAdapter(), Filterable {

    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private var mFilter: Filter? = null

    private var mOriginalItems: List<Team>? = null

    override fun getCount(): Int {
        return mItems.size
    }

    override fun getItem(position: Int): Any {
        return mItems[position]
    }

    override fun getItemId(position: Int): Long {
        return mItems[position].id.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = inflate(mInflater)

        val item = getItem(position) as Team
        binding.txtDropdownItem.text = item.name

        return binding.txtDropdownItem
    }

    override fun getFilter(): Filter {
        if (mFilter == null)
            mFilter = TeamFilter()

        return mFilter!!
    }


    inner class TeamFilter(): Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()

            if (mOriginalItems == null)
                mOriginalItems = mItems

            var list = mOriginalItems!!

            if (constraint != null && !constraint.isBlank())
                list = list.filter { x -> x.name.contains(constraint, ignoreCase = true) }

            results.values = list
            results.count = list.size

            return results
        }

        @Suppress("unchecked_cast")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            mItems = results!!.values as List<Team>
            if (results.count == 0)
                notifyDataSetInvalidated()
            else
                notifyDataSetChanged()
        }

    }
}