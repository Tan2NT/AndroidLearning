package com.example.forecastmvvm.ui.weather.future.list

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.forecastmvvm.R
import com.example.forecastmvvm.data.db.unitlocalized.future.UnitSpecificsimpleFutureWeatherEntry
import com.example.forecastmvvm.ui.base.ScopeFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.future_list_weather_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

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
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "Next 16 days"
    }

    private fun List<UnitSpecificsimpleFutureWeatherEntry>.toFutureWeatherItems() : List<FutureWeatherItem>{
        return this.map {
            FutureWeatherItem(it)
        }
    }

    private fun initRecycleView(items : List<FutureWeatherItem>){
        val groupAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(items)
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@FutureListWeatherFragment.context)
            adapter = groupAdapter
        }

        groupAdapter.setOnItemClickListener{ item, view ->
            Toast.makeText(this@FutureListWeatherFragment.context, "clicked", Toast.LENGTH_LONG).show()
        }
    }

}
