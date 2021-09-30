package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


class ModUserVsUser extends Mod {
	
	private static final long serialVersionUID = 1L;
	
	
	public ModUserVsUser() {
		
		super();
	}
		

	public void actionPerformed(ActionEvent evt) {
		Object src = evt.getSource();
		if (src == newGameButton)
			doNewGame();
		else if (src == exitButton)
			doExit();
	}

	
	// Nuovo gioco
	void doNewGame() {
		
		if (gameInProgress == true) {
			message.setText("Finish the current gomokuGame first!");
			return;
		}
		
		for (int row = 0; row < 13; row++)
			for (int col = 0; col < 13; col++)
				board[row][col] = EMPTY;
		
		currentPlayer = BLACK;
		message.setText("BLACK:  Make your move.");
		whiteScoreLbl.setText("White: "+Integer.toString(whiteScore));
		blackScoreLbl.setText("Black: "+Integer.toString(blackScore));
		
		gameInProgress = true;
		newGameButton.setEnabled(false);
		exitButton.setEnabled(true);
		win_r1 = -1;  // This value indicates that no red line is to be drawn.
		repaint();
		
		
		playMusic();
	}

	
	// Uscita dalla partita con vincita dell'altro player.
	void doExit() {

		if (gameInProgress == false) {
			message.setText("There is no gomokuGame in progress!");
			return;
		}
		if (currentPlayer == WHITE) {
			message.setText("BLACK wins because WHITE leaves!");
			blackScore++;
			gameOver("BLACK wins!");
		}
		else {
			message.setText("WHITE wins because BLACK leaves!");
			whiteScore++;
			gameOver("WHITE wins!");
		}
		newGameButton.setEnabled(true);
		exitButton.setEnabled(false);
		gameInProgress = false;
		
		
		stopMusic();
	}


	// Click cella
	void doClickSquare(int row, int col) {
		// This is called by mousePressed() when a player clicks on the
		// square in the specified row and col.  It has already been checked
		// that a gomokuGame is, in fact, in progress.

		/* Check that the user clicked an empty square.  If not, show an
      error message and exit. */

		if ( board[row][col] != EMPTY ) {
			if (currentPlayer == BLACK)
				message.setText("BLACK:  Please click an empty square.");
			else
				message.setText("WHITE:  Please click an empty square.");
			return;
		}

		/* Make the move.  Check if the board is full or if the move
      is a winning move.  If so, the gomokuGame ends.  If not, then it's
      the other user's turn. */

		board[row][col] = currentPlayer;  // Make the move.
		Graphics g = getGraphics();
		drawPiece(g, currentPlayer, row, col);
		g.dispose();

		if (winner(row,col)) {  // First, check for a winner.
			if (currentPlayer == WHITE) {
				whiteScore++;
				gameOver("WHITE wins!");
			}
			else {
				blackScore++;
				gameOver("BLACK wins!");
			}
			Graphics w = getGraphics();
			drawWinLine(w);
			w.dispose();
			return;
		}

		boolean emptySpace = false;     // Check if the board is full.
		for (int i = 0; i < 13; i++)
			for (int j = 0; j < 13; j++)
				if (board[i][j] == EMPTY)
					emptySpace = true;
		if (emptySpace == false) {
			gameOver("The gomokuGame ends in a draw.");
			return;
		}

		/* Continue the gomokuGame.  It's the other player's turn. */

		if (currentPlayer == BLACK) {
			currentPlayer = WHITE;
			message.setText("WHITE:  Make your move.");
		}
		else {  
			currentPlayer = BLACK;
			message.setText("BLACK:  Make your move.");
		}

	}


	// Disegna un pezzo nel quadrato in (riga, colonna). Il colore è specificato
	// dal parametro pezzo, che dovrebbe essere NERO o BIANCO.
	private void drawPiece(Graphics g, int piece, int row, int col) {
		
		Image checker = null;
		
		if (piece == WHITE) {
			try {
				checker = ImageIO.read(new File("images/white.png"));
			} catch (IOException e) {}
		}
		else {
			try {
				checker = ImageIO.read(new File("images/black.png"));
			} catch (IOException e) {}
		}
		
		g.drawImage(checker, col*50, row*50, null);
	}

	
	public void paint(Graphics g) {

		// Disegna griglia
		g.setColor(Color.darkGray);

        int szOfCell = getSizeBoard()/13;
        for (int k = 0; k < 13; k++) {
            g.drawLine(0, k * szOfCell, getSizeBoard(), k * szOfCell);
            g.drawLine(k * szOfCell, 0, k * szOfCell, getSizeBoard());
        }
		
		
        // Disegna bordi
		g.setColor(Color.white);
		g.drawRect(0,0,getSize().width-1,getSize().height-1);
		g.drawRect(1,1,getSize().width-3,getSize().height-3);

		
		// Disegna pezzi sul tavolo di gioco
		for (int row = 0; row < 13; row++)
			for (int col = 0; col < 13; col++)
				if (board[row][col] != EMPTY)
					drawPiece(g, board[row][col], row, col);

		
		/* Se la partita è stata vinta, allora win_r1 >= 0. Disegna una linea per segnare
      	   i cinque pezzi vincenti. */
		if (win_r1 >= 0)
			drawWinLine(g);
	}


	public void mousePressed(MouseEvent evt) {

		if (gameInProgress == false)
			message.setText("Click \"New Game\" to start a new gomokuGame.");
		else {
			int col = evt.getX() / 50;
			int row = evt.getY() / 50;
			if (col >= 0 && col < 13 && row >= 0 && row < 13)
				doClickSquare(row,col);
		}
	}
	
}