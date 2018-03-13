package protocol;

import io.Creator;
import io.SerializerBuffer;

public class Ok extends Message {
	public static final Creator<Ok> CREATOR = Ok::new;
	
	@Override
	public void writeToBuff(SerializerBuffer ms) {
		ms.write(Flag.OK);
	}

	@Override
	public void readFromBuff(SerializerBuffer ms) {
		
	}

}
