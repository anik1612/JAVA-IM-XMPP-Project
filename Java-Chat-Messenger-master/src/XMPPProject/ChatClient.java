package XMPPProject;

import java.util.Collection;

import javax.swing.JOptionPane;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

public class ChatClient {
	static Collection<RosterEntry> entries;

	public static void login(String userName, String password) throws XMPPException {

		ConnectionConfiguration config = new ConnectionConfiguration("127.0.0.1", 5222);
		ley.connection = new XMPPConnection(config);

		ley.connection.connect();
		ley.connection.login(userName, password);
	}

	public static void register() throws Exception {
		ConnectionConfiguration config = new ConnectionConfiguration("127.0.0.1", 5222);
		ley.connection = new XMPPConnection(config);
		try {
			ley.connection.connect();
			String name = JOptionPane.showInputDialog("Enter Your Username: ");
			String password = JOptionPane.showInputDialog("Enter Your Password: ");
			String passcon = JOptionPane.showInputDialog("Confirm Your Password: ");
			// String Name = JOptionPane.showInputDialog("Enter Your Full Name: ");
			if (password.equals(passcon)) {
				try {
					AccountManager manager = ley.connection.getAccountManager();

//                    Map<String, String> map = new HashMap<String, String>();
//                    map.put(Name, Name);
					manager.createAccount(name, password);

					JOptionPane.showMessageDialog(null, "Account Created");
					int input = JOptionPane.showConfirmDialog(null, "Do you want to LOGIN now ?");
					if (input == 0) {
						ley l = new ley();
						l.setVisible(true);
					} else if (input == 1) {
						JOptionPane.showMessageDialog(null, "Thanks For Registration, See You Soon");
					} else if (input == 2) {
						System.exit(0);
					}
				} catch (XMPPException e) {
					e.printStackTrace();
				}
			} else {
				JOptionPane.showMessageDialog(null, "Your Password Doesn't Match, Please Try Again");
			}

		} catch (Exception e) {
			throw e;
		}
	}

	public static void displayBuddyList() {
		Roster roster = ley.connection.getRoster();
		entries = roster.getEntries();
		@SuppressWarnings("unused")
		String buddy = null;

		for (RosterEntry r : entries) {
			if (roster.getPresence(r.getUser()).isAvailable()) {
				System.out.print(roster.getPresence(r.getUser()).getFrom());
				buddy = r.toString();
			}
		}
	}

	public void disconnect() {
		ley.connection.disconnect();
	}

	public static void setstatus(String stas) {

		Presence presence = new Presence(Presence.Type.available);

		presence.setStatus(stas);
		String temp = presence.getStatus();

		System.out.println(temp);
		presence.setPriority(0);

		presence.setMode(Presence.Mode.available);

		// Send the presence packet through the connection

		String service = ley.connection.getServiceName();

		System.out.print(service);

		ley.connection.sendPacket(presence);

		// Sleep for 20 seconds
		try {
			Thread.sleep(20000);
		} catch (InterruptedException ex) {
		}
	}
}