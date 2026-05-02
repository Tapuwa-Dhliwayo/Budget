package com.example.budgettracker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.budgettracker.R
import com.example.budgettracker.data.model.CategorySpending
import com.example.budgettracker.utils.CurrencyUtils
import com.example.budgettracker.utils.PercentageUtils

class CategorySpendingAdapter :
    ListAdapter<CategorySpending, CategorySpendingAdapter.SpendingViewHolder>(SpendingDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpendingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category_spending, parent, false)
        return SpendingViewHolder(view)
    }

    override fun onBindViewHolder(holder: SpendingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class SpendingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val iconText: TextView = itemView.findViewById(R.id.text_category_icon)
        private val nameText: TextView = itemView.findViewById(R.id.text_category_name)
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progress_budget)
        private val spentText: TextView = itemView.findViewById(R.id.text_spent)
        private val statusText: TextView = itemView.findViewById(R.id.text_status)

        fun bind(spending: CategorySpending) {
            iconText.text = spending.categoryIcon
            iconText.setBackgroundColor(spending.categoryColor)
            nameText.text = spending.categoryName

            val progress = spending.percentageUsed.coerceIn(0.0, 100.0).toInt()
            progressBar.progress = progress

            val progressColor = when {
                spending.isOverBudget -> ContextCompat.getColor(itemView.context, R.color.ra_danger)
                spending.percentageUsed >= 80 -> ContextCompat.getColor(itemView.context, R.color.ra_warning)
                else -> ContextCompat.getColor(itemView.context, R.color.ra_success)
            }
            progressBar.progressTintList = android.content.res.ColorStateList.valueOf(progressColor)

            spentText.text = "${CurrencyUtils.format(spending.totalSpent)} / ${CurrencyUtils.format(spending.budgetLimit)} shield"

            statusText.text = when {
                spending.isOverBudget -> {
                    statusText.setTextColor(ContextCompat.getColor(itemView.context, R.color.ra_danger))
                    "${CurrencyUtils.format(-spending.remaining)} shield breach"
                }
                spending.remaining > 0 -> {
                    statusText.setTextColor(ContextCompat.getColor(itemView.context, R.color.ra_success))
                    "${PercentageUtils.formatWhole(spending.percentageUsed)} used · ${CurrencyUtils.format(spending.remaining)} shield strength"
                }
                else -> {
                    statusText.setTextColor(ContextCompat.getColor(itemView.context, R.color.ra_warning))
                    "Shield cap reached"
                }
            }
        }
    }

    class SpendingDiffCallback : DiffUtil.ItemCallback<CategorySpending>() {
        override fun areItemsTheSame(oldItem: CategorySpending, newItem: CategorySpending): Boolean {
            return oldItem.categoryId == newItem.categoryId
        }

        override fun areContentsTheSame(oldItem: CategorySpending, newItem: CategorySpending): Boolean {
            return oldItem == newItem
        }
    }
}
