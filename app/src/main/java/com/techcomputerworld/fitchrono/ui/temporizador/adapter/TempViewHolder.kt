package com.techcomputerworld.fitchrono.ui.temporizador.adapter

import android.R
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.techcomputerworld.fitchrono.databinding.ItemTimerBinding
import com.techcomputerworld.fitchrono.domain.model.TempInfo
import com.techcomputerworld.fitchrono.ui.temporizador.TempViewModel


class TempViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemTimerBinding.bind(view)
    private val tempViewModel: TempViewModel = TempViewModel()


    fun render(
        tempInfo: TempInfo,
        position: Int, //recibir la posicion / get the position
        onClickPlay: (TempInfo) -> Unit,
        onClickStop: (TempInfo) -> Unit

    ) {
        // Determinar si el botón debe mostrar play o pause
        val isActive = TempViewModel.activePositions.contains(position)
        //binding.numOrder.text = cronoInfo.numOrder.toString()
        //me tendre que traer al viewModel dentro del ViewHolder
        /* binding.buttonPlay.setImageResource(
            if (isActive) R.drawable.ic_pause
            else R.drawable.ic_play
        )*/
        binding.buttonPlay.setOnClickListener {
            onClickPlay(tempInfo)
            //TempViewModel.startTimer(tempInfo)

        }
        binding.timer.text = tempInfo.timer
        binding.buttonStop.setOnClickListener {
            //TempViewModel.stopTimer(tempInfo)
            onClickStop(tempInfo)
        }

    }
}


