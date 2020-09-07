package XMPPProject;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;


@SuppressWarnings("serial")
public class ley extends JFrame{

	public static JFrame frame;
//	JPanel panel;
//	JTextField u_name,field1,field2,field3;
	//JPasswordField pass;
	JButton connect;
	public static XMPPConnection connection;
	Collection<RosterEntry> list;
	public static ArrayList<String> buttons;

	public ley()
	{
		set_layout_login_page();
	}


	void set_layout_login_page(){

		frame = new JFrame("LOGIN/REGISTER");

		final JPasswordField pass;
		final JTextField u_name;
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(350,250);

		JPanel panel1 = new JPanel();
		final JPanel main = new JPanel();
		JPanel panel2 = new JPanel();
		JButton login = new JButton();
		
		Container pane = frame.getContentPane();

		pane.setLayout(new BorderLayout(5,5));
		main.setLayout(new BoxLayout(main,BoxLayout.Y_AXIS));
		panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));
		panel2.setLayout(new BoxLayout(panel2, BoxLayout.X_AXIS));

		panel1.add( new JLabel("     USERNAME    "));
		u_name = new JTextField("",15);
		panel1.add(u_name);
		panel1.add( new JLabel("      "));

		main.add(new JLabel("   "));
		main.add(panel1);
		main.add(new JLabel("   "));


		panel2.add(new JLabel("     PASSWORD   "));
		pass = new JPasswordField("",15);
		
		panel2.add(pass);
		panel2.add( new JLabel("      "));
		main.add(panel2);

		main.add(new JLabel("   "));

		login = new JButton("Login");
		JButton register = new JButton("Register");

		login.addActionListener(new ActionListener(){
			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent event) {
				
				try {
					String uname =  u_name.getText();
					String pas =  pass.getText();
					main.setVisible(false);
					frame.removeAll();
					frame.setTitle("BUDDY LIST");
					new retrieve_roster(uname,pas);
				} catch (XMPPException ex) {
					Logger.getLogger(ley.class.getName()).log(Level.SEVERE, null, ex);
					JOptionPane.showMessageDialog(null, "You Enter Wrong Information");
				}		
			}
					
		});
		
		register.addActionListener(new ActionListener(){

			@SuppressWarnings("static-access")
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ChatClient cl = new ChatClient();
				try {
					cl.register();
				} catch (Exception e) {
					e.printStackTrace();
				}	
			}
		});
		
		main.add(login);
		pane.add(main,BorderLayout.NORTH);	
		main.add(register);
		
		frame.setVisible(true);
	}

	
	public static void main(String[] args){

		new ley();

	}


	
//Below this ...two classes are file transfer related........Not implemented properly....

	public class receive_file implements Runnable{

		public receive_file(){

			new Thread(this).start();
		}
		@Override
		public void run() {
			FileTransferManager manager = new FileTransferManager(connection);
			manager.addFileTransferListener(new FileTransferListener() {
				public void fileTransferRequest(FileTransferRequest request) {
					try {
						int ret = JOptionPane.showConfirmDialog(null, request.getRequestor() + "sending a file named" + request.getFileName());
						System.out.println("returned value" + ret);
						if(ret == 0){
							System.out.println("returned value" + ret);
							IncomingFileTransfer transfer = request.accept();
							transfer.recieveFile(new File("received_file"));
							System.out.println("File Received complete");}
					} catch (XMPPException e) {
						e.printStackTrace();
					}
				}
			});	
		}
	}

	


	public class sendfileto implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {

			String file_path = JOptionPane.showInputDialog("enter the path of file");

			String receiver = e.getActionCommand();
			System.out.println("in sendfileto" + receiver);
			FileTransferManager mgr = new FileTransferManager(connection);
			OutgoingFileTransfer transfer = mgr.createOutgoingFileTransfer(receiver);
			File file = new File(file_path);
			try {
				transfer.sendFile(file,"file");

				while(!transfer.isDone())
				{
					System.out.println(transfer.getProgress() + " is done!");
				}
				System.out.println("loop broken"+transfer.getProgress() + " is done!");
			} catch (Exception ex) {
				Logger.getLogger(ley.class.getName()).log(Level.SEVERE, null, ex);
			}

		}
	}

}

