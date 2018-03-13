package protocol;

import io.Creator;
import io.SerializerBuffer;

public class Request extends Message {
	public static final Creator<Request> CREATOR = Request::new;
	
	@Override
	public void writeToBuff(SerializerBuffer ms) {
		ms.write(Flag.REQUEST); 
		
	}

	@Override
	public void readFromBuff(SerializerBuffer ms) {
		// TODO 
		
	}

}
