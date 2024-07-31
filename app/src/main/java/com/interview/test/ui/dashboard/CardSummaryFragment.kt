package com.interview.test.ui.dashboard

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.interview.test.R
import com.interview.test.adapter.TransactionsAdapter
import com.interview.test.base.Constants
import com.interview.test.databinding.FragmentCardSummaryBinding
import com.interview.test.model.CardResponse
import com.interview.test.model.Transaction
import com.interview.test.utils.getColorRes
import com.interview.test.utils.getObjectFromJson
import com.interview.test.utils.toModelString
import com.interview.test.viewmodel.CardSummaryViewModel
import com.patrykandpatrick.vico.core.cartesian.axis.AxisPosition
import com.patrykandpatrick.vico.core.cartesian.axis.BaseAxis
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.core.common.component.TextComponent
import com.patrykandpatrick.vico.core.common.data.ExtraStore
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import kotlin.math.abs
import kotlin.random.Random


/**
 * A simple [Fragment] subclass.
 */
class CardSummaryFragment : Fragment() {

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

    fun parseDate(input: String): Date {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//        dateFormat.timeZone = TimeZone.getTimeZone(ZoneId.systemDefault())
        return dateFormat.parse(input)!!
    }

    fun toText(input: Date): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return dateFormat.format(input)
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
        setupObserver()

//        viewModel.getCardSummary()

        lifecycleScope.launch {
            requireContext().getObjectFromJson<CardResponse>("card_summary.json").let {
                viewModel.updateCardSummary(it)
            }
        }

        binding.btnTryNow.setOnClickListener {
            viewModel.getCardSummary()
        }

        viewModel.transactions.observe(viewLifecycleOwner) { transactions ->


            transactions.filter { it.status == "success" && it.type == "debit" }
                .groupBy { LocalDate.parse(it.date.orEmpty(), DateTimeFormatter.ofPattern(Constants.TF_DEFAULT_1)) }
//                .mapKeys { entry -> LocalDate.parse(entry.key) }
                .mapValues { entry -> entry.value.sumOf { abs(it.amount ?: 0.0) } }
                .also {
                    if (it.keys.isNotEmpty()) {
                        setupChart2(it)
                    }
                }
                .forEach { transaction ->
                    Timber.e("date---->${transaction.key}-------amount---->${transaction.value}")

//                val date = parseDate(transaction.date.orEmpty())
//                val dateOnly = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
//                dailyTotals[dateOnly] = (dailyTotals.getOrDefault(dateOnly, 0f) + (transaction.amount ?: 0.0)).toFloat()
                }
        }

//        setupChart(viewModel.transactions.value.orEmpty())
        /*binding.chartView.apply {
//            chart?.persistentMarkers = { marker at PERSISTENT_MARKER_X }
//            PersistentMarkerScope
//            val horizontalLineYKey = ExtraStore.Key<Float>()
            val data = listOf("2022-07-01" to 2f, "2022-07-02" to 6f, "2022-07-04" to 4f).associate { (dateString, yValue) ->
                LocalDate.parse(dateString) to yValue
            }
            val xValuesToDates = data.keys.associateBy { it.toEpochDay().toFloat() }
//            val chartEntryModel = entryModelOf(xValuesToDates.keys.zip(data.values, ::entryOf))
//            val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("d MMM")
//            val horizontalAxisValueFormatter = AxisValueFormatter<AxisPosition.Horizontal> { value, _ ->
//                (xValuesToDates[value] ?: LocalDate.ofEpochDay(value.toLong())).format(dateTimeFormatter)
//            }

            chart?.bottomAxis  = HorizontalAxis.bottom(
                label = TextComponent(color = Color.BLACK),
            )
            chart?.startAxis  = VerticalAxis.start(
                label = TextComponent(color = Color.BLUE),
                titleComponent = TextComponent(color = Color.RED),
            )
//            ( chart?.bottomAxis as? HorizontalAxis)?.titleComponent = TextComponent(color = Color.RED)
//            startAxis
//            this.setModel(chartEntryModel)

            val modelProducer = CartesianChartModelProducer()

//            val data =
//                mapOf(
//                    LocalDate.parse("2022-07-01") to 2f,
//                    LocalDate.parse("2022-07-02") to 6f,
//                    LocalDate.parse("2022-07-04") to 4f,
//                )
            val xToDateMapKey = ExtraStore.Key<Map<Float, LocalDate>>()
//            chart?.bottomAxis = HorizontalAxis.buil {
//                valueFormatter = { x, _ -> "Label $x" }
//                // Other configurations
//            }
            val xToDates = data.keys.associateBy { it.toEpochDay().toFloat() }
//            modelProducer.runTransaction {
//                lineSeries { series(xToDates.keys, data.values) }
//                extras { it[xToDateMapKey] = xToDates }
//            }

            val dateTimeFormatter = DateTimeFormatter.ofPattern("d MMM")
//            CartesianValueFormatter { x, chartValues, _ ->
//                (chartValues.model.extraStore[xToDateMapKey][x] ?: LocalDate.ofEpochDay(x.toLong()))
//                    .format(dateTimeFormatter)
//            }

            val marker = DefaultCartesianMarker(
                label = TextComponent()
            )

//            (chart?.bottomAxis as BaseAxis).guideline = null
//            (chart?.bottomAxis as BaseAxis).titleComponent = TextComponent(
//                color = requireContext().getColorRes(R.color.red)
//            )
            this.modelProducer = modelProducer
            chart?.persistentMarkers = { marker at PERSISTENT_MARKER_X }
            lifecycleScope.launch {
                modelProducer.runTransaction {
//                    lineSeries { series(xValue, xValue.map { Random.nextFloat() * 15 }) }
                    lineSeries { series(xValuesToDates.keys, data.values) }
                    extras { it[xToDateMapKey] = xToDates }
                }

            }
        }*/
    }
