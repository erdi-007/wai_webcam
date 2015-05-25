package crypto;

import java.math.BigInteger;
import java.security.*;

public class MD5 {
	
	private final static String salt = "97856173E618D82";
	
	public static String create(String data)
	{
		data = salt + data;
		
		try 
		{
			MessageDigest hashAlgorithm = MessageDigest.getInstance("MD5");
			byte[] digest = hashAlgorithm.digest(data.getBytes());
			BigInteger bigInt = new BigInteger(1, digest);
			String hashedData = bigInt.toString(16);
			return hashedData;
		} 
		catch (NoSuchAlgorithmException e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}

	public static Boolean validate(String raw, String hashed) 
	{
		String data = MD5.create(raw);
		return data.equals(hashed);
	}
}
