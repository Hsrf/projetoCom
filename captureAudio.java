import java.util.Timer;
import java.util.TimerTask;

public class captureAudio extends  Thread {
    Timer timer;
    byte audioData[];

    public captureAudio(){
        timer = new Timer();
        timer.schedule(new sendAudio(), 200);
    }

    class sendAudio extends TimerTask{
        public void run(){

            System.out.println("funciona");
            /*
            //envia o audio
            //zera o array do audio

             */
            timer.schedule(new sendAudio(), 200);
        }
    }

    public static void main(String[] args) {
        new captureAudio();
        while(true){
            //vai capturando o audio
        }
    }
}
