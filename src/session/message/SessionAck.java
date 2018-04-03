package session.message;

import protocol.Flag;
import session.SessionInfo;
import util.Creator;
import util.SerializerBuffer;

public class SessionAck extends AbstractSessionMessage {
	public final static Creator<SessionAck> CREATOR = SessionAck::new;
		
	private SessionInfo info;
	
	private SessionAck(long id) {
		super(Flag.SESSION_ACK, id);
	}
	
	public SessionAck(long id, SessionInfo info) {
		this(id);
		this.info = info;
	}
	
	private SessionAck() {
		this(0);
	}
	
	public SessionInfo getSessionInfo() {
		return info;
	}
	
	@Override
	public void writeToBuff(SerializerBuffer ms) {
		ms.putLong(id);
		info.writeToBuff(ms);
	}
	
	@Override
	public void readFromBuff(SerializerBuffer ms) {
		id = ms.getLong();
		info = SessionInfo.CREATOR.init();
		info.readFromBuff(ms);
	}
}
