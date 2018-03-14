package protocol;

import io.Creator;
import io.SerializerBuffer;

public class Request extends Message {
	public static final Creator<Request> CREATOR = Request::new;
	
	private String hostname;
	
	public Request(String hostname) {
		this.hostname = hostname;
	}
	
	public Request() {
		this("www.example.com");
	}
	
	@Override
	public void writeToBuff(SerializerBuffer ms) {
		ms.write(Flag.REQUEST); 
		ms.writeString(hostname);
	}

	@Override
	public void readFromBuff(SerializerBuffer ms) {
		this.hostname = ms.getString();
		
	}

}
