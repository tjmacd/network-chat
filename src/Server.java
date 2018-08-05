/**
 * This class runs a server for sending and recieving messages
 *
 * @author TJ MacDougall
 */

import java.io.*;
import java.net.*;
import java.sql.Timestamp;
import java.util.Date;

public class Server {

	public static void main(String[] args) {
		if (args.length != 1){
			System.err.println("Usage: java Server <port>");
			System.exit(1);
		}

		int port = Integer.parseInt(args[0]);
		Mailbox mailbox = new Mailbox();
		Date date = new Date();

		try(PrintWriter log = new PrintWriter(new BufferedWriter(
				new FileWriter("server.log")), true);
				ServerSocket serverSocket = new ServerSocket(port)){
			System.out.println("Server hosted on port " + port);
			log.println(new Timestamp(date.getTime()) + " Server hosted on port " + port);

			while(true){
				new ServerThread(serverSocket.accept(), mailbox, log, date).start();
			}

		} catch(IOException e){
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
