/**
 * ServerThread class extends the java.lang.Thread class. It defines a thread
 * which is created for each connected client, and processes commands sent by
 * the client.
 *
 * @author TJ MacDougall
 */
import java.net.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.io.*;
import java.util.regex.*;

public class ServerThread extends Thread {
	private Socket socket = null;
	private String name;
	private Mailbox mailbox;
	private PrintWriter toLog;
	private Date date;

	/**
	 * Constructor method
	 * @param socket  The socket that will be used
	 * @param mailbox Mailbox object for the client
	 * @param log     Output stream for the log file
	 * @param date    current date for datestamps
	 */
	public ServerThread(Socket socket, Mailbox mailbox, PrintWriter log, Date date) {
		this.socket = socket;
		this.mailbox = mailbox;
		this.toLog = log;
		this.date = date;
	}

	/**
	 * Defines the procedure run by the thread; processes commands sent from the client
	 */
	public void run() {
		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			String inputLine;
			Pattern commandPattern = Pattern.compile("([\\w]+)[\\s]*([\\w]*)[ ]*(.*)");

			while((inputLine = in.readLine()) != null){
				Matcher matcher = commandPattern.matcher(inputLine);
				if(matcher.find()){
					String command = matcher.group(1);
					String arg1 = matcher.group(2);
					String arg2 = matcher.group(3);

					if(command.equals("login")){
						this.name = arg1;
						if(name.equals("")){
							out.println("Invalid username");
							break;
						} else {
							printToLog(name + " logged in");
							out.println("Login successful");
						}
					}
					else if(command.equals("send")){
						mailbox.send(arg1, name + ": " + arg2);
						printToLog(name + " to " + arg1 + ": "+ arg2);
						out.println("Message sent");
					}
					else if(command.equals("fetch")){
						LinkedList<String> messages = mailbox.fetch(name);
						if(messages != null){
							for(String message : messages){
								out.println(message);
							}
							// Form feed character signals the end of the message list
							out.println("\f");
						} else {
							out.println("null");
						}
					} else {
						out.println("Unknown command");
					}
				} else {
					out.println("Invalid command");
				}
			}
		}
		catch (IOException e) {
			printToLog(name + " disconnected");
		}

		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Prints a string to the log file and outputs to console
	 * @param output String to be printed to log
	 */
	private void printToLog(String output){
		synchronized(toLog){
			toLog.println(new Timestamp(date.getTime()) + " " + output);
		}
		synchronized(System.out){
			System.out.println(output);
		}
	}
}
