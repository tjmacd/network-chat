import java.io.*;
import java.net.Socket;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

/**
 * A Java Swing interface for the chat client
 *
 * @author TJ MacDougall
 */
public class Client extends JFrame {
	private File configFile = new File("login.cfg");

	private JButton fetchButton;
    private JTextArea inputBox;
    private JLabel jLabel1;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private JLabel label;
    private JTextPane outputBox;
    private JButton sendButton;
    private JTextField sendToBox;

    protected PrintWriter out;
    protected BufferedReader in;
    protected Socket socket;

    private StyledDocument doc;
    private String username;

	/**
	 * Constructor
	 * @param title        Title displayed in window
	 * @param socket       The socket being used by the client
	 * @param outputStream The text stream to send commands to the server
	 * @param inputStream  The stream to recieve messages from the server
	 */
    public Client(String title, Socket socket, PrintWriter outputStream, BufferedReader inputStream) {
    	super(title);
    	this.socket = socket;
    	this.out = outputStream;
    	this.in = inputStream;
        initComponents();
        login();
    }

	/**
	 * Initializes all the Swing components
	 */
    private void initComponents() {

        jScrollPane1 = new JScrollPane();
        outputBox = new JTextPane();
        jLabel1 = new JLabel();
        sendToBox = new JTextField();
        jScrollPane2 = new JScrollPane();
        inputBox = new JTextArea();
        sendButton = new JButton();
        fetchButton = new JButton();
        label = new JLabel(" ");

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        outputBox.setEditable(false);
        jScrollPane1.setViewportView(outputBox);
        doc = (StyledDocument) outputBox.getDocument();
        //TODO: word wrap
        //TODO: Text formatting
        //TODO: Enter sends message
        //TODO: autoscroll

        jLabel1.setText("Send to:");

        inputBox.setColumns(20);
        inputBox.setRows(5);
        jScrollPane2.setViewportView(inputBox);

        sendButton.setText("Send");

        fetchButton.setText("Fetch");

        //Generated using NetBeans
        GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(sendToBox))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fetchButton, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
                            .addComponent(sendButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(label, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(sendToBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(sendButton)
                        .addGap(18, 18, 18)
                        .addComponent(fetchButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(label))
        );

        inputBox.setLineWrap(true);
        inputBox.setWrapStyleWord(true);

		// Event listener for 'send' button
        sendButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendMessage();

			}
        });

		// Event listener for 'fetch' button
        fetchButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		fetchMessage();
        	}
        });

        pack();
        setVisible(true);
    }

	/**
	 * Sets the socket to be used by this client
	 * @param socket Socket object to be used by the client
	 */
    public void setSocket(Socket socket){
    	this.socket = socket;
    }

	/**
	 * Prints text to StyledDocument doc which is displayed in outputBox
	 * @param text String text to be displayed in outputBox
	 */
    public void print(String text){
    	try {
			doc.insertString(doc.getLength(), "\n" + text, null);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}

    }

	/**
	 * Displays a LoginDialog to log into the server
	 */
    public void login(){
    	LoginDialog confirmDetails = new LoginDialog(this, configFile);
    	confirmDetails.setVisible(true);
    	this.username = confirmDetails.getLoginName();
    	if(username != null){
    		outputBox.setText("Logged in as " + username);
    	} else {
    		System.exit(0);
    	}
    }

	/**
	 * Sends a the message contained in inputBox to the server as a string
	 * in the format: 'send [recipient] [message]'
	 */
    private void sendMessage(){
    	String destination = sendToBox.getText();
		String message = inputBox.getText();
		if(destination.isEmpty()){
			label.setText("You must enter a message recipient.");
			sendToBox.requestFocusInWindow();
		} else if (message.isEmpty()){
			label.setText("Please enter a message.");
			inputBox.requestFocusInWindow();
		} else {
			out.println("send " + destination + " " + message);
			inputBox.setText("");
			try {
				label.setText(in.readLine());
				print("To " + destination + ": " + message);
			} catch (IOException e1) {
				label.setText(e1.getMessage());
				e1.printStackTrace();
			}
		}
    }

	/**
	 * Recieves a String of multiple messages from the server intended for the
	 * current username.
	 */
    private void fetchMessage() {
    	out.println("fetch");
    	try{
    		String fromServer = in.readLine();
    		if(fromServer.equals("null")){
    			label.setText("No new messages.");
    		} else {
    			print(fromServer);
    			int count = 1;
				// Server sends Form Feed char '\f' to signal the end of the message list
    			while(!((fromServer = in.readLine()).charAt(0) == '\f')){
    				print(fromServer);
    				count++;
    			}
    			label.setText(count + " messages retrieved");
    		}
    	} catch(IOException ex){
    		ex.printStackTrace();
    	}
    }

    public static void main(String[] args){
    	SwingUtilities.invokeLater(new Runnable() {
    		public void run() {
    			Socket socket = null;
    	    	PrintWriter out = null;
    	    	BufferedReader in = null;
    	    	Client app = new Client("Chat Window", socket, out, in);
    		}
    	});
    }
}
