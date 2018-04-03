package session;

import certification.CertificateIdentifier;
import io.MySerializable;
import util.Creator;
import util.SerializerBuffer;

public class SessionIdentifier implements MySerializable, Comparable<SessionIdentifier> {
	public final static Creator<SessionIdentifier> CREATOR = SessionIdentifier::new;
	
	private long id;
	private CertificateIdentifier certificateIdentifier;
	
	private SessionIdentifier() {}

	public SessionIdentifier(long id, CertificateIdentifier identifier) {
		super();
		this.id = id;
		this.certificateIdentifier = identifier;
	}

	public long getId() {
		return id;
	}
	
	public CertificateIdentifier getIdentifier() {
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
	
	@Override
	public int hashCode() {
		return (int) id;
	}
	
	@Override
	public int compareTo(SessionIdentifier other) {
		return (int) (id - other.id);
	}
}
