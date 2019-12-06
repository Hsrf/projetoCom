import java.io.IOException;

public class mainin1 {

	public static void main(String[] args) throws IOException {
		cliente client = new cliente();

		Thread b = new receberMsg(client);
		b.start();
	}
}
