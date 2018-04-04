package app.service.server;

import java.net.InetSocketAddress;
import java.util.NoSuchElementException;

import certification.CertificationRetriever;
import certification.client.CertificationClient;
import io.IOEntity;
import service.server.ServiceServer;
import session.SessionInfo;
import session.SessionManager;
import session.explorer.SessionExplorer;

public interface ExplorerServer extends SessionExplorer, SessionManager, CertificationRetriever, CertificationClient, IOEntity, ServiceServer {
	SessionInfo getSession(InetSocketAddress address) throws NoSuchElementException;
}
