package protocol;

import io.Creator;
import io.SerializerBuffer;

public class StartService extends Message {
	public static final Creator<StartService> CREATOR = StartService::new;
	
	public StartService() {
		super(Flag.START_SERVICE);
	}
	
	@Override
	public void writeToBuff(SerializerBuffer ms) {
		
	}

	@Override
	public void readFromBuff(SerializerBuffer ms) {
		
	}
	
	@Override
	public String toString() {
		return "Start Service";
	}
}
