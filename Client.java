import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.awt.*;

import javax.swing.*;

/**
 *
 * @author 100493250
 */
public class Client extends JFrame {

	private JButton fetchButton;
    private JTextArea inputBox;
    private JLabel jLabel1;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private JTextPane outputBox;
    private JButton sendButton;
    private JTextField sendToBox;
	
    public Client(String title) {
    	super(title);
        initComponents();
    }

    private void initComponents() {

        jScrollPane1 = new JScrollPane();
        outputBox = new JTextPane();
        jLabel1 = new JLabel();
        sendToBox = new JTextField();
        jScrollPane2 = new JScrollPane();
        inputBox = new JTextArea();
        sendButton = new JButton();
        fetchButton = new JButton();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        outputBox.setEditable(false);
        jScrollPane1.setViewportView(outputBox);

        jLabel1.setText("Send to:");

        inputBox.setColumns(20);
        inputBox.setRows(5);
        jScrollPane2.setViewportView(inputBox);

        sendButton.setText("Send");

        fetchButton.setText("Fetch");

        GroupLayout layout = new GroupLayout(getContentPane());
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
                            .addComponent(sendButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(sendButton)
                        .addGap(18, 18, 18)
                        .addComponent(fetchButton)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        pack();
    }
    
    public static void main(String[] args){
    	EventQueue.invokeLater(new Runnable() {
    		Socket socket = null;
			PrintWriter out = null;
			BufferedReader in = null;
			Client app = new Client("Chat Window");
			LoginDialog confirmDetails = new LoginDialog(app, socket, out, in);
    		public void run() {
    			

    			app.setVisible(true);
    			confirmDetails.setLocationRelativeTo(app);
    			confirmDetails.pack();
    			confirmDetails.setVisible(true);


    			while(confirmDetails.isVisible()){
    				//System.err.println(System.nanoTime());
    			}
    			String username = confirmDetails.getLoginName();

    			//System.out.println(username);
    			app.outputBox.setText("Logged in as " + username);

    			if(true){
    				System.out.println("IAMSTUPUD");
    			}
    		}
    	});
    	
    }
}
