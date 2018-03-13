package protocol;

import io.Creator;
import io.SerializerBuffer;

public class StopService extends Message {
	public static final Creator<StopService> CREATOR = StopService::new;
	
	@Override
	public void writeToBuff(SerializerBuffer ms) {
		ms.write(Flag.STOP_SERVICE);
	}

	@Override
	public void readFromBuff(SerializerBuffer ms) {
		// TODO Auto-generated method stub
		
	}

}
