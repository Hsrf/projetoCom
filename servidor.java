import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class servidor {

	public static void main(String[] args){
		//Porta usada pelo servidor
		int serverPort = 9999;

		try {
			DatagramSocket serverSocket = new DatagramSocket(serverPort);
			DatagramPacket receivePacket;
			
			byte[] receiveData = new byte[1024];
			byte[] sendData;

			//Porta e enderecoIP do cliente1 e do cliente2
			int port1 = -1, port2 = -1;
			InetAddress ip1, ip2;
			
			while(true) {
				receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);
				if(port1 == -1) {
					//Seta os valores para a porta e enderecoIP do cliente1
					port1 = receivePacket.getPort();
					ip1 = receivePacket.getAddress();
					System.out.println("Hello");
				}else if(port2 == -1) {
					//Seta os valores para a porta e enderecoIP do cliente2
					port2 = receivePacket.getPort();
					ip2 = receivePacket.getAddress();

					//Envia ao cliente2 as informacoes do cliente1(WIP)
					sendData = (Integer.toString(port1)).getBytes();
					DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("localhost"), port2);
					serverSocket.send(sendPacket);

					//Envia ao cliente1 as informacoes do cliente2(WIP)
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
