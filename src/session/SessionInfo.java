package session;

import certification.Certificate;
import io.MySerializable;
import util.Creator;
import util.SerializerBuffer;

public class SessionInfo implements MySerializable {
	public final static Creator<SessionInfo> CREATOR = SessionInfo::new;
	
	private long id;
	private Certificate certificate;
	
	private SessionInfo() {}

	public SessionInfo(long id, Certificate certificate) {
		super();
		this.id = id;
		this.certificate = certificate;
	}

	public long getId() {
		return id;
	}

	public Certificate getCertificate() {
		return certificate;
	}
	
	public SessionIdentifier getIdentifier() {
		return new SessionIdentifier(id, certificate.getIdentifier());
	}
	
	@Override
	public void writeToBuff(SerializerBuffer ms) {
		ms.putLong(id);
		certificate.writeToBuff(ms);
	}
	
	@Override
	public void readFromBuff(SerializerBuffer ms) {
		id = ms.getLong();
		certificate = Certificate.CREATOR.init();
		certificate.readFromBuff(ms);
	}
	
}
