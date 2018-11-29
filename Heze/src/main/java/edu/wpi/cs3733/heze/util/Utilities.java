package edu.wpi.cs3733.heze.util;

import java.util.UUID;

public class Utilities {
	
	/**
	 * 
	 * @param len The length of the String to be generated
	 * @return A pseudorandom String of length len (maximum 32 characters)
	 */
	public static String generateKey(int len)
	{
		String uniqueID = UUID.randomUUID().toString().replace("-", "");
		
		if (len >= 0 && len < 32 && uniqueID.length() == 32)
		{
			return uniqueID.substring(0, uniqueID.length() - (32 - len));
		}
		
		return uniqueID;
	}
}
