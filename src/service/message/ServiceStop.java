package service.message;

import protocol.Flag;
import protocol.Message;
import util.Creator;
import util.SerializerBuffer;

public class ServiceStop extends Message {
	public static final Creator<ServiceStop> CREATOR = ServiceStop::new;
	
	public ServiceStop() {
		super(Flag.SERVICE_STOP);
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
