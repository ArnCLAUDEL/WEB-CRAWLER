package protocol;

public final class Flag {
	
	public static final byte INIT = 1;
	public static final byte START_SERVICE = 2;
	public static final byte STOP_SERVICE = 3;
	public static final byte REQUEST = 4;
	public static final byte REPLY = 5;
	public static final byte OK = 6;
	public static final byte FORGET = 7;
	public static final byte DECLINE = 8;
	
	private Flag() {}
}
