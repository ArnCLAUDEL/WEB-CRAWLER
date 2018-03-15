package protocol;

import io.Creator;
import io.SerializerBuffer;

public class Init extends Message {
	public static final Creator<Init> CREATOR = Init::new;
	
	private long previousId;
	private String name;
	private int nbTaskMax;
	private int nbProcessUnits;
	
	private Init() {
		super(Flag.INIT);
	}
	
	public Init(String name, int nbTaskMax, int nbProcessUnits, long previousId) {
		this();
		this.name = name;
		this.nbTaskMax = nbTaskMax;
		this.nbProcessUnits = nbProcessUnits;
		this.previousId = previousId;
	}
	
	public Init(String name, int nbTaskMax, int nbProcessUnits) {
		this(name, nbTaskMax, nbProcessUnits, 0);
	}	
	
	public long getPreviousId() {
		return previousId;
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
		ms.writeString(name);
		ms.putInt(nbTaskMax);
		ms.putInt(nbProcessUnits);
	}

	@Override
	public void readFromBuff(SerializerBuffer ms) {
		this.name = ms.getString();
		this.nbTaskMax = ms.getInt();
		this.nbProcessUnits = ms.getInt();
	}
}
