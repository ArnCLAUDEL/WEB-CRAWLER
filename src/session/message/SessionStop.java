package session.message;

import protocol.Flag;
import session.SessionIdentifier;
import util.Creator;
import util.SerializerBuffer;

public class SessionStop extends AbstractSessionMessage {
	public final static Creator<SessionStop> CREATOR = SessionStop::new;
	
	private SessionIdentifier sessionIdentifier;
	
	private SessionStop(long id) {
		super(Flag.SESSION_STOP, id);
	}
	
	private SessionStop() {
		this(0);
	}
	
	public SessionStop(long id, SessionIdentifier sessionIdentifier) {
		this(id);
		this.sessionIdentifier = sessionIdentifier;
	}
	
	public SessionIdentifier getSessionIdentifier() {
		return sessionIdentifier;
	}
	
	@Override
	public void writeToBuff(SerializerBuffer ms) {
		ms.putLong(id);
		sessionIdentifier.writeToBuff(ms);
	}
	
	@Override
	public void readFromBuff(SerializerBuffer ms) {
		id = ms.getLong();
		sessionIdentifier = SessionIdentifier.CREATOR.init();
		sessionIdentifier.readFromBuff(ms);
	}
}
