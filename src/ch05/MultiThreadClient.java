package ch05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

// 1단계 - 함수로 분리 해서 리팩토링 진행
public class MultiThreadClient {

	public static void main(String[] args) {

		System.out.println("@@@ 클라이언트 실행 @@@");

		try (Socket socket = new Socket("localhost", 5000);) {
			PrintWriter socketWriter = new PrintWriter(socket.getOutputStream(), true);

			BufferedReader socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			BufferedReader keyboardReader = new BufferedReader(new InputStreamReader(System.in));

			startReadThread(socketReader);
			startWriterThread(socketWriter, keyboardReader);
		} catch (Exception e) {
			e.printStackTrace();
		}
	} // end of main

	// 서버에서 받는 스레드
	private static void startReadThread(BufferedReader socketReader) {
		Thread readThred = new Thread(() -> {
			try {
				String serverMsg;
				while ((serverMsg = socketReader.readLine()) != null) {
					System.out.println("Server MSG :" + serverMsg);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		readThred.start(); // 실행
		// waitForThreadToEnd(readThred); // 대기
	}

	// 서버에 보내는 스레드
	private static void startWriterThread(PrintWriter socketWriter, BufferedReader keyBufferedReader) {
		Thread writerThred = new Thread(() -> {
			String clientMsg;
			try {
				while ((clientMsg = keyBufferedReader.readLine()) != null) {
					socketWriter.println(clientMsg);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}) {

		};
		writerThred.start(); // 실행
		// waitForThreadToEnd(writerThred);// 대기
	}

	// 스레드 종료 대기
	private static void waitForThreadToEnd(Thread thread) {
		try {
			thread.join();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

} // end of class
