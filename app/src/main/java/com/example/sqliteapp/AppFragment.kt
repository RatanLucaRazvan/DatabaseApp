package com.example.sqliteapp

import android.R
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.SearchView
import android.widget.Switch
import android.widget.Toast
import com.example.sqliteapp.databinding.FragmentAppBinding
import java.lang.Exception

class AppFragment : Fragment() {
    private var _binding: FragmentAppBinding? = null
    private val binding get() = _binding!!
    var customerArrayAdapter: ArrayAdapter<CustomerModel>? = null
    var dataBaseHelper: DataBaseHelper? = null
    var selectedCustomer: CustomerModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAppBinding.inflate(inflater, container, false)
        val view = binding.root
        setupElements()
        return view
    }

    private fun setupElements() {
        val addButton: Button = binding.addButton
        val viewButton: Button = binding.viewButton
        val deleteButton: Button = binding.deleteButton
        val updateButton: Button = binding.updateButton
        val nameEditText: EditText = binding.nameTextEdit
        val ageEditText: EditText = binding.ageTextEdit
        val isActiveSwitch: Switch = binding.activeSwitch
        val listView: ListView = binding.customerList
        val searchField: EditText = binding.searchView
//        val text: String = searchField.query.toString()

        dataBaseHelper = DataBaseHelper(requireContext())

        showCustomerOnListView(listView)

        addButton.setOnClickListener {
            var customerModel: CustomerModel
            try {
                customerModel = CustomerModel(
                    -1,
                    nameEditText.text.toString(),
                    Integer.parseInt(ageEditText.text.toString()),
                    isActiveSwitch.isChecked
                )
//                Log.d("Debug", isActiveSwitch.isChecked.toString())
                val dataBaseHelper: DataBaseHelper = DataBaseHelper(requireContext())
                val success: Boolean = dataBaseHelper.addOne(customerModel)
                Toast.makeText(context, customerModel.toString(), Toast.LENGTH_SHORT).show()
            }
            catch (e: Exception){
                Toast.makeText(context, "Error creating customer", Toast.LENGTH_SHORT).show()
                customerModel = CustomerModel(-1, "error", 0, false)
            }

            showCustomerOnListView(listView)
            nameEditText.text.clear()
            ageEditText.text.clear()
        }

        viewButton.setOnClickListener {
            val dataBaseHelper: DataBaseHelper = DataBaseHelper(requireContext())

            showCustomerOnListView(listView)
            searchField.text.clear()
//            Toast.makeText(context, everyone.toString(), Toast.LENGTH_SHORT).show()
        }

        listView.setOnItemClickListener { parent, view, position, id ->
                selectedCustomer = parent.getItemAtPosition(position) as CustomerModel
            Log.d("Debug", selectedCustomer.toString())
                selectedCustomer?.let {
                    nameEditText.setText(it.getName())
                    ageEditText.setText(it.getAge().toString())
                    isActiveSwitch.isChecked = it.getActive()!!
                }
        }

        deleteButton.setOnClickListener{
            if(selectedCustomer == null)
            {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            }
            selectedCustomer?.let {
                dataBaseHelper!!.deleteOne(it)
                showCustomerOnListView(listView)
                Toast.makeText(
                    context,
                    "Deleted " + it.toString(),
                    Toast.LENGTH_SHORT
                )
                    .show()
                selectedCustomer = null
                nameEditText.text.clear()
                ageEditText.text.clear()
                searchField.text.clear()
            }
        }

        updateButton.setOnClickListener {
            if(selectedCustomer == null)
            {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            }
            selectedCustomer?.let{
                dataBaseHelper!!.updateOne(it, nameEditText.text.toString(), ageEditText.text.toString().toInt(), isActiveSwitch.isChecked)
                showCustomerOnListView(listView)
                selectedCustomer = null
                nameEditText.text.clear()
                ageEditText.text.clear()
                searchField.text.clear()
            }
        }
        searchField.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                showFilteredCustomers(listView, s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}

        })

    }

    private fun showFilteredCustomers(listView: ListView, filter: String){
        customerArrayAdapter = ArrayAdapter<CustomerModel>(
            requireContext(),
            R.layout.simple_list_item_1,
            dataBaseHelper!!.getFiltered(filter)
        )
        listView.adapter = customerArrayAdapter
    }
    private fun showCustomerOnListView(listView: ListView) {
        customerArrayAdapter = ArrayAdapter<CustomerModel>(
            requireContext(),
            R.layout.simple_list_item_1,
            dataBaseHelper!!.getEveryone()
        )
        listView.adapter = customerArrayAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}