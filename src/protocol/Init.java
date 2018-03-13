package protocol;

import io.Creator;
import io.SerializerBuffer;

public class Init extends Message {
	public static final Creator<Init> CREATOR = Init::new;
	
	private String name;
	private int nbTaskMax;
	private int nbProcessUnits;
	
	private Init() {}
	
	public Init(String name, int nbTaskMax, int nbProcessUnits) {		
		this.name = name;
		this.nbTaskMax = nbTaskMax;
		this.nbProcessUnits = nbProcessUnits;
	}	
	
	public String getName() {
		return name;
	}

	public int getNbTaskMax() {
		return nbTaskMax;
	}

	public int getNbProcessUnits() {
		return nbProcessUnits;
	}

	@Override
	public void writeToBuff(SerializerBuffer ms) {
		ms.write(Flag.INIT);
		ms.writeString(name);
		ms.writeInt(nbTaskMax);
		ms.writeInt(nbProcessUnits);
	}

	@Override
	public void readFromBuff(SerializerBuffer ms) {
		this.name = ms.getString();
		this.nbTaskMax = ms.getInt();
		this.nbProcessUnits = ms.getInt();
	}
}
