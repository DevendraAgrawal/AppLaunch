package com.applaunch

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.applaunch.databinding.LoginScreenBinding

class LoginFragment : Fragment() {
    private var _binding: LoginScreenBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LoginScreenBinding.inflate(inflater, container, false)

        if(SessionManager(requireContext()).getIsLogin()){
            findNavController().navigate(R.id.action_loginScreen_to_userListFragment)
        }

        binding.loginBtn.setOnClickListener {
            Log.e("Name", binding.name.text.toString())
            Log.e("Name", binding.password.text.toString())
            if (binding.name.text.isNullOrBlank()) {
                binding.name.error = "Please enter the email and password"
                return@setOnClickListener
            }else if(binding.name.text.toString().equals("testapp@google.com") && binding.password.text.toString().equals("Test@123456")) {
                binding.name.error = null
                //Navigate to next fragment
                findNavController().navigate(R.id.action_loginScreen_to_userListFragment)
            }else{
                Toast.makeText(requireContext(), "Please enter the valid email and password", Toast.LENGTH_LONG).show()
            }

        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}