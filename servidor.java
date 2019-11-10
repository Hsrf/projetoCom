import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class servidor {

	public static byte[] concatenarBytes(byte[] bytes1, int port){
		//Transforma a forma em um array de bytes
		byte[] bytes2 = Integer.toString(port).getBytes();

		byte[] new_byte = new byte[bytes1.length + bytes2.length];
		int k = 0;

		for(int i = 0 ; i < bytes1.length ; i++) new_byte[k++] = bytes1[i];
		for(int i = 0 ; i < bytes2.length ; i++) new_byte[k++] = bytes2[i];

		return new_byte;
	}

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
			InetAddress ip1 = null, ip2 = null;
			
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

					//Envia ao cliente2 as informacoes do cliente1
					//sendData = (Integer.toString(port1)).getBytes();
					sendData = concatenarBytes(ip1.getAddress(), port1);
					DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ip2, port2);
					serverSocket.send(sendPacket);

					//Envia ao cliente1 as informacoes do cliente2
					sendData = concatenarBytes(ip2.getAddress(), port2);
					sendPacket = new DatagramPacket(sendData, sendData.length, ip1, port1);
					serverSocket.send(sendPacket);

					System.out.println("World");
				}
			}
		} catch (IOException e) {
			System.out.println("Problema");
		}
	}
}
