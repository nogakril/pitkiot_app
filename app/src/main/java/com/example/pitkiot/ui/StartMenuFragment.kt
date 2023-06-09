package com.example.pitkiot.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.pitkiot.R
import com.example.pitkiot.ui.dialog.buildExitDialog

class StartMenuFragment : Fragment(R.layout.fragment_start_menu) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.create_new_game_btn)?.setOnClickListener {
            val action = StartMenuFragmentDirections.actionStartMenuFragmentToCreateNewGameFragment()
            findNavController().navigate(action)
        }
        view.findViewById<Button>(R.id.join_game_btn)?.setOnClickListener {
            val action = StartMenuFragmentDirections.actionStartMenuFragmentToJoinGameFragment()
            findNavController().navigate(action)
        }
        view.findViewById<Button>(R.id.instructions_button)?.setOnClickListener {
            val action = StartMenuFragmentDirections.actionStartMenuFragmentToInstructionsFragment()
            findNavController().navigate(action)
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            buildExitDialog(requireContext(), requireActivity())
        }
    }
}