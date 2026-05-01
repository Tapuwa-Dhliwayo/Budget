package com.example.budgettracker.ui.networth

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.budgettracker.R
import com.example.budgettracker.data.database.AppDatabase
import com.example.budgettracker.data.model.AssetType
import com.example.budgettracker.data.model.FinancialAsset
import com.example.budgettracker.data.model.NetWorthSnapshot
import com.example.budgettracker.data.model.NetWorthSummary
import com.example.budgettracker.data.repository.NetWorthRepository
import com.example.budgettracker.ui.common.configureToolbar
import com.example.budgettracker.utils.CurrencyUtils
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class NetWorthFragment : Fragment(R.layout.fragment_net_worth) {

    private val viewModel: NetWorthViewModel by viewModels {
        val db = AppDatabase.getInstance(requireContext())
        NetWorthViewModelFactory(
            NetWorthRepository(db.assetDao(), db.debtDao(), db.netWorthSnapshotDao())
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val summaryText: TextView = view.findViewById(R.id.text_net_worth_summary)
        val assetsContainer: LinearLayout = view.findViewById(R.id.layout_assets)
        val snapshotsContainer: LinearLayout = view.findViewById(R.id.layout_snapshots)
        val emptyAssets: TextView = view.findViewById(R.id.text_empty_assets)
        val snapshotButton: MaterialButton = view.findViewById(R.id.button_create_snapshot)
        val addAssetButton: FloatingActionButton = view.findViewById(R.id.fab_add_asset)

        snapshotButton.setOnClickListener { showSnapshotDialog() }
        addAssetButton.setOnClickListener { showAssetDialog() }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                state.error?.let {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                }
                state.summary?.let { summary ->
                    renderSummary(summaryText, summary)
                    renderAssets(assetsContainer, emptyAssets, summary.assets)
                    renderSnapshots(snapshotsContainer, summary.recentSnapshots)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        configureToolbar(
            title = "Net Worth",
            subtitle = "Assets minus debts",
            menuRes = null
        )
        viewModel.load()
    }

    private fun renderSummary(textView: TextView, summary: NetWorthSummary) {
        val movement = summary.movementSincePrevious?.let {
            val prefix = if (it >= 0.0) "+" else ""
            "\nLast snapshot movement: $prefix${CurrencyUtils.format(it)}"
        }.orEmpty()
        val debtMovement = summary.debtMovementSincePrevious?.let {
            "\nDebt movement: ${CurrencyUtils.format(it)} reduced"
        }.orEmpty()
        val assetMovement = summary.assetMovementSincePrevious?.let {
            val prefix = if (it >= 0.0) "+" else ""
            "\nAsset movement: $prefix${CurrencyUtils.format(it)}"
        }.orEmpty()

        textView.text = "Current Net Worth\n" +
                "${CurrencyUtils.format(summary.currentNetWorth)}\n" +
                "Assets: ${CurrencyUtils.format(summary.totalAssets)} · Debts: ${CurrencyUtils.format(summary.totalDebts)}" +
                movement + assetMovement + debtMovement + "\n\n" +
                summary.guidance
    }

    private fun renderAssets(
        container: LinearLayout,
        emptyText: TextView,
        assets: List<FinancialAsset>
    ) {
        container.removeAllViews()
        emptyText.visibility = if (assets.isEmpty()) View.VISIBLE else View.GONE
        assets.forEach { asset ->
            container.addView(createAssetCard(asset))
        }
    }

    private fun renderSnapshots(container: LinearLayout, snapshots: List<NetWorthSnapshot>) {
        container.removeAllViews()
        if (snapshots.isEmpty()) {
            container.addView(simpleText("No snapshots yet. Save one when your current position is ready."))
            return
        }
        snapshots.forEach { snapshot ->
            val movement = "Assets ${CurrencyUtils.format(snapshot.totalAssets)} · Debts ${CurrencyUtils.format(snapshot.totalDebts)}"
            container.addView(simpleText("${snapshot.snapshotDate}: ${CurrencyUtils.format(snapshot.netWorth)}\n$movement"))
        }
    }

    private fun createAssetCard(asset: FinancialAsset): View {
        val card = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(18, 16, 18, 16)
            setBackgroundResource(R.drawable.bg_expense_filter_panel)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 10, 0, 0)
            }
        }
        card.addView(TextView(requireContext()).apply {
            text = "${asset.name} · ${asset.assetType.label}"
            textSize = 17f
            setTextColor(requireContext().getColor(R.color.ink_primary))
            setTypeface(typeface, android.graphics.Typeface.BOLD)
        })
        card.addView(TextView(requireContext()).apply {
            text = CurrencyUtils.format(asset.currentValue)
            textSize = 22f
            setTextColor(requireContext().getColor(R.color.ink_primary))
            setTypeface(typeface, android.graphics.Typeface.BOLD)
        })
        if (asset.notes.isNotBlank()) {
            card.addView(simpleText(asset.notes))
        }
        card.addView(MaterialButton(requireContext()).apply {
            text = "Update value"
            setOnClickListener { showAssetDialog(asset) }
        })
        return card
    }

    private fun simpleText(textValue: String): TextView {
        return TextView(requireContext()).apply {
            text = textValue
            textSize = 14f
            setTextColor(requireContext().getColor(R.color.ink_secondary))
            setPadding(0, 8, 0, 8)
        }
    }

    private fun showAssetDialog(asset: FinancialAsset? = null) {
        val view = layoutInflater.inflate(R.layout.dialog_asset, null)
        val name = view.findViewById<EditText>(R.id.input_asset_name)
        val value = view.findViewById<EditText>(R.id.input_asset_value)
        val notes = view.findViewById<EditText>(R.id.input_asset_notes)
        val typeSpinner = view.findViewById<Spinner>(R.id.spinner_asset_type)

        setupSpinner(typeSpinner, AssetType.values().map { it.label })
        asset?.let {
            name.setText(it.name)
            name.isEnabled = false
            value.setText(it.currentValue.toString())
            notes.setText(it.notes)
            typeSpinner.setSelection(AssetType.values().indexOf(it.assetType))
            typeSpinner.isEnabled = false
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(if (asset == null) "Add Asset" else "Update Asset Value")
            .setView(view)
            .setPositiveButton("Save") { _, _ ->
                val amount = value.text.toString().toDoubleOrNull() ?: 0.0
                if (asset == null) {
                    viewModel.addAsset(
                        name = name.text.toString(),
                        assetType = AssetType.values()[typeSpinner.selectedItemPosition],
                        value = amount,
                        notes = notes.text.toString()
                    )
                } else {
                    viewModel.updateAssetValue(
                        assetId = asset.assetId,
                        value = amount,
                        notes = notes.text.toString()
                    )
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showSnapshotDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_net_worth_snapshot, null)
        val notes = view.findViewById<EditText>(R.id.input_snapshot_notes)
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Save Net Worth Snapshot")
            .setMessage("This records today’s assets, debts, and net worth so future months can show real movement.")
            .setView(view)
            .setPositiveButton("Save") { _, _ ->
                viewModel.createSnapshot(notes.text.toString())
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun setupSpinner(spinner: Spinner, items: List<String>) {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }
}
