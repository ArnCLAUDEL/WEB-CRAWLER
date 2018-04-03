package protocol;

import io.MySerializable;

public abstract class Message implements MySerializable {
	protected final static byte OK = 1;
	protected final static byte ERROR = 2;
	
	private final byte flag;
	
	public Message(byte flag) {
		this.flag = flag;
	}
	
	public byte getFlag() {
		return flag;
	}
}
