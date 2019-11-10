import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class cliente {
	
	DatagramSocket port;
	gui graphicUI;
	//Informacoes do servidor
	int serverPort = 9999;
	InetAddress serverIP = InetAddress.getByName("localhost");
	//Informacoes do outro cliente
	int otherPort;
	InetAddress otherIP = InetAddress.getByName("localhost");

	DatagramPacket sendPacket;
	DatagramPacket receivePacket;

	public cliente() throws IOException {
		port = new DatagramSocket();
		otherPort = -1;

		//Inicializando o cliente no servidor
		sendPack("", serverIP, serverPort);
	}
	
	public void sendPack(String str, InetAddress adressIP, int porta) throws IOException {
		byte[] sendData = (str).getBytes();
		sendPacket = new DatagramPacket(sendData, sendData.length, adressIP, porta);
		port.send(sendPacket);
	}

	//Funcao para pegar a porta da mensagem que e recebida do servidor
	public int getPort(byte[] bytes){
		String str = "";
		for(int i = 4 ; i < 9 ; i++) {
			str = str + (bytes[i] - '0');
		}
		return  Integer.parseInt(str);
	}
	
	public String receivePack() throws IOException {
		byte[] receiveData = new byte[50];
		receivePacket = new DatagramPacket(receiveData, receiveData.length);
		port.receive(receivePacket);
		String new_str = new String(receivePacket.getData());

		if(receivePacket.getPort() == serverPort && serverIP.equals(receivePacket.getAddress())) {
			otherIP = InetAddress.getByAddress(Arrays.copyOfRange(receiveData, 0, 4));
			otherPort = getPort(receiveData);
			graphicUI = new gui(this);
			return "";
		}
		return new_str;
	}
}
