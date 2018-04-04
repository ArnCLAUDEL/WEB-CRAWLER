package service.message;

import protocol.Flag;
import protocol.Message;
import util.Creator;
import util.SerializerBuffer;

public class ServiceStart extends Message {
	public static final Creator<ServiceStart> CREATOR = ServiceStart::new;
	
	public ServiceStart() {
		super(Flag.SERVICE_START);
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
