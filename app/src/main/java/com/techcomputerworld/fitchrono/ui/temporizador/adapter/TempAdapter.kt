package com.techcomputerworld.fitchrono.ui.temporizador.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.techcomputerworld.fitchrono.R
import com.techcomputerworld.fitchrono.domain.model.CronoInfo
import com.techcomputerworld.fitchrono.domain.model.TempInfo
import com.techcomputerworld.fitchrono.ui.temporizador.TempViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TempAdapter (
    private var tempList: List<TempInfo> = emptyList(),

    private val onClickPlay:(TempInfo) -> Unit,
    private val onClickStop:(TempInfo) -> Unit):
    RecyclerView.Adapter<TempViewHolder>() {

    private val _tempList = MutableStateFlow<List<TempInfo>>(emptyList() )
    //val tempList: StateFlow<List<TempInfo>> get() = _tempList
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TempViewHolder {
        return TempViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_timer, parent, false)
        )
    }
    fun updateList(list: List<TempInfo>){
        tempList = list
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: TempViewHolder, position: Int) {
        val item = tempList[position]
        holder.render(
            item,
            position,
            onClickPlay = {tempInfo ->
                TempViewModel.activePositions.add(position) //actualizar el estado del boton actual / update the state of current button
                notifyItemChanged(position) //refrescar viste / refresh view
                TempViewModel.startTimer(tempInfo)
            },
            onClickStop = {tempInfo ->
                TempViewModel.activePositions.add(position) // //actualizar el estado del boton actual / update the state of current button
                notifyItemChanged(position)
                TempViewModel.stopTimer(tempInfo)
            }
        )
    }


    override fun getItemCount() = tempList.size
}

fun TempViewModel.Companion.startTimer(unit: Any) {}
