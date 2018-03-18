package protocol;

import io.Creator;
import io.SerializerBuffer;

public class Forget extends Message {
	public static final Creator<Forget> CREATOR = Forget::new;

	public Forget() {
		super(Flag.FORGET);
	}
	
	@Override
	public void writeToBuff(SerializerBuffer ms) {
		
	}

	@Override
	public void readFromBuff(SerializerBuffer ms) {
		
	}

	@Override
	public String toString() {
		return "Forget";
	}
}
