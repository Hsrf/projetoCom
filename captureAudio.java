import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;
import java.util.Timer;
import java.util.TimerTask;

public class captureAudio implements Runnable {
    Timer timer;
    byte audioData[];
    private volatile boolean stop;

    public captureAudio(){
        timer = new Timer();
        stop = false;
    }

    public void run() {
        timer.schedule(new sendAudio(), 500);
        while(!stop){
            //armazena as informacoes de audio em audioData
        }
    }

    class sendAudio extends TimerTask{
        public void run() {
            //envia audio pro outro cliente
            //zera o array
            System.out.println("funcionando");
            if(!stop) timer.schedule(new sendAudio(), 500);
        }
    }

    public void setStop(boolean stop){
        this.stop = stop;
    }
}
