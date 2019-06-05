import java.security.SecureRandom;

public class Encryptor {
	private String key;
	private String ciphertext;
	private String ciphertextASCII;
	
	public Encryptor() {}
	
	public Encryptor(String message, int base) {
		encrypt(message, base);
	}
	
	public void encrypt(String message, int base) {
		if(base == 2) {
			//binary message
			//Random random = ThreadLocalRandom.current();
			SecureRandom random = new SecureRandom();
			byte[] r = new byte[message.length()/8];
			random.nextBytes(r);
			this.key = new String(r);
			
			byte[] bytes = this.key.getBytes();
			StringBuilder binary = new StringBuilder();
			
			for(byte b : bytes) {
				int val = b;
				for(int i = 0; i < 8; i++) {
					binary.append((val & 128) == 0 ? 0 : 1);
					val <<= 1;
				}
			}
			
			this.key = binary.toString();
			this.ciphertext = "";
			
			for(int i = 0; i < message.length(); i++) {
				int p = Integer.parseInt(Character.toString(message.charAt(i)));
				int k = Integer.parseInt(Character.toString(this.key.charAt(i)));
				int c = (p + k) % 2;
				this.ciphertext += c;
			}
			
		} else if(base == 8) {
			//ascii message
		}
	}

	public String getKey() {
		return key;
	}
	
	public String getKeyHex() {
		String str = "";
		int dec;
		
		for(int i = 0;  i <= this.key.length() - 4; i += 4) {
			dec = Integer.parseInt(this.key.substring(i, i + 4), 2);
			str += Integer.toHexString(dec);
		}
		
		return str;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getCiphertext() {
		return ciphertext;
	}
	
	public String getCiphertextHex() {
		String str = "";
		int dec;
		
		for(int i = 0;  i <= this.ciphertext.length() - 4; i += 4) {
			dec = Integer.parseInt(this.ciphertext.substring(i, i + 4), 2);
			str += Integer.toHexString(dec);
		}
		
		return str;
	}

	public void setCiphertext(String ciphertext) {
		this.ciphertext = ciphertext;
	}

	public String getCiphertextASCII() {
		return ciphertextASCII;
	}

	public void setCiphertextASCII(String ciphertextASCII) {
		this.ciphertextASCII = ciphertextASCII;
	}
}
