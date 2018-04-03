package app.cass;

import certification.CertificationRetriever;
import certification.server.CertificationAuthority;
import io.IOEntity;
import session.server.SessionServer;

public interface CASS extends CertificationAuthority, SessionServer, CertificationRetriever, IOEntity {

}
