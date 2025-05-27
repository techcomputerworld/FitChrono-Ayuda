package com.techcomputerworld.fitchrono.ui.cronometro.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.techcomputerworld.fitchrono.R
import com.techcomputerworld.fitchrono.domain.model.CronoInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.toList

class CronoAdapter ( private var cronoList : List<CronoInfo> = emptyList() ) :
    RecyclerView.Adapter<CronoViewHolder>() {

    private val _cronolist = MutableStateFlow<List<CronoInfo>>(emptyList())
    val cronolist: StateFlow<List<CronoInfo>> get() = _cronolist

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CronoViewHolder {
        return CronoViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_crono, parent, false)
        )
    }
    //Este metodo se encarga de decirle al ViewHolder lo que tiene que pintar
    override fun onBindViewHolder(holder: CronoViewHolder, position: Int) {

        holder.render(cronoList[position])
    }
    //obtener el numero de elementos que hay en la lista actual
    override fun getItemCount() = cronoList.size

    fun updateCronoList(newList: List<CronoInfo>) {
        cronoList = newList
        notifyDataSetChanged()
    }
}



