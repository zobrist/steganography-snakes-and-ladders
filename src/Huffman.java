import java.util.PriorityQueue; 
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map; 
  
class HuffmanNode { 
    int data; 
    char c; 
  
    HuffmanNode left; 
    HuffmanNode right; 
} 
   
class MyComparator implements Comparator<HuffmanNode> { 
    public int compare(HuffmanNode x, HuffmanNode y) {
        return x.data - y.data; 
    } 
} 
  
public class Huffman { 
	
	private HashMap<Character, Integer> charFreqMap = new HashMap<Character, Integer>();
	private HashMap<Character, String> huffmanCodeMap = new HashMap<Character, String>();
	private String huffmanMessage;
	
	public Huffman(String message) {
		createHuffmanTree(message);
		
		huffmanMessage = "";
		for(int i = 0; i < message.length(); i++) {
			huffmanMessage = huffmanMessage + huffmanCodeMap.get(message.charAt(i));
		}
	}
	
    public void printCode(HuffmanNode root, String s) { 
        if (root.left == null && root.right == null) { 
            huffmanCodeMap.put(root.c, s);
          
            return; 
        } 
 
        printCode(root.left, s + "0"); 
        printCode(root.right, s + "1"); 
    } 
   
    public void countFreq(String message) {
    	for(int i = 0; i < message.length(); i++) {
    		char c = message.charAt(i);
    		Integer val = charFreqMap.get(c);
    		
    		if(val != null)
    			charFreqMap.put(c, new Integer(val + 1));
    		else
    			charFreqMap.put(c, 1);
		}
    }
    
    public void createHuffmanTree(String message) {
    	
    	countFreq(message);
        
        PriorityQueue<HuffmanNode> q = new PriorityQueue<HuffmanNode>(charFreqMap.size(), new MyComparator()); 
  
        for (Map.Entry<Character, Integer> entry : charFreqMap.entrySet()) { 
            HuffmanNode hn = new HuffmanNode(); 
  
            hn.c = entry.getKey(); 
            hn.data = entry.getValue(); 
  
            hn.left = null; 
            hn.right = null; 
            q.add(hn); 
        } 
  
        HuffmanNode root = null; 
  
        while (q.size() > 1) { 
  
            HuffmanNode x = q.peek(); 
            q.poll(); 
  
            HuffmanNode y = q.peek(); 
            q.poll(); 
  
            HuffmanNode f = new HuffmanNode(); 
            f.data = x.data + y.data; 
            f.c = '-'; 
            f.left = x;  
            f.right = y;  
            root = f; 
  
            q.add(f); 
        } 
        
        printCode(root, ""); 
    }

	public String getHuffmanMessage() {
		return huffmanMessage;
	}

	public void setHuffmanMessage(String huffmanMessage) {
		this.huffmanMessage = huffmanMessage;
	}
} 
  
// This code is contributed by Kunwar Desh Deepak Singh 