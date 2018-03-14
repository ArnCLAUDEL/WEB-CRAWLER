package protocol;

import io.Creator;
import io.SerializerBuffer;

public class Ok extends Message {
	public static final Creator<Ok> CREATOR = Ok::new;

	private long id;
	
	private Ok() {}
	
	public Ok(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}
	
	@Override
	public void writeToBuff(SerializerBuffer ms) {
		ms.write(Flag.OK);
	}

	@Override
	public void readFromBuff(SerializerBuffer ms) {
		
	}

}
