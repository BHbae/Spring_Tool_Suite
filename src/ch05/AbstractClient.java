package ch05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

// 2단계 - 상속 활용 리팩토링 단계
public abstract class AbstractClient {

	private ServerSocket serverSocket;
	private Socket socket;
	private BufferedReader socketReader;
	private PrintWriter socketWriter;
	private BufferedReader keyboardReader;

	// 실행
	public final void run() {
		try {
			setupSocket();
			Request();
			setupStream();
			startChat();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			clenUp();
		}
	}

	// 1. IP, 포트 번호 입력
	protected abstract void setupSocket() throws IOException;

	// 2. 서버요청
	protected abstract void Request() throws IOException;

	// 3. 연결된 서버 스트임 입출력?
	private void setupStream() throws IOException {
		socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		socketWriter = new PrintWriter(socket.getOutputStream(), true);
		keyboardReader = new BufferedReader(new InputStreamReader(System.in));
	}

	// 시작
	private void startChat() {
		Thread readThread = createReadThread();
		Thread wriThread = createWriterThread();

		readThread.start();
		wriThread.start();

		try {
			readThread.join();
			wriThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// 출력 - 캡슐화
	private Thread createReadThread() {
		return new Thread(() -> {
			try {
				String serverMsg;
				while ((serverMsg = socketReader.readLine()) != null) {
					System.out.println("Server Chat : " + serverMsg);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	// 입력 - 캡슐화
	private Thread createWriterThread() {
		return new Thread(() -> {
			String socketMsg;
			try {
				while ((socketMsg = keyboardReader.readLine()) != null) {
					socketWriter.println(socketMsg);
					socketWriter.flush();

				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	// 서버 연결종료 - 캡슐화
	private void clenUp() {
		try {
			if (socket != null) {
				socket.close();
			}
			if (serverSocket != null) {
				serverSocket.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

}
