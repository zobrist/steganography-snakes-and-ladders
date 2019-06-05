import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class Game {
	private int numPlayers, end, seed;
	private int[] diceSequence;
	private HashMap<Integer, Integer> snakesHM;
	private HashMap<Integer, Integer> laddersHM;
	private HashMap<Integer, Integer> winsHM;
	private ArrayList<Integer> p1 = new ArrayList<Integer>();
	private ArrayList<Integer> p2 = new ArrayList<Integer>();
	private ArrayList<Integer> p3 = new ArrayList<Integer>();
	private ArrayList<Integer> p4 = new ArrayList<Integer>();
	private ArrayList<Integer> s1 = new ArrayList<Integer>();
	private ArrayList<Integer> s2 = new ArrayList<Integer>();
	private ArrayList<Integer> s3 = new ArrayList<Integer>();
	private ArrayList<Integer> s4 = new ArrayList<Integer>();
	
	public Game(int numPlayers, int end, HashMap<Integer, Integer> snakesHM, HashMap<Integer, Integer> laddersHM, int[] diceSequence, int seed) {
		this.numPlayers = numPlayers;
		this.seed = seed;
		this.end = end;
		this.snakesHM = snakesHM;
		this.laddersHM = laddersHM;
		this.diceSequence = diceSequence;
		
		System.out.println("Number of Players: " + numPlayers);
		System.out.println("End: " + end);
		
		winsHM = new HashMap<Integer, Integer>();
		winsHM.put(1, 0);
		winsHM.put(2, 0);
		winsHM.put(3, 0);
		winsHM.put(4, 0);
	}
	
	public void start() {
		Integer[] arr = Arrays.stream(diceSequence).boxed().toArray(Integer[]::new);
		ArrayList<Integer> arrayList = new ArrayList<Integer>(Arrays.asList(arr));
	
		int[] state = new int[numPlayers];
		int games = 1;
		int i = 0;
		
		while(!arrayList.isEmpty()) {
			for(i = 0; i < numPlayers && !arrayList.isEmpty(); i++) {
				int outcome = arrayList.remove(0);
				
				do{
					state[i] = state[i] + outcome;
					
					if(i == 0) {
						p1.add(outcome);
						s1.add(state[i]);
					} else if(i == 1) {
						p2.add(outcome);
						s2.add(state[i]);
					} else if(i == 2) {
						p3.add(outcome);
						s3.add(state[i]);
					} else if(i == 3) {
						p4.add(outcome);
						s4.add(state[i]);
					}
					
					if(state[i] > end) {
						state[i] = end - (state[i] - end);
						
						if(i == 0) {
							s1.add(state[i]);
						} else if(i == 1) {
							s2.add(state[i]);
						} else if(i == 2) {
							s3.add(state[i]);
						} else if(i == 3) {
							s4.add(state[i]);
						}
						
					} else if(state[i] == end) {
						if(numPlayers >= 1) {
							System.out.println("Player 1 Dice Sequence: " + p1);
							System.out.println("Player 1 State Sequence: " + s1);
						}
						
						if(numPlayers >= 2) {
							System.out.println("Player 2 Dice Sequence: " + p2);
							System.out.println("Player 2 State Sequence: " + s2);
						}
						
						if(numPlayers >= 3) {
							System.out.println("Player 3 Dice Sequence: " + p3);
							System.out.println("Player 3 State Sequence: " + s3);
						}
						
						if(numPlayers >= 4) {
							System.out.println("Player 4 Dice Sequence: " + p4);
							System.out.println("Player 4 State Sequence: " + s4);
						}
						
						System.out.println("Player " + (i+1) + " wins \n");
						winsHM.put(i+1, winsHM.get(i+1) + 1);
						p1 = new ArrayList<Integer>();
						p2 = new ArrayList<Integer>();
						p3 = new ArrayList<Integer>();
						p4 = new ArrayList<Integer>();
						s1 = new ArrayList<Integer>();
						s2 = new ArrayList<Integer>();
						s3 = new ArrayList<Integer>();
						s4 = new ArrayList<Integer>();
						state = new int[numPlayers];
						games += 1;
						
						break;
					} else if(laddersHM.containsKey(state[i])) {
						state[i] = laddersHM.get(state[i]);
						

						if(i == 0) {
							s1.add(state[i]);
						} else if(i == 1) {
							s2.add(state[i]);
						} else if(i == 2) {
							s3.add(state[i]);
						} else if(i == 3) {
							s4.add(state[i]);
						}
					} else if(snakesHM.containsKey(state[i])) {
						state[i] = snakesHM.get(state[i]);
						

						if(i == 0) {
							s1.add(state[i]);
						} else if(i == 1) {
							s2.add(state[i]);
						} else if(i == 2) {
							s3.add(state[i]);
						} else if(i == 3) {
							s4.add(state[i]);
						}
					}
					
					if(arrayList.isEmpty() || arrayList.get(0) != 6)
						break;
					
					outcome = arrayList.remove(0);
					
				}while(outcome == 6);
					
			}
			
		}
		
		int wins = winsHM.get(1) + winsHM.get(2) + winsHM.get(3) + winsHM.get(4);
		
		if(wins != games) {
			if(i >= 4)
				i = 0;
			Random rand = new Random(seed);
			while(winsHM.get(1) + winsHM.get(2) + winsHM.get(3) + winsHM.get(4) != games) {
				if(i == 4)
					i = 0;
				for(; i < numPlayers; i++) {
					int outcome = rand.nextInt(6) + 1;
					
					do{
						state[i] = state[i] + outcome;
						
						if(i == 0) {
							p1.add(outcome);
							s1.add(state[i]);
						} else if(i == 1) {
							p2.add(outcome);
							s2.add(state[i]);
						} else if(i == 2) {
							p3.add(outcome);
							s3.add(state[i]);
						} else if(i == 3) {
							p4.add(outcome);
							s4.add(state[i]);
						}
						
						if(state[i] > end) {
							state[i] = end - (state[i] - end);
							
							if(i == 0) {
								s1.add(state[i]);
							} else if(i == 1) {
								s2.add(state[i]);
							} else if(i == 2) {
								s3.add(state[i]);
							} else if(i == 3) {
								s4.add(state[i]);
							}
							
						} else if(state[i] == end) {
							if(numPlayers >= 1) {
								System.out.println("Player 1 Dice Sequence: " + p1);
								System.out.println("Player 1 State Sequence: " + s1);
							}
							
							if(numPlayers >= 2) {
								System.out.println("Player 2 Dice Sequence: " + p2);
								System.out.println("Player 2 State Sequence: " + s2);
							}
							
							if(numPlayers >= 3) {
								System.out.println("Player 3 Dice Sequence: " + p3);
								System.out.println("Player 3 State Sequence: " + s3);
							}
							
							if(numPlayers >= 4) {
								System.out.println("Player 4 Dice Sequence: " + p4);
								System.out.println("Player 4 State Sequence: " + s4);
							}
							
							System.out.println("Player " + (i+1) + " wins \n");
							winsHM.put(i+1, winsHM.get(i+1) + 1);
							p1 = new ArrayList<Integer>();
							p2 = new ArrayList<Integer>();
							p3 = new ArrayList<Integer>();
							p4 = new ArrayList<Integer>();
							s1 = new ArrayList<Integer>();
							s2 = new ArrayList<Integer>();
							s3 = new ArrayList<Integer>();
							s4 = new ArrayList<Integer>();
							state = new int[numPlayers];
							
							break;
						} else if(laddersHM.containsKey(state[i])) {
							state[i] = laddersHM.get(state[i]);
							

							if(i == 0) {
								s1.add(state[i]);
							} else if(i == 1) {
								s2.add(state[i]);
							} else if(i == 2) {
								s3.add(state[i]);
							} else if(i == 3) {
								s4.add(state[i]);
							}
						} else if(snakesHM.containsKey(state[i])) {
							state[i] = snakesHM.get(state[i]);
							

							if(i == 0) {
								s1.add(state[i]);
							} else if(i == 1) {
								s2.add(state[i]);
							} else if(i == 2) {
								s3.add(state[i]);
							} else if(i == 3) {
								s4.add(state[i]);
							}
						}
						
						outcome = rand.nextInt(6) + 1;
						
					}while(outcome == 6);
						
				}
				
			}
		}
		
		System.out.println("Game Ends");
		
		System.out.println("Message length: 200");//50 100 150 200
		System.out.println("Dimension: " + Math.sqrt(end));
		System.out.println("Number of games: " + games);
		System.out.println("Wins 1: " + winsHM.get(1));
		System.out.println("Wins 2: " + winsHM.get(2));
		System.out.println("Wins 3: " + winsHM.get(3));
		System.out.println("Wins 4: " + winsHM.get(4));
	}
		
	public void startServer() {
		int port = 12345;
		boolean listening = true;
		
		try (ServerSocket serverSocket = new ServerSocket(port)) { 
            while (listening) {
	           
	        }
	    } catch (IOException e) {
            System.err.println("Could not listen on port " + port);
            System.exit(-1);
        }
	}
}
