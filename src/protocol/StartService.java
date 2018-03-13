package protocol;

import io.Creator;
import io.SerializerBuffer;

public class StartService extends Message {
	public static final Creator<StartService> CREATOR = StartService::new;
	
	@Override
	public void writeToBuff(SerializerBuffer ms) {
		ms.write(Flag.START_SERVICE);
	}

	@Override
	public void readFromBuff(SerializerBuffer ms) {
		// TODO Auto-generated method stub
		
	}

}
