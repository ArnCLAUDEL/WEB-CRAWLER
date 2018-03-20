package protocol;

import util.Creator;
import util.SerializerBuffer;

public class Decline extends Message {
	public static final Creator<Decline> CREATOR = Decline::new;
	
	private Request request;
	
	private Decline() {
		super(Flag.DECLINE);
	}
	
	public Decline(Request request) {
		this();
		this.request = request;
	}
	
	@Override
	public void writeToBuff(SerializerBuffer ms) {
		request.writeToBuff(ms);
	}

	@Override
	public void readFromBuff(SerializerBuffer ms) {
		this.request = Request.CREATOR.init();
		this.request.readFromBuff(ms);
	}

	@Override
	public String toString() {
		return "Decline";
	}
}
