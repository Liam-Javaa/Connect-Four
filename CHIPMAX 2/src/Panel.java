import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
public class Panel extends JPanel implements ActionListener {
	int size = main.size;
	char HUMAN = main.HUMAN;
	static boolean didPlayerJustMove = false;
	JButton one = new JButton("");
	Font red = new Font(one.getFont().getName(), one.getFont().getStyle(), one.getFont().getSize() * 10);
	Font yellow = new Font(one.getFont().getName(), one.getFont().getStyle(), one.getFont().getSize() * 7);
	Image thinking = new ImageIcon("output-onlinepngtools.png").getImage();
	Image Red = new ImageIcon("Red.png").getImage();
	Image Yellow = new ImageIcon("Yellow.png").getImage();
	boolean think = false;
	static int moveCount = 0;
	Panel(){
		setLayout(null);
		one.setBounds(0,0,size * 7 + 5, size * 6 + 5);
		one.setContentAreaFilled(false);
		one.setBorderPainted(false);
		System.out.println(one.getFont().getName());
		one.setFont(red);
		one.addActionListener((ActionListener) this);
		add(one);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		setBackground(Color.blue);
		for (int row = 0; row < 6; row++) {
			for (int col = 0; col < 7; col++) {
				if (main.board[row][col] == 'Y') {
					g.setColor(Color.yellow);
					g.drawImage(Yellow, col * main.size + 5, 500 - row * main.size + 5, size - 5, size - 5, this);
				} else if (main.board[row][col] == 'R') {
					g.setColor(Color.red);
					g.drawImage(Red, col * main.size + 5, 500 - row * main.size + 5, size - 5, size - 5, this);
				} else {
					g.setColor(Color.white);
					g.fillOval(col * main.size + 5, 500 - row * main.size + 5, size - 5, size - 5);
				}
				
			}
		}
		repaint();
		if(didPlayerJustMove == true) {
			main.printBoard(main.board);
			didPlayerJustMove = false;
			g.drawImage(thinking, 0, (int)(size * 6.1), 316, 48, this);
		}
		else if (main.AIPLAYING && main.checkWinner() == ' ') {
			main.findBestMove(main.AI, HUMAN);
			main.EXPONENTIAL++;
			moveCount++;
			main.number = 0;
			main.AIPLAYING = false;
			
		} 
		if (main.checkWinner() != ' ') {
			setBackground(Color.decode("#00BBFF"));
			one.setText("Tie");
			if(main.checkWinner() == 'Y') {
				one.setFont(yellow);
				one.setText("Yellow Wins!");
			}
			if(main.checkWinner() == 'R') {
				one.setText("Red Wins!");
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!main.AIPLAYING && main.checkWinner() == ' ') {
			Point mouseX = one.getMousePosition();
			System.out.println(mouseX.x);
			if (mouseX.x > 0 && mouseX.x < size) {
				main.makeMove(0, HUMAN);
				main.AIPLAYING = true;
			} else if (mouseX.x > size + 5 && mouseX.x < size * 2) {
				main.makeMove(1, HUMAN);
				main.AIPLAYING = true;
			} else if (mouseX.x > size * 2 + 5 && mouseX.x < size * 3) {
				main.makeMove(2, HUMAN);
				main.AIPLAYING = true;
			} else if (mouseX.x > size * 3 + 5 && mouseX.x < size * 4) {
				main.makeMove(3, HUMAN);
				main.AIPLAYING = true;
			} else if (mouseX.x > size * 4 + 5 && mouseX.x < size * 5) {
				main.makeMove(4, HUMAN);
				main.AIPLAYING = true;
			} else if (mouseX.x > size * 5 + 5 && mouseX.x < size * 6) {
				main.makeMove(5, HUMAN);
				main.AIPLAYING = true;
			} else if (mouseX.x > size * 6 + 5 && mouseX.x < size * 7) {
				main.makeMove(6, HUMAN);
				main.AIPLAYING = true;
			}
			didPlayerJustMove = true;
			repaint();
		}
	}
}
