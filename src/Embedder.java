import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Embedder {
	private Tile[][] tile;
	private Snake[] snake;
	private Ladder[] ladder;
	private int[] diceArray;
	
	public Embedder() {}
	
	public Embedder(String c1, String c2, int seed, int msgLength) {
		embed(c1, c2, seed, msgLength);
	}
	
	public void embed(String c1, String c2, int seed, int msgLength) {
		int n = c1.length() / 4;
		
		//position snakes
		String head = c1.substring(0, n);
		String tail = c1.substring(n, 2*n);
		String top = c1.substring(2*n, 3*n);
		String bottom = c1.substring(3*n, 4*n);
		
		SecureRandom random = new SecureRandom();
		random.setSeed(seed);
		tile = new Tile[n][n];
		tile[0][0] = new Tile();
		tile[0][0].taken = true;
		
		if(n % 2 == 0) {
			tile[0][n-1] = new Tile();
			tile[0][n-1].taken = true;
		} else {
			tile[n-1][n-1] = new Tile();
			tile[n-1][n-1].taken = true;
		}
		
		System.out.println("before positioning snakes");
		snake = new Snake[n];
		int ctr1 = 1, ctr2 = 1;
		
		int numSnakes = 9;
		List<Integer> range = IntStream.range(1, n).boxed().collect(Collectors.toList());
		
		Random r = new Random();
		while(range.size() > numSnakes) {
			range.remove(r.nextInt(range.size()));
		}
		
//		for(int i = 0; i < head.length(); i++) {
		for(int i : range) {
			int x1 = i;
			int c = Integer.parseInt(Character.toString(head.charAt(i)), n);
			int r1 = random.nextInt(n);
			int y1 = (c + r1) % n;
			
			while(tile[x1][y1] != null) {
				r1 = random.nextInt(n);
				ctr1++;
				y1 = (c + r1) % n;
			}
			
			tile[x1][y1] = new Tile();
			tile[x1][y1].taken = true;
			
			int x2 = random.nextInt(n);
			c = Integer.parseInt(Character.toString(tail.charAt(i)), n);
			int r2 = random.nextInt(n);
			int y2 = (c + r2) % n;
			
			while(true) {
				
				if(tile[x2][y2] == null) {
					if(y2 < y1)
						break;
					else if((y1 % 2 == 0 && y1 == y2 && x2 < x1) || (y1 % 2 != 0 && y1 == y2 && x2 > x1))
						break;
				}
				
				x2 = random.nextInt(n);
				r2 = random.nextInt(n);
				ctr2++;
				y2 = (c + r2) % n;
			}
			
			tile[x2][y2] = new Tile();
			tile[x2][y2].taken = true;
			
			snake[i] = new Snake(x1, y1, x2, y2, ctr1, ctr2);
			
			ctr1 = 1;
			ctr2 = 1;
		}
		System.out.println("before positioning ladders");
		
		ladder = new Ladder[n];
		
		int numLadders = 9;
		range = IntStream.range(1, n).boxed().collect(Collectors.toList());
		
		r = new Random();
		while(range.size() > numSnakes) {
			range.remove(r.nextInt(range.size()));
		}
		
//		for(int i = 0; i < top.length(); i++) {
		for(int i : range) {
			int y1 = i;
			int c = Integer.parseInt(Character.toString(top.charAt(i)), n);
			int r1 = random.nextInt(n);
			int x1 = (c + r1) % n;
			
			while(tile[x1][y1] != null) {
				System.out.println("inside 1st while");
				r1 = random.nextInt(n);
				ctr1++;
				x1 = (c + r1) % n;
			}
			
			tile[x1][y1] = new Tile();
			tile[x1][y1].taken = true;
			
			int y2 = random.nextInt(n);
			c = Integer.parseInt(Character.toString(bottom.charAt(i)), n);
			int r2 = random.nextInt(n);
			int x2 = (c + r2) % n;
			
			while(true) {
				System.out.println("inside 2nd while");
				if(tile[x2][y2] == null) {
					if(y2 < y1)
						break;
					else if((y1 % 2 == 0 && y1 == y2 && x2 < x1) || (y1 % 2 != 0 && y1 == y2 && x2 > x1))
						break;
				}
				
				y2 = random.nextInt(n);
				r2 = random.nextInt(n);
				ctr2++;
				x2 = (c + r2) % n;
			}
			
			tile[x2][y2] = new Tile();
			tile[x2][y2].taken = true;
			
			ladder[i] = new Ladder(x1, y1, x2, y2, ctr1, ctr2);
			
			ctr1 = 1;
			ctr2 = 1;
		}
		
		//generate dice sequence
		String c6;
		System.out.println("before conversion embeeder");
		if(c2.length() > 0) {
			BigInteger num = Main.base10ConversionBigInt(c2, n);
			c6 = 1 + Main.convert(num, BigInteger.valueOf(6)) + 1;
		} else {
			int m = msgLength;
			c6 = 0 + Main.convert(m, 6) + 1;
		}
		System.out.println("after conversion embeeder");
		diceArray = new int[c6.length()];
		for(int i = 0; i < c6.length(); i++) {
			diceArray[i] = Integer.parseInt(Character.toString(c6.charAt(i))) + 1;
		}
	}
	
	public Tile[][] getTile() {
		return tile;
	}

	public void setTile(Tile[][] tile) {
		this.tile = tile;
	}

	public int[] getDiceArray() {
		return diceArray;
	}

	public void setDiceArray(int[] diceArray) {
		this.diceArray = diceArray;
	}

	public Snake[] getSnake() {
		return snake;
	}

	public void setSnake(Snake[] snake) {
		this.snake = snake;
	}

	public Ladder[] getLadder() {
		return ladder;
	}

	public void setLadder(Ladder[] ladder) {
		this.ladder = ladder;
	}
}
