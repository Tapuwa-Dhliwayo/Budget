package com.example.budgettracker.ui.categories

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.budgettracker.R
import com.example.budgettracker.data.database.AppDatabase
import com.example.budgettracker.data.repository.CategoryRepository
import kotlinx.coroutines.launch

class CategoryListFragment : Fragment(R.layout.fragment_category_list) {

    private val viewModel: CategoryViewModel by viewModels {
        val database = AppDatabase.getInstance(requireContext())
        val repository = CategoryRepository(database.categoryDao())
        CategoryViewModelFactory(repository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_categories)
        val emptyText: TextView = view.findViewById(R.id.text_empty)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Simple adapter for categories (would implement full adapter similar to expenses)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    // Update UI
                    emptyText.visibility = if (state.categories.isEmpty()) View.VISIBLE else View.GONE
                }
            }
        }
    }
}