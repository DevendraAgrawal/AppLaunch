package com.applaunch.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.applaunch.R
import com.applaunch.SessionManager
import com.applaunch.databinding.UserListFragmentBinding
import com.applaunch.db.entity.UserForm


class UserListFragment : Fragment(), UserListAdapter.CallBack {

    private var _binding: UserListFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: UserListViewModel
    var userAdapter: UserListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = UserListFragmentBinding.inflate(inflater, container, false)
        viewModel = UserListViewModel()

        val sessionManager = SessionManager(requireContext())
        sessionManager.setIsAppOpen(true)

        binding.list.apply {
            this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            viewModel.getAllUser(requireContext()).observe(viewLifecycleOwner) { userList ->
                val data = ArrayList<User>().apply {
                    (userList as ArrayList<UserForm>).forEach { user ->
                        add(User(user.firstName, user.lastName, user.email, user.image))
                    }
                }
                if (userList.isNullOrEmpty()) {
                    binding.noData.visibility = View.VISIBLE
                    binding.list.visibility = View.GONE
                } else {
                    binding.noData.visibility = View.GONE
                    binding.list.visibility = View.VISIBLE
                }
                userAdapter = UserListAdapter(data, this@UserListFragment)
                this.adapter = userAdapter
                swipeLeftDelete()
            }
        }


        binding.addBtn.setOnClickListener {
            findNavController().navigate(R.id.action_userListFragment_to_userFormFragment)
        }

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    Navigation.findNavController(requireView()).popBackStack()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        return binding.root
    }

    private fun swipeLeftDelete() {
        val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object :
            ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                Toast.makeText(requireContext(), "on Move", Toast.LENGTH_SHORT).show()
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                Toast.makeText(requireContext(), "Successfully delete the user", Toast.LENGTH_SHORT)
                    .show()
                val position = viewHolder.adapterPosition
                if (userAdapter != null) {
                    val user = userAdapter?.userList?.get(position)
                    viewModel.deleteUser(
                        requireContext(),
                        UserForm(user?.firstName!!, user.lastName, user.email, user.image)
                    )
                    (userAdapter?.userList as ArrayList<User>).removeAt(position)
                    userAdapter?.notifyDataSetChanged()
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(binding.list)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun openWeather() {
        findNavController().navigate(R.id.action_userListFragment_to_weatherFragment)
    }

}