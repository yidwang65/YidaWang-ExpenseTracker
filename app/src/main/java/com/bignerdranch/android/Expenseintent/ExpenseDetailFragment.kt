package com.bignerdranch.android.Expenseintent

import android.content.Intent
import android.util.Log
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bignerdranch.android.criminalintent.R
import com.bignerdranch.android.criminalintent.databinding.FragmentExpenseDetailBinding
import kotlinx.coroutines.launch
import java.util.Date

private const val DATE_FORMAT = "EEE, MMM, dd"
private const val TAG = "ExpenseListFragment"


class ExpenseDetailFragment : Fragment() {
    private val _dic: List<String> = listOf("Food", "Entertainment","Housing","Utilities","Fuel","Automotive","Misc")
    private var _binding: FragmentExpenseDetailBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val args: ExpenseDetailFragmentArgs by navArgs()

    private val expenseDetailViewModel: ExpenseDetailViewModel by viewModels {
        ExpenseDetailViewModelFactory(args.expenseId)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            FragmentExpenseDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.apply {
//            expenseTitle.doOnTextChanged { text, _, _, _ ->
//                expenseDetailViewModel.updateExpense { oldExpense ->
//                    oldExpense.copy(title = text.toString())
//                }
//            }
//        }

        binding.apply {
            expenseTitle.doOnTextChanged { text, _, _, _ ->
                if(text.toString().toInt() in 0..6){
                    expenseDetailViewModel.updateExpense { oldExpense ->
                        oldExpense.copy(title = text.toString().toInt())
                    }
            }
            }
            expenseAmount.doOnTextChanged { text, _, _, _ ->
                expenseDetailViewModel.updateExpense { oldExpense ->
                    oldExpense.copy(amount = text.toString().toDouble())
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                expenseDetailViewModel.expense.collect { expense ->
                    expense?.let { updateUi(it) }
                }
            }
        }

        setFragmentResultListener(
            DatePickerFragment.REQUEST_KEY_DATE
        ) { _, bundle ->
            val newDate =
                bundle.getSerializable(DatePickerFragment.BUNDLE_KEY_DATE) as Date
            expenseDetailViewModel.updateExpense { it.copy(date = newDate) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateUi(expense: Expense) {
        binding.apply {

            if (expenseTitle.text.toString() != expense.title.toString()) {
                expenseTitle.setText(expense.title.toString())
            }

            expenseDate.text = expense.date.toString()
            expenseDate.setOnClickListener {
                findNavController().navigate(
                    ExpenseDetailFragmentDirections.selectDate(expense.date)
                )
            }
            if (expenseAmount.text.toString() != expense.amount.toString()) {
                expenseAmount.setText(expense.amount.toString())
            }


        }
    }


}
