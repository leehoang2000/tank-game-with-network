package client;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import org.w3c.dom.Text;

import TankGame.TankGameClient;
import client.message.ClientSideListener;
import client.message.ClientSideSender;
import client.message.PacketParser;

public class Client implements ActionListener {
	private JFrame mainMenuFrame;
	private int serverPort = 55000;
	private String serverIP = "localhost";
	private JPanel mainMenuPanel;
	private JLabel serverIPLabel;
	private JTextField serverIPText;
	private JLabel serverPortLabel;
	private JTextField serverPortText;
	private JButton connectBtn;

	public static void main(String args[]) throws Exception {
		// Initialize

		Client client = new Client();
		client.showMainMenu();

	}

	public void startClient(int server_port, String server_ip) throws IOException {
		ClientSideSender clientSideSender = new ClientSideSender(server_ip, server_port);
		TankGameClient tankGame = new TankGameClient(clientSideSender);
		tankGame.start();
		PacketParser packetParser = new PacketParser(tankGame, clientSideSender);
		ClientSideListener clientSideListener = new ClientSideListener(clientSideSender.getClientSocket(),
				packetParser);

		clientSideListener.start();
		clientSideSender.sendRequestConnectMessage();
	}

	public Client() {
		prepareGUI();
	}

	private void prepareGUI() {
		mainMenuFrame = new JFrame("Main Menu");
		mainMenuFrame.setSize(700, 100);
		mainMenuPanel = new JPanel();
		serverIPLabel = new JLabel("server IP: ");
		serverIPText = new JTextField(serverIP);
		serverIPText.setColumns(10);
		serverPortLabel = new JLabel("server port: ");
		serverPortText = new JTextField(String.valueOf(serverPort));
		connectBtn = new JButton("connect");
		connectBtn.addActionListener(this);
		
		mainMenuPanel.add(serverIPLabel);
		mainMenuPanel.add(serverIPText);
		mainMenuPanel.add(serverPortLabel);
		mainMenuPanel.add(serverPortText);
		mainMenuPanel.add(connectBtn);
		
		mainMenuPanel.setLayout(new FlowLayout());
		
		mainMenuFrame.add(mainMenuPanel);
	}

	private void showMainMenu() {
		mainMenuFrame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		if(action.equals("connect")) {
			serverPort = Integer.valueOf(serverPortText.getText());
			serverIP = serverIPText.getText();
			try {
				startClient(serverPort,serverIP);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		mainMenuFrame.setVisible(false);
	}

}