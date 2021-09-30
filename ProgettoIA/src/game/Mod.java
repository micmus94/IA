package game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JButton;
import javax.swing.JLabel;


public class Mod extends Canvas implements ActionListener, MouseListener{
	
	private static final long serialVersionUID = 1L;	
	
	private int sizeBoard;
	public static int N = 13;	
	
	JButton exitButton;   // player corrente abbandona la partita in corso
	JButton newGameButton;  // solo quando la partita è terminata.

	JLabel message;
	JLabel whiteScoreLbl, blackScoreLbl;

	int[][] board;

	static final int EMPTY = 0,
			WHITE = 1,      
			BLACK = 2;

	boolean gameInProgress;

	int currentPlayer;      // solo quando la partita è in corso

	int win_r1, win_c1, win_r2, win_c2;  // da (win_r1,win_c1) a (win_r2,win_c2).
	// Quando non c'è una pila di 5, win_r1 = -1. Questo valore viene impostato dal metodo count().
	
	int whiteScore = 0, blackScore = 0;
	
	// Sounds
	Clip clip = null;
	
	
	public Mod() {
		sizeBoard = 650;
		
		setBackground(new Color(249,232,126));
		addMouseListener(this);		
		exitButton = new JButton();
		exitButton.addActionListener(this);
		exitButton.setEnabled(false);
		newGameButton = new JButton();
		newGameButton.addActionListener(this);
		
		message = new JLabel("",JLabel.CENTER);
		whiteScoreLbl = new JLabel("", JLabel.CENTER);
		blackScoreLbl = new JLabel("", JLabel.CENTER);
		board = new int[N][N];
	}
	
	
	// Game over
	void gameOver(String str) {
		
		// notifica chi vince
		message.setText(str);
		whiteScoreLbl.setText("White: "+Integer.toString(whiteScore));
		blackScoreLbl.setText("Black: "+Integer.toString(blackScore));
		
		// Inizializza gioco
		newGameButton.setEnabled(true);
		exitButton.setEnabled(false);
		gameInProgress = false;
		
		
		stopMusic();
	}
	
	
	// Winner, viene richiamato ogni volta che viene posizionato un pezzo sul tavolo di gioco
	public boolean winner(int row, int col) {

		if (count( board[row][col], row, col, 1, 0 ) >= 5)
			return true;
		if (count( board[row][col], row, col, 0, 1 ) >= 5)
			return true;
		if (count( board[row][col], row, col, 1, -1 ) >= 5)
			return true;
		if (count( board[row][col], row, col, 1, 1 ) >= 5)
			return true;

		/* Se si arriva in questo punto, la partita non è ancora stata vinta. Viene resettata la
		 * variabile win_r1, per fare in modo che non venga disegnata la linea rossa. */
		win_r1 = -1;
		
		return false;	// nessun vincitore
	}

	/* Conta il numero di pezzi del giocatore passato a partire dalla cella
	 *  (row, col) e si estende lungo la direzione specificata da
	 *  (dirX, dirY). Si presume che il giocatore abbia un pezzo in
	 *  (row, col). Questo metodo esamina le celle (row + dirX, col + dirY),
	 *  (row + 2*dirX, col + 2*dirY), ... fino a quando non controlla una cella che è
	 *  fuori dal tavolo di gioco oppure non contiene uno dei pezzi del player.
	 *  Inoltre, imposta (win_r1, win_c1) per contrassegnare l'ultima posizione dove 
	 *  ha visto uno dei pezzi del player. Quindi, controlla nella
	 *  direzione opposta, le celle (row - dirX, col - dirY),
	 *  (row - 2*dirX, col - 2*dirY), ... e fa la stessa cosa.
	 *  
	 *  Questa volta imposta (win_r2, win_c2) per segnare l'ultimo pezzo.
	
		I valori di dirX e dirY devono essere 0, 1 o -1.
		Almeno uno di questi deve essere diverso da zero.*/
	private int count(int player, int row, int col, int dirX, int dirY) {

		int ct = 1;  // numero di pezzi in pila del player.

		int r, c;    // riga e colonna da controllare

		r = row + dirX;  // si controlla una specifica direzione
		c = col + dirY;
		
		// cella sul tavolo gioco e contiene uno dei pezzi del player
		while ( r >= 0 && r < 13 && c >= 0 && c < 13 && board[r][c] == player ) {
			ct++;
			r += dirX;  // si controlla la prossima cella in questa direzione
			c += dirY;
		}

		win_r1 = r - dirX;  // La penultima cella controllata.
		win_c1 = c - dirY;  // L'ULTIMA cella controllata era fuori dal tavolo di gioco.
							// oppure non conteneva uno dei pezzi del player.

		r = row - dirX;  // controlla nella direzione opposta
		c = col - dirY;
		
		// cella sul tavolo gioco e contiene uno dei pezzi del player
		while ( r >= 0 && r < 13 && c >= 0 && c < 13 && board[r][c] == player ) {
			ct++;
			r -= dirX;   // si controlla la prossima cella in questa direzione
			c -= dirY;
		}

		win_r2 = r + dirX;
		win_c2 = c + dirY;

		// Ora, (win_r1,win_c1) e (win_r2,win_c2) identificano gli estremi della pila dei pezzi in fila.

		return ct;
	}
	
	// Disegna una linea rossa dal centro della cella (win_r1, win_c1) al centro della cella (win_r2, win_c2).
	public void drawWinLine(Graphics g) {
		g.setColor(Color.red);
		g.drawLine( 25 + 50*win_c1, 25 + 50*win_r1, 25 + 50*win_c2, 25 + 50*win_r2 );
		if (win_r1 == win_r2)
			g.drawLine( 25 + 50*win_c1, 25 + 50*win_r1, 25 + 50*win_c2, 25 + 50*win_r2 );
		else
			g.drawLine( 25 + 50*win_c1, 25 + 50*win_r1, 25 + 50*win_c2, 25 + 50*win_r2 );		
	}
	
	
	// get size board
	public int getSizeBoard() {
		return sizeBoard;
	}

	
	// set size board
	public void setSizeBoard(int sizeBoard) {
		this.sizeBoard = sizeBoard;
	}
	
	
	// Sounds
	public void playMusic() {

		try {
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File("sounds/soundtrack.wav").getAbsoluteFile());
			
			clip = AudioSystem.getClip();
			clip.open(audioIn);
			clip.loop(Clip.LOOP_CONTINUOUSLY);
			clip.start();
		}
		catch (Exception e) { }
	}
	
	public void stopMusic() {
		clip.stop();
		clip.flush();
		clip.setFramePosition(0);
	}

	
	public void mousePressed(MouseEvent evt) { }
	public void mouseReleased(MouseEvent evt) { }
	public void mouseClicked(MouseEvent evt) { }
	public void mouseEntered(MouseEvent evt) { }
	public void mouseExited(MouseEvent evt) { }

	public void actionPerformed(ActionEvent e) { }
}