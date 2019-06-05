import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class Player extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	private JLabel imageL, titleL, diceL;
	private BufferedImage logo;
	private JButton newGameB, howToPlayB, exitB, startB, backB, rollB, menuB;
	private JTextField ipTF, usernameTF;
	private JPanel boardP, playersP;
	
	private String ipAddress, username;
	private HashMap<Integer, Cell> labelCellHM;
	private Transparent transparentP;
	
	private Cell[][] cell;

	public Player(String name) {
		super(name);
		
		try {
			UIManager.setLookAndFeel(new NimbusLookAndFeel());
			SwingUtilities.updateComponentTreeUI(this);
		} 
		catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		setSize(600, 400);
		setLayout(null);
		this.getContentPane().setBackground(Color.WHITE);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		try {
			logo = ImageIO.read(new File("./res/logo.png"));
			imageL = new JLabel(new ImageIcon(logo));
			imageL.setBounds(20, 70, 250, 250);
			add(imageL);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		titleL = new JLabel("<html><span style=\"font-family:Arial;font-size:15px;\">Snakes and Ladders</html>", SwingConstants.CENTER);
		titleL.setBounds(10, 10, 560, 50);
		add(titleL);
		
		newGameB = new JButton("New Game");
		newGameB.addActionListener(this);
		newGameB.setBounds(310, 70, 260, 70);
		add(newGameB);
		
		howToPlayB = new JButton("How To Play");
		howToPlayB.addActionListener(this);
		howToPlayB.setBounds(310, 165, 260, 70);
		add(howToPlayB);
		
		exitB = new JButton("Exit");
		exitB.addActionListener(this);
		exitB.setBounds(310, 260, 260, 70);
		add(exitB);
		
		setLocationRelativeTo(null);
//		setUndecorated(true);
		repaint();
		revalidate();
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == newGameB) {
			newGame();
		} else if(e.getSource() == howToPlayB) {
			howToPlay();
		} else if(e.getSource() == exitB) {
			System.exit(0);
		} else if(e.getSource() == startB) {
			start();
		} else if(e.getSource() == backB) {
			remove(startB);
			remove(backB);
			remove(ipTF);
			remove(usernameTF);
			
			add(newGameB);
			add(howToPlayB);
			add(exitB);
			
			repaint();
			revalidate();
		}
	}
	
	public void newGame() {
		remove(newGameB);
		remove(howToPlayB);
		remove(exitB);
		
		ipTF = new JTextField("IP Address");
		ipTF.setBounds(310, 70, 260, 70);
		add(ipTF);
		
		usernameTF = new JTextField("Username");
		usernameTF.setBounds(310, 165, 260, 70);
		add(usernameTF);
		
		startB = new JButton("Start");
		startB.addActionListener(this);
		startB.setBounds(310, 260, 125, 70);
		add(startB);
		
		backB = new JButton("Back");
		backB.addActionListener(this);
		backB.setBounds(445, 260, 125, 70);
		add(backB);
		
		repaint();
		revalidate();
	}
	
	public void howToPlay() {
		
	}
	
	public void start() {
		ipAddress = ipTF.getText();
		username = usernameTF.getText();
		
		remove(ipTF);
		remove(usernameTF);
		remove(imageL);
		remove(titleL);
		remove(startB);
		remove(backB);
		
		setSize(830, 660);
		setLocationRelativeTo(null);
		
		int  n = 10;
		boardP = new JPanel(new GridLayout(n, n, 1, 1));
		boardP.setBounds(10, 10, 600, 600);
		boardP.setBackground(Color.CYAN);
		
		labelCellHM = new HashMap<Integer, Cell>();
		
		cell = new Cell[n][n];
		
		int label;
		for(int i = 0; i < n; i++) {
			label = n * (n - i);
			
			if(i % 2 != 0) 
				label = label - n + 1;
			
			for(int j = 0; j < n; j++) {
				cell[i][j] = new Cell(new GridLayout(2, 1));
				
				if((i+j)%2 == 0)
					cell[i][j].setBackground(Color.WHITE);
				else
					cell[i][j].setBackground(Color.CYAN);
				
				cell[i][j].add(new JLabel(""+ label));
				cell[i][j].label = label;
				labelCellHM.put(label, cell[i][j]);
				
				if(i % 2 == 0)
					label -= 1;
				else
					label += 1;
				
				boardP.add(cell[i][j]);
			}
		}
		
		playersP = new JPanel(new GridLayout(4, 1, 10, 10));
		playersP.setBounds(620, 10, 180, 300);
//		playersP.setBackground(Color.MAGENTA);
		playersP.setOpaque(false);
		
		JPanel p1 = new JPanel(new GridLayout(1, 2));
		p1.setBackground(new Color(255, 255, 180));
		
		JLabel piece1 = new JLabel(new ImageIcon("./res/piece7.png"));
		JLabel name1 = new JLabel("Player 1");
		p1.add(piece1);
		p1.add(name1);
		
		JPanel p2 = new JPanel(new GridLayout(1, 2));
		p2.setBackground(new Color(180, 180, 255));
		
		JLabel piece2 = new JLabel(new ImageIcon("./res/piece2.png"));
		JLabel name2 = new JLabel("Player 2");
		p2.add(piece2);
		p2.add(name2);
		
		JPanel p3 = new JPanel(new GridLayout(1, 2));
		p3.setBackground(new Color(255, 180, 180));
		
		JLabel piece3 = new JLabel(new ImageIcon("./res/piece5.png"));
		JLabel name3 = new JLabel("Player 3");
		p3.add(piece3);
		p3.add(name3);
		
		JPanel p4 = new JPanel(new GridLayout(1, 2));
		p4.setBackground(new Color(180, 255, 180));
		
		JLabel piece4 = new JLabel(new ImageIcon("./res/piece3.png"));
		JLabel name4 = new JLabel("Player 4");
		p4.add(piece4);
		p4.add(name4);
		
		playersP.add(p1);
		playersP.add(p2);
		playersP.add(p3);
		playersP.add(p4);
		
		
		diceL = new JLabel(new ImageIcon("./res/die1.png"));
		diceL.setOpaque(true);
		diceL.setBackground(new Color(250, 250, 250));
		diceL.setBounds(620, 390, 180, 100);
		
		rollB = new JButton("Roll");
		rollB.addActionListener(this);
		rollB.setBounds(620, 500, 180, 50);
		
		menuB = new JButton("Menu");
		menuB.addActionListener(this);
		menuB.setBounds(620, 560, 180, 50);
		
		add(boardP);
		add(playersP);
		add(diceL);
		add(rollB);
		add(menuB);
		
		repaint();
		revalidate();
		
		transparentP = new Transparent(createSnakesPoint(), createLaddersPoint(), null, null);
		transparentP.setBounds(10, 10, 600, 600);
		
		remove(boardP);
		add(transparentP);
		add(boardP);
		
		repaint();
		revalidate();
	}
	
	public ArrayList<Point2D> createSnakesPoint() {
		ArrayList<Point2D> points = new ArrayList<Point2D>();
		
		
		Cell c = labelCellHM.get(70);
		Rectangle rec = c.getBounds();
		Point p = c.getLocation();
		int newX = p.x + (int)Math.ceil(rec.width/2.0);
		int newY = p.y + (int)Math.ceil(rec.height/2.0);
		points.add(new Point2D.Double(newX, newY));
		
		c = labelCellHM.get(15);
		rec = c.getBounds();
		p = c.getLocation();
		newX = p.x + (int)Math.ceil(rec.width/2.0);
		newY = p.y + (int)Math.ceil(rec.height/2.0);
		points.add(new Point2D.Double(newX, newY));
		
		c = labelCellHM.get(20);
		rec = c.getBounds();
		p = c.getLocation();
		newX = p.x + (int)Math.ceil(rec.width/2.0);
		newY = p.y + (int)Math.ceil(rec.height/2.0);
		points.add(new Point2D.Double(newX, newY));
		
		c = labelCellHM.get(10);
		rec = c.getBounds();
		p = c.getLocation();
		newX = p.x + (int)Math.ceil(rec.width/2.0);
		newY = p.y + (int)Math.ceil(rec.height/2.0);
		points.add(new Point2D.Double(newX, newY));
		
		System.out.println("snake points: " + points);
		return points;
	}
	
	public ArrayList<Point2D> createLaddersPoint() {
		ArrayList<Point2D> points = new ArrayList<Point2D>();
		
		
		Cell c = labelCellHM.get(67);
		Rectangle rec = c.getBounds();
		Point p = c.getLocation();
		int newX = p.x + (int)Math.ceil(rec.width/2.0);
		int newY = p.y + (int)Math.ceil(rec.height/2.0);
		points.add(new Point2D.Double(newX, newY));
		
		c = labelCellHM.get(35);
		rec = c.getBounds();
		p = c.getLocation();
		newX = p.x + (int)Math.ceil(rec.width/2.0);
		newY = p.y + (int)Math.ceil(rec.height/2.0);
		points.add(new Point2D.Double(newX, newY));
		
		c = labelCellHM.get(94);
		rec = c.getBounds();
		p = c.getLocation();
		newX = p.x + (int)Math.ceil(rec.width/2.0);
		newY = p.y + (int)Math.ceil(rec.height/2.0);
		points.add(new Point2D.Double(newX, newY));
		
		c = labelCellHM.get(91);
		rec = c.getBounds();
		p = c.getLocation();
		newX = p.x + (int)Math.ceil(rec.width/2.0);
		newY = p.y + (int)Math.ceil(rec.height/2.0);
		points.add(new Point2D.Double(newX, newY));
		
		System.out.println("ladder points: " + points);
		return points;
	}
	
	public static void main(String[] args) {
		new Player("Snakes and Ladders");
	}
}
