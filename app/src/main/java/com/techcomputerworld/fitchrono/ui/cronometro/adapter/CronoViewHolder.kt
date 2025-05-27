package com.techcomputerworld.fitchrono.ui.cronometro.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.techcomputerworld.fitchrono.databinding.ItemCronoBinding
import com.techcomputerworld.fitchrono.domain.model.CronoInfo

class CronoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemCronoBinding.bind(view)


    fun render(cronoInfo: CronoInfo) {

        binding.numOrder.text = cronoInfo.numOrder.toString()
        binding.time.text = cronoInfo.time
        binding.sumTime.text = cronoInfo.timeSum
    }

}

