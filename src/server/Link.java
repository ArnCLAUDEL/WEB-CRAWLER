package server;

public class Link{
	
	private String hostname;
	private String path;
	
	public Link(String h, String p) {
		hostname=h;
		path=p;
	}
	
	public String getLink() {
		return path;
	}
	
	public String getHostname() {
		return hostname;
	}
	
	public static void main(String[] args) {
		String a = "a";
		String a1 = "a1";
		String b = "b";
		
		System.out.println(a+=b);
		System.out.println(a1.concat(b));
	}
}