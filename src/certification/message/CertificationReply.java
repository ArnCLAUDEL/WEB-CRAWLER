package certification.message;

import java.util.Optional;

import certification.Certificate;
import protocol.Flag;
import util.Creator;
import util.SerializerBuffer;

public class CertificationReply extends AbstractCertificationMessage {
	public final static Creator<CertificationReply> CREATOR = CertificationReply::new;
		
	private Optional<Certificate> certificate;
	private Optional<String> errorMessage;
	
	private CertificationReply(long id) {
		super(Flag.CERTIFICATION_REPLY, id);
	}
	
	private CertificationReply() {
		this(0);
	}

	public CertificationReply(long id, Certificate certificate) {
		this(id);
		this.certificate = Optional.of(certificate);
		this.errorMessage = Optional.empty();
	}
	
	public CertificationReply(long id, String errorMessage) {
		this(id);
		this.certificate = Optional.empty();
		this.errorMessage = Optional.ofNullable(errorMessage);
	}

	public Optional<Certificate> getCertificate() {
		return certificate;
	}
	
	public Optional<String> getErrorMessage() {
		return errorMessage;
	}

	@Override
	public void writeToBuff(SerializerBuffer ms) {
		ms.putLong(id);
		if(certificate.isPresent()) {
			ms.put(OK);
			certificate.get().writeToBuff(ms);
		} else {
			ms.put(ERROR);
			ms.putString(errorMessage.get());
		}
	}

	@Override
	public void readFromBuff(SerializerBuffer ms) {
		id = ms.getLong();
		byte flag = ms.get();
		if(flag == OK) {
			Certificate certificate = Certificate.CREATOR.init();
			certificate.readFromBuff(ms);
			this.certificate = Optional.of(certificate);
			this.errorMessage = Optional.empty();
		} else {
			this.certificate = Optional.empty();
			this.errorMessage = Optional.of(ms.getString());
		}
	}

}
