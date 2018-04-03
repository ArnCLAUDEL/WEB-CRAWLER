package service.message;

import protocol.Flag;
import protocol.Message;
import util.Creator;
import util.SerializerBuffer;

public class Forget extends Message {
	public static final Creator<Forget> CREATOR = Forget::new;

	public Forget() {
		super(Flag.FORGET);
	}
	
	@Override
	public void writeToBuff(SerializerBuffer ms) {
		
	}

	@Override
	public void readFromBuff(SerializerBuffer ms) {
		
	}

	@Override
	public String toString() {
		return "Forget";
	}
}
