package com.techcomputerworld.fitchrono.ui.cronometro


import android.os.SystemClock
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techcomputerworld.fitchrono.domain.model.CronoInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.onEmpty
import javax.inject.Inject


/* No voy a trabajar directamente con una capa de datos asi que hay que trabajar con el viewmodel solamente
* Loqs datos me los proporciona el viewmodel
*
* */
@HiltViewModel
class CronoViewModel @Inject constructor(): ViewModel() {

    private val _tiempoTranscurrido = MutableStateFlow("00:00:00.000")
    val tiempoTranscurrido: StateFlow<String> get() = _tiempoTranscurrido
    //Otro tiempo para el cronometro Chronometer2
    private val _tiempoTranscurrido2 = MutableStateFlow("00:00:00.000")
    val tiempoTranscurrido2: StateFlow<String> get() = _tiempoTranscurrido2

    private val _cronolist = MutableStateFlow<List<CronoInfo>>(emptyList())
    val cronolist: StateFlow<List<CronoInfo>> get() = _cronolist
    //ultimo tiempo registrado
    private var job: Job? = null
    private var job2: Job? = null
    private var baseTime: Long  = 0L
    private var elapsedPausedTime: Long = 0L
    private var lapBaseTime = 0L
    //baseTime2 y elapsedPausedTime2 del cronometro 2 el pequeño
    private var baseTime2: Long = 0L
    private var elapsedPausedTime2: Long = 0L

    private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    //variable para cuando se ponga a true no dejar que se actualice el segundo cronometro
    private var updateChronometer2 = false
    var isRunning = false
   /* init {
        restartChronometer(basetime = 0L)
    }*/
   fun startChronometer() {
       if (!isRunning) {
           baseTime = SystemClock.elapsedRealtime() - elapsedPausedTime
           baseTime2 = SystemClock.elapsedRealtime() - elapsedPausedTime2
           // Inicializa el lap timer también

           isRunning = true
           job = coroutineScope.launch {
               while (isRunning) {
                   updateChronometer()
                   delay(10)  // Actualiza cada 10 ms
               }
           }
           //hilo del segundo cronometro
           job2 = coroutineScope.launch {
               while (isRunning) {
                   updateChronometer2()
                   delay(10)  // Actualiza cada 10 ms
               }
           }
       }
   }

    fun stopChronometer() {
        if (isRunning) {
            elapsedPausedTime = SystemClock.elapsedRealtime() - baseTime  // Guarda el tiempo al pausar
            elapsedPausedTime2 = SystemClock.elapsedRealtime() - baseTime2
            isRunning = false
            job?.cancel()
            job2?.cancel()

        }

    }
    fun restartChronometer() {
        stopChronometer()
        baseTime = 0L
        baseTime2 = 0L
        elapsedPausedTime = 0L
        elapsedPausedTime2 = 0L
        _tiempoTranscurrido.value = "00:00:00.000"
        _tiempoTranscurrido2.value = "00:00:00.000"
        _cronolist.value = emptyList()
    }

    fun addLap(tiempoTranscurrido1: String, tiempoTranscurrido2:String) {
        val time1: String = tiempoTranscurrido1
        val time2: String = tiempoTranscurrido2

        val newCronoInfo = CronoInfo(
            numOrder = _cronolist.value.size + 1,
            time = time1,
            timeSum = "+ $time2"
            //timeSum = if (_cronolist.value.isNotEmpty()) time2 else "00:00"
        )
        // Agrega el nuevo dato
        _cronolist.value = _cronolist.value + newCronoInfo
        //val elapsedLapMillis = SystemClock.elapsedRealtime() - lapBaseTime
        baseTime2 = SystemClock.elapsedRealtime() // Reinicia el cronómetro de vuelta
        _tiempoTranscurrido2.value = "00:00:00.000"
        updateChronometer2()
    }


    /*private fun updateChronometer() {
        val elapsedMillis = SystemClock.elapsedRealtime() - baseTime
        val elapsedLapMillis = SystemClock.elapsedRealtime() - lapBaseTime

        _tiempoTranscurrido.value = formatTime(elapsedMillis)
        _tiempoTranscurrido2.value = formatTime(elapsedLapMillis)
    }*/

    /*fun formatTime(millis: Long): String {
        val hours = millis / 3600000
        val minutes = (millis % 3600000) / 60000
        val seconds = (millis % 60000) / 1000
        val milliseconds = millis % 1000
        return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, milliseconds)
    }*/
    private fun updateChronometer() {
        val elapsedMillis = SystemClock.elapsedRealtime() - baseTime
        val hours = (elapsedMillis / 3600000).toInt()
        val minutes = ((elapsedMillis % 3600000) / 60000).toInt()
        val seconds = ((elapsedMillis % 60000) / 1000).toInt()
        val milliseconds = (elapsedMillis % 1000).toInt()

        _tiempoTranscurrido.value = String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, milliseconds)
        if (updateChronometer2 == false) {
            _tiempoTranscurrido2.value = String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, milliseconds)
            //updateChronometer2()
        }

    }
    private fun updateChronometer2() {
        updateChronometer2 = true
        val elapsedMillis = SystemClock.elapsedRealtime() - baseTime2
        val hours = (elapsedMillis / 3600000).toInt()
        val minutes = ((elapsedMillis % 3600000) / 60000).toInt()
        val seconds = ((elapsedMillis % 60000) / 1000).toInt()
        val milliseconds = (elapsedMillis % 1000).toInt()

        _tiempoTranscurrido2.value = String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, milliseconds)
    }

    //posible formato del tiempo

    override fun onCleared() {
        super.onCleared()
        coroutineScope.cancel() // Cancelamos la corrutina al destruir el ViewModel
    }
}