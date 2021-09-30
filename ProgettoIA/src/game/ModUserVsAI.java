package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


class ModUserVsAI extends Mod {

	private static final long serialVersionUID = 1L;

	// Image checker
	Image checkerWHITE, checkerBLACK = null;
	
	// AI
	AIManager aiManager = null;
		
	
	
	public ModUserVsAI() {
		
		super();
		
		initImageChecker();
		
		aiManager = new AIManager();
	}
	
	
	public void actionPerformed(ActionEvent evt) {
		Object src = evt.getSource();
		if (src == newGameButton)
			doNewGame();
		else if (src == exitButton)
			doExit();
	}


	// Nuova partita
	void doNewGame() {
		
		// Inizializza il tavolo di gioco
		for (int row = 0; row < 13; row++)
			for (int col = 0; col < 13; col++)
				board[row][col] = EMPTY;
		
		currentPlayer = WHITE;  // First player
		message.setText("WHITE:  Make your move.");
		
		whiteScoreLbl.setText("White: "+Integer.toString(whiteScore));
		blackScoreLbl.setText("Black: "+Integer.toString(blackScore));
				
		gameInProgress = true;
		newGameButton.setEnabled(false);
		exitButton.setEnabled(true);
		
		win_r1 = -1;  // NO Linea Rossa
		repaint();
		
				
		playMusic();
	}
	

	// Uscita gioco
	void doExit() {
		
		// Verifica chi ha vinto
		if (currentPlayer == WHITE) {
			message.setText("BLACK wins because WHITE leaves!");
			blackScore++;
			gameOver("BLACK wins!");
		}
		else {
			message.setText("WHITE wins because BLACK leaves!");
			//p1Score++;
			//gameOver("WHITE wins!");
		}
		
		// Inizializza gioco
		newGameButton.setEnabled(true);
		exitButton.setEnabled(false);
		gameInProgress = false;
		
		
		stopMusic();
	}

	
	// Click cella
	void doClickSquare(int row, int col) {		
		
		// Verifica cella vuota
		if ( board[row][col] != EMPTY ) {
			if (currentPlayer == WHITE)
				message.setText("Not empty...Click an empty square!");
			
			return;
		}

		
		// Mossa
		board[row][col] = currentPlayer;
		Graphics g = getGraphics();
		drawPiece(g, currentPlayer, row, col);
		g.dispose();

		// Verifica se la mossa è vincente
		if (winner(row,col)) {
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
			
			
			stopMusic();
			return;
		}

		// Verifica se il tavolo di gioco è completo
		boolean emptySpace = false;
		for (int i = 0; i < 13; i++)
			for (int j = 0; j < 13; j++)
				if (board[i][j] == EMPTY)
					emptySpace = true;
		if (emptySpace == false) {
			gameOver("Game ends in a draw!");
			
			
			stopMusic();
			return;
		}

		
		// Turno dell'altro player
		if (currentPlayer == BLACK) {
			currentPlayer = WHITE;
			message.setText("WHITE:  Make your move.");
		}
		else {  
			currentPlayer = BLACK;
			Move mos = aiManager.ai_move(board);
			doClickSquare(mos.getRow(), mos.getColumn());
		}
		
		//displayMatrix();
	}
	

	// Mostra tavolo di gioco
	public void displayMatrix() {
		for(int i=0;i<13;i++)
		{
			for(int j=0;j<13;j++)
			{
				System.out.print(board[i][j]);
			}
			
			System.out.println();
		}
	}

	
	// Inizializza Image checker	
	private void initImageChecker()
	{
		try {
			checkerWHITE = ImageIO.read(new File("images/white.png"));
			checkerBLACK = ImageIO.read(new File("images/black.png"));
		} catch (IOException e) {}
	}
	
	
	// Disegna pezzi
	private void drawPiece(Graphics g, int piece, int row, int col) {
		
		// Disegna pezzo
		if (piece == WHITE) {
			g.drawImage(checkerWHITE, col*50, row*50, null);
		}
		else {
			g.drawImage(checkerBLACK, col*50, row*50, null);
		}
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

	
	// Mouse pressed
	public void mousePressed(MouseEvent evt) {
		
		if (gameInProgress == false)
			message.setText("Click \"New Game\" to start a new game!");
		else {
			int col = evt.getX() / 50;
			int row = evt.getY() / 50;
			if (col >= 0 && col < 13 && row >= 0 && row < 13)
				doClickSquare(row,col);
		}
	}

}