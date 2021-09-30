package game;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;


public class GomokuManager extends JFrame {

	private static final long serialVersionUID = 1L;
	
	
	public GomokuManager(Mod board) {
		
		add(board);
		add(board.newGameButton);
		add(board.exitButton);

		board.message.setForeground(Color.white);
		board.message.setFont(new Font("Serif", Font.BOLD, 20));
		add(board.message);
		board.whiteScoreLbl.setForeground(Color.white);
		board.whiteScoreLbl.setFont(new Font("Serif", Font.BOLD, 30));
		add(board.whiteScoreLbl);
		board.blackScoreLbl.setForeground(Color.black);
		board.blackScoreLbl.setFont(new Font("Serif", Font.BOLD, 30));
		add(board.blackScoreLbl);

		

		board.setBounds(16,11,board.getSizeBoard(),board.getSizeBoard());
		board.newGameButton.setBounds(710, 60, 200, 60);
		board.exitButton.setBounds(715, 150, 190, 60);
		
		Image newGame_image = null, exit_image = null;
		try {
			newGame_image = ImageIO.read(new File("images/newGame.jpg"));
			exit_image = ImageIO.read(new File("images/exit.jpg"));
		} catch (IOException e1) {	}
		
		board.newGameButton.setIcon(new ImageIcon(newGame_image.getScaledInstance(200, 60, Image.SCALE_SMOOTH)));
		board.exitButton.setIcon(new ImageIcon(exit_image.getScaledInstance(190, 60, Image.SCALE_SMOOTH)));
				
		board.message.setBounds(115, 675, 500, 30);
		board.whiteScoreLbl.setBounds(710, 300, 190, 60);
		board.blackScoreLbl.setBounds(710, 400, 190, 60);
		
		
		setTitle("GOMOKU");
		setSize(1000, 800);
		setLayout(null);
		getContentPane().setBackground(Color.LIGHT_GRAY);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		setResizable(false);
		
		Image icon = null;	
		
		// set icon
		try {
			icon = ImageIO.read(new File("images/icon.png"));
		} catch (IOException e) {   }

		setIconImage(icon);				
	}
	
	
	public void paint(Graphics g) {
		
		Image background = null;
		
		try {
			background = ImageIO.read(new File("images/background.jpg"));
		} catch (IOException e) {}
		
		// Disegna background
		g.drawImage(background, 0, 23, 1000, 780, null);
	}
	
}