package service.message;

import protocol.Flag;
import protocol.Message;
import util.Creator;
import util.SerializerBuffer;

public class ServiceOk extends Message {
	public static final Creator<ServiceOk> CREATOR = ServiceOk::new;

	public ServiceOk() {
		super(Flag.SERVICE_OK);
	}

	@Override
	public void writeToBuff(SerializerBuffer ms) {
		
	}

	@Override
	public void readFromBuff(SerializerBuffer ms) {
		
	}

	@Override
	public String toString() {
		return "Ok";
	}
}
