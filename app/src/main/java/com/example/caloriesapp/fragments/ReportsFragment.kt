package com.example.caloriesapp.fragments

import AddWeightDialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.caloriesapp.R
import com.example.caloriesapp.databinding.FragmentReportsBinding
import com.example.caloriesapp.network.RetrofitInstance
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class WeightEntry(
    val date: String,
    val weight: Double
)

class ReportsFragment : Fragment() {

    private var _binding: FragmentReportsBinding? = null
    private val binding get() = _binding!!

    private lateinit var weightEntriesAdapter: WeightEntriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView
        binding.weightEntriesRecycler.layoutManager = LinearLayoutManager(requireContext())
        weightEntriesAdapter = WeightEntriesAdapter()
        binding.weightEntriesRecycler.adapter = weightEntriesAdapter
        context?.let { RetrofitInstance.init(it) }

        binding.btnAddWeight.setOnClickListener {
            // Logic to add a weight entry
            // Example: Open a dialog to input weight and date
            val addWeightDialog = AddWeightDialogFragment()
            addWeightDialog.show(parentFragmentManager, "AddWeightDialog")
        }


        fetchWeightEntries()
    }

    fun refreshData() {
        fetchWeightEntries() // Call the method that fetches the weight data and updates the chart
    }

    private fun fetchWeightEntries() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.baseApi.getWeights()
                if (response.isSuccessful) {
                    val weightEntries = response.body()
                    withContext(Dispatchers.Main) {
                        weightEntries?.let {
                            setupChart(it)
                            weightEntriesAdapter.submitList(it)
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireContext(),
                            "Failed to load weight data: ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireContext(),
                        "Error fetching weight data: ${e.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun setupChart(weightEntries: List<WeightEntry>) {
        val entries = weightEntries.mapIndexed { index, weightEntry ->
            Entry(index.toFloat(), weightEntry.weight.toFloat())
        }

        val lineDataSet = LineDataSet(entries, "Weight").apply {
            color = resources.getColor(android.R.color.holo_blue_dark, null)
            valueTextColor = resources.getColor(android.R.color.black, null)
            lineWidth = 2f
            circleRadius = 4f
            setCircleColor(resources.getColor(android.R.color.holo_blue_dark, null))
            setDrawFilled(true)
        }

        val lineData = LineData(lineDataSet)
        binding.weightChart.data = lineData
        binding.weightChart.description.isEnabled = false

        val xAxis = binding.weightChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.labelRotationAngle = -45f

        binding.weightChart.invalidate() // Refresh the chart
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class WeightEntriesAdapter : RecyclerView.Adapter<WeightEntriesAdapter.WeightViewHolder>() {

    private val weightEntries = mutableListOf<WeightEntry>()

    fun submitList(entries: List<WeightEntry>) {
        weightEntries.clear()
        weightEntries.addAll(entries)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeightViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_weight_entry, parent, false)
        return WeightViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeightViewHolder, position: Int) {
        val entry = weightEntries[position]
        holder.bind(entry)
    }

    override fun getItemCount(): Int = weightEntries.size

    class WeightViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dateView: TextView = itemView.findViewById(R.id.tv_date)
        private val weightView: TextView = itemView.findViewById(R.id.tv_weight)

        fun bind(entry: WeightEntry) {
            dateView.text = entry.date
            weightView.text = "${entry.weight} kg"
        }
    }
}

