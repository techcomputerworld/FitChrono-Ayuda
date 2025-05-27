package com.techcomputerworld.fitchrono.ui.temporizador

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.techcomputerworld.fitchrono.R
import com.techcomputerworld.fitchrono.databinding.FragmentTempBinding
import com.techcomputerworld.fitchrono.domain.model.TempInfo
import com.techcomputerworld.fitchrono.ui.temporizador.adapter.TempAdapter
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class TempFragment : Fragment() {

    private var _binding: FragmentTempBinding? = null
    private val binding get() = _binding!!
    private val TempViewModel by viewModels<TempViewModel>()
    private lateinit var tempAdapter: TempAdapter
    // CoroutineScope para el lifecycle del Fragment
    private var viewModelJob: Job? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTempBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()


    }


    private fun initUI() {
        //initnpHoour np = NumberPicker
        initnpHour()
        initnpMinutes()
        initnpSecond()
        initButtons()
        initList()
    }

    private fun initButtons() {
        binding.ibPlayStop.setOnClickListener {
            val hours = binding.npHour.value.toString().toIntOrNull() ?: 0
            val minutes = binding.npMinutes.value.toString().toIntOrNull() ?: 0
            val seconds = binding.npSecond.value.toString().toIntOrNull() ?: 0

            val totalSeconds = (hours * 3600) + (minutes * 60) + seconds

            if (totalSeconds > 0) {
                TempViewModel.addTimer(totalSeconds)

                // Notificar al adaptador que los datos han cambiado
                //tempAdapter.notifyDataSetChanged()
            }
            else {
                //Toast.makeText( this, "lo que sea", Toast.LENGTH_SHORT).show()
                Toast.makeText(requireContext(), getString(R.string.buttontime), Toast.LENGTH_SHORT).show()
            }


        }
    }

    private fun initList() {
        //Configurar RecyclerView con lista vacia
        tempAdapter = TempAdapter(
            onClickPlay = { item -> TempViewModel.buttonPlay(item) },
            onClickStop = { item-> TempViewModel.buttonStop(item)}
        )
        //RecyclerView id rvTimers
        binding.rvTimers.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = tempAdapter
        }
        //Observar cambios en la lista de TempList

        lifecycleScope.launch {
            TempViewModel.timers.collect { timers ->
                tempAdapter.updateList(timers)
            }
        }
        /*viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                TempViewModel.timers.collect { timers ->
                    TempAdapter.submitList(timers) // Actualiza el adaptador
                }
            }
        }*/
    }

    private fun initnpHour() {
        binding.npHour.minValue = 0
        binding.npHour.maxValue = 9
        binding.npHour.setFormatter { value -> String.format("%02d", value) }

        // Escuchar cambios de valor en el NumberPicker
        binding.npHour.setOnValueChangedListener { picker, oldVal, newVal ->
            binding.npHour.value = picker.value

            //Toast.makeText(this, "Cambió de $oldVal a $newVal", Toast.LENGTH_SHORT).show()
        }


    }
    private fun initnpMinutes() {
        binding.npMinutes.minValue = 0
        binding.npMinutes.maxValue = 59
        binding.npMinutes.setFormatter { value -> String.format("%02d", value) }

        // Escuchar cambios de valor en el NumberPicker
        binding.npMinutes.setOnValueChangedListener { picker, oldVal, newVal ->
            binding.npMinutes.value = picker.value

            //Toast.makeText(this, "Cambió de $oldVal a $newVal", Toast.LENGTH_SHORT).show()
        }
    }
    private fun initnpSecond() {
        binding.npSecond.minValue = 0
        binding.npSecond.maxValue = 59
        binding.npSecond.setFormatter { value -> String.format("%02d", value) }

        // Escuchar cambios de valor en el NumberPicker
        binding.npSecond.setOnValueChangedListener { picker, oldVal, newVal ->
            binding.npSecond.value = picker.value

            //Toast.makeText(this, "Cambió de $oldVal a $newVal", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    


}