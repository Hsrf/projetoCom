import javax.sound.sampled.TargetDataLine;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.sql.Date;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;

public class recorder extends Thread {
    public TargetDataLine audio_in;
    public DatagramSocket out;
    byte byte_buff[] = new byte[512];
    public cliente client;

    public recorder(TargetDataLine audio_in, cliente client) {
        this.audio_in = audio_in;
        try {
            this.out = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        this.client = client;
    }

    public static byte[] intToByte(int i){
        byte[] msg = ByteBuffer.allocate(4).putInt(i).array();
        return msg;
    }

    public static byte[] concatenarBytes(byte[] bytes1, byte[] bytes2){
        byte[] new_byte = new byte[bytes1.length + bytes2.length];
        int k = 0;

        for(int i = 0 ; i < bytes1.length ; i++) new_byte[k++] = bytes1[i];
        for(int i = 0 ; i < bytes2.length ; i++) new_byte[k++] = bytes2[i];

        return new_byte;
    }

    int startSequenceNumber = 0;
    int hora;
    public byte[] createRTP(int m){
        byte[] b;
        hora = Calendar.getInstance().get(Calendar.HOUR)*3600;
        hora += Calendar.getInstance().get(Calendar.MINUTE)*60;
        hora += Calendar.getInstance().get(Calendar.SECOND);

        byte[] VPXCC = {(byte)0x80};
        byte[] MPT = Arrays.copyOfRange(intToByte(14 + m), 3, 4);
        byte[] sequenceNumber = Arrays.copyOfRange(intToByte(startSequenceNumber), 2, 4);
        byte[] timestamp = intToByte(hora);
        byte[] SSRC = intToByte((int)Math.random()*(1 << 17));

        b = VPXCC;
        b = concatenarBytes(b, MPT);
        b = concatenarBytes(b, sequenceNumber);
        b = concatenarBytes(b, timestamp);
        b = concatenarBytes(b, SSRC);

        startSequenceNumber = (startSequenceNumber+1) % (1 << 17);
        return b;
    }

    int i = 1;
    public void run() {
        int m = (1 << 8);
        startSequenceNumber = (int)Math.random() * (1 << 17);
        System.out.println("Thread recorder init.");
        while(client.calling) {
            audio_in.read(byte_buff, 0, byte_buff.length);
            //Aqui nos geramos o cabecalho RTP
            byte[] header = createRTP(m);
            //Aqui nos concatenamos o cabecalho gerado com os dados
            byte[] header_byte_buff = concatenarBytes(header, byte_buff);
            m = 0;

            DatagramPacket data = new DatagramPacket(header_byte_buff, header_byte_buff.length, client.otherIP, client.portOtherAudio);
            //System.out.println("send #" + (i++));
            try {
                out.send(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        audio_in.close();
        audio_in.drain();
        System.out.println("Thread recorder stop.");
    }
}
