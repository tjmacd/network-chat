import java.io.*;
import java.net.*;

public class Server {
	
	public static void main(String[] args) {
		if (args.length != 1){
			System.err.println("Usage: java Server <port>");
			System.exit(1);
		}
		
		int port = Integer.parseInt(args[0]);
		Mailbox mailbox = new Mailbox();
		
		try(ServerSocket serverSocket = new ServerSocket(port)){
			System.out.println("Server hosted on port " + port);

			while(true){
				new ServerThread(serverSocket.accept(), mailbox).start();
			}
			
		} catch(IOException e){
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}
}