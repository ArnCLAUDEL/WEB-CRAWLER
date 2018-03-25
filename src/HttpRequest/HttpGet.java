package HttpRequest;

import io.SerializerBuffer;

public class HttpGet extends HttpRequest{

	private final String hostname;
	private final String relativLink;
	
	public HttpGet(String hostname,String relativLink) {
		// TODO Auto-generated constructor stub
		this.hostname=hostname;
		this.relativLink=relativLink;
	}

	@Override
	public void writeToBuff(SerializerBuffer ms) {
		// TODO Auto-generated method stub
		
		
		String getRequest = "GET "+relativLink+" HTTP/1.1\r\n" + 
						"Host: "+hostname+"\r\n" + 
						"Connection: Keep-Alive\r\n\r\n";
		
		ms.getBuffer().put(getRequest.getBytes());
	}

	@Override
	public void readFromBuff(SerializerBuffer ms) {
	
	}

}