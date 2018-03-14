package protocol;

import io.Creator;
import io.SerializerBuffer;

public class Abort extends Message {
	public static final Creator<Abort> CREATOR = Abort::new;
	
	public Abort() {
		super(Flag.ABORT);
	}
	
	@Override
	public void writeToBuff(SerializerBuffer ms) {
		
	}

	@Override
	public void readFromBuff(SerializerBuffer ms) {

	}

}
