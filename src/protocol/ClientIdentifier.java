package protocol;

import java.nio.channels.SocketChannel;

public class ClientIdentifier implements Comparable<ClientIdentifier> {
	
	public static final class BUILDER {
		private String name;
		private SocketChannel channel;
		private int nbTaskMax;
		private int nbProcessUnits;
		private int nbRequestsSent;
		private int nbSuccessfulRequests;
		private int nbDeclinedRequests;
		
		public BUILDER(String name, SocketChannel channel) {
			if(channel == null)
				throw new IllegalArgumentException("Excepting a non-null SocketChannel");
			
			this.name = name;
			this.channel = channel;
			channel.hashCode();
		}
		
		public ClientIdentifier build() {
			return new ClientIdentifier(name, channel, nbTaskMax, nbProcessUnits, nbSuccessfulRequests, nbRequestsSent, nbDeclinedRequests);
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
	
	private SocketChannel channel;
	private int nbTaskMax = 1;
	private int nbProcessUnits = 1;
	private int nbSuccessfulRequests = 0;
	private int nbDeclinedRequests = 0;
	private int nbRequestsSent = 0;
	
	private ClientIdentifier(String name, SocketChannel channel, int nbTaskMax, int nbProcessUnits,
			int nbSuccessfulRequests, int nbRequestsSent, int nbDeclinedRequests) {		
		this.id = channel.hashCode();
		this.name = name;
		this.channel = channel;
		this.nbTaskMax = nbTaskMax;
		this.nbProcessUnits = nbProcessUnits;
		this.nbSuccessfulRequests = nbSuccessfulRequests;
		this.nbDeclinedRequests = nbDeclinedRequests;
	}
	
	public ClientIdentifier(String name, SocketChannel channel) {
		if(channel == null)
			throw new IllegalArgumentException("Excepting a non-null SocketChannel");
		
		this.id = channel.hashCode();
		this.name = name;
		this.channel = channel;
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

	public SocketChannel getChannel() {
		return channel;
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
	
}
