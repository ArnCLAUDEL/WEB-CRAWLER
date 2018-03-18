package protocol;

import io.Creator;
import io.SerializerBuffer;

public class StopService extends Message {
	public static final Creator<StopService> CREATOR = StopService::new;
	
	public StopService() {
		super(Flag.STOP_SERVICE);
	}
	
	@Override
	public void writeToBuff(SerializerBuffer ms) {
		
	}

	@Override
	public void readFromBuff(SerializerBuffer ms) {
		
	}
	
	@Override
	public String toString() {
		return "Stop Service";
	}

}
