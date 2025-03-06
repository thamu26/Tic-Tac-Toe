package com.example.tic_tac_toe

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.children
import pl.droidsonroids.gif.GifImageView

class HomeActivity : AppCompatActivity() {
    private var playerTurn = true  // true for X, false for O
    private val board = Array(3) { CharArray(3) }  // Game board, 3x3 grid
    private lateinit var congratulationsTextView: TextView
    private lateinit var playerXScoreTextView: TextView
    private lateinit var playerOScoreTextView: TextView
    private var playerXScore = 0
    private var playerOScore = 0
    private lateinit var congratulationsGifImageView: GifImageView
    private lateinit var papersGifImageView: GifImageView // Initialize Papers GifImageView
    private lateinit var cardView: androidx.cardview.widget.CardView // Declare CardView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Initialize UI elements
        val gridLayout = findViewById<GridLayout>(R.id.gridLayout)
        val resetButton = findViewById<Button>(R.id.btnReset)
        congratulationsTextView = findViewById(R.id.tvCongratulations)
        playerXScoreTextView = findViewById(R.id.tvPlayerXScore)
        playerOScoreTextView = findViewById(R.id.tvPlayerOScore)
        congratulationsGifImageView = findViewById(R.id.congratulationsGif)
        papersGifImageView = findViewById(R.id.papersGif) // Initialize Papers GifImageView
        cardView = findViewById(R.id.cardView) // Initialize CardView

        // Set up grid buttons with click listeners
        for (i in 0 until gridLayout.childCount) {
            val button = gridLayout.getChildAt(i) as Button
            val row = i / 3
            val col = i % 3
            button.setOnClickListener { onCellClicked(button, row, col) }
        }

        // Set up reset button listener
        resetButton.setOnClickListener {
            resetBoard()
            playerTurn = true // Reset player turn to X
        }

        // Initial reset to start a new game
        resetBoard()
    }

    // Initialize the game board with empty spaces
    private fun initializeBoard() {
        for (i in 0..2) {
            for (j in 0..2) {
                board[i][j] = ' '
            }
        }

        // Reset the appearance of all buttons
        val gridLayout = findViewById<GridLayout>(R.id.gridLayout)
        gridLayout.children.forEach {
            val button = it as Button
            button.text = ""
            button.isEnabled = true
            button.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        }

        // Hide the congratulations TextView and GifImageView at the start
        congratulationsTextView.visibility = View.GONE
        congratulationsTextView.text = ""
        congratulationsGifImageView.visibility = View.GONE // Hide the GifImageView
        papersGifImageView.visibility = View.GONE // Hide the "Papers" GifImageView at the start
        cardView.visibility = View.GONE // Hide the CardView at the start
    }

    // Handle cell click event
    @SuppressLint("SetTextI18n")
    private fun onCellClicked(button: Button, row: Int, col: Int) {
        // If the cell is already occupied, show a message
        if (board[row][col] != ' ') {
            Toast.makeText(this, "Cell already occupied", Toast.LENGTH_SHORT).show()
            return
        }

        // Update the button text and the board state based on the current player's turn
        button.text = if (playerTurn) "X" else "O"
        board[row][col] = if (playerTurn) 'X' else 'O'

        // Highlight the clicked cell
        button.setBackgroundColor(if (playerTurn) Color.LTGRAY else Color.LTGRAY)

        // Check for winner or draw
        when {
            checkWinner() -> {
                // Show the congratulations message and update the score
                congratulationsTextView.text = "Congratulations! Player ${if (playerTurn) "X" else "O"} wins!"
                congratulationsTextView.visibility = View.VISIBLE
                if (playerTurn) {
                    playerXScore++
                    playerXScoreTextView.text = "X: $playerXScore"
                } else {
                    playerOScore++
                    playerOScoreTextView.text = "O: $playerOScore"
                }

                // Show the GifImageView and CardView on winning
                congratulationsGifImageView.visibility = View.VISIBLE // Show the GifImageView
                cardView.visibility = View.VISIBLE // Show the CardView

                // Delay to show the "Papers" GIF after "Congratulations"
                android.os.Handler().postDelayed({
                    congratulationsGifImageView.visibility = View.GONE // Hide the "Congratulations" GIF
                    papersGifImageView.visibility = View.VISIBLE // Show the "Papers" GIF
                    // After the "Papers" GIF finishes, hide it
                    android.os.Handler().postDelayed({
                        papersGifImageView.visibility = View.GONE // Hide the "Papers" GIF
                        // Reset the game after the "Papers" GIF finishes
                        resetBoard()
                    }, 3000) // Delay for 3 seconds (assuming your "Papers" GIF is 3 seconds long)
                }, 2000) // Delay for 2 seconds
            }
            isBoardFull() -> {
                Toast.makeText(this, "It's a draw!", Toast.LENGTH_LONG).show()
            }
        }

        // Disable the clicked cell
        button.isEnabled = false

        // Switch turns
        playerTurn = !playerTurn
    }

    // Check if there is a winner
    private fun checkWinner(): Boolean {
        // Check rows and columns
        for (i in 0..2) {
            if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0] != ' ') return true
            if (board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[0][i] != ' ') return true
        }
        // Check diagonals
        return (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] != ' ') ||
                (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2] != ' ')
    }

    // Check if the board is full
    private fun isBoardFull(): Boolean {
        return board.all { row -> row.all { cell -> cell != ' ' } }
    }

    // Reset the game board
    @SuppressLint("SetTextI18n")
    private fun resetBoard() {
        // Reinitialize the board
        initializeBoard()

        // Reset the score
        playerXScore = 0
        playerOScore = 0
        playerXScoreTextView.text = "X: $playerXScore"
        playerOScoreTextView.text = "O: $playerOScore"

        // Reset player turn to X
        playerTurn = true
    }
}