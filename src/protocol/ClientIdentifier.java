package protocol;

import util.Cheat;

public class ClientIdentifier implements Comparable<ClientIdentifier> {
	
	
	private final long id = Cheat.RANDOM.nextLong();
	
	@Override
	public int compareTo(ClientIdentifier clientId) {
		return (int) (this.id - clientId.id);
	}
	
}
