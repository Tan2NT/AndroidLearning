package com.tantnt.forecast.ui.weather.future.list

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager

import com.tantnt.forecast.R
import com.tantnt.forecast.data.db.entity.LocalDateTimeConverter
import com.tantnt.forecast.data.db.unitlocalized.future.list.UnitSpecificsimpleFutureWeatherEntry
import com.tantnt.forecast.ui.base.ScopeFragment
import com.xwray.groupie.*
import com.xwray.groupie.groupiex.plusAssign
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.future_list_weather_fragment.*
import kotlinx.android.synthetic.main.header_item.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import org.threeten.bp.LocalDate

class FutureListWeatherFragment : ScopeFragment(), KodeinAware{

    override val kodein by  closestKodein()
    private val viewModalFactory : FutureListWeatherViewModalFactory by instance()


    private lateinit var viewModel: FutureListWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.future_list_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModalFactory)
		.get(FutureListWeatherViewModel::class.java)
        bindUI()
        (activity as? AppCompatActivity)?.supportActionBar?.title =  getString(R.string.next_16_days)
    }

    private fun bindUI() = launch (Dispatchers.Main){
        val futureWeatherEntries = viewModel.futureWeatherEntries.await()

        futureWeatherEntries.observe(this@FutureListWeatherFragment, Observer {
            if(it == null) return@Observer

            group_loading.visibility = View.GONE

            updateWeatherToNext16Days()

            initRecycleView(it.toFutureWeatherItems())
        })
    }

    private fun updateWeatherToNext16Days(){
        (activity as? AppCompatActivity)?.supportActionBar?.title = viewModel.getRequestedCity()
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = getString(R.string.next_16_days)
    }

    private fun List<UnitSpecificsimpleFutureWeatherEntry>.toFutureWeatherItems() : List<FutureWeatherItem>{
        return this.map {
            FutureWeatherItem(it)
        }
    }

    val dataSection = Section()

    private fun initRecycleView(items : List<FutureWeatherItem>){

        var groupAdapter = GroupAdapter<ViewHolder>().apply {
            //addAll(items)
        }

        //groupAdapter += HeaderItem("Tin tin")
        val section = Section()
        section.setHeader(HeaderItem("Tin tin"))
        section.addAll(items)
        groupAdapter += section


        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@FutureListWeatherFragment.context)
            adapter = groupAdapter
        }

        groupAdapter.setOnItemClickListener{ item, view ->
            (item as? FutureWeatherItem)?.let {
                showWeatherDetail(item.weatherEntry.datetime, view)
            }
        }
    }

    private  fun showWeatherDetail(date: LocalDate, view : View){
        val dateString = LocalDateTimeConverter.toDateString(date)!!
        val actionDetail = FutureListWeatherFragmentDirections.actionDetail(dateString)
        Navigation.findNavController(view).navigate(actionDetail)
    }

}

class HeaderItem (private val title: String) : Item() {
    override fun bind(
        viewHolder: com.xwray.groupie.kotlinandroidextensions.ViewHolder,
        position: Int
    ) {
        viewHolder.header_title.text = title
    }

    override fun getLayout(): Int {
        return R.layout.header_item
    }

}

