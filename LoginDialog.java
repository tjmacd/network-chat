import java.awt.event.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.*;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

class LoginDialog extends JDialog implements ActionListener, PropertyChangeListener{
	private String username = null;
	private String hostname = null;
	private String port = null;
	
	private JTextField usernameInput;
	private JTextField hostnameInput;
	private JTextField portInput;
	
	private JOptionPane optionPane;
	private String btnString1 = "Login";
	private String btnString2 = "Cancel";
	
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	
	public LoginDialog(Frame frame, Socket socket, PrintWriter out, BufferedReader in){
		super(frame);
		this.socket = socket;
		this.out = out;
		this.in = in;
		
		setTitle("Confirm Details");
		
		usernameInput = new JTextField(10);
		hostnameInput = new JTextField(10);
		portInput = new JTextField(10);
		
		//Display text fields
		String usernameLabel = "Username:";
		String hostnameLabel = "Hostname:";
		String portLabel = "Port:";
		
		Object[] array = {usernameLabel, usernameInput, hostnameLabel, hostnameInput, portLabel, portInput};
		
		Object[] options = {btnString1, btnString2};
		
		optionPane = new JOptionPane(array, JOptionPane.PLAIN_MESSAGE, JOptionPane.YES_NO_OPTION, null, options, options[0]);
		
		setContentPane(optionPane);
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				optionPane.setValue(new Integer(JOptionPane.CLOSED_OPTION));
			}
		});
		
		addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent ce){
				usernameInput.requestFocusInWindow();
			}
		});
		
		optionPane.addPropertyChangeListener(this);
	}
	
	public String getLoginName(){
		return username;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		optionPane.setValue(btnString1);
	}



	@Override
	public void propertyChange(PropertyChangeEvent event) {
		String prop = event.getPropertyName();
		
		if(isVisible() && (event.getSource() == optionPane) && (JOptionPane.VALUE_PROPERTY.equals(prop) 
				|| JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))) {
			Object value = optionPane.getValue();
			if (value == JOptionPane.UNINITIALIZED_VALUE){
				return;
			}
			
			optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
			
			if(value.equals(btnString1)){
				username = usernameInput.getText();
				hostname = hostnameInput.getText();
				port = portInput.getText();
				
				if(!username.matches("[\\w]+")){
					JOptionPane.showMessageDialog(this, "Username is not valid. \nValid characters are letters, numerals and underscores.",
							"Invalid Username", JOptionPane.ERROR_MESSAGE);
					usernameInput.requestFocusInWindow();
				} else if (hostname.equals("")){
					JOptionPane.showMessageDialog(this, "Please enter the hostname of the server.",
							"Invalid Hostname", JOptionPane.ERROR_MESSAGE);
					hostnameInput.requestFocusInWindow();
				} else if (!port.matches("[\\d]+")){
					JOptionPane.showMessageDialog(this, "Please enter the port number of the server.",
							"Invalid Port", JOptionPane.ERROR_MESSAGE);
					portInput.requestFocusInWindow();
				}
				else {
					String fromServer = "";
					try{
						socket = new Socket(hostname, Integer.parseInt(port));
						out = new PrintWriter(socket.getOutputStream(), true);
						in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						
						out.println("login " + username);
						fromServer = in.readLine();
						if(fromServer.equals("Login successful")){
							clearAndHide();
						} else {
							JOptionPane.showMessageDialog(this, "Unable to log into server",
									"Could not connect", JOptionPane.ERROR_MESSAGE);
						}
					} catch(UnknownHostException e){
						//System.err.println("Host " + hostname + " cannot be found.");
						JOptionPane.showMessageDialog(this, "Cannot connect to host " + hostname + ".",
								"Could not connect", JOptionPane.ERROR_MESSAGE);
					} catch(IOException e){
						//System.err.println("Could not get I/O for the connection to " + hostname);
						JOptionPane.showMessageDialog(this, "Could not get I/O for the connection to " + hostname + ".");
					}
					
					
				}
			} else {
				clearAndHide();
			}
		}
		
		
	}
	
	public void clearAndHide() {
		usernameInput.setText(null);
		hostnameInput.setText(null);
		portInput.setText(null);
		setVisible(false);
	}
	
}
