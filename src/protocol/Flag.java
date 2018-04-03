package protocol;

public final class Flag {
	public static final byte INIT = 1;
	public static final byte START_SERVICE = 32;
	public static final byte STOP_SERVICE = 3;
	public static final byte REQUEST = 4;
	public static final byte REPLY = 5;
	public static final byte OK = 6;
	public static final byte FORGET = 7;
	public static final byte DECLINE = 8;
	public static final byte ABORT = 9;
	
	public static final byte CERTIFICATION_REQUEST = 10;
	public static final byte CERTIFICATION_REPLY = 11;
	public static final byte CERTIFICATION_GET = 12;
	
	public static final byte SESSION_REQUEST = 13;
	public static final byte SESSION_REPLY = 14;
	public static final byte SESSION_INIT = 15;
	public static final byte SESSION_ACK = 16;
	public static final byte SESSION_START = 17;
	public static final byte SESSION_STOP = 18;
	public static final byte SESSION_FORGET = 19;
	
	private Flag() {}
}
