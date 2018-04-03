package service.message;

import protocol.Flag;
import protocol.Message;
import util.Creator;
import util.SerializerBuffer;

public class Abort extends Message {
	public static final Creator<Abort> CREATOR = Abort::new;
	
	private Request request;
	
	private Abort() {
		super(Flag.ABORT);
	}
	
	public Abort(Request request) {
		this();
		this.request = request;
	}
	
	@Override
	public void writeToBuff(SerializerBuffer ms) {
		request.writeToBuff(ms);
	}

	@Override
	public void readFromBuff(SerializerBuffer ms) {
		request = Request.CREATOR.init();
		request.readFromBuff(ms);
	}

	@Override
	public String toString() {
		return "Abort";
	}
}
