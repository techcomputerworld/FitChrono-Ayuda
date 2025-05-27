package com.techcomputerworld.fitchrono.ui.temporizador

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.ViewModel
import com.techcomputerworld.fitchrono.domain.model.TempInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class TempViewModel @Inject constructor(): ViewModel() {

    companion object  {
        //Posiciones activas de los botones
        var activePositions = mutableSetOf<Int>() // Posiciones activas
        fun startTimer(tempInfo: TempInfo) {
            startTimer(tempInfo)
        }
        fun stopTimer(tempInfo: TempInfo) {
            stopTimer(tempInfo)
        }
        lateinit var tempInfo: TempInfo
    }
    //inicio una MutableList que directamente va a tener los elementos TempInfo que son los tiempos que añade el usuario
    //private val timersList: MutableList<List<TempInfo>> = mutableListOf()

    private var timersList = mutableListOf<TempInfo>()
    //lateinit var _timers: MutableStateFlow<List<TempInfo>>
    //val _timers: MutableList<TempInfo> = mutableListOf()
    var _timers = MutableStateFlow<List<TempInfo>>(emptyList())
    //val _timers: MutableList<TempInfo> = mutableListOf()
    val timers: StateFlow<List<TempInfo>> get() = _timers
    //val timers: StateFlow<List<TempInfo>> = _timers.asStateFlow()
    //val timers = _timers.asStateFlow() // Exponer solo como lectura
    private val timerMap = mutableMapOf<Int, CountDownTimer?>()
    //private lateinit var tempAdapter: TempAdapter
    //fun addTimer(totalSeconds: Int) : TempInfo {}


    fun addTimer(totalSeconds: Int) {

        val newTimer = TempInfo (
            id = _timers.value.size,
            totalTime = totalSeconds,
            remainingTime = totalSeconds,
            timer = "00:00:00",
            isRunning = false

        )

        //formateo del tiempo de segundos a, hh:mm:ss
        newTimer.timer = convertSeconds(newTimer.totalTime)
        //timersList.add(newTimer)
        _timers.value += newTimer
        /*_timers.update { currentList ->
            currentList + newTimer
        }*/
        // Actualizar StateFlow (forma correcta)
      /*  _timers.update { currentList ->
            currentList + newTimer
        }*/
        //tempAdapter.notifyDataSetChanged()
        // Crear una nueva lista con el temporizador añadido
        /*val updatedTimers = _timers.value.toMutableList().apply {
            add(newTimer)
        }*/

        /*Segun yo el collect solo lo hace una vez, yo digo que uses observer, pero creo que si quieres una lista de temporizadores,
        la funcion addTimer te deberia retornar un TempInfo , y ese item lo añades a la lista,
        pero creo que cada item de la lista deberia de tener su propio observador,
        siento que falta codigo que muestres,
        como donde declaras _timers y donde haces el "temporizador", osea el conteo de segundos*/
        // Asignar la nueva lista a _timers.value
        //_timers.value = updatedTimers
        // Log para depuración: nuevo temporizador creado
        // Log para depuración: nuevo temporizador creado
        Log.d("TimerDebug", "Nuevo temporizador creado: id = ${newTimer.id}, timer = ${newTimer.timer}")
        // Log para depuración: temporizador añadido
        Log.d("TimerDebug", "Temporizador añadido. Total de temporizadores: ${_timers.value.size}")
        // Notificar al adaptador que los datos han cambiado

        //tempAdapter.notifyDataSetChanged() // Si usas un adaptador personalizado
        //return newTimer
    }
    /*fun startTimer(tempInfo: TempInfo) {
        cancelTimer(tempInfo.id)

        val countDownTimer = object : CountDownTimer(tempInfo.remainingTime * 1000L, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = (millisUntilFinished / 1000).toInt()
                updateTimer(tempInfo.id, seconds, true)
            }

            override fun onFinish() {
                updateTimer(tempInfo.id, 0, false)
            }
        }.start()

        timerMap[tempInfo.id] = countDownTimer
    }*/
    fun buttonPlay(tempInfo: TempInfo) {
        //inicio del temporizador
        startTimer(tempInfo)
        //cambiar el icono del boton a que se vea el de pausar el tiempo
        changeIcon()
        
    }
    fun changeIcon() {

    }
    fun buttonStop(tempInfo: TempInfo) {

    }
    fun startTimer(tempInfo: TempInfo) {
        Log.d("Timer", "startTimer() llamado con ID: ${tempInfo.id}, tiempo: ${tempInfo.remainingTime}")

        cancelTimer(tempInfo.id)

        if (tempInfo.remainingTime <= 0) {
            updateTimer(tempInfo.id, 0, false)
            return
        }

        val countDownTimer = object : CountDownTimer(tempInfo.remainingTime * 1000L, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = (millisUntilFinished / 1000).toInt()
                Log.d("Timer", "onTick ID: ${tempInfo.id} => $seconds segundos restantes")
                updateTimer(tempInfo.id, seconds, true)
            }

            override fun onFinish() {
                Log.d("Timer", "Temporizador finalizado para ID: ${tempInfo.id}")
                updateTimer(tempInfo.id, 0, false)
            }
        }

        countDownTimer.start()
        timerMap[tempInfo.id] = countDownTimer
    }
    private fun updateTimer(id: Int, remainingTime: Int, isRunning: Boolean) {
        _timers.value = _timers.value.map { timer ->
            if (timer.id == id) {
                timer.copy(
                    remainingTime = remainingTime,
                    timer = formatTime(remainingTime),
                    isRunning = isRunning
                )
            } else {
                timer
            }
        }
    }
    //para cancelar un tiempo
    fun cancelTimer(id: Int) {
        timerMap[id]?.cancel()
        timerMap.remove(id)
    }
    fun stopTimer(tempInfo: TempInfo) {

        timerMap[tempInfo.id]?.cancel()
    }

    private fun pauseTimer(id: Int) {
        cancelTimer(id)
        val timerToUpdate = _timers.value.find { it.id == id } ?: return
        updateTimer(id, timerToUpdate.remainingTime, false)
    }
    /*fun removeTimer(timer: TempInfo) {
        stopTimer(timer)
        timersMap.remove(timer.id)
        //_timers.update { it.filter { it.id != timer.id } }
    }*/
    fun addTime(timer: TempInfo, seconds: Int) {
        _timers.update { list ->
            list.map { if (it.id == timer.id) it.copy(remainingTime = it.remainingTime + seconds) else it }
        }
    }
    private fun formatTime(seconds: Int): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60
        return String.format("%02d:%02d:%02d", hours, minutes, secs)
    }

    private fun convertSeconds(newTimer: Int): String {
        val hour = newTimer / 3600
        val min = (newTimer % 3600) / 60
        val sec = newTimer % 60

        // Formatear el resultado como una cadena
        return String.format("%02d:%02d:%02d", hour, min, sec)
    }


}