package com.techcomputerworld.fitchrono.ui.temporizador

import com.techcomputerworld.fitchrono.domain.model.TempInfo

sealed class TempUIState {
    object Loading : TempUIState()
    data class Success(val tempInfo: TempInfo) : TempUIState()
    data class Error(val msg: String) : TempUIState()
}