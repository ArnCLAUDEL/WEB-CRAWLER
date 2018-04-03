package session.message;

import java.util.Optional;

import certification.Certificate;
import protocol.Flag;
import util.Creator;
import util.SerializerBuffer;

public class SessionReply extends AbstractSessionMessage {
	public final static Creator<SessionReply> CREATOR = SessionReply::new;

	private Optional<Certificate> certificate;
	private Optional<String> errorMessage;
	
	private SessionReply(long id) {
		super(Flag.SESSION_REPLY, id);
	}
	
	private SessionReply() {
		this(0);
	}
	
	public SessionReply(long id, Certificate certificate) {
		this(id);
		this.certificate = Optional.of(certificate);
		this.errorMessage = Optional.empty();
	}
	
	public SessionReply(long id, String errorMessage) {
		this(id);
		this.certificate = Optional.empty();
		this.errorMessage = Optional.of(errorMessage);
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
