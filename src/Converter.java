import java.math.BigInteger;

public class Converter {
	private static char symbols[] = new char[] { '0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t' };
	
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
