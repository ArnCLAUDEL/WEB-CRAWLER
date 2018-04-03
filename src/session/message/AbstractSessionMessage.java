package session.message;

import protocol.Message;
import protocol.TrackedMessage;

public abstract class AbstractSessionMessage extends Message implements TrackedMessage {

	protected long id;
	
	protected AbstractSessionMessage(byte flag, long id) {
		super(flag);
		this.id = id;
	}
	
	@Override
	public long getId() {
		return id;
	}
}
