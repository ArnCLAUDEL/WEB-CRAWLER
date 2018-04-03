package certification.message;

import certification.CertificateIdentifier;
import protocol.Flag;
import util.Creator;
import util.SerializerBuffer;

public class CertificationGet extends AbstractCertificationMessage {
	public final static Creator<CertificationGet> CREATOR = CertificationGet::new;
	
	private CertificateIdentifier identifier;
	
	private CertificationGet(long id) {
		super(Flag.CERTIFICATION_GET, id);
	}
	
	private CertificationGet() {
		this(0);
	}
	
	public CertificationGet(long id, CertificateIdentifier identifier) {
		this(id);
		if(identifier == null)
			throw new NullPointerException("Expecting a non-null CertificateIdentifier.");
		this.identifier = identifier;
	}
	
	public CertificateIdentifier getIdentifier() {
		return identifier;
	}
	
	@Override
	public void writeToBuff(SerializerBuffer ms) {
		ms.putLong(id);
		identifier.writeToBuff(ms);
	}
	
	@Override
	public void readFromBuff(SerializerBuffer ms) {
		id = ms.getLong();
		identifier = CertificateIdentifier.CREATOR.init();
		identifier.readFromBuff(ms);
	}
	
}
