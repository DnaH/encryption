package useful;


public class Krypto
{
	public static char rotate(char direction, char value, byte s)
	{
		if(direction == 'l' || direction == 'L')
		{
			//if(value >= 0)
			//{
				char a = (char) (value >>> (16-s));
				char b = (char) (value << s);
				return (char) (a | b);
			/*}
			else
			{
				//Löschen des Vorzeichenbits:
				value = (short) (( (short) (value << 1) ) >>> 1);
				short a = (short) (value >>> (16-s));
				short b = (short) (value << s);
				value = (short) (a | b);
				value += Math.pow(2, s);
				return value;
			}*/
		}
		else if(direction == 'r' || direction == 'R')
		{
			char a = (char) (value >>> s);
			char b = (char) (value << (16-s));
			return (char) (a | b);
		}
		else
		{
			return (Character) null;
		}
		
	}
	
	public static String charArrayToString(char[][] inputNumbers, int stringLength)
	{
		String output = "";
		for(int i=0; i < inputNumbers.length; i++)
		{
			for(int j=0; j < inputNumbers[0].length; j++)
			{
				output += inputNumbers[i][j];
			}
		}
		//angefügte Nullen wieder entfernen:
		output.substring(0, stringLength - 1);
		
		return output;
	}
	
	public static char[][] splitString64(String input)
	{
		char[][] characters;
		
		if(input.length() % 4 != 0)
		{
			characters = new char[input.length() / 4 + 1][4];
		}
		else
		{
			characters = new char[input.length() / 4][4];
		}
		
		for(int i=0; i < characters.length; i++)
		{
			//Durchlaufen der 4-Char-Blöcke (64 Bit)
			for(int j=0; j < characters[0].length; j++)
			{
				//inneres Array (je 4 Elemente=Zeichen)
				if(input.length() < i * 4 + j + 1)
				{
					characters[i][j] = (char) 0;
				}
				else
				{
					characters[i][j] = input.charAt(i * 4 + j);
				}
			}
		}
		
		return characters;
	}
 	
	public static char[] splitLong(long input)
	{
		char[] chars = new char[4];
		
		chars[3] = (char) (input & 0xffff);
		chars[2] = (char) ((input >>> 16) & 0xffff);
		chars[1] = (char) ((input >>> 32) & 0xffff);
		chars[0] = (char) ((input >>> 48) & 0xffff);
		
		return chars;
	}
	
	public static long mergeLong(char[] input)
	{
		long in0 = input[0];
		long in1 = input[1];
		long in2 = input[2];
		long in3 = input[3];
				
		return ( (in0 << 48) + (in1 << 32) + (in2 << 16) + in3);
	}
	
	public static byte[] longToBytes(long l) {
	    byte[] result = new byte[8];
	    for (int i = 7; i >= 0; i--) {
	        result[i] = (byte)(l & 0xFF);
	        l >>= 8;
	    }
	    return result;
	}
}
