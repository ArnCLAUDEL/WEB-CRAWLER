package protocol;

import util.Creator;
import util.SerializerBuffer;

public class Ok extends Message {
	public static final Creator<Ok> CREATOR = Ok::new;

	private long id;
	
	private Ok() {
		super(Flag.OK);
	}
	
	public Ok(long id) {
		this();
		this.id = id;
	}

	public long getId() {
		return id;
	}
	
	@Override
	public void writeToBuff(SerializerBuffer ms) {
		
	}

	@Override
	public void readFromBuff(SerializerBuffer ms) {
		
	}

	@Override
	public String toString() {
		return "Ok";
	}
}
