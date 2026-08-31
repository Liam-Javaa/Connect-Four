
public class Position {

	char[][] board;
	int wins;
	int losses;
	int occurs;

	
	Position(char[][] board, int wins, int losses, int occurs){
		this.board = board;
		this.losses = losses;
		this.wins = wins;
		this.occurs = occurs;
	}
	public String toString() {
		char[][] tboard = board;
		int twins = wins;
		int tlosses = losses;
		String line = "";
		for(int row = 0; row < board.length; row++) {
			for(int col = 0; col < board[row].length; col++) {
				line += tboard[row][col];
			//	System.out.print(tboard[row][col]);
			}
		}
		
		line += twins + " " + tlosses + " " + occurs;
		return line;
	}
}
