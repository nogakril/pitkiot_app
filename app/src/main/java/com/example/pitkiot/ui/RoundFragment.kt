package com.example.pitkiot.ui

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.pitkiot.R
import com.example.pitkiot.data.PitkiotRepositoryImpl
import com.example.pitkiot.data.enums.Team.TEAM_A
import com.example.pitkiot.data.enums.Team.TEAM_B
import com.example.pitkiot.data.models.UiState.Companion.showError
import com.example.pitkiot.utils.OnSwipeTouchListener
import com.example.pitkiot.utils.TeamsDialog
import com.example.pitkiot.viewmodel.RoundViewModel

class RoundFragment : Fragment(R.layout.fragment_round) {

    private val args: RoundFragmentArgs by navArgs()
    private lateinit var viewModel: RoundViewModel
    private lateinit var swipeView: View
    private lateinit var countdownText: TextView
    private lateinit var wordTextView: TextView
    private lateinit var startRoundTitle: TextView
    private lateinit var scoreAndSkipsText: TextView
    private lateinit var scoreSummaryText: TextView
    private lateinit var nextTeamAndPlayerText: TextView
    private lateinit var startRoundBtn: Button
    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            /* owner = */ this,
            /* factory = */ RoundViewModel.Factory(
                pitkiotRepositoryFactory = ::PitkiotRepositoryImpl,
                gamePinFactory = { args.gamePin }
            )
        ).get()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        countdownText = view.findViewById(R.id.countdown_text)
        wordTextView = view.findViewById(R.id.word_text_view)
        startRoundTitle = view.findViewById(R.id.start_round_title)
        swipeView = view.findViewById(R.id.swipe_view)
        scoreAndSkipsText = view.findViewById(R.id.score_and_skips_text)
        nextTeamAndPlayerText = view.findViewById(R.id.next_team_and_player_text)
        scoreSummaryText = view.findViewById(R.id.score_summary_text)
        startRoundBtn = view.findViewById(R.id.start_round_btn)

        startRoundBtn.setOnClickListener {
            viewModel.startNewRound()
            setRoundUiComponentsVisibility(roundStart = true)
        }

        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            uiState.errorMessage?.let { uiState.showError(requireContext()) }
            if (uiState.inRound) {
                scoreAndSkipsText.text = getString(R.string.score_and_skips_placeholder, uiState.score, uiState.skipsLeft)
                wordTextView.text = uiState.curWord
                startRoundTitle.text = uiState.timeLeftToRound.toString()
            } else {
                nextTeamAndPlayerText.text = getString(R.string.next_team_and_player_placeholder, uiState.curPlayer, uiState.curTeam.customName)
                scoreSummaryText.text = getString(R.string.score_summary_text, TEAM_A.customName, uiState.teamAScore, TEAM_B.customName, uiState.teamBScore)
                startRoundTitle.text = getString(R.string.start_round_title)
                setRoundUiComponentsVisibility(roundStart = false)
            }

            if (uiState.gameEnded) {
                val winner = viewModel.onGameEndedReturnWinner()
                val action = RoundFragmentDirections.actionRoundFragmentToGameSummaryFragment(uiState.teamAScore, uiState.teamBScore, winner, args.gamePin)
                findNavController().navigate(action)
            }

            if (uiState.showTeamsDivisionDialog) {
                dialog = TeamsDialog(requireContext(), viewModel.getPlayersByTeam(TEAM_A), viewModel.getPlayersByTeam(TEAM_B))
                dialog.show()
                uiState.showTeamsDivisionDialog = false
            }
        }

        swipeView.setOnTouchListener(object : OnSwipeTouchListener(requireContext()) {
            override fun onSwipeRight() {
                super.onSwipeRight()
                handleSwipe(R.drawable.styled_box_green) {
                    viewModel.onCorrectGuess()
                }
            }
            override fun onSwipeLeft() {
                super.onSwipeLeft()
                handleSwipe(R.drawable.styled_box_red) {
                    viewModel.onSkipAttempt()
                }
            }
        })
    }

    private fun handleSwipe(resource: Int, action: () -> Unit) {
        action.invoke()
        colorScreenAfterSwipe(resource)
    }

    private fun colorScreenAfterSwipe(resource: Int) {
        swipeView.setBackgroundResource(resource)
        Handler(Looper.getMainLooper()).postDelayed({
            swipeView.setBackgroundResource(R.drawable.styled_box)
        }, 500)
    }

    private fun setRoundUiComponentsVisibility(roundStart: Boolean) {
        // Round
        swipeView.visibility = if (roundStart) VISIBLE else INVISIBLE
        scoreAndSkipsText.visibility = if (roundStart) VISIBLE else INVISIBLE
        wordTextView.visibility = if (roundStart) VISIBLE else INVISIBLE

        // Ready to play?
        nextTeamAndPlayerText.visibility = if (roundStart) INVISIBLE else VISIBLE
        startRoundBtn.visibility = if (roundStart) INVISIBLE else VISIBLE
        scoreSummaryText.visibility = if (roundStart) INVISIBLE else VISIBLE
    }
}