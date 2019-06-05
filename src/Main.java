import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Main {
	private static char symbols[] = new char[] { '0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t' };
	private HashMap<Integer, Square> labelSquare;
	private HashMap<Integer, Integer> snakesHM;
	private HashMap<Integer, Integer> laddersHM;
	
	public static void main(String[] args) {
		new Main("", 10);
	}
	
	public static void frequency() {
		int n = 12;			//8 10 12
		int seed;
		SecureRandom random;
		List<Integer> list = new ArrayList<Integer>();
		Encoder encoder;
		Encryptor encryptor;
		Embedder embedder;
		int length = 200;	//50 100 150 200
		System.out.println("Message length: " + length);
		System.out.println("Dimension: " + n);
		encoder = new Encoder(randomAlphaNumeric(length));
		encryptor = new Encryptor(encoder.getEncodedMessage(), 2);
			
		random = new SecureRandom();
		seed = random.nextInt(1000);
			 
		BigInteger cD = base10ConversionBigInt(encryptor.getCiphertext(), 2);
			
		String cN = convert(cD, BigInteger.valueOf(n));
			
		String c1, c2;
		if(cN.length() <= 4*n) {
			c1 = cN;
			char[] zeros = new char[4*n];
			Arrays.fill(zeros, '0');
				
			c1 = c1 + new String(zeros).substring(0, 4 * n - cN.length());
			c2 = "";
		} else {
			c1 = cN.substring(0, 4*n);
			c2 = cN.substring(4*n, cN.length());
		}
			
		embedder = new Embedder(c1, c2, seed, cN.length());
		Collections.addAll(list, Arrays.stream(embedder.getDiceArray()).boxed().toArray(Integer[]::new));
		
		System.out.println("Frequency 1: " + Collections.frequency(list, 1));
		System.out.println("Frequency 2: " + Collections.frequency(list, 2));
		System.out.println("Frequency 3: " + Collections.frequency(list, 3));
		System.out.println("Frequency 4: " + Collections.frequency(list, 4));
		System.out.println("Frequency 5: " + Collections.frequency(list, 5));
		System.out.println("Frequency 6: " + Collections.frequency(list, 6));
	}
	
	public Main(String message, int n) {
//		String message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi fermentum felis felis, non lacinia massa blandit sodales. Proin dictum tellus ut "
//				+ "dignissim pharetra. Sed varius purus eu ex posuere pretium. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. "
//				+ "Nam molestie ornare lacus, in dignissim magna imperdiet semper. Maecenas posuere metus eget dignissim consectetur. Suspendisse non lectus quis urna "
//				+ "ultrices porta sed cursus purus. In elementum ornare sapien, sagittis faucibus libero ullamcorper vel. Vivamus fringilla leo in laoreet mollis. "
//				+ "Maecenas bibendum enim libero, ut hendrerit ligula facilisis sed.";
		System.out.println("Original Message: " + message);
		System.out.println("Original Message length: " + message.length()*8 + " bits");
		Encoder encoder = new Encoder(message);
		
		System.out.println();
		String encodedMessage = encoder.getEncodedMessage();
		System.out.println("Encoded message (Binary): " + encodedMessage);
		System.out.println("Encoded message (Hex): " + encoder.getEncodedMessageHex());
		System.out.println("Encoded message Length: " + encodedMessage.length() + " bits");
		
		System.out.println();
		Encryptor encryptor = new Encryptor(encodedMessage, 2);
		String key = encryptor.getKey();
		System.out.println("Symmetric key (Binary): " + key);
		System.out.println("Symmetric key (Hex): " + encryptor.getKeyHex());
		System.out.println("Symmetric key length: " + key.length() + " bits");
		
		System.out.println();
		String ciphertext = encryptor.getCiphertext();
		System.out.println("Ciphertext (Binary): " + ciphertext);
		System.out.println("Ciphertext (Hex): " + encryptor.getCiphertextHex());
		System.out.println("Ciphertext length: " + ciphertext.length() + " bits");
		
		SecureRandom random = new SecureRandom();
		int seed = random.nextInt(1000);
		System.out.println("Random Seed: " + seed + "\n");
		
		System.out.println("Gameboard dimension: " + n + " x " + n + "\n");
		 
		BigInteger cD = base10ConversionBigInt(ciphertext, 2);
		System.out.println("Ciphertext (Base 10): " + cD);
		
		String cN = convert(cD, BigInteger.valueOf(n));
		System.out.println("Ciphertext (Base " + n + "): " + cN + "\n");
		System.out.println("Ciphertext (Base " + n + ") Length : " + cN.length() + "\n");
		
		String c1, c2;
		if(cN.length() <= 4*n) {
			c1 = cN;
			char[] zeros = new char[4*n];
			Arrays.fill(zeros, '0');
			
			c1 = c1 + new String(zeros).substring(0, 4 * n - cN.length());
			c2 = "";
		} else {
			c1 = cN.substring(0, 4*n);
			c2 = cN.substring(4*n, cN.length());
		}
		
		System.out.println("First partition (C1): " + c1);
		System.out.println("First partition length: " + c1.length());
		System.out.println("Second partition (C2): " + c2);
		System.out.println("Second partition length: " + c2.length() + "\n");
		
		Embedder embedder = new Embedder(c1, c2, seed, cN.length());
		System.out.println("Snakes (x, y, counter): ");
		Snake[] snakes = embedder.getSnake();
		for(int i = 0; i < snakes.length; i++) {
			System.out.println("Snake #" + (i+1) + ": " + "Head(" + snakes[i].headX + ", " + snakes[i].headY + 
					", " + snakes[i].headCtr + ") Tail(" + snakes[i].tailX + ", " + snakes[i].tailY + 
					", " + snakes[i].tailCtr + ")");
		}
		
		System.out.println();
		System.out.println("Ladders (x, y, counter): ");
		Ladder[] ladders = embedder.getLadder();
		for(int i = 0; i < ladders.length; i++) {
			System.out.println("Ladder #" + (i+1) + ": " + "Top(" + ladders[i].topX + ", " + ladders[i].topY + 
					", " + ladders[i].topCtr + ") Bottom(" + ladders[i].bottomX + ", " + ladders[i].bottomY + 
					", " + ladders[i].bottomCtr + ")");
		}
		
		System.out.println();
		System.out.println("Dice sequence: " + Arrays.toString(embedder.getDiceArray()));
		System.out.println("Dice sequence length: " + embedder.getDiceArray().length);
		
		labelSquare = new HashMap<Integer, Square>();
		Square[][] cell = new Square[n][n];
		
		int label;
		for(int i = 0; i < n; i++) {
			label = n * (n - i);
			
			if(i % 2 != 0) 
				label = label - n + 1;
			
			for(int j = 0; j < n; j++) {
				cell[i][j] = new Square();
				cell[i][j].label = label;
				labelSquare.put(label, cell[i][j]);
				
				if(i % 2 == 0)
					label -= 1;
				else
					label += 1;

			}
		}
		
		laddersHM = new HashMap<Integer, Integer>();
		snakesHM = new HashMap<Integer, Integer>();
		
		for(int i = 0; i < ladders.length; i++) {
			Ladder l = ladders[i];
			Snake s = snakes[i];
			
			laddersHM.put(cell[n-l.bottomY-1][l.bottomX].label, cell[n-l.topY-1][l.topX].label);
			snakesHM.put(cell[n-s.headY-1][s.headX].label, cell[n-s.tailY-1][s.tailX].label);
		}
		
		int numPlayers = 4;
		Game newGame = new Game(numPlayers, n*n, snakesHM, laddersHM, embedder.getDiceArray(), seed);
		newGame.start();
	}
	

	private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	public static String randomAlphaNumeric(int count) {
		StringBuilder builder = new StringBuilder();
		while (count-- != 0) {
		int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
		builder.append(ALPHA_NUMERIC_STRING.charAt(character));
		}
		return builder.toString();
	}
	
	class Square {
		public int label;
	}
	
	public static BigInteger base10ConversionBigInt(String number, int sBase) {
		return new BigInteger(number, sBase);
	}
	
	public static int base10Conversion(String number, int sBase) {
		int dec = 0;
		for(int i = 0, j = number.length() - 1; i < number.length(); i++, j--) {
			dec = (int) (dec + (Integer.parseInt(Character.toString(number.charAt(i))) * Math.pow(sBase, j)));
		}
		
		return dec;
	}
	
	public static String convert(BigInteger number, BigInteger base) {
		return convert(number, base, 0, "");
	}
	
	public static String convert(BigInteger number, BigInteger base, int position, String result) {
		if (number.compareTo(base.pow(position + 1)) == -1) {
			return symbols[(number.divide(base.pow(position))).intValue()] + result;
		} else {
			BigInteger remainder = number.mod(base.pow(position + 1));
			return convert(number.subtract(remainder), base, position + 1, symbols[(remainder.divide(base.pow(position))).intValue()] + result);
		}
	}
	
	public static String convert(int number, int base) {
        return convert(number, base, 0, "");
    }

    private static String convert(int number, int base, int position, String result) {
        if ( number < Math.pow(base, position + 1) ) {
            return symbols[(number / (int)Math.pow(base, position))] + result;
        } else {
            int remainder = (number % (int)Math.pow(base, position + 1));
            return convert (  number - remainder, base, position + 1, symbols[remainder / (int)( Math.pow(base, position))] + result );
        }
    }
}
