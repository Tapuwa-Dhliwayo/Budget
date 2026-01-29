package com.example.budgettracker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.budgettracker.R
import com.example.budgettracker.data.model.ExpenseWithCategory
import com.example.budgettracker.utils.CurrencyUtils
import com.example.budgettracker.utils.DateUtils

class ExpenseAdapter(
    private val onExpenseClick: (ExpenseWithCategory) -> Unit,
    private val onExpenseLongClick: (ExpenseWithCategory) -> Boolean
) : ListAdapter<ExpenseWithCategory, ExpenseAdapter.ExpenseViewHolder>(ExpenseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expense, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val iconText: TextView = itemView.findViewById(R.id.text_category_icon)
        private val amountText: TextView = itemView.findViewById(R.id.text_amount)
        private val descriptionText: TextView = itemView.findViewById(R.id.text_description)
        private val categoryText: TextView = itemView.findViewById(R.id.text_category)
        private val dateText: TextView = itemView.findViewById(R.id.text_date)
        private val photoIndicator: ImageView = itemView.findViewById(R.id.icon_photo)

        fun bind(expense: ExpenseWithCategory) {
            iconText.text = expense.categoryIcon
            iconText.setBackgroundColor(expense.categoryColor)

            amountText.text = CurrencyUtils.format(expense.amount)
            descriptionText.text = expense.description
            categoryText.text = expense.categoryName

            dateText.text = when {
                DateUtils.isToday(expense.date) -> "Today"
                DateUtils.isYesterday(expense.date) -> "Yesterday"
                else -> DateUtils.formatDateForDisplay(expense.date)
            }

            photoIndicator.visibility = if (expense.photoPath != null) View.VISIBLE else View.GONE

            itemView.setOnClickListener { onExpenseClick(expense) }
            itemView.setOnLongClickListener { onExpenseLongClick(expense) }
        }
    }

    class ExpenseDiffCallback : DiffUtil.ItemCallback<ExpenseWithCategory>() {
        override fun areItemsTheSame(oldItem: ExpenseWithCategory, newItem: ExpenseWithCategory): Boolean {
            return oldItem.expenseId == newItem.expenseId
        }

        override fun areContentsTheSame(oldItem: ExpenseWithCategory, newItem: ExpenseWithCategory): Boolean {
            return oldItem == newItem
        }
    }
}