import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;

public class main {

	static char[][] board = {
			{' ',' ',' ',' ',' ',' ',' '},
			{' ',' ',' ',' ',' ',' ',' '},
			{' ',' ',' ',' ',' ',' ',' '},
			{' ',' ',' ',' ',' ',' ',' '},
			{' ',' ',' ',' ',' ',' ',' '},
			{' ',' ',' ',' ',' ',' ',' '}
	};
	static int size = 100;
	static boolean AIPLAYING = true;
	static char HUMAN = 'Y';
	static char AI = 'R';
	
	static int EXPONENTIAL = 0;
	static ArrayList<Position> positionsY = new ArrayList<>();
	static ArrayList<Position> positionsR = new ArrayList<>();
	static File datar = new File("datar.txt");
	static File datay = new File("datay.txt");
	static int number = 0;
	public static void main(String[] args) throws FileNotFoundException {
		
		if(AIPLAYING) {
			AI = 'Y';
			HUMAN = 'R';
		}
		Scanner scany = new Scanner(datay);
		while(scany.hasNextLine()) {
			String t = scany.nextLine();
			positionsY.add(toPosition(t));
		}
		
		//System.out.println(positionsY.get(0).toString());
		
		
		
		Scanner scanr = new Scanner(datar);
		while(scanr.hasNextLine()) {
			String t = scanr.nextLine();
			positionsR.add(toPosition(t));
		}
		//System.out.println(positionsR.get(0).toString());
		
		for(int i = 0; i < positionsY.size(); i++) {
			for(int j = i; j < positionsY.size(); j++) {
				if(((double)positionsY.get(i).wins) / ((double)positionsY.get(i).losses) < ((double)positionsY.get(j).wins) / ((double)positionsY.get(j).losses)) {
					Position temp = positionsY.get(j);
					positionsY.set(j, positionsY.get(i));
					positionsY.set(j, temp);
				}
			}
		}
		for(int i = 0; i < positionsR.size(); i++) {
			for(int j = i; j < positionsR.size(); j++) {
				if(((double)positionsR.get(i).wins) / ((double)positionsR.get(i).losses) < ((double)positionsR.get(j).wins) / ((double)positionsR.get(j).losses)) {
							Position temp = positionsR.get(j);
							positionsR.set(j, positionsR.get(i));
							positionsR.set(j, temp);
				}
			}
		}
		
		
		
		
		
		
		
		

		
		JFrame frame = new JFrame("CONNECT FOUR");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(7 * size + 20, 6 * size + 50 + 50);
		frame.add(new Panel());
		frame.setVisible(true);
	}
	
	static void makeMove(int place, char player) {
		int placeRow = 0;
		for(int row = 0; row < board.length; row++) {
			
			if(board[row][place] != ' ') {
				placeRow++;
			}
		}
		if(placeRow != 7) {
			board[placeRow][place] = player;
		}
	}
	static void printBoard(char[][] board) {
		for(int row = board.length -1 ; row >= 0; row--) {
			System.out.print("| ");
			for(int col = 0; col < board[0].length; col++) {
				System.out.print(board[row][col] + " | ");
			}
			System.out.println();
		}
	}
	static char checkWinner() {
		for(int row = 0; row < board.length; row++) {
			for(int col = 0; col < board[row].length; col++) {
				char temp = board[row][col];
				if(temp != ' ') {
					try {
						if(		
								board[row][col+1] == temp &&
								board[row][col+2] == temp &&
								board[row][col+3] == temp) {
							return temp;
						}
					} catch (Exception e) {
					}
					try {
						if(		
								board[row+1][col] == temp &&
								board[row+2][col] == temp &&
								board[row+3][col] == temp) {
							return temp;
						}
					} catch (Exception e) {
					}
					
					try {
						if(		
								board[row-1][col-1] == temp &&
								board[row-2][col-2] == temp &&
								board[row-3][col-3] == temp) {
							return temp;
						}
					} catch (Exception e) {
					}
					try {
						if(		
								board[row+1][col-1] == temp &&
								board[row+2][col-2] == temp &&
								board[row+3][col-3] == temp) {
							return temp;
						}
					} catch (Exception e) {
					}
					
				}
			}
		}
		for(int row = 0; row < board.length; row++) {
			for(int col = 0; col < board[row].length; col++) {
				if(board[row][col] == ' ') {
					return ' ';
				}
			}
		}
			return 'e';
	}
	
