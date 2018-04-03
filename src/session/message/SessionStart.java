package session.message;

import certification.CertificateIdentifier;
import protocol.Flag;
import util.Creator;
import util.SerializerBuffer;

public class SessionStart extends AbstractSessionMessage {
	public final static Creator<SessionStart> CREATOR = SessionStart::new;
	
	private CertificateIdentifier certificateIdentifier;
	
	private SessionStart(long id) {
		super(Flag.SESSION_START, id);
	}
	
	private SessionStart() {
		this(0);
	}
	
	public SessionStart(long id, CertificateIdentifier certificateIdentifier) {
		this(id);
		this.certificateIdentifier = certificateIdentifier;
	}
	
	public CertificateIdentifier getCertificateIdentifier() {
		return certificateIdentifier;
	}
	
	@Override
	public void writeToBuff(SerializerBuffer ms) {
		ms.putLong(id);
		certificateIdentifier.writeToBuff(ms);
	}
	
	@Override
	public void readFromBuff(SerializerBuffer ms) {
		id = ms.getLong();
		certificateIdentifier = CertificateIdentifier.CREATOR.init();
		certificateIdentifier.readFromBuff(ms);
	}
}
