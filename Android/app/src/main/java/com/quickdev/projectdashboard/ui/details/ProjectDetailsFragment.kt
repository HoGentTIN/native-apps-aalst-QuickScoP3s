package com.quickdev.projectdashboard.ui.details

import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.quickdev.projectdashboard.R
import com.quickdev.projectdashboard.data.database.getDatabase
import com.quickdev.projectdashboard.databinding.FragmentDetailsProjectBinding
import com.quickdev.projectdashboard.viewmodels.ProjectDetailsViewModel
import com.quickdev.projectdashboard.viewmodels.adapters.TaskAdapter

class ProjectDetailsFragment: Fragment() {

	private lateinit var binding: FragmentDetailsProjectBinding
	private lateinit var tasksAdapter: TaskAdapter

	private val navArgs: ProjectDetailsFragmentArgs by navArgs()
	private val viewModel: ProjectDetailsViewModel by lazy {
		val activity = requireNotNull(this.activity) {
			"You can only access the viewModel after onActivityCreated()"
		}

		val projectId = navArgs.projectId
		val database = getDatabase(activity)

		ViewModelProviders
			.of(this, ProjectDetailsViewModel.Factory(projectId, database))
			.get(ProjectDetailsViewModel::class.java)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		binding = FragmentDetailsProjectBinding.inflate(inflater)

		viewModel.setInitialNames(navArgs.projectName, navArgs.teamName)

		binding.viewModel = viewModel
		binding.lifecycleOwner = this

		binding.tabsProjectdetailsTasks.addOnTabSelectedListener(tabSelectedListener)

		tasksAdapter = TaskAdapter { taskId ->

		}

		binding.listProjectDetailsTasks.layoutManager = LinearLayoutManager(context)
		binding.listProjectDetailsTasks.adapter = tasksAdapter
		ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.listProjectDetailsTasks)

		startListeners()
		registerObservers()

		return binding.root
	}

	private fun startListeners() {
		binding.pullRefreshProjectdetails.setOnRefreshListener {
			binding.viewModel?.refreshData()
		}
	}

	private fun registerObservers() {
		binding.viewModel?.tasks?.observe(this, Observer { tasks ->
			if (!viewModel.isFinshedTabSelected) {
				val badge = binding.tabsProjectdetailsTasks.getTabAt(0)?.orCreateBadge

				if (tasks.isNotEmpty()) {
					badge?.isVisible = true
					badge?.number = tasks.size
				}
				else
					badge?.isVisible = false
			}

			tasksAdapter.setList(tasks)
		})

		binding.viewModel?.isLoading?.observe(this, Observer { isLoading: Boolean? ->
			if (isLoading != null)
				binding.pullRefreshProjectdetails.isRefreshing = isLoading
		})

		binding.viewModel?.taskFinishedResponse?.observe(this, Observer { httpCode: Int? ->
			if (httpCode != null) {
				when (httpCode) {
					200 -> { }
					504 -> Snackbar.make(
						binding.listProjectDetailsTasks,
						R.string.httperror_504,
						Snackbar.LENGTH_SHORT
					).show()
					else -> Snackbar.make(
						binding.listProjectDetailsTasks,
						R.string.httperror_400,
						Snackbar.LENGTH_SHORT
					).show()
				}
			}
		})
	}

	override fun onDestroyView() {
		super.onDestroyView()
		binding.tabsProjectdetailsTasks.removeOnTabSelectedListener(tabSelectedListener)
	}

	private val tabSelectedListener = object: TabLayout.OnTabSelectedListener {
		override fun onTabReselected(tab: TabLayout.Tab?) { }

		override fun onTabUnselected(tab: TabLayout.Tab?) { }

		override fun onTabSelected(tab: TabLayout.Tab?) {
			when(tab) {
				binding.tabsProjectdetailsTasks.getTabAt(0) -> binding.viewModel?.isFinshedTabSelected = false
				binding.tabsProjectdetailsTasks.getTabAt(1) -> binding.viewModel?.isFinshedTabSelected = true
			}

			binding.viewModel?.refreshData()
		}
	}

	private val itemTouchHelperCallback = object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
		override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean = false

		override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
			val task = tasksAdapter.getItemAt(viewHolder.adapterPosition)
			binding.viewModel?.setFinished(task.id)

			// Reset swipe
			tasksAdapter.notifyItemChanged(viewHolder.adapterPosition)
		}

		override fun isItemViewSwipeEnabled(): Boolean {
			return !viewModel.isFinshedTabSelected // If we're on the 'open tasks' page, enable swipe
		}

		override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
			val foregroundView = (viewHolder as TaskAdapter.ViewHolder).viewForeground
			getDefaultUIUtil().clearView(foregroundView)
		}

		override fun onChildDrawOver(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder?, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
			val foregroundView = (viewHolder as TaskAdapter.ViewHolder).viewForeground
			getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);
		}

		override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
			val foregroundView = (viewHolder as TaskAdapter.ViewHolder).viewForeground
			getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);
		}
	}
}