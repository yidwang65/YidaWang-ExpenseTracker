package com.bignerdranch.android.Expenseintent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.criminalintent.databinding.ListItemExpenseBinding
import java.util.UUID

class ExpenseHolder(
    private val binding: ListItemExpenseBinding
) : RecyclerView.ViewHolder(binding.root) {
    private val dic: List<String> = listOf("Food", "Entertainment","Housing","Utilities","Fuel","Automotive","Misc")

    fun bind(expense: Expense, onExpenseClicked: (expenseId: UUID) -> Unit) {
        binding.expenseTitle.text = dic[expense.title]
        binding.expenseDate.text = expense.date.toString()

        binding.root.setOnClickListener {
            onExpenseClicked(expense.id)
        }
        binding.expenseAmount.text = expense.amount.toString()

    }
}

class ExpenseListAdapter(
    private val expenses: List<Expense>,
    private val onExpenseClicked: (expenseId: UUID) -> Unit
) : RecyclerView.Adapter<ExpenseHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExpenseHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemExpenseBinding.inflate(inflater, parent, false)
        return ExpenseHolder(binding)
    }

    override fun onBindViewHolder(holder: ExpenseHolder, position: Int) {
        val expense = expenses[position]
        holder.bind(expense, onExpenseClicked)
    }
    override fun getItemViewType(position: Int): Int {
        val expense = expenses[position]
        return expense.title
    }
    override fun getItemCount() = expenses.size
}
