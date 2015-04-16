import java.net.*;
import java.util.LinkedList;
import java.io.*;
import java.util.regex.*;

public class ServerThread extends Thread {
	private Socket socket = null;
	String name;
	Mailbox mailbox;
	
	public ServerThread(Socket socket, Mailbox mailbox) {
		this.socket = socket;
		this.mailbox = mailbox;
	}
	
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
							//socket.close();
							break;
						} else {
							System.out.println(name + " logged in");
							out.println("Login successful");
						}
					} 
					else if(command.equals("send")){
						mailbox.send(arg1, name + ": " + arg2);
						System.out.println(name + " to " + arg1 + ": "+ arg2);
						out.println("Message sent");
					} 
					else if(command.equals("fetch")){
						LinkedList<String> messages = mailbox.fetch(name);
						if(messages != null){
							for(String message : messages){
								out.println(message);
							}
						} else {
							out.println("No new messages");
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
			System.out.println(name + " disconnected");
		}
		
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
