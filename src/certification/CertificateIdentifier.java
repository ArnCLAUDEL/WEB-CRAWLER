package certification;

import io.MySerializable;
import util.Creator;
import util.SerializerBuffer;

public class CertificateIdentifier implements MySerializable, Comparable<CertificateIdentifier> {
	public final static Creator<CertificateIdentifier> CREATOR = CertificateIdentifier::new;
	
	private long id;
	private byte type;
	
	private CertificateIdentifier() {}
	
	public CertificateIdentifier(long id, byte type) {
		this.id = id;
		this.type = type;
	}
	
	public long getId() {
		return id;
	}

	public byte getType() {
		return type;
	}

	@Override
	public int hashCode() {
		return (int) id;
	}
	
	@Override
	public int compareTo(CertificateIdentifier other) {
		return (int) (id - other.id);
	}
	
	@Override
	public void writeToBuff(SerializerBuffer ms) {
		ms.putLong(id);
		ms.put(type);
	}

	@Override
	public void readFromBuff(SerializerBuffer ms) {
		id = ms.getLong();
		type = ms.get();
	}
	
}
