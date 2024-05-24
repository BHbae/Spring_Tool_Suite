package ch06;

import java.io.IOException;
import java.net.Socket;

public class ChatClient extends AbstractClient {

	public ChatClient(String name) {
		super(name);
	}

	@Override
	protected void connectToServer() throws IOException {
		// AbstractClient --> 부모 클래스 --> 서버측과 연결 된 소켓을 주입해주어 한다.
		// 
		super.setSocket(new Socket("192.168.0.48", 5000));
		
	}
	
	public static void main(String[] args) {
	ChatClient chatClient = new ChatClient("오른");
	chatClient.run();
		
		
	}
	
	
	
}
