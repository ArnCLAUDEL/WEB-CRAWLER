package protocol;

public final class Flag {
	
	public static final byte SERVICE_INIT = 1;
	public static final byte SERVICE_START = 32;
	public static final byte SERVICE_STOP = 3;
	public static final byte SERVICE_REQUEST = 4;
	public static final byte SERVICE_REPLY = 5;
	public static final byte SERVICE_OK = 6;
	public static final byte SERVICE_FORGET = 7;
	public static final byte SERVICE_DECLINE = 8;
	public static final byte SERIVE_ABORT = 9;
	
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
