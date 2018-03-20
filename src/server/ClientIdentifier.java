package server;

import util.Cheat;

public class ClientIdentifier implements Comparable<ClientIdentifier> {
	
	public static final class BUILDER {
		private String name;
		private int nbTaskMax;
		private int nbProcessUnits;
		private int nbRequestsSent;
		private int nbSuccessfulRequests;
		private int nbDeclinedRequests;
		
		public BUILDER(String name) {
			this.name = name;
		}
		
		public ClientIdentifier build() {
			return new ClientIdentifier(name, nbTaskMax, nbProcessUnits, nbSuccessfulRequests, nbRequestsSent, nbDeclinedRequests);
		}
		
		public BUILDER nbTaskMax(int nbTaskMax) {
			this.nbTaskMax = nbTaskMax;
			return this;
		}
		
		public BUILDER nbProcessUnits(int nbProcessUnits) {
			this.nbProcessUnits = nbProcessUnits;
			return this;
		}
		
		public BUILDER nbDeclinedRequests(int nbDeclinedRequests) {
			this.nbDeclinedRequests = nbDeclinedRequests;
			return this;
		}
		
		public BUILDER nbSuccessfulRequests(int nbSuccessfulRequests) {
			this.nbSuccessfulRequests = nbSuccessfulRequests;
			return this;
		}
		
		public BUILDER nbRequestsSent(int nbRequestsSent) {
			this.nbRequestsSent = nbRequestsSent;
			return this;
		}
	
	}
	
	private final long id;
	private final String name;
	
	private int nbTaskMax = 1;
	private int nbProcessUnits = 1;
	private int nbSuccessfulRequests = 0;
	private int nbDeclinedRequests = 0;
	private int nbRequestsSent = 0;
	
	private ClientIdentifier(String name, int nbTaskMax, int nbProcessUnits,
			int nbSuccessfulRequests, int nbRequestsSent, int nbDeclinedRequests) {		
		this.id = Cheat.getId();
		this.name = name;
		this.nbTaskMax = nbTaskMax;
		this.nbProcessUnits = nbProcessUnits;
		this.nbSuccessfulRequests = nbSuccessfulRequests;
		this.nbDeclinedRequests = nbDeclinedRequests;
	}
	
	private ClientIdentifier(String name, long id) {
		this.id = Cheat.getId();
		this.name = name;
	}
	
	public ClientIdentifier(String name) {
		this(name, Cheat.getId());
	}
	
	public static ClientIdentifier makeUnregistered() {
		return new ClientIdentifier("Unregistered ClientIdentifier", -1);
	}

	@Override
	public int compareTo(ClientIdentifier clientId) {
		return (int) (this.id - clientId.id);
	}
	
	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getNbTaskMax() {
		return nbTaskMax;
	}

	public int getNbProcessUnits() {
		return nbProcessUnits;
	}

	public int getNbSuccessfulRequests() {
		return nbSuccessfulRequests;
	}

	public int getNbDeclinedRequests() {
		return nbDeclinedRequests;
	}

	public int getNbRequestsSent() {
		return nbRequestsSent;
	}
	
	@Override
	public String toString() {
		return "Client " + id;
	}
}
