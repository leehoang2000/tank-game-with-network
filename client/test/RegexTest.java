package client.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {

	public static void main (String[] args) throws IOException
	{
		/*int server_port = 55000;
		String server_ip = "localhost";
		
		String sentence = "4~6-300-300";
		System.out.println("Sentence sent: " + sentence);

		DatagramSocket clientSocket = new DatagramSocket();
		InetAddress IPAddress = InetAddress.getByName(server_ip);
		byte[] sendData = new byte[1024];
		byte[] receiveData = new byte[1024];
		sendData = sentence.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, server_port);
		clientSocket.send(sendPacket);
		clientSocket.close();*/
		
		String text1 = "1-2-3-4";
		ArrayList<Integer> ids = new ArrayList<Integer>();
		while(text1.length()>0) {
			if(text1.contains("-")) {
				ids.add(Integer.valueOf(text1.substring(0, text1.indexOf('-'))));
				text1 = text1.substring(text1.indexOf('-')+1,text1.length());
			} else {
				ids.add(Integer.valueOf(text1));
				break;
			}	
		}
	}
	
}
