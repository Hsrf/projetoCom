import java.io.IOException;
import java.util.Scanner;

public class receberMsg extends Thread{

	cliente client;

	public receberMsg(cliente client) {
		this.client = client;
	}

	public void run() {
		String str;

		while (true) {
			try {
				str = client.receiveMsg();
				if(!str.equals("")) {
					client.graphicUI.textArea.append(client.idOther + ": " + str + "\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
