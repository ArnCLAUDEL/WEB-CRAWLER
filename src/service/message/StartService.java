package service.message;

import protocol.Flag;
import protocol.Message;
import util.Creator;
import util.SerializerBuffer;

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
