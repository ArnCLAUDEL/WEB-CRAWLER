package session.message;

import certification.CertificateIdentifier;
import protocol.Flag;
import util.Creator;
import util.SerializerBuffer;

public class SessionInit extends AbstractSessionMessage {
	public final static Creator<SessionInit> CREATOR = SessionInit::new;
	
	private String name;
	private int nbTaskMax;
	private int nbProcessUnits;
	private CertificateIdentifier certificateIdentifier;
	
	private SessionInit(long id) {
		super(Flag.SESSION_INIT, id);
	}
	
	private SessionInit() {
		this(0);
	}
	
	public SessionInit(long id, String name, int nbTaskMax, int nbProccessUnits, CertificateIdentifier certificateIdentifier) {
		this(id);
		this.name = name;
		this.nbTaskMax = nbTaskMax;
		this.nbProcessUnits = nbProccessUnits;
		this.certificateIdentifier = certificateIdentifier;
	}
	
	public String getName() {
		return name;
	}
	
	public CertificateIdentifier getCertificateIdentifier() {
		return certificateIdentifier;
	}
		
	public int getNbTaskMax() {
		return nbTaskMax;
	}

	public int getNbProcessUnits() {
		return nbProcessUnits;
	}

	@Override
	public void writeToBuff(SerializerBuffer ms) {
		ms.putLong(id);
		ms.putString(name);
		ms.putInt(nbTaskMax);
		ms.putInt(nbProcessUnits);
		certificateIdentifier.writeToBuff(ms);
	}
	
	@Override
	public void readFromBuff(SerializerBuffer ms) {
		id = ms.getLong();
		name = ms.getString();
		nbTaskMax = ms.getInt();
		nbProcessUnits = ms.getInt();
		certificateIdentifier = CertificateIdentifier.CREATOR.init();
		certificateIdentifier.readFromBuff(ms);
	}
}
