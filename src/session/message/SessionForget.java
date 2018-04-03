package session.message;

import protocol.Flag;
import session.SessionIdentifier;
import util.Creator;
import util.SerializerBuffer;

public class SessionForget extends AbstractSessionMessage {
	public final static Creator<SessionForget> CREATOR = SessionForget::new;
	
	private SessionIdentifier sessionIdentifier;
	
	private SessionForget(long id) {
		super(Flag.SESSION_FORGET, id);
	}
	
	private SessionForget() {
		this(0);
	}
	
	public SessionForget(long id, SessionIdentifier sessionIdentifier) {
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
