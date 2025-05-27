package com.techcomputerworld.fitchrono.ui.cronometro

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.techcomputerworld.fitchrono.R
import com.techcomputerworld.fitchrono.databinding.FragmentCronoBinding
import com.techcomputerworld.fitchrono.ui.cronometro.adapter.CronoAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


//marca esta clase como que necesito usar la inyeccion de dependencias.
@AndroidEntryPoint
class CronoFragment : Fragment() {

    private val CronoViewModel by viewModels<CronoViewModel>()
    private lateinit var cronoAdapter: CronoAdapter
    private var _binding: FragmentCronoBinding? = null
    private val binding get() = _binding!!



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCronoBinding.inflate(layoutInflater, container, false)
        return binding.root
        //observar los cambios en el StateFlow

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ChronometerChanged()
        initUI()
    }

    private fun ChronometerChanged() {

        // Observar los cambios en el StateFlow
        lifecycleScope.launch {
            CronoViewModel.tiempoTranscurrido.collectLatest { time ->
                binding.chronometer.text = time
            }
        }
        //observar los cambios en chronometer2
        lifecycleScope.launch {
            CronoViewModel.tiempoTranscurrido2.collectLatest { time2 ->
                binding.chronometer2.text = time2
            }
        }




    }

    private fun initUI() {
        initButton()
        initListeners()
        initList()
    }
    private fun initButton() {
        //iniciar el btStop en invisible
        binding.btStop.visibility = INVISIBLE
        binding.btLap.visibility = INVISIBLE
    }

    private fun initListeners() {

        //iniciamos la escucha de los botones
        //boton iniciar
        binding.btStart.setOnClickListener(){
            val baseTime = SystemClock.elapsedRealtime()

            binding.btStart.visibility = INVISIBLE
            binding.btStop.visibility = VISIBLE
            binding.btRestart.visibility = INVISIBLE
            binding.btLap.visibility = VISIBLE
            CronoViewModel.startChronometer()

        }
        //boton detener
        binding.btStop.setOnClickListener {
            binding.btStop.visibility = INVISIBLE
            binding.btStart.visibility = VISIBLE
            binding.btRestart.visibility = VISIBLE
            binding.btLap.visibility = INVISIBLE
            CronoViewModel.stopChronometer()

        }
        binding.btRestart.setOnClickListener {
            CronoViewModel.restartChronometer()

        }

        binding.btLap.setOnClickListener {
            var tiempo1 = CronoViewModel.tiempoTranscurrido.value
            var tiempo2 = CronoViewModel.tiempoTranscurrido2.value
            CronoViewModel.addLap(tiempo1, tiempo2)
        }

    }
    private fun initList() {
        // Configurar RecyclerView con lista vacía
        cronoAdapter = CronoAdapter(emptyList())
        binding.rvCrono.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cronoAdapter
        }
        //observar cambios en la lista de cronoList
        lifecycleScope.launch {
            CronoViewModel.cronolist.collectLatest { laps ->
                cronoAdapter.updateCronoList(laps)
            }
        }
        /* binding.rvCrono.layoutManager = LinearLayoutManager(context)
        binding.rvCrono.adapter = adapter*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}