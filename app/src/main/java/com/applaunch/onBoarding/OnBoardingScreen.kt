package com.applaunch.onBoarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.applaunch.R
import com.applaunch.SessionManager
import com.applaunch.databinding.OnboardingFragmentBinding

class OnBoardingScreen: Fragment() {

    private var _binding: OnboardingFragmentBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = OnboardingFragmentBinding.inflate(inflater, container, false)

        val sessionManager = SessionManager(requireContext())

        if(sessionManager.getIsLogin()){
            findNavController().navigate(R.id.action_onBoardingScreen_to_loginScreen)
        }

        binding.loginBtn.setOnClickListener {
            findNavController().navigate(R.id.action_onBoardingScreen_to_loginScreen)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}