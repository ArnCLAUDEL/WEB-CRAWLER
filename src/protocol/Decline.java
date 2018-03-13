package protocol;

import io.Creator;
import io.SerializerBuffer;

public class Decline extends Message {
	public static final Creator<Decline> CREATOR = Decline::new;
	
	@Override
	public void writeToBuff(SerializerBuffer ms) {
		ms.write(Flag.DECLINE);
	}

	@Override
	public void readFromBuff(SerializerBuffer ms) {
		// TODO Auto-generated method stub
		
	}

}