	static void findBestMove(char player, char opponent) {
		double bestScore = Integer.MIN_VALUE;
		ArrayList<Integer> random = new ArrayList<>();
		int[] bestMove = {-1,-1};
		for(int col = 0; col < board[0].length; col++) {
			System.out.print(".");
			int placeRow = 0;
			for(int row = 0; row < board.length - 1; row++) {
				if(board[row][col] != ' ') {
					placeRow++;
				}
			}
			if(placeRow != 6) {
				if (board[placeRow][col] == ' ') {
					board[placeRow][col] = player;
					int score = MiniMax(false, 0, player, opponent , col);
					board[placeRow][col] = ' ';
					if (score > bestScore) {
						bestScore = score;
						random.clear();
						bestMove[0] = placeRow;
						bestMove[1] = col;
					}
					if(score == bestScore) {
						random.add(col);
					}
					if (score != bestScore && bestScore > -100) {
					} 
				}
			}
		}
		if(random.size() > 1) {
			int t = (int)(Math.random() * random.size());
			bestMove[1] = random.get(t);
			int placeRow = 0;
			for(int row = 0; row < board.length; row++) {
				if(board[row][bestMove[1]] != ' ') {
					placeRow++;
				}
			}
			bestMove[0] = placeRow;
		}
		if(bestMove[0] != -1) {
		//	System.out.println(bestMove[0] + " " + bestMove[1]);
			board[bestMove[0]][bestMove[1]] = player;
		}
		else {
			System.out.println("Good Game! \nIt's a tie!");
		}
		System.out.println();
	}

	static int MiniMax(boolean isMax, int depth, char player, char opponent, int col1) {
		char check = checkWinner();
		int wait = (int) (7 + Math.pow(1.1, EXPONENTIAL));
		//wait = 8;
		if(depth == 1) {
			System.out.println(check + " " + col1);
			System.out.println();
		}
		
		if(check == player) {
			//System.out.println("player wins");
			if(depth == 2) {
				System.out.println("win" + col1);
			}
			return 1000000 - depth;
		}
		else if(check == opponent) {
			
			if(depth == 1) {
				//System.out.println("loss");
			}
			return -1000000 + depth;
		}
		else if(check == 'e') {
			return 0;
		}
		else if(depth >= wait) {
			
			return -100;
		}
		else if (player == 'Y' && Panel.moveCount < 3 && depth == 0) {
			//System.out.println("In This LOPP");
			for(int i = 0; i < positionsY.size(); i++) {
				if((areBoardsSame(board,positionsY.get(i).board) || areBoardsSame(flip(board),positionsY.get(i).board)) && positionsY.get(i).occurs >= 30) {
					if(positionsY.get(i).losses == 0) {
						return 9999;
					}
					return ((int) (((((double) positionsY.get(i).wins) / ((double) positionsY.get(i).occurs )) - 0.5) * 20000));
				}
			}
		}
		else if (player == 'R' && Panel.moveCount < 3 && depth == 0) {
			for(int i = 0; i < positionsR.size(); i++) {
				//System.out.println(i);
				if((areBoardsSame(board,positionsR.get(i).board) || areBoardsSame(flip(board),positionsR.get(i).board)) && positionsR.get(i).occurs >= 30) {
					if(positionsR.get(i).losses ==0) {
						return 9999;
					}
					return ((int) (((((double) positionsR.get(i).wins) / ((double) positionsR.get(i).losses)) - 0.5) * 20000));
				}
			}
		}
		
		
		if(isMax) {
			int bestScore = Integer.MIN_VALUE;
				for(int col = 0; col < board[0].length; col++) {
					int placeRow = 0;
					for(int row = 0; row < board.length; row++) {
						if(board[row][col] != ' ') {
							placeRow++;
						}
					}
					if(placeRow != 6) {
						if(board[placeRow][col] == ' ') {
							board[placeRow][col] = player;
							int score = MiniMax(false, depth + 1, player, opponent, col);
							board[placeRow][col] = ' ';
							if(score > bestScore) {
								bestScore = score;
							}
						}
					}
					
				}
			return bestScore;
		}
		else {
			
			int bestScore = Integer.MAX_VALUE;
			for(int col = 0; col < board[0].length; col++) {
				int placeRow = 0;
				for(int row = 0; row < board.length; row++) {
					if(board[row][col] != ' ') {
						placeRow++;
					}
				}
				if(placeRow != 6) {
					if(board[placeRow][col] == ' ') {
						board[placeRow][col] = opponent;
						int score = MiniMax(true, depth + 1, player, opponent, col);
						board[placeRow][col] = ' ';
						if(score < bestScore) {
							bestScore = score;
						}
					}
				}
			}
			return bestScore;
		}
	}
	
