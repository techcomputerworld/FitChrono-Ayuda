package com.techcomputerworld.fitchrono.domain.model


data class TempInfo(
    var id: Int,                    // Identificador único
    var totalTime: Int,             // Tiempo total en segundos
    var timer: String,              // Tiempo expresado en cadena de texto HH:MM:SS
    var remainingTime: Int,         // Tiempo restante en segundos
    var isRunning: Boolean = false  // Estado del temporizador
)
