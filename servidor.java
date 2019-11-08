import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class servidor {

	public static void main(String[] args){
		
		try {
			DatagramSocket serverSocket = new DatagramSocket(8888);
			DatagramPacket receivePacket;
			
			byte[] receiveData = new byte[1024];
			byte[] sendData;
			int port1 = -1, port2 = -1;
			
			while(true) {
				receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);
				if(port1 == -1) {
					port1 = receivePacket.getPort();
					System.out.println("Hello");
				}else if(port2 == -1) {
					port2 = receivePacket.getPort();
					sendData = (Integer.toString(port1)).getBytes();
					DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("localhost"), port2);
					serverSocket.send(sendPacket);
					sendData = (Integer.toString(port2)).getBytes();
					sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("localhost"), port1);
					serverSocket.send(sendPacket);
					System.out.println("World");
					System.out.println(port1);
					System.out.println(port2);
				}
			}
		} catch (IOException e) {
			System.out.println("Problema");
		}
	}
}
