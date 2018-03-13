package protocol;

import io.Creator;
import io.SerializerBuffer;

public class Forget extends Message {
	public static final Creator<Forget> CREATOR = Forget::new;

	@Override
	public void writeToBuff(SerializerBuffer ms) {
		ms.write(Flag.FORGET);
	}

	@Override
	public void readFromBuff(SerializerBuffer ms) {
		// TODO Auto-generated method stub
		
	}

}
