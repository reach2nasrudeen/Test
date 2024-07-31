package com.interview.test.ui.dashboard

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.interview.test.R
import com.interview.test.adapter.TransactionsAdapter
import com.interview.test.databinding.FragmentCardSummaryBinding
import com.interview.test.model.GraphType
import com.interview.test.viewmodel.CardSummaryViewModel
import com.patrykandpatrick.vico.core.cartesian.axis.AxisPosition
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.core.common.component.LineComponent
import com.patrykandpatrick.vico.core.common.component.TextComponent
import com.patrykandpatrick.vico.core.common.data.ExtraStore
import com.patrykandpatrick.vico.core.common.shape.Shape
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter


/**
 * A simple [Fragment] subclass.
 */
class CardSummaryFragment : Fragment() {

    private var monthChartModelProducer: CartesianChartModelProducer? = null
    private var yearChartModelProducer: CartesianChartModelProducer? = null
    private var dayChartModelProducer: CartesianChartModelProducer? = null

    private val viewModel: CardSummaryViewModel by viewModel<CardSummaryViewModel>()

    private var _binding: FragmentCardSummaryBinding? = null
    private val binding
        get() = _binding!!

    private var transactionsAdapter: TransactionsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCardSummaryBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.apply {
            ivBack.isVisible = true
            textTitle.setText(R.string.text_card_details)

            ivBack.setOnClickListener {
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
        }
        setupTransactions()
        setupTabs()
        setupObserver()

        viewModel.getCardSummary()

        binding.btnTryNow.setOnClickListener {
            viewModel.getCardSummary()
        }
    }

    private fun setupTransactions() {
        transactionsAdapter = TransactionsAdapter()
        binding.rvTransactions.adapter = transactionsAdapter
    }

    private fun setupTabs() {

        binding.tabLayout.getTabAt(viewModel.graphType.value?.position ?: 0)?.select()
        binding.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {

                when (tab?.text) {
                    getString(R.string.text_day) -> GraphType.DAY
                    getString(R.string.text_month) -> GraphType.MONTH
                    getString(R.string.text_yearly) -> GraphType.YEARLY
                    else -> null
                }?.let {
                    viewModel.updateSelectedGraphType(it)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // No-Op
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // No-Op
            }

        })
    }

    private fun setupObserver() {
        viewModel.transactions.observe(viewLifecycleOwner) {
            transactionsAdapter?.updateData(it)
            if (it.isNotEmpty()) {
                setupDayChart(viewModel.getTransactionsForDay())
                setupMonthChart(viewModel.getTransactionsForMonth())
                setupYearChart(viewModel.getTransactionsForYear())
            }
        }
    }

    private fun setupDayChart(data: Map<LocalDate, Double>) {
        if (data.isEmpty()) return

        binding.chartViewDay.apply {
            if (this.modelProducer == null) {
                dayChartModelProducer = CartesianChartModelProducer()
                this.modelProducer = dayChartModelProducer

                (chart?.bottomAxis as? HorizontalAxis)?.label = TextComponent(color = Color.BLACK)
                (chart?.startAxis as? VerticalAxis)?.label = TextComponent(color = Color.BLACK)
                chart?.marker = DefaultCartesianMarker(
                    label = TextComponent(color = Color.BLACK),
                    labelPosition = DefaultCartesianMarker.LabelPosition.AroundPoint,
                    guideline = LineComponent(
                        color = Color.BLACK,
                        shape = Shape.Rectangle,
                        strokeColor = Color.BLACK,
                        strokeThicknessDp = 5f
                    )
                )

                val xToDateMapKey = ExtraStore.Key<Map<Float, LocalDate>>()
                val xToDates = data.keys.associateBy { it.toEpochDay().toFloat() }
                val dateTimeFormatter = DateTimeFormatter.ofPattern("d MMM")

                (chart?.bottomAxis as HorizontalAxis<AxisPosition.Horizontal.Bottom>).valueFormatter =
                    CartesianValueFormatter { x, chartValues, _ ->
                        (chartValues.model.extraStore[xToDateMapKey][x.toFloat()]
                            ?: LocalDate.ofEpochDay(x.toLong()))
                            .format(dateTimeFormatter)
                    }
                lifecycleScope.launch {
                    dayChartModelProducer?.runTransaction {
                        lineSeries { series(xToDates.keys, data.values) }
                        extras { it[xToDateMapKey] = xToDates }
                    }
                }
            }
        }
    }

    private fun setupMonthChart(data: Map<LocalDate, Double>) {
        if (data.isEmpty()) return

        binding.chartViewMonth.apply {
            if (this.modelProducer == null) {
                monthChartModelProducer = CartesianChartModelProducer()
                this.modelProducer = monthChartModelProducer
                (chart?.bottomAxis as? HorizontalAxis)?.label = TextComponent(color = Color.BLACK)
                (chart?.startAxis as? VerticalAxis)?.label = TextComponent(color = Color.BLACK)

                chart?.marker = DefaultCartesianMarker(
                    label = TextComponent(color = Color.BLACK),
                    labelPosition = DefaultCartesianMarker.LabelPosition.AroundPoint,
                    guideline = LineComponent(
                        color = Color.BLACK,
                        shape = Shape.Pill,
                        strokeColor = Color.BLACK,
                        strokeThicknessDp = 5f
                    )
                )

                val xToDateMapKey = ExtraStore.Key<Map<Float, LocalDate>>()
                val xToDates = data.keys.associateBy { it.toEpochDay().toFloat() }
                val dateTimeFormatter = DateTimeFormatter.ofPattern("MMM")

                (chart?.bottomAxis as HorizontalAxis<AxisPosition.Horizontal.Bottom>).valueFormatter =
                    CartesianValueFormatter { x, chartValues, _ ->
                        (chartValues.model.extraStore[xToDateMapKey][x.toFloat()]
                            ?: LocalDate.ofEpochDay(x.toLong()))
                            .format(dateTimeFormatter)
                    }
                lifecycleScope.launch {
                    monthChartModelProducer?.runTransaction {
                        lineSeries { series(xToDates.keys, data.values) }
                        extras { it[xToDateMapKey] = xToDates }
                    }
                }
            }
        }
    }

    private fun setupYearChart(data: Map<Int, Double>) {
        if (data.isEmpty()) return

        binding.chartViewYear.apply {
            if (this.modelProducer == null) {
                yearChartModelProducer = CartesianChartModelProducer()
                this.modelProducer = yearChartModelProducer
            }

            (chart?.bottomAxis as? HorizontalAxis)?.label = TextComponent(color = Color.BLACK)
            (chart?.startAxis as? VerticalAxis)?.label = TextComponent(color = Color.BLACK)
            chart?.marker = DefaultCartesianMarker(
                label = TextComponent(color = Color.BLACK),
                labelPosition = DefaultCartesianMarker.LabelPosition.AroundPoint,
                guideline = LineComponent(
                    color = Color.BLACK,
                    shape = Shape.rounded(15),
                    strokeColor = Color.BLACK,
                    strokeThicknessDp = 5f
                )
            )

            lifecycleScope.launch {
                yearChartModelProducer?.runTransaction {
                    lineSeries { series(data.keys, data.values) }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        transactionsAdapter = null
    }

}