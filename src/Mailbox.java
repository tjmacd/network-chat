import java.util.HashMap;
import java.util.LinkedList;


public class Mailbox {
	private HashMap<String, LinkedList<String>> boxes;
	
	public Mailbox(){
		boxes = new HashMap<String, LinkedList<String>>();
	}
	
	public synchronized void send(String name, String message){
		if(boxes.containsKey(name)){
			boxes.get(name).add(message);
		} else{
			LinkedList<String> newList = new LinkedList<String>();
			newList.add(message);
			boxes.put(name, newList);
		}
	}
	
	public synchronized LinkedList<String> fetch(String name){
		return boxes.remove(name);
	}
}
