package app.client;

import certification.client.CertificationClient;
import io.IOEntity;
import service.client.ClientProtocolHandler;
import session.client.SessionClient;

public interface Client extends ClientProtocolHandler, SessionClient, CertificationClient, IOEntity{

}