//    fun parseDate(dateStr: String): Date {
//        val dateFormat = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
//        dateFormat.timeZone = TimeZone.getTimeZone(ZoneId.systemDefault())
//        return dateFormat.parse(dateStr)!!
//    }

    private fun setupChart(transactions: List<Transaction>) {
//        transactions.filter { it.status == "success" && it.type == "debit"}.sortedByDescending { parseDate(it.date.orEmpty()) }.forEach { transaction ->
//            val date = parseDate(transaction.date.orEmpty())
//            val dateOnly = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
//            dailyTotals[dateOnly] = (dailyTotals.getOrDefault(dateOnly, 0f) + (transaction.amount ?: 0.0)).toFloat()
//        }

        binding.chartView.apply {

            (chart?.bottomAxis as? HorizontalAxis)?.label = TextComponent(
                color = Color.BLACK
            )
            (chart?.startAxis as? VerticalAxis)?.label = TextComponent(
                color = Color.BLACK
            )

            val modelProducer = CartesianChartModelProducer()


//            chart?.bottomAxis = HorizontalAxis.bottom(
//                label = TextComponent(color = Color.BLACK),
//            )
//            chart?.startAxis = VerticalAxis.start(
//                label = TextComponent(color = Color.BLACK),
//            )

//            val marker = DefaultCartesianMarker()

            (chart?.bottomAxis as BaseAxis).titleComponent = TextComponent(
                color = Color.RED
            )
            this.modelProducer = modelProducer
//            chart?.persistentMarkers = { marker at PERSISTENT_MARKER_X }
            lifecycleScope.launch {
                modelProducer.runTransaction {
                    lineSeries { series(xValue, xValue.map { Random.nextFloat() * 15 }) }
//                    lineSeries { series(xValuesToDates.keys, data.values) }
//                    extras { it[xToDateMapKey] = xToDates }
                }


            }
        }
    }

    private fun setupChart2(data: Map<LocalDate, Double>) {
        Timber.e("setupChart2--came here")
        val xValues = data.keys.toList()
        val yValues = data.values.toList()
//        transactions.filter { it.status == "success" && it.type == "debit"}.sortedByDescending { parseDate(it.date.orEmpty()) }.forEach { transaction ->
//            val date = parseDate(transaction.date.orEmpty())
//            val dateOnly = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
//            dailyTotals[dateOnly] = (dailyTotals.getOrDefault(dateOnly, 0f) + (transaction.amount ?: 0.0)).toFloat()
//        }
        val modelProducer = CartesianChartModelProducer()
        binding.chartView.apply {
            this.modelProducer = modelProducer
            (chart?.bottomAxis as? HorizontalAxis)?.label = TextComponent(
                color = Color.BLACK
            )
            (chart?.startAxis as? VerticalAxis)?.label = TextComponent(
                color = Color.BLACK
            )
//            (chart?.bottomAxis as HorizontalAxis<AxisPosition.Horizontal.Bottom>).valueFormatter = CartesianValueFormatter { x, _, _ ->
//                xValues[x.toInt()]
//                }
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
                modelProducer.runTransaction {
                    lineSeries { series(xToDates.keys, data.values) }
                    extras { it[xToDateMapKey] = xToDates }
                }

            }


//            val labelListKey = ExtraStore.Key<List<String>>()
//
//            (chart?.bottomAxis as HorizontalAxis<AxisPosition.Horizontal.Bottom>).valueFormatter =
//                CartesianValueFormatter { x, chartValues, _ ->
//                    chartValues.model.extraStore[labelListKey][x.toInt()]
//                }



//            chart?.persistentMarkers = { marker at PERSISTENT_MARKER_X }
//            lifecycleScope.launch {
//                modelProducer.runTransaction {
//                    lineSeries { series(yValues) }
////                    lineSeries { series(xValuesToDates.keys, data.values) }
//                    extras { it[labelListKey] = xValues }
//
//                }
//            }
        }
    }

    companion object {
        private val xValue = (1..50).toList()

        private const val PERSISTENT_MARKER_X = 7f

    }

    private fun setupTransactions() {
        transactionsAdapter = TransactionsAdapter()
        binding.rvTransactions.adapter = transactionsAdapter
    }

    private fun setupObserver() {
        viewModel.transactions.observe(viewLifecycleOwner) {
            transactionsAdapter?.updateData(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        transactionsAdapter = null
    }

}