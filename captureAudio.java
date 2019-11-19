import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;
import java.util.Timer;
import java.util.TimerTask;

public class captureAudio implements Runnable {
    Timer timer;
    byte audioData[];
    private volatile boolean stop;
    TargetDataLine line;
    DataLine.Info info;
    AudioFormat format;

    public captureAudio(){
        timer = new Timer();
        stop = false;
        format = new AudioFormat(50, 16, 1, false, true);
        info = new DataLine.Info(TargetDataLine.class, format);

        if(!AudioSystem.isLineSupported(info)){
            System.out.println("Deu ruim man");
        }
        try {
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
        }catch (LineUnavailableException e){
            System.out.println(e);
        }
    }

    public void run() {
        timer.schedule(new sendAudio(), 20);
        ByteArrayOutputStream out  = new ByteArrayOutputStream();
        int numBytesRead;

        line.start();
        audioData = new byte[line.getBufferSize() / 5];

        while(!stop){
            //armazena as informacoes de audio em audioData
            numBytesRead = line.read(audioData, 0, audioData.length);
            out.write(audioData, 0, numBytesRead);
        }
        line.stop();
    }

    class sendAudio extends TimerTask{
        public void run() {
            //envia audio pro outro cliente
            //zera o array
            audioData = new byte[1024];
            if(!stop) timer.schedule(new sendAudio(), 20);
        }
    }

    public void setStop(boolean stop){
        this.stop = stop;
    }
}
