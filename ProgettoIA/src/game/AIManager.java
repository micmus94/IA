package game;
/*import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;*/

import it.unical.mat.embasp.base.Handler;
import it.unical.mat.embasp.base.InputProgram;
import it.unical.mat.embasp.base.Output;
import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import it.unical.mat.embasp.languages.asp.ASPMapper;
import it.unical.mat.embasp.languages.asp.AnswerSet;
import it.unical.mat.embasp.languages.asp.AnswerSets;
import it.unical.mat.embasp.platforms.desktop.DesktopHandler;
import it.unical.mat.embasp.specializations.dlv2.desktop.DLV2DesktopService;

public class AIManager {
	
	private static String encodingResource="encodings/gomoku";
	
	private static Handler handler;
	
	
	public Move ai_move(int board[][]) {
		
		// Mostra programma ASP (String)
		//System.out.println(getEncodings());
		
		Move move = null;
		
		handler = new DesktopHandler(new DLV2DesktopService("lib/dlv2.win.x64_5"));
		
		InputProgram facts= new ASPInputProgram();
		for(int i=0;i<Mod.N;i++){
			
			for(int j=0;j<Mod.N;j++){
				
				//if(board[i][j]!=0){
					try {
						facts.addObjectInput(new Cell(i, j, board[i][j]));
					} catch (Exception e) {
						e.printStackTrace();
					}
				//}
				
			}
			
		}
		handler.addProgram(facts);
		
		InputProgram encoding = new ASPInputProgram();
		//encoding.addProgram(getEncodings());
		encoding.addFilesPath(encodingResource);
		handler.addProgram(encoding);
		//handler.startAsync(new MyCallback(gomokuMatrix));
		
		
		// register the class for reflection
		try {
			ASPMapper.getInstance().registerClass(Move.class);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		Output o =  handler.startSync();
		
		AnswerSets answers = (AnswerSets) o;
		for(AnswerSet a:answers.getAnswersets()){

			try {
				for(Object obj:a.getAtoms()){
					if(obj instanceof Move){
						move = (Move) obj;
						System.out.println(move.getRow()+" "+move.getColumn());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		return move;		
	}

	
	// Lettura file programma ASP (String)
	/*private static String getEncodings() {
		BufferedReader reader;
		StringBuilder builder = new StringBuilder();
		try {
			reader = new BufferedReader(new FileReader(encodingResource));
			String line = "";
			while ((line = reader.readLine()) != null) {
				builder.append(line);
				builder.append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return builder.toString();
	}*/

}