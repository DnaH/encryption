package RC2;

import useful.Krypto;

public class RC2
{
	//globale Variablen:
	private static byte[] sValues = {1, 2, 3, 5};
	private static byte[] sValuesR = {5, 3, 2, 1};
	private static int keyPos;
	
	
	//Verschlüsslungsmethoden:

	public static String encrypt (String input, String key)
	{
		//Besetzen des char-Arrays:
		char[][] inChars = Krypto.splitString64(input);
		
		//Schlüsselaufbereitung:
		char[] keys = UsableKeys.get(key);
		
		//Array für verschlüsselte Werte:
		char[][] outChars= new char[inChars.length][4];
		for(int i=0; i < outChars.length; i++)
		{
			//Verschlüsselung jedes einzelnen Blocks:
			outChars[i] = encryptBlock(inChars[i], keys);
		}
		
		return Krypto.charArrayToString(outChars, input.length());
	}
	
	public static long encryptLong(long input, String inputKey)
	{
		char[] block = Krypto.splitLong(input);
		char[] keys = UsableKeys.get(inputKey);
		
		return Krypto.mergeLong(encryptBlock(block, keys));
	}
	
	private static char[] encryptBlock(char[] input, char[] keys)
	{
		keyPos = -1;
		char[] data = input;
		
		for(int i=0; i < 16; i++)
		{
			data = mixRound(data, keys);
			
			if(i == 4 || i == 10)
			{
				data = mashRound(data, keys);
			}
		}
		
		return data;
	}
	
	private static char[] mashRound(char[] data, char[] keys)
	{
		char[] mashedStuff = {data[0], data[1], data[2], data[3]};
		
		mashedStuff[0] = (char) ((mashedStuff[0] + keys[mashedStuff[3] & 63]) /*% 65536*/);
		mashedStuff[1] = (char) ((mashedStuff[1] + keys[mashedStuff[0] & 63]) /*% 65536*/);
		mashedStuff[2] = (char) ((mashedStuff[2] + keys[mashedStuff[1] & 63]) /*% 65536*/);
		mashedStuff[3] = (char) ((mashedStuff[3] + keys[mashedStuff[2] & 63]) /*% 65536*/);
		
		return mashedStuff;
	}
	
	private static char[] mixRound(char[] data, char[] keys)
	{		
		char[] fullyMixed = {data[0], data[1], data[2], data[3]};
		for(int i=0; i<4; i++)
		{
			keyPos++;
			fullyMixed = mix(fullyMixed, keys[keyPos], sValues[i]);
		}
		
		return fullyMixed;
	}
	
	private static char[] mix(char[] data, char key, byte s)
	{
		//16-Bit-Datenströme in einzelne Variablen speichern:
		char[] mixedData = {data[0], data[1], data[2], data[3]};
		/*char data0 = data[0];
		char data1 = data[1];
		char data2 = data[2];
		char data3 = data[3];
		
		/*
		 *  R[i] = R[i] + K[j] + (R[i-1] & R[i-2]) + ((~R[i-1]) & R[i-3]);
   		R[i] = R[i] rol s[i];
		 *  */
		
		//Einbringen des Keys:
		mixedData[0] = (char) ((mixedData[0] + key) /*% 65536*/); //% 2^16
		
		//Weitere Operationen:
		mixedData[0] = (char) ((mixedData[0] +
			(char)
				(
				( (char) (mixedData[2] & mixedData[3]) + (char) (~mixedData[3] & mixedData[1]) ) /*% 65536 */)
				)
		% 65536 ); 
				
		//Linksrotation:
		mixedData[0] = Krypto.rotate('l', mixedData[0], s); 
		
		//Reihenfolge verändern:
		char[] out = {mixedData[1], mixedData[2], mixedData[3], mixedData[0]};
		/*data[0] = data1;
		data[1] = data2;
		data[2] = data3;
		data[3] = data0;*/
		
		return out;
	}

	
	//Entschlüsslungsmethoden:
	private static char[] rMix(char[] data, char key, byte s)
	{
		//Reihenfolge verändern:
		char[] mixedData = {data[3], data[0], data[1], data[2]};
		/*char data0 = data[3];
		char data1 = data[0];
		char data2 = data[1];
		char data3 = data[2];*/
		
		//Rechtsrotation:
		mixedData[0] = Krypto.rotate('r', mixedData[0], s);
		
		//Weitere Operationen:
		mixedData[0] = (char) ((mixedData[0] -
			(char)
				(
				( (char)(mixedData[2] & mixedData[3]) + (char)(~mixedData[3] & mixedData[1]) ) /*% 65536 */)
				)
			% 65536 );
		
		//Herausrechnen des Keys:
		mixedData[0] = (char) ((mixedData[0] - key) /*% 65536*/); //% 2^16
		
		//Speichern der Variablen in einem Array:
		char[] out = {mixedData[0], mixedData[1], mixedData[2], mixedData[3]};
		/*data[0] = data0;
		data[1] = data1;
		data[2] = data2;
		data[3] = data3;*/
		
		return out;
	}
	
	private static char[] rMixRound(char[] data, char[] keys)
	{		
		char[] fullyMixed = {data[0], data[1], data[2], data[3]};
		for(int i=0; i<4; i++)
		{
			fullyMixed = rMix(fullyMixed, keys[keyPos], sValuesR[i]);
			keyPos--;
		}
		/*
		for(int i=3; i >= 0; i--)
		{
			fullyMixed = rMix(fullyMixed, keys[keyPos], sValues[i]);
			keyPos--;
		}*/
		
		return fullyMixed;
	}
	
	private static char[] rMashRound(char[] data, char[] keys)
	{
		char[] mashedStuff = {data[0], data[1], data[2], data[3]};
		
		mashedStuff[3] = (char) ((mashedStuff[3] - keys[mashedStuff[2] & 63]) /*% 65536*/);
		mashedStuff[2] = (char) ((mashedStuff[2] - keys[mashedStuff[1] & 63]) /*% 65536*/);
		mashedStuff[1] = (char) ((mashedStuff[1] - keys[mashedStuff[0] & 63]) /*% 65536*/);
		mashedStuff[0] = (char) ((mashedStuff[0] - keys[mashedStuff[3] & 63]) /*% 65536*/);
		
		return mashedStuff;
	}
	
	public static char[] decryptBlock(char[] input, char[] keys)
	{
		keyPos = 63;
		char[] data = input;
		
		for(int i=0; i < 16; i++)
		{
			data = rMixRound(data, keys);
			
			if(i == 4 || i == 10)
			{
				data = rMashRound(data, keys);
			}
		}
		
		return data;
	}
	
	public static long decryptLong(long input, String inputKey)
	{
		char[] block = Krypto.splitLong(input);
		char[] keys = UsableKeys.get(inputKey);
		
		return Krypto.mergeLong(decryptBlock(block, keys));
	}
	
	public static String decrypt(String input, String key)
	{
		//Besetzen des char-Arrays:
		char[][] inChars = Krypto.splitString64(input);
		
		//Schlüsselaufbereitung:
		char[] keys = UsableKeys.get(key);
		
		//Array für entschlüsselte Werte:
		char[][] outChars= new char[inChars.length][4];
		for(int i=0; i < outChars.length; i++)
		{
			//Verschlüsselung jedes einzelnen Blocks:
			outChars[i] = decryptBlock(inChars[i], keys);
		}
		
		return Krypto.charArrayToString(outChars, input.length());
	}

	//Main:
	public static void main(String[] args)
	{
		long a = 7851678454653875325L;
		long b = encryptLong(a, "ba");
		System.out.println(b);
		System.out.println(decryptLong(b, "ba"));
	}

}
