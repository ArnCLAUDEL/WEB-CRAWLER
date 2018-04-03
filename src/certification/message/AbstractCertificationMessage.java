package certification.message;

import protocol.Message;
import protocol.message.TrackedMessage;

public abstract class AbstractCertificationMessage extends Message implements TrackedMessage {

	protected long id;
	
	public AbstractCertificationMessage(byte flag, long id) {
		super(flag);
		this.id = id;
	}
	
	@Override
	public long getId() {
		return id;
	}

}
