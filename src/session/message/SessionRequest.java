package session.message;

import certification.CertificateIdentifier;
import protocol.Flag;
import util.Creator;
import util.SerializerBuffer;

public class SessionRequest extends AbstractSessionMessage {
	public final static Creator<SessionRequest> CREATOR = SessionRequest::new;
	
	private CertificateIdentifier certificateIdentifier;
	
	private SessionRequest(long id) {
		super(Flag.SESSION_REQUEST, id);
	}
	
	private SessionRequest() {
		this(0);
	}
	
	public SessionRequest(long id, CertificateIdentifier certificateIdentifier) {
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
