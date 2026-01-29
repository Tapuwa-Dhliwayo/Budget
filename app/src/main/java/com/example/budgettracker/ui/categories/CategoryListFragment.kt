package com.example.budgettracker.ui.categories

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.budgettracker.R
import com.example.budgettracker.data.database.AppDatabase
import com.example.budgettracker.data.entity.CategoryEntity
import com.example.budgettracker.data.repository.CategoryRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class CategoryListFragment : Fragment(R.layout.fragment_category_list) {

    private val viewModel: CategoryViewModel by viewModels {
        val db = AppDatabase.getInstance(requireContext())
        CategoryViewModelFactory(CategoryRepository(db.categoryDao()))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val container: LinearLayout = view.findViewById(R.id.layout_categories)
        val fab: FloatingActionButton = view.findViewById(R.id.fab_add_category)

        fab.setOnClickListener {
            showCategoryDialog()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                container.removeAllViews()

                state.categories.forEach { category ->
                    val item = layoutInflater.inflate(
                        R.layout.item_category,
                        container,
                        false
                    )

                    item.findViewById<TextView>(R.id.text_icon).text = category.icon
                    item.findViewById<TextView>(R.id.text_name).text = category.name
                    item.findViewById<TextView>(R.id.text_limit).text =
                        "Limit: ${category.budgetLimit}"

                    item.setOnClickListener {
                        showCategoryDialog(category)
                    }

                    item.setOnLongClickListener {
                        confirmDelete(category)
                        true
                    }

                    container.addView(item)
                }
            }
        }

        viewModel.loadCategories()
    }

    private fun showCategoryDialog(category: CategoryEntity? = null) {
        val dialogView =
            layoutInflater.inflate(R.layout.dialog_category, null)

        val name = dialogView.findViewById<TextView>(R.id.input_name)
        val icon = dialogView.findViewById<TextView>(R.id.input_icon)
        val limit = dialogView.findViewById<TextView>(R.id.input_limit)

        category?.let {
            name.text = it.name
            icon.text = it.icon
            limit.text = it.budgetLimit.toString()
        }

        AlertDialog.Builder(requireContext())
            .setTitle(if (category == null) "Add Category" else "Edit Category")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val entity = CategoryEntity(
                    id = category?.id ?: 0,
                    name = name.text.toString(),
                    icon = icon.text.toString(),
                    color = category?.color ?: 0xFF009688.toInt(),
                    budgetLimit = limit.text.toString().toDoubleOrNull() ?: 0.0
                )

                viewLifecycleOwner.lifecycleScope.launch {
                    if (category == null) {
                        viewModel.addCategory(entity)
                    } else {
                        viewModel.updateCategory(entity)
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun confirmDelete(category: CategoryEntity) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete category?")
            .setMessage("This will hide the category but keep expense history.")
            .setPositiveButton("Delete") { _, _ ->
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.deleteCategory(category.id)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
