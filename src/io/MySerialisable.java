package io;

import util.SerializerBuffer;

public interface MySerialisable {
	public void writeToBuff(SerializerBuffer ms);
	public void readFromBuff(SerializerBuffer ms);
}
