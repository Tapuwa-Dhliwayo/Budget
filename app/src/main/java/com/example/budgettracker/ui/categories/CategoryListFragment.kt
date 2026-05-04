package com.example.budgettracker.ui.categories

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.budgettracker.R
import com.example.budgettracker.data.database.AppDatabase
import com.example.budgettracker.data.entity.CategoryEntity
import com.example.budgettracker.data.repository.CategoryRepository
import com.example.budgettracker.data.repository.UserProfileRepository
import com.example.budgettracker.ui.common.configureToolbar
import com.example.budgettracker.utils.CurrencyUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.progressindicator.LinearProgressIndicator
import kotlinx.coroutines.launch

class CategoryListFragment : Fragment(R.layout.fragment_category_list) {

    private val viewModel: CategoryViewModel by viewModels {
        val db = AppDatabase.getInstance(requireContext())
        CategoryViewModelFactory(CategoryRepository(db.categoryDao()))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Category list components
        val container: LinearLayout = view.findViewById(R.id.layout_categories)
        val fab: FloatingActionButton = view.findViewById(R.id.fab_add_category)

        fab.setOnClickListener {
            showCategoryDialog()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                container.removeAllViews()
                val strongestShield = state.categories.maxOfOrNull { it.budgetLimit }?.coerceAtLeast(1.0) ?: 1.0

                state.categories.forEachIndexed { index, category ->
                    val item = layoutInflater.inflate(
                        R.layout.item_category,
                        container,
                        false
                    )

                    val shieldPercent = ((category.budgetLimit / strongestShield) * 100.0).toInt().coerceIn(0, 100)
                    val statusColor = colorForShieldCap(category.budgetLimit)

                    (item as MaterialCardView).apply {
                        strokeColor = requireContext().getColor(statusColor)
                        strokeWidth = if (category.budgetLimit <= 0.0) dp(2) else dp(1)
                    }
                    item.findViewById<TextView>(R.id.text_icon).text = category.icon
                    item.findViewById<TextView>(R.id.text_name).text = category.name
                    item.findViewById<TextView>(R.id.text_loadout_rank).text =
                        "#${(index + 1).toString().padStart(2, '0')}"
                    item.findViewById<TextView>(R.id.text_category_status).apply {
                        text = shieldStatusLabel(category.budgetLimit)
                        setTextColor(requireContext().getColor(statusColor))
                    }
                    item.findViewById<LinearProgressIndicator>(R.id.progress_category_shield).apply {
                        progress = shieldPercent
                        setIndicatorColor(requireContext().getColor(statusColor))
                        trackColor = requireContext().getColor(R.color.ra_surface_elevated)
                    }
                    item.findViewById<TextView>(R.id.text_limit).text =
                        "Shield cap: ${CurrencyUtils.format(category.budgetLimit)} · ${loadoutAdvice(category.budgetLimit)}"

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

    override fun onResume() {
        super.onResume()

        viewLifecycleOwner.lifecycleScope.launch {
            val db = AppDatabase.getInstance(requireContext())
            val userRepo = UserProfileRepository(db.userProfileDao())
            val user = userRepo.getOrCreateUser()

            configureToolbar(
                title = "Spending Loadout",
                subtitle = "Tune the categories that shape your recovery run, ${user.firstName}.",
                menuRes = null
            )
        }
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
            .setTitle(if (category == null) "Add Loadout Category" else "Tune Loadout Category")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                if (name.text.toString().isBlank()) {
                    Toast.makeText(requireContext(), "Category name is required.", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
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
            .setTitle("Archive loadout category?")
            .setMessage("This hides the category but keeps the spending log history intact.")
            .setPositiveButton("Archive") { _, _ ->
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.deleteCategory(category.id)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun shieldStatusLabel(limit: Double): String {
        return when {
            limit <= 0.0 -> "NO SHIELD"
            limit >= 3000.0 -> "HEAVY SHIELD"
            limit >= 1000.0 -> "READY"
            else -> "LIGHT SHIELD"
        }
    }

    private fun colorForShieldCap(limit: Double): Int {
        return when {
            limit <= 0.0 -> R.color.ra_danger
            limit >= 3000.0 -> R.color.ra_primary
            limit >= 1000.0 -> R.color.ra_success
            else -> R.color.ra_warning
        }
    }

    private fun loadoutAdvice(limit: Double): String {
        return when {
            limit <= 0.0 -> "set a cap to activate this defense"
            limit >= 3000.0 -> "high-cap category"
            limit >= 1000.0 -> "active defense"
            else -> "tight cap, watch closely"
        }
    }

    private fun dp(value: Int): Int {
        return (value * resources.displayMetrics.density).toInt()
    }
}
