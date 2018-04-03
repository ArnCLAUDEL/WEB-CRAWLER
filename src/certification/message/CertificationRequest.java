package certification.message;

import certification.CertificateRequest;
import protocol.Flag;
import util.Creator;
import util.SerializerBuffer;

public class CertificationRequest extends AbstractCertificationMessage {
	public final static Creator<CertificationRequest> CREATOR = CertificationRequest::new;
	
	private CertificateRequest certificateRequest;
	
	private CertificationRequest(long id) {
		super(Flag.CERTIFICATION_REQUEST, id);
	}
	
	private CertificationRequest() {
		this(0);
	}
	
	public CertificationRequest(long id, CertificateRequest certificateRequest) {
		this(id);
		if(certificateRequest == null)
			throw new NullPointerException("Expecting a non-null CertificateRequest.");
		this.certificateRequest = certificateRequest;
	}

	public CertificateRequest getCertificateRequest() {
		return certificateRequest;
	}

	@Override
	public void writeToBuff(SerializerBuffer ms) {
		ms.putLong(id);
		certificateRequest.writeToBuff(ms);
	}

	@Override
	public void readFromBuff(SerializerBuffer ms) {
		id = ms.getLong();
		certificateRequest = CertificateRequest.CREATOR.init();
		certificateRequest.readFromBuff(ms);
	}

}
