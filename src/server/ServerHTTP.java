package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

public class ServerHTTP implements Runnable{
	private ServerSocketChannel ssc;
	private Selector selector;
	private ByteBuffer bb;
	private Server server;
	private String webSite;
	
	public ServerHTTP(int p,Server s) throws IOException{
		ssc =ServerSocketChannel.open();
		ssc.configureBlocking(false);
		ssc.bind(new InetSocketAddress(p));
		selector = Selector.open();
		bb = ByteBuffer.allocateDirect(512);
		server = s;
		ssc.register(selector, SelectionKey.OP_ACCEPT);	
	}
	
	void accept() throws IOException {
		SocketChannel sc = ssc.accept();
		if( sc == null) {
			System.out.println("Rien à accepter");
			return;
		}
		sc.configureBlocking(false);
		sc.register(selector, SelectionKey.OP_READ);
		System.out.println("accept:"+sc);
	}
	
	void repeat(SelectionKey sk) throws IOException {
		SocketChannel sc = (SocketChannel) sk.channel();
		bb.clear();
		if(sc.read(bb) == -1) {
			System.out.println("connection" + sc +" closed");
			sk.cancel();
			sc.close();
			return;
		}
		bb.flip();
		
		scanUrl(bb);
	}
	
	
	
	void scanUrl(ByteBuffer bb){
		Charset c= Charset.forName("UTF-8");
		CharBuffer cb = c.decode(bb);
        Scanner scanner = new Scanner(cb.toString());
        webSite=((scanner.findInLine("webSite=.* HTTP")).replace("webSite=", "")).replace(" HTTP", "");
        scanner.close();
        server.scan(webSite);
        bb.clear();
	}
	public String getWebSite() {
		return webSite;
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				selector.select();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for( SelectionKey sk: selector.selectedKeys() ) {
				if(sk.isAcceptable() ) {
					try {
						accept();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else if( sk.isReadable() ) {
					try {
						repeat(sk);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			selector.selectedKeys().clear();	
		}
		
	}
}
