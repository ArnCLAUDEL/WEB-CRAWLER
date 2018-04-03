package app.service;

import certification.CertificationRetriever;
import certification.client.CertificationClient;
import io.IOEntity;
import service.server.ServerProtocolHandler;
import session.SessionManager;
import session.explorer.SessionExplorer;

public interface ExplorerServer extends SessionExplorer, SessionManager, CertificationRetriever, CertificationClient, IOEntity, ServerProtocolHandler {

}