	static Position toPosition(String string) {
		char[][] tboard = new char[6][7];
		int pos = 0;
		
		for(int row = 0; row < tboard.length; row++) {
			for(int col = 0; col < tboard[row].length; col++) {
				tboard[row][col] = string.charAt(pos);
				pos++;
			}
		}
		
		String temp = string.substring(42);
		int twins = Integer.parseInt(temp.substring(0, temp.indexOf(' ')));
		temp = temp.substring(temp.indexOf(' ') + 1);
		int tlosses = Integer.parseInt(temp.substring(0,temp.indexOf(' ')));
		int tOc = Integer.parseInt(temp.substring(temp.indexOf(' ') + 1));
		return new Position(tboard, twins, tlosses, tOc);
	}
	
	
	static boolean areBoardsSame(char[][] pointBoard, char[][] checkBoard) {
		for(int row = 0; row < pointBoard.length; row++) {
			for(int col = 0; col < pointBoard[row].length; col++) {
				if(pointBoard[row][col] != checkBoard[row][col]) {
					return false;
				}
			}
		}
		return true;
	}

	public static void write(char c) {
		if(c == 'R') {
			String greatString = "";
			for(int i = 0; i < positionsR.size(); i++) {
				greatString += positionsR.get(i).toString() + "\n";
			}
			try {
	            // Overwrites the file if it exists, or creates a new one
				Files.writeString(Path.of("datar.txt"), greatString);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		}
		if(c == 'Y') {
			String greatString = "";
			for(int i = 0; i < positionsY.size(); i++) {
				greatString += positionsY.get(i).toString() + "\n";
			}
			
			try {
	            // Overwrites the file if it exists, or creates a new one
				Files.writeString(Path.of("datay.txt"), greatString);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		}
	}

	public static void reset() {
		// TODO Auto-generated method stub
		for(int row = 0; row < board.length; row++) {
			for(int col = 0; col < board[row].length; col++) {
				board[row][col] = ' ';
			}
		}
		Panel.didPlayerJustMove = false;
		AIPLAYING = false;
	}
	
	public static char[][] flip(char[][] board){
		char[][] blank = new char [6][7];
		for(int row = 0; row < board.length; row++) {
			for(int col = 0; col < board[row].length; col++) {
				switch (col) {
				case 0:
					blank[row][6] = board[row][col];
					break;
				case 1:
					blank[row][5] = board[row][col];
					break;
				case 2:
					blank[row][4] = board[row][col];
					break;
				case 3:
					blank[row][3] = board[row][col];
					break;
				case 4:
					blank[row][2] = board[row][col];
					break;
				case 5:
					blank[row][1] = board[row][col];
					break;
				case 6:
					blank[row][0] = board[row][col];
					break;
				}
			}
		}
		return blank;
	}
	
	public char checkAlmost4(char[][] board) {
		
		for(int row = 0; row < board.length; row++) {
			
			for(int col = 0; col < board[row].length; col++) {
				
				char temp = board[row][col];
				if (temp != ' ') {
					int check = 1;
					for (int i = 1; i <= 3; i++) {
						try {
							if (board[row][col - i] == temp) {
								check++;
							}
						} catch (Exception e) {
							break;
						}
					}
					if (check == 3) {
						return temp;
					}
					check = 1;
					for (int i = 1; i <= 3; i++) {
						try {
							if (board[row][col + i] == temp) {
								check++;
							}
						} catch (Exception e) {
							break;
						}
					}
					if (check == 3) {
						return temp;
					}
					check = 1;
				}
			}
			
		}
		
		return ' ';
		
	}
}
