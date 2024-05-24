package ch05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

// 1단계 - 함수로 분리 해서 리팩토링 진행
public class MultiThreadClient2 {

	public static void main(String[] args) {

		System.out.println("### 클라이언트 실행 ###");
		try (Socket socket = new Socket("localhost", 5000);) {
			System.out.println("connected to the server !!");

			// 서버와 통신을 위한 스트림 초기화
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
			BufferedReader keyboardRreader = new BufferedReader(new InputStreamReader(System.in));

			startReadThread(bufferedReader);
			starWriterThread(printWriter, keyboardRreader);
			// 메인 스레드 대기 어디에있지??? 가족성이 떨어짐
			// starWriterThread() <---- 내부에 있음
		} catch (Exception e) {
			e.printStackTrace();
		}

	} // end of main

	// 1. 클라이언트로부터 데이터를 읽는 스레드 시작 메서드 생성
	private static void startReadThread(BufferedReader reader) {

		Thread readThread = new Thread(() -> {
			try {
				String msg;
				while ((msg = reader.readLine()) != null) {
					System.out.println("client 에서 온 msg : " + msg);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		readThread.start();
	}

	// 2. 키보드에서 립력을 받아 클라이언트 측으로 데이터를 전송하는 스레드
	private static void starWriterThread(PrintWriter writer, BufferedReader keyboardRreader) {

		Thread writeThread = new Thread(() -> {
			try {
				String msg;
				while ((msg = keyboardRreader.readLine()) != null) {
					// 전송
					writer.println(msg);
					writer.flush();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		writeThread.start();
		try {
			// 메인스레드 대기함
			writeThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

} // end of class
