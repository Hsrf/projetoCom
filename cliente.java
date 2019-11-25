import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class cliente {

    public static byte[] concatenarBytes(byte[] bytes1, byte[] bytes2){

        byte[] new_byte = new byte[bytes1.length + bytes2.length];
        int k = 0;

        for(int i = 0 ; i < bytes1.length ; i++) new_byte[k++] = bytes1[i];
        for(int i = 0 ; i < bytes2.length ; i++) new_byte[k++] = bytes2[i];

        return new_byte;
    }

	//Informacoes do servidor
	int serverPort = 9999;
	InetAddress serverIP = InetAddress.getByName("localhost");
    //Informacoes desse cliente
    DatagramSocket portThisMsg = new DatagramSocket();
    DatagramSocket portThisAudio = new DatagramSocket();
    idGui idUI;
    gui graphicUI;
    String idThis = "YOU";
	//Informacoes do outro cliente
	int portOtherMsg;
	int portOtherAudio;
	boolean otherOnline;
	InetAddress otherIP = InetAddress.getByName("localhost");
	String idOther = "OTHER";

	DatagramPacket sendPacket;
	DatagramPacket receivePacket;

	public cliente() throws IOException {
		portOtherMsg = -1;
		portOtherAudio = -1;
		otherOnline = false;

		idUI = new idGui(this);
	}

	//Funcao utilizada para o cliente enviar mensagens
	public void sendMsg(byte msg[], InetAddress adressIP, int porta) throws IOException {
        sendPacket = new DatagramPacket(msg, msg.length, adressIP, porta);
        portThisMsg.send(sendPacket);
    }

    public void sendAudio(byte b[], InetAddress addressIP, int porta) throws  IOException {
        sendPacket = new DatagramPacket(b, b.length, addressIP, porta);
        portThisAudio.send(sendPacket);
    }

    //Usando apenas para conferencia
	public void sendPack(String str, InetAddress adressIP, int porta) throws IOException {
		byte[] sendData = (str).getBytes();
		sendPacket = new DatagramPacket(sendData, sendData.length, adressIP, porta);
		portThisMsg.send(sendPacket);
	}

	public static int byteToInt(byte[] bytes){
        ByteBuffer wrapped = ByteBuffer.wrap(bytes, 0, bytes.length);
        return wrapped.getInt();
    }
	
	public String receivePack() throws IOException {
		byte[] receiveData = new byte[1024];
		receivePacket = new DatagramPacket(receiveData, receiveData.length);
		portThisMsg.receive(receivePacket);
		String new_str = new String(receivePacket.getData(), 0, receivePacket.getLength());

		if(receivePacket.getPort() == serverPort && serverIP.equals(receivePacket.getAddress())) {
			otherIP = InetAddress.getByAddress(Arrays.copyOfRange(receiveData, 0, 4));
			portOtherMsg = byteToInt(Arrays.copyOfRange(receiveData, 4, 8));
			portOtherAudio = byteToInt(Arrays.copyOfRange(receiveData, 8, 12));;
			idOther = new String(receivePacket.getData(), 12, receivePacket.getLength()-(4*3));
			graphicUI = new gui(this);
			return "";
		}
		return new_str;
	}
}
