package service.message;

import protocol.Flag;
import protocol.Message;
import util.Creator;
import util.SerializerBuffer;

public class ServiceDecline extends Message {
	public static final Creator<ServiceDecline> CREATOR = ServiceDecline::new;
	
	private ServiceRequest request;
	
	private ServiceDecline() {
		super(Flag.SERVICE_DECLINE);
	}
	
	public ServiceDecline(ServiceRequest request) {
		this();
		this.request = request;
	}
	
	@Override
	public void writeToBuff(SerializerBuffer ms) {
		request.writeToBuff(ms);
	}

	@Override
	public void readFromBuff(SerializerBuffer ms) {
		this.request = ServiceRequest.CREATOR.init();
		this.request.readFromBuff(ms);
	}

	@Override
	public String toString() {
		return "Decline";
	}
}
