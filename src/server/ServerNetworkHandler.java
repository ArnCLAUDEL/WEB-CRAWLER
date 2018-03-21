package server;

public interface ServerNetworkHandler extends NetworkWriter {
	void update(ClientIdentifier oldClientId, ClientIdentifier newClientId);
}
