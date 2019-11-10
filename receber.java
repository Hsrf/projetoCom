import java.io.IOException;
import java.util.Scanner;

public class receber extends Thread{
	Scanner in = new Scanner(System.in);

	cliente client;

	public receber(cliente client) {
		this.client = client;
	}

	public void run() {
		String str;

		while (true) {
			try {
				str = client.receivePack();
				if(!str.equals("")) {
					client.graphicUI.textArea.append("OTHER: " + str + "\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
