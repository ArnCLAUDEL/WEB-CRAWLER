package protocol;

import io.Creator;
import io.SerializerBuffer;

public class Reply extends Message {
	public static final Creator<Reply> CREATOR = Reply::new;
	
	public Reply() {
		super(Flag.REPLY);
	}
	
	@Override
	public void writeToBuff(SerializerBuffer ms) {
	}

	@Override
	public void readFromBuff(SerializerBuffer ms) {
		
	}

}
