package com.example.twitterclient.util;


public class Constants {
    public static final int httpCallTimeoutMilliSeconds = 300;
	public static final int tweetsQueryLimit = 10;
	
	public static enum FragmentType {
		
		HOME(1), MENTIONS(2), USER(3);
		
		private int typeCode;
		 
		private FragmentType(int tc) {
			typeCode = tc;
		}

		public int getTypeCode() {
			return typeCode;
		}
	}
}
