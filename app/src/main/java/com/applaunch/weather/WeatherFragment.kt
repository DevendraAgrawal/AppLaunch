package com.applaunch.weather

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.applaunch.R
import com.applaunch.SessionManager
import com.applaunch.databinding.WeatherFragmentBinding

class WeatherFragment: Fragment() {
    private var _binding: WeatherFragmentBinding? = null
    val binding get() = _binding!!

    lateinit var  viewModel: WeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = WeatherFragmentBinding.inflate(inflater, container, false)
        viewModel = WeatherViewModel()

        viewModel.getWeatherDetails()

        viewModel.weatherDetails.observe(viewLifecycleOwner) {
            binding.temp.text = it.current.temp
            binding.humidity.text = it.current.humidity
            binding.windSpeed.text = it.current.wind_speed
            binding.weatherType.text = it.current.weather[0].main
        }

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.logout.setOnClickListener {
            showAlertDialog()
        }
        return binding.root
    }

    private fun showAlertDialog() {
        val messageBoxView = LayoutInflater.from(requireContext())
            .inflate(R.layout.custom_logout_alert, null)

        val messageBoxBuilder = AlertDialog.Builder(requireContext()).setView(messageBoxView)

        val messageBoxInstance = messageBoxBuilder.show()

        messageBoxView.findViewById<Button>(R.id.yesBtn)
            .setOnClickListener() {
                val sessionManager = SessionManager(requireContext())
                sessionManager.setIsAppOpen(false)
                this@WeatherFragment.startActivity(
                    Intent().setClassName(requireContext(), "com.applaunch.MainActivity")
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                )
            }

        messageBoxView.findViewById<Button>(R.id.noBtn)
            .setOnClickListener() {
                messageBoxInstance.dismiss()
            }
        messageBoxInstance.setCancelable(false)
    }

    override fun onStart() {
        super.onStart()

        (activity as AppCompatActivity).apply {
            supportActionBar?.hide()
        }
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).apply {
            supportActionBar?.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}