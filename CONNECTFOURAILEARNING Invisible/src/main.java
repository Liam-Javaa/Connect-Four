import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class main {

	static char[][] board = {
			{' ',' ',' ',' ',' ',' ',' '},
			{' ',' ',' ',' ',' ',' ',' '},
			{' ',' ',' ',' ',' ',' ',' '},
			{' ',' ',' ',' ',' ',' ',' '},
			{' ',' ',' ',' ',' ',' ',' '},
			{' ',' ',' ',' ',' ',' ',' '}
	};
	
	static ArrayList<Position> positionsY = new ArrayList<>();
	static ArrayList<Position> positionsR = new ArrayList<>();
	
	
	static int size = 10;
	static boolean AIPLAYING = false;
	static char HUMAN = 'Y';
	static char AI = 'R';
	static int EXPONENTIAL = 0;
	static File datar = new File("datar.txt");
	static File datay = new File("datay.txt");
	
	
	static int moveCount = 0;
	static boolean didPlayerJustMove = false;
	static boolean didAIJustMove = false;
	static boolean recordR = false;
	static int[] recordintR = {-1,-1,-1};
	static boolean recordY = false;
	static int[] recordintY = {-1,-1,-1};
	static int gamesPlayed = 0;
	boolean sstop = false;
	boolean think = false;
	//static File datar = new File("datar.txt");
	public static void main(String[] args) throws Exception {

		double small = 0.01;
	    
	    
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
		
		
		
		
		
		while(true) {
			if(didPlayerJustMove == true) {
				if(moveCount <= 8) {
					if(moveCount != 4) {
					}
					boolean tear = false;
					for(int i = 0; i < main.positionsY.size(); i++) {
						if(main.areBoardsSame(main.board, main.positionsY.get(i).board) || areBoardsSame(flip(board), positionsY.get(i).board)) {
							tear = true;
							recordY = true;
							main.positionsY.get(i).occurs++;
							recordintY[moveCount/4] = i;
							break;
						}
					}
					if(!tear) {
						char[][] newBoard = {
								{' ',' ',' ',' ',' ',' ',' '},
								{' ',' ',' ',' ',' ',' ',' '},
								{' ',' ',' ',' ',' ',' ',' '},
								{' ',' ',' ',' ',' ',' ',' '},
								{' ',' ',' ',' ',' ',' ',' '},
								{' ',' ',' ',' ',' ',' ',' '}
						};
						for(int row = 0; row < newBoard.length; row++) {
							for(int col = 0; col < newBoard[row].length; col++) {
								newBoard[row][col] = main.board[row][col];
							}
						}
						main.positionsY.add(new Position(newBoard,0,0,1));
						recordY = true;
						
						recordintY[moveCount/4] = main.positionsY.size() - 1;
					}
				}
				didPlayerJustMove = false;
			}
			if(didAIJustMove) {
				moveCount += 4;
				if(moveCount <= 12) {
					if(moveCount != 4) {
						//System.out.println("GOOD");
					}
					boolean tear = false;
					for(int i = 0; i < main.positionsR.size(); i++) {
						if(main.areBoardsSame(main.board, main.positionsR.get(i).board) || areBoardsSame(flip(board), positionsR.get(i).board)) {
							tear = true;
							recordR = true;
							main.positionsR.get(i).occurs++;
							recordintR[moveCount/4 - 1] = i;
							break;
						}
					}
					if(!tear) {
						char[][] newBoard = {
								{' ',' ',' ',' ',' ',' ',' '},
								{' ',' ',' ',' ',' ',' ',' '},
								{' ',' ',' ',' ',' ',' ',' '},
								{' ',' ',' ',' ',' ',' ',' '},
								{' ',' ',' ',' ',' ',' ',' '},
								{' ',' ',' ',' ',' ',' ',' '}
						};
						for(int row = 0; row < newBoard.length; row++) {
							for(int col = 0; col < newBoard[row].length; col++) {
								newBoard[row][col] = main.board[row][col];
							}
						}
						main.positionsR.add(new Position(newBoard,0,0,1));
						recordR = true;
						
						recordintR[moveCount/4 - 1] = main.positionsR.size() - 1;
					}
				}
				didAIJustMove = !didAIJustMove;
				if(moveCount == 4) {
					
				}
			}
			else if (main.AIPLAYING && main.checkWinner() == ' ') {
				
				main.findBestMove(AI,HUMAN);
				main.AIPLAYING = false;
				didAIJustMove = true;
				
				
			}
			else if(!main.AIPLAYING && main.checkWinner() == ' ') {
				main.findBestMove(HUMAN,AI);
				didPlayerJustMove = true;
				main.AIPLAYING = true;
			//	main.printBoard();
			}
			if (main.checkWinner() != ' ') {
				if (main.checkWinner() == 'Y') {
					if (recordR == true) {
						main.positionsR.get(recordintR[0]).losses++;
						main.positionsR.get(recordintR[1]).losses++;
						main.positionsR.get(recordintR[2]).losses++;
						recordR = false;
					}
					if (recordY == true) {
						main.positionsY.get(recordintY[0]).wins++;
						main.positionsY.get(recordintY[1]).wins++;
						main.positionsY.get(recordintY[2]).wins++;
						recordY = false;
					}
				}
				if (main.checkWinner() == 'R') {
					if (recordR == true) {
						main.positionsR.get(recordintR[0]).wins++;
						main.positionsR.get(recordintR[1]).wins++;
						main.positionsR.get(recordintR[2]).wins++;
						recordR = false;
					}
					if (recordY == true) {
						main.positionsY.get(recordintY[0]).losses++;
						main.positionsY.get(recordintY[1]).losses++;
						main.positionsY.get(recordintY[2]).losses++;
						recordY = false;
					}
				}
				gamesPlayed++;
				if (gamesPlayed % 1000 == 0) {
					main.write('R');
					main.write('Y');
				}
				
				main.reset();
				
				
				System.out.println(gamesPlayed);
			}
		}
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
	static void printBoard() {
		for(int row = board.length -1 ; row >= 0; row--) {
			System.out.print("| ");
			for(int col = 0; col < board[0].length; col++) {
				System.out.print(board[row][col] + " | ");
			}
			System.out.println();
		}
	}
	static char checkWinner() {
		boolean space = false;
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
				else {
					space = true;
				}
			}
		}
		if(space) {
			return ' ';
		}
			return 'e';
	}
	
	static void findBestMove(char player, char opponent) {
		double bestScore = Integer.MIN_VALUE;
		ArrayList<Integer> random = new ArrayList<>();
		int[] bestMove = {-1,-1};
		for(int col = 0; col < board[0].length; col++) {
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
	}

	static int MiniMax(boolean isMax, int depth, char player, char opponent, int col1) {
		char check = checkWinner();
		int wait = (int) (7 + Math.pow(1.1, EXPONENTIAL));
		wait = 4;
		
		if(check == player) {
			return 1000000 - depth;
		}
		else if(check == opponent) {
			return -1000000 - depth;
		}
		else if(check == 'e') {
			return 0;
		}
		else if(depth >= wait) {
			
			return -100;
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
		moveCount = 0;
		didPlayerJustMove = false;
		didAIJustMove = false;
		recordR = false;
		recordintR[0] = -1;
		recordintR[1] = -1;
		recordintR[2] = -1;
		recordY = false;
		recordintY[0] = -1;
		recordintY[1] = -1;
		recordintY[2] = -1;
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
}
