package game;

import javax.swing.JOptionPane;

//Start game
public class StartGame {
	
	public static void main(String[] args) {
		
		String[] list = new String[] {"-------", "User vs AI", "User vs User"};
		String mod = (String)JOptionPane.showInputDialog(null, "Choose Game Modality:", "Game Modality", JOptionPane.PLAIN_MESSAGE, null, list, list[0]);
		
		if(mod != null)
			if(mod.equals("User vs AI"))
				new GomokuManager(new ModUserVsAI());
			else if(mod.equals("User vs User"))
				new GomokuManager(new ModUserVsUser());
			else if(mod.equals("-------"))
				JOptionPane.showMessageDialog(null, "NO Game Modality Selected!", "Error", JOptionPane.INFORMATION_MESSAGE);
	}
	
}