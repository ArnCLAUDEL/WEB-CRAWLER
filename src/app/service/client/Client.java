package app.service.client;

import certification.client.CertificationClient;
import io.IOEntity;
import service.client.ServiceClient;
import session.client.SessionClient;

public interface Client extends ServiceClient, SessionClient, CertificationClient, IOEntity{

}
