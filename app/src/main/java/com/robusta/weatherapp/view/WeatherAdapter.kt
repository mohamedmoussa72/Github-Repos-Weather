package com.robusta.weatherapp.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.robusta.data.model.location.WeatherData
import com.robusta.weatherapp.databinding.ItemWeatherImageBinding
import javax.inject.Inject

class WeatherAdapter @Inject constructor() : RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {
    lateinit var binding: ItemWeatherImageBinding
    lateinit var context: Context
    var list: MutableList<WeatherData> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemWeatherImageBinding.inflate(inflater, parent, false)
        context = parent.context
        return ViewHolder()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        list[position].let { holder.bindItems(it) }
        holder.setIsRecyclable(false)

    }
    fun add(data: List<WeatherData>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()

    }

    inner class ViewHolder : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bindItems(element: WeatherData) {
            binding.apply {
                Glide.with(itemView.context).load(element.image).into(ivItemWeatherImage)

                pbItemCategoryProgressBar.visibility = View.GONE
                tvItemWeatherCityName.text = element.citName
                tvItemWeatherDegree.text = element.degree
                tvItemWeatherMinDegree.text = element.degreeMin
                tvItemWeatherMaxDegree.text = element.degreeMax
                tvItemWeatherWindSpeed.text = element.windSpeed
                tvItemWeatherStatus.text = element.degreeStatus

            }
        }
    }

}
