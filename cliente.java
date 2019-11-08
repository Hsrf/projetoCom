import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class cliente {
	
	DatagramSocket port;
	int otherPort;
	gui graphicUI;

	DatagramPacket sendPacket;
	DatagramPacket receivePacket;

	public cliente() throws IOException {
		port = new DatagramSocket();
		otherPort = -1;
		sendPack("", 8888);
	}
	
	public void sendPack(String str, int porta) throws IOException {
		byte[] sendData = (str).getBytes();
		sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("localhost"), porta);
		port.send(sendPacket);
	}
	
	public String receivePack() throws IOException {
		byte[] receiveData = new byte[50];
		receivePacket = new DatagramPacket(receiveData, receiveData.length);
		port.receive(receivePacket);
		String new_str = new String(receivePacket.getData());
		
		if(receivePacket.getPort() == 8888) {
			new_str = new_str.substring(0, 5);
			otherPort = Integer.parseInt(new_str);
			graphicUI = new gui(this);
			return "";
		}
		return new_str;
	}
}
