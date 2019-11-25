import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

public class servidor {

	public static byte[] concatenarBytes(byte[] bytes1, byte[] bytes2){
		byte[] new_byte = new byte[bytes1.length + bytes2.length];
		int k = 0;

		for(int i = 0 ; i < bytes1.length ; i++) new_byte[k++] = bytes1[i];
		for(int i = 0 ; i < bytes2.length ; i++) new_byte[k++] = bytes2[i];

		return new_byte;
	}

	public static byte[] intToByte(int i){
		byte[] msg = ByteBuffer.allocate(4).putInt(i).array();
		return msg;
	}

	public static void main(String[] args){
		//Porta usada pelo servidor
		int serverPort = 9999;

		try {
			DatagramSocket serverSocket = new DatagramSocket(serverPort);
			DatagramPacket receivePacket;
			
			byte[] receiveData = new byte[1024];
			byte[] sendData;

			//Informacoes dos clientes
			String id1 = null, id2 = null;
			boolean isOnline1 = false, isOnline2 = false;
            int portMsg1 = -1, portMsg2 = -1;
			int portAudio1 = -1, portAudio2 = -1;
			InetAddress ip1 = null, ip2 = null;
			
			while(true) {
				receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);

				if(ip1 == null) {

					//Seta os valores para a porta e enderecoIP do cliente1
					portMsg1 = receivePacket.getPort();
					ip1 = receivePacket.getAddress();
					isOnline1 = true;
					ByteBuffer wrapped = ByteBuffer.wrap(receivePacket.getData(), 0, 4);
					portAudio1 = wrapped.getInt();
					id1 = new String(receivePacket.getData(), 4, receivePacket.getLength()-4);
					//System.out.print(id1 + " " + portAudio1);
					System.out.println("Hello");

				}else if(ip2 == null) {

					//Seta os valores para a porta e enderecoIP do cliente2
					portMsg2 = receivePacket.getPort();
					ip2 = receivePacket.getAddress();
					isOnline2 = true;
					ByteBuffer wrapped = ByteBuffer.wrap(receivePacket.getData(), 0, 4);
					portAudio2 = wrapped.getInt();
					id2 = new String(receivePacket.getData(), 4, receivePacket.getLength()-4);
					//System.out.print(id2 + " " + portAudio2);

					//Envia ao cliente2 as informacoes do cliente1
					sendData = concatenarBytes(ip1.getAddress(), intToByte(portMsg1));
					sendData = concatenarBytes(sendData, intToByte(portAudio1));
					sendData = concatenarBytes(sendData, id1.getBytes());
					DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ip2, portMsg2);
					serverSocket.send(sendPacket);

					//Envia ao cliente1 as informacoes do cliente2
					sendData = concatenarBytes(ip2.getAddress(), intToByte(portMsg2));
					sendData = concatenarBytes(sendData, intToByte(portAudio2));
					sendData = concatenarBytes(sendData, id2.getBytes());
					sendPacket = new DatagramPacket(sendData, sendData.length, ip1, portMsg1);
					serverSocket.send(sendPacket);

					System.out.println("World");
				}
			}
		} catch (IOException e) {
			System.out.println("Problema");
		}
	}
}
