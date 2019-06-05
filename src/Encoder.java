
public class Encoder {
	private String message;
	private String encodedMessage;
	private Huffman huffman;
	
	public Encoder() {}
	
	public Encoder(String message) {
		encode(message);
	}
	
	public void encode(String message) {
		this.message = message;
		
		this.huffman = new Huffman(this.message);
		String huffmanMessage = huffman.getHuffmanMessage();
		
//		System.out.println("Huffman Encoded Message: " + huffmanMessage);
		//System.out.println("Huffman Encoded Message Length: " + huffmanMessage.length() + " bits");
		
		String padding = "00000000".substring(0, 8-(huffmanMessage.length() % 8));
		this.encodedMessage = padding + huffmanMessage;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getEncodedMessage() {
		return encodedMessage;
	}

	public void setEncodedMessage(String encodedMessage) {
		this.encodedMessage = encodedMessage;
	}
	
	public String getEncodedMessageASCII() {
		String str = "";
		char nextChar;
		
		for(int i = 0;  i <= this.encodedMessage.length() - 8; i += 8) {
			nextChar = (char) Integer.parseInt(this.encodedMessage.substring(i, i + 8), 2);
			str += nextChar;
		}
		
		return str;
	}
	
	public String getEncodedMessageHex() {
		//return new BigInteger(this.encodedMessage, 2).toString(16);
		
		String str = "";
		int dec;
		
		for(int i = 0;  i <= this.encodedMessage.length() - 4; i += 4) {
			dec = Integer.parseInt(this.encodedMessage.substring(i, i + 4), 2);
			str += Integer.toHexString(dec);
		}
		
		return str;
	}
}
