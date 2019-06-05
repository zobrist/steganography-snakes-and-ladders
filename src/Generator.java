import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class Generator extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JLabel playersL, dimensionL, encodingL, cryptographicL, keyL, seedL, messageL;
	private JTextField seedTF;
	private JTextArea keyTA, messageTA;
	private JComboBox<String> playersCB, dimensionCB, encodingCB, cryptographicCB;
	private JButton generateGameB, closeB, generateSeedB, generateKeyB, startB;
	private JScrollPane keySP, messageSP;
	private JFrame outputFrame;
	
	private String encodings[] = {"Huffman (local)", "Huffman (global)"};
	private String dimensions[] = {"8 x 8", "10 x 10", "12 x 12"};
	private String players[] = {"1", "2", "3", "4"};
	private String cryptos[] = {"Bit Stream Cipher", "AES"};
	
	private Encoder encoder;
	private Encryptor encryptor;
	private Embedder embedder;
	
	private String secretMessage, encodedMessage, ciphertext, key;
	private int seed, nDimension, numPlayers;
	
	private HashMap<Integer, Cell> labelCellHM;
	private HashMap<Integer, Integer> snakesHM;
	private HashMap<Integer, Integer> laddersHM;
	
	private ArrayList<Point2D> snakesPoints;
	private ArrayList<Point2D> laddersPoints;
	
	private ArrayList<Integer> snakesCounter;
	private ArrayList<Integer> laddersCounter;
	
	private Transparent transparentP;
	
	public Generator(String name) {
		super(name);
		
		try {
			UIManager.setLookAndFeel(new NimbusLookAndFeel());
			SwingUtilities.updateComponentTreeUI(this);
		} 
		catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		setSize(600, 500);
		setLayout(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		playersL = new JLabel("Number of Players");
		playersL.setBounds(10, 10, 200, 30);
		
		playersCB = new JComboBox<String>(players);
		playersCB.setBounds(170, 10, 300, 30);
		
		add(playersL);
		add(playersCB);
		
		dimensionL = new JLabel("Gameboard Dimension");
		dimensionL.setBounds(10, 50, 200, 30);
		
		dimensionCB = new JComboBox<String>(dimensions);
		dimensionCB.setBounds(170, 50, 300, 30);
		
		add(dimensionL);
		add(dimensionCB);
		
		encodingL = new JLabel("Encoding Scheme");
		encodingL.setBounds(10, 90, 200, 30);
		
		encodingCB = new JComboBox<String>(encodings);
		encodingCB.setBounds(170, 90, 300, 30);
		
		add(encodingL);
		add(encodingCB);
		
		cryptographicL = new JLabel("Cryptographic Scheme");
		cryptographicL.setBounds(10, 130, 200, 30);
		
		cryptographicCB = new JComboBox<String>(cryptos);
		cryptographicCB.setBounds(170, 130, 300, 30);
		
		add(cryptographicL);
		add(cryptographicCB);
		
		seedL = new JLabel("Random Seed");
		seedL.setBounds(10, 170, 200, 30);
		
		seedTF = new JTextField();
		seedTF.setBounds(170, 170, 300, 30);
		
		generateSeedB = new JButton("Generate");
		generateSeedB.addActionListener(this);
		generateSeedB.setBounds(480, 170, 100, 30);
		
		add(seedL);
		add(seedTF);
		add(generateSeedB);
		
		keyL = new JLabel("Symmetric Key");
		keyL.setBounds(10, 210, 200, 30);
		
		keyTA = new JTextArea();
		keyTA.setLineWrap(true);
		keyTA.setWrapStyleWord(true);
		
		keySP = new JScrollPane(keyTA);
		keySP.setBounds(170, 210, 300, 70);
		
		generateKeyB = new JButton("Generate");
		generateKeyB.addActionListener(this);
		generateKeyB.setBounds(480, 210, 100, 30);
		
		add(keyL);
		add(keySP);
		add(generateKeyB);
		
		messageL = new JLabel("Secret Message");
		messageL.setBounds(10, 290, 200, 30);
		
		messageTA = new JTextArea();
		messageTA.setLineWrap(true);
		messageTA.setWrapStyleWord(true);
		
		messageSP = new JScrollPane(messageTA);
		messageSP.setBounds(170, 290, 300, 100);
		
		add(messageL);
		add(messageSP);
		
		generateGameB = new JButton("Generate Game");
		generateGameB.addActionListener(this);
		generateGameB.setBounds(10, 410, 280, 30);
		
		closeB = new JButton("Close Generator");
		closeB.addActionListener(this);
		closeB.setBounds(300, 410, 280, 30);
		
		add(generateGameB);
		add(closeB);
		
		encoder = new Encoder();
		encryptor = new Encryptor();
		embedder = new Embedder();
		labelCellHM = new HashMap<Integer, Cell>();
		snakesHM = new HashMap<Integer, Integer>();
		laddersHM = new HashMap<Integer, Integer>();
		
		snakesPoints = new ArrayList<Point2D>();
		laddersPoints = new ArrayList<Point2D>();
		
		snakesCounter = new ArrayList<Integer>();
		laddersCounter = new ArrayList<Integer>();
		
		setLocationRelativeTo(null);
		repaint();
		revalidate();
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == generateGameB) {
			generate();
		} else if(e.getSource() == closeB) {
			System.exit(0);
		} else if(e.getSource() == generateKeyB) {
			String cipher = (String) cryptographicCB.getSelectedItem();
			secretMessage = messageTA.getText(); 
			
			if(cipher.equalsIgnoreCase("Bit Stream Cipher")) {
				if(secretMessage == null || secretMessage.trim().equalsIgnoreCase("")) {
					JOptionPane.showMessageDialog(this, "Please enter secret message first.");
				} else {
					encoder.encode(secretMessage);
					encryptor.encrypt(encoder.getEncodedMessage(), 2);
					keyTA.setText(encryptor.getKeyHex());
				}
				
			} else if(cipher.equalsIgnoreCase("AES")) {
				JOptionPane.showMessageDialog(this, "You select AES");
			}
		} else if(e.getSource() == generateSeedB) {
			SecureRandom random = new SecureRandom();
			seedTF.setText("" + random.nextInt(1000000));
		} else if(e.getSource() == startB) {
			Game newGame = new Game(numPlayers, nDimension*nDimension, snakesHM, laddersHM, embedder.getDiceArray(), seed);
			newGame.start();
		}
	}
	
	public void generate() {
		System.out.println("Generate method");
		this.secretMessage = messageTA.getText();
		this.encodedMessage = encoder.getEncodedMessage();
		this.ciphertext = encryptor.getCiphertext();
		this.seed = Integer.parseInt(seedTF.getText());
		this.key = keyTA.getText();
		this.numPlayers = Integer.parseInt((String) playersCB.getSelectedItem());
		
		String dimension = (String) dimensionCB.getSelectedItem();
		if(dimension.equalsIgnoreCase("8 x 8")) {
			this.nDimension = 8;
		} else if(dimension.equalsIgnoreCase("10 x 10")) {
			this.nDimension = 10;
		} else if(dimension.equalsIgnoreCase("12 x 12")) {
			this.nDimension = 12;
		}
		
		System.out.println("Before conversion");
		BigInteger cD = Converter.base10ConversionBigInt(ciphertext, 2);
		String cN = Converter.convert(cD, BigInteger.valueOf(nDimension));
		
		System.out.println("After conversion");
		
		String c1, c2;
		if(cN.length() <= 4*nDimension) {
			c1 = cN;
			char[] zeros = new char[4*nDimension];
			Arrays.fill(zeros, '0');
			
			c1 = c1 + new String(zeros).substring(0, 4 * nDimension - cN.length());
			c2 = "";
		} else {
			c1 = cN.substring(0, 4*nDimension);
			c2 = cN.substring(4*nDimension, cN.length());
		}
		
		System.out.println("before embedding");
		embedder.embed(c1, c2, seed, cN.length());
		System.out.println("after embedding");
		
		System.out.println("Secret Message: " + secretMessage);
		System.out.println("Encoded Message: " + encodedMessage);
		System.out.println("Ciphertext: " + ciphertext);
		System.out.println("C1: " + c1);
		System.out.println("C2: " + c2);
		System.out.println("Seed: " + seed);
		System.out.println("Key: " + key);
		System.out.println("Players: " + numPlayers);
		System.out.println("Dimension: " + nDimension);
		
		outputFrame = new JFrame("Generated Snakes and Ladders Game");
		outputFrame.setLayout(null);
		outputFrame.setSize(1000, 660);
		outputFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		outputFrame.setLocationRelativeTo(this);
		outputFrame.setVisible(true);
		
		int n = nDimension;
				
		JPanel boardP = new JPanel(new GridLayout(n, n, 1, 1));
		boardP.setBounds(10, 10, 600, 600);
		boardP.setBackground(Color.CYAN);
		
		Cell[][] cell = new Cell[n][n];
		
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
		
		Ladder[] ladders = embedder.getLadder();
		Snake[] snakes = embedder.getSnake();
		
		for(int i = 0; i < ladders.length; i++) {
			Ladder l = ladders[i];
			Snake s = snakes[i];
			
//			cell[n-l.topY-1][l.topX].add(new JLabel("LT" + (i+1)));
//			cell[n-l.bottomY-1][l.bottomX].add(new JLabel("LB" + (i+1)));
			if(l != null) {
				laddersHM.put(cell[n-l.bottomY-1][l.bottomX].label, cell[n-l.topY-1][l.topX].label);
				laddersCounter.add(ladders[i].topCtr);
				laddersCounter.add(ladders[i].bottomCtr);
			}
			
//			cell[n-s.headY-1][s.headX].add(new JLabel("SH" + (i+1)));
//			cell[n-s.tailY-1][s.tailX].add(new JLabel("ST" + (i+1)));
			
			if(s != null) {
				snakesHM.put(cell[n-s.headY-1][s.headX].label, cell[n-s.tailY-1][s.tailX].label);
				snakesCounter.add(snakes[i].headCtr);
				snakesCounter.add(snakes[i].tailCtr);
			}
			
//			System.out.println("Snake #" + (i+1) + ": " + "Head(" + snakes[i].headX + ", " + snakes[i].headY + 
//					", " + snakes[i].headCtr + ") Tail(" + snakes[i].tailX + ", " + snakes[i].tailY + 
//					", " + snakes[i].tailCtr + ")");
//			
//			System.out.println("Ladder #" + (i+1) + ": " + "Top(" + ladders[i].topX + ", " + ladders[i].topY + 
//					", " + ladders[i].topCtr + ") Bottom(" + ladders[i].bottomX + ", " + ladders[i].bottomY + 
//					", " + ladders[i].bottomCtr + ")");
		}
		
//		boardHM.put(labelCellHM.get(n*n).label, -1);
		
		int diceArr[] = embedder.getDiceArray();
		JLabel[] dice = new JLabel[diceArr.length];
		
		JPanel diceP = new JPanel(new WrapLayout(FlowLayout.LEFT, 3, 3));
		
		JScrollPane scrollPane = new JScrollPane(diceP);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBackground(Color.LIGHT_GRAY);
		scrollPane.setBounds(620, 10, 355, 540);
		scrollPane.setBorder(null);
		
		for(int i = 0; i < diceArr.length; i++) {
			dice[i] = new JLabel("<html><span style=\"font-family:Arial;font-size:15px;\">" + diceArr[i] + "</html>", SwingConstants.CENTER);
			dice[i].setOpaque(true);
			dice[i].setBackground(new Color(255, 236, 193));
			dice[i].setPreferredSize(new Dimension(30, 30));
			dice[i].setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
			diceP.add(dice[i]);
		}
		
//		System.out.println("Dice sequence: " + Arrays.toString(embedder.getDiceArray()));
		
		startB = new JButton("Start Game");
		startB.addActionListener(this);
		startB.setBounds(620, 560, 355, 50);
		
		outputFrame.add(boardP);
		outputFrame.add(scrollPane);
		outputFrame.add(startB);
		
		outputFrame.repaint();
		outputFrame.revalidate();
		
		repaint();
		revalidate();
		
//		labelCellHM.get(1).setBackground(Color.YELLOW);
//		labelCellHM.get(n*n).setBackground(Color.BLUE);
		
		snakesPoints = getSnakesPoints();
		laddersPoints = getLaddersPoints();
		transparentP = new Transparent(snakesPoints, laddersPoints, snakesCounter, laddersCounter);
		transparentP.setBounds(10, 10, 600, 600);
		
		outputFrame.remove(boardP);
		outputFrame.add(transparentP);
		outputFrame.add(boardP);
		
		outputFrame.repaint();
		outputFrame.revalidate();
		
		repaint();
		revalidate();
	}
	
	public ArrayList<Point2D> getSnakesPoints() {
		ArrayList<Point2D> points = new ArrayList<Point2D>();
		
		Iterator it = snakesHM.entrySet().iterator();
		
		while(it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			int head = (int) pair.getKey();
			int tail = (int) pair.getValue();
			
			Cell c = labelCellHM.get(head);
			Rectangle rec = c.getBounds();
			Point p = c.getLocation();
			int newX = p.x + (int)Math.ceil(rec.width/2.0);
			int newY = p.y + (int)Math.ceil(rec.height/2.0);
			points.add(new Point2D.Double(newX, newY));
			
			
			c = labelCellHM.get(tail);
			rec = c.getBounds();
			p = c.getLocation();
			newX = p.x + (int)Math.ceil(rec.width/2.0);
			newY = p.y + (int)Math.ceil(rec.height/2.0);
			points.add(new Point2D.Double(newX, newY));
			
			it.remove();
		}
		
		return points;
	}
	
	public ArrayList<Point2D> getLaddersPoints() {
		ArrayList<Point2D> points = new ArrayList<Point2D>();
		
		Iterator it = laddersHM.entrySet().iterator();
		
		while(it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			int bottom = (int) pair.getKey();
			int top = (int) pair.getValue();
			
			Cell c = labelCellHM.get(bottom);
			Rectangle rec = c.getBounds();
			Point p = c.getLocation();
			int newX = p.x + (int)Math.ceil(rec.width/2.0);
			int newY = p.y + (int)Math.ceil(rec.height/2.0);
			points.add(new Point2D.Double(newX, newY));
			
			
			c = labelCellHM.get(top);
			rec = c.getBounds();
			p = c.getLocation();
			newX = p.x + (int)Math.ceil(rec.width/2.0);
			newY = p.y + (int)Math.ceil(rec.height/2.0);
			points.add(new Point2D.Double(newX, newY));
			
			it.remove();
		}
		
		return points;
	}
		
	public static void main(String[] args) {
		new Generator("Snakes and Ladders Game Generator");
	}
}
