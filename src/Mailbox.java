import java.util.HashMap;
import java.util.LinkedList;

/**
 * Object which stores a list messages for multiple users
 */
public class Mailbox {
	private HashMap<String, LinkedList<String>> boxes;

	public Mailbox(){
		boxes = new HashMap<String, LinkedList<String>>();
	}

	/**
	 * Stores a message sent to the mailbox
	 * @param name    The intended message recipient
	 * @param message Text of the message
	 */
	public synchronized void send(String name, String message){
		if(boxes.containsKey(name)){
			boxes.get(name).add(message);
		} else{
			LinkedList<String> newList = new LinkedList<String>();
			newList.add(message);
			boxes.put(name, newList);
		}
	}

	/**
	 * Returns all the messages for a given user
	 * @param  name The username to whom the messages belong
	 * @return      A list of messages
	 */
	public synchronized LinkedList<String> fetch(String name){
		return boxes.remove(name);
	}
}
