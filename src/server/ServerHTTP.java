package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import client.SimpleClient;
import javafx.scene.transform.Scale;

public class ServerHTTP {

    public static void main(String[] args){
        try{
            ServerSocket serverSocket = new ServerSocket(8000);

            for(;;){
                Object block = new Object();
                RequestHandler handler = new RequestHandler(block, serverSocket);
                handler.start();

                try{
                    synchronized(block){
                        System.out.println("Server thread paused...");
                        block.wait();
                        System.out.println("Server thread creating new RequestHandler...");
                    }
                }catch(InterruptedException e){
                    System.out.println("Can't be interrupted!");
                    e.printStackTrace();
                }
            }

        }catch(IOException e){
            System.out.println("IOException!");
            e.printStackTrace();
        }
    }
}

class RequestHandler extends Thread {

    Object block;
    ServerSocket serverSocket;
    BufferedReader socketReader;
    PrintWriter socketWriter;

    public RequestHandler(Object block, ServerSocket serverSocket){
        this.block = block;
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        try{
            System.out.println("Waiting for request...");
            Socket clientSocket = serverSocket.accept();
            System.out.println("Connection made.");


            socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));            
            socketWriter = new PrintWriter(clientSocket.getOutputStream(), true);

            String input=socketReader.readLine();
            String patWebSite= "webSite=.* HTTP";
            Scanner scanner = new Scanner(input);
            String site=((scanner.findInLine(patWebSite)).replace("webSite=", "")).replace(" HTTP", "");
            scanner.close();
            String [] args = {site};
            
            SimpleServer.main(args);
            
        }catch(IOException e){
            System.out.println("IOException!");
            e.printStackTrace();
        }
    }

}
