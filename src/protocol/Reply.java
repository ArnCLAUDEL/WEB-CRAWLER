package protocol;

import io.Creator;
import io.SerializerBuffer;

public class Reply extends Message {
	public static final Creator<Reply> CREATOR = Reply::new;
	
	@Override
	public void writeToBuff(SerializerBuffer ms) {
		ms.write(Flag.REPLY);
	}

	@Override
	public void readFromBuff(SerializerBuffer ms) {
		// TODO 
		
	}

}