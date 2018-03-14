package protocol;

import io.Creator;
import io.SerializerBuffer;

public class Abort extends Message {
	public static final Creator<Abort> CREATOR = Abort::new;
	
	@Override
	public void writeToBuff(SerializerBuffer ms) {
		ms.write(Flag.ABORT);
	}

	@Override
	public void readFromBuff(SerializerBuffer ms) {
		// TODO 
	}

}
