package com.example.pitkiot.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import com.example.pitkiot.R
import com.example.pitkiot.viewmodel.GameViewModel
import com.example.pitkiot.viewmodel.GameViewModelFactory

class JoinGameFragment : Fragment(R.layout.fragment_join_game) {
    private lateinit var viewModel: GameViewModel
    private lateinit var gamePinText: EditText
    private lateinit var playerNameText: EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, GameViewModelFactory()).get()
        playerNameText = view.findViewById<EditText>(R.id.player_nickname_edit_text)
        gamePinText = view.findViewById<EditText>(R.id.game_pin_edit_text)
        view.findViewById<Button>(R.id.register_player_btn).setOnClickListener(::handleRegisterPlayer)
    }

    private fun handleRegisterPlayer(view: View) {
        val playerName = playerNameText.text.toString()
        Toast.makeText(context, playerName, Toast.LENGTH_SHORT).show()
        navigateToPlayerWaitingRoom(view)
    }
    private fun navigateToPlayerWaitingRoom(view: View?) {
        val action = JoinGameFragmentDirections.actionJoinGameFragmentToPlayerWaitingRoomFragment()
        findNavController().navigate(action)
    }
    private fun validateGamePin() {
        TODO() // Make sure: game exists, the game status is on_create?, else show error
    }
    private fun validateNickName() {
        TODO() // Make sure: name valid (not to long, no curses? maybe there a funny API for that), unique name in game, else show error
    }
}