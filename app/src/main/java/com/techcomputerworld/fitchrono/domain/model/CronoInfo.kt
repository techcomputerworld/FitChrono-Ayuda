package com.techcomputerworld.fitchrono.domain.model

data class CronoInfo (
    //orden de llegada por tiempo
    var numOrder: Int,
    //Justo el tiempo de llegada
    val time:String,
    //El tiempo que se le suma con el siguiente tiempo
    val timeSum: String
)


