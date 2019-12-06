import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

public class servidor {

	//Informacoes dos clientes
	public String id1, id2;
	public boolean isOnline1, isOnline2;
	public int portMsg1 = -1, portMsg2 = -1;
	public int portAudio1 = -1, portAudio2 = -1;
	public InetAddress ip1 = null, ip2 = null;

	public servidor(){
		//Informacoes dos clientes
		this.id1 = "Cliente 1"; this.id2 = "Cliente 2";
		this.isOnline1 = false; this.isOnline2 = false;
		this.portMsg1 = -1; this.portMsg2 = -1;
		this.portAudio1 = -1; this.portAudio2 = -1;
		this.ip1 = null; this.ip2 = null;
	}

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

	public static byte[] boolToByte(boolean b){
		byte[] bytes = new byte[]{(byte) (b ? 1:0)};
		return bytes;
	}

	public static void main(String[] args){
		//Porta usada pelo servidor
		int serverPort = 9999;
		servidor server = new servidor();

		//Abrindo a interface do servidor
		serverUI sUI = new serverUI(server);
		sUI.updateStatus();

		try {
			DatagramSocket serverSocket = new DatagramSocket(serverPort);
			DatagramPacket receivePacket;
			
			byte[] receiveData = new byte[1024];
			byte[] sendData;
			
			while(true) {
				receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);
				DatagramPacket sendPacket;

				if(server.ip1 == null) {

					//Seta os valores para a porta e enderecoIP do cliente1
					server.portMsg1 = receivePacket.getPort();
					server.ip1 = receivePacket.getAddress();
					server.isOnline1 = true;
					ByteBuffer wrapped = ByteBuffer.wrap(receivePacket.getData(), 0, 4);
					server.portAudio1 = wrapped.getInt();
					server.id1 = new String(receivePacket.getData(), 4, receivePacket.getLength()-4);
					//System.out.print(id1 + " " + portAudio1);
					System.out.println("Hello");

				}else if(server.ip1.equals(receivePacket.getAddress()) && server.portMsg1 == receivePacket.getPort()){
					//Envia uma mensagem ao cliente 2 sobre o novo estado do cliente 1
					server.isOnline1 = !server.isOnline1;
					sendData = boolToByte(server.isOnline1);
					sendPacket = new DatagramPacket(sendData, sendData.length, server.ip2, server.portMsg2);
					serverSocket.send(sendPacket);

				}else if(server.ip2 == null) {

					//Seta os valores para a porta e enderecoIP do cliente2
					server.portMsg2 = receivePacket.getPort();
					server.ip2 = receivePacket.getAddress();
					server.isOnline2 = true;
					ByteBuffer wrapped = ByteBuffer.wrap(receivePacket.getData(), 0, 4);
					server.portAudio2 = wrapped.getInt();
					server.id2 = new String(receivePacket.getData(), 4, receivePacket.getLength()-4);
					//System.out.print(id2 + " " + portAudio2);

					//Envia ao cliente2 as informacoes do cliente1
					sendData = concatenarBytes(server.ip1.getAddress(), intToByte(server.portMsg1));
					sendData = concatenarBytes(sendData, intToByte(server.portAudio1));
					sendData = concatenarBytes(sendData, server.id1.getBytes());
					sendPacket = new DatagramPacket(sendData, sendData.length, server.ip2, server.portMsg2);
					serverSocket.send(sendPacket);

					//Envia ao cliente1 as informacoes do cliente2
					sendData = concatenarBytes(server.ip2.getAddress(), intToByte(server.portMsg2));
					sendData = concatenarBytes(sendData, intToByte(server.portAudio2));
					sendData = concatenarBytes(sendData, server.id2.getBytes());
					sendPacket = new DatagramPacket(sendData, sendData.length, server.ip1, server.portMsg1);
					serverSocket.send(sendPacket);

					System.out.println("World");
				}else if(server.ip2.equals(receivePacket.getAddress()) && server.portMsg2 == receivePacket.getPort()){
					//Envia uma mensagem ao cliente 1 sobre o novo estado do cliente 2
					server.isOnline2 = !server.isOnline2;
					sendData = boolToByte(server.isOnline2);
					sendPacket = new DatagramPacket(sendData, sendData.length, server.ip1, server.portMsg1);
					serverSocket.send(sendPacket);
				}

				sUI.updateStatus();
			}
		} catch (IOException e) {
			System.out.println("Problema");
		}
	}
}
