package protocol;

import io.Creator;
import io.SerializerBuffer;

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

}
