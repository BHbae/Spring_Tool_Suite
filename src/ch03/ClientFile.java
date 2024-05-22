package ch03;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientFile {

	public static void main(String[] args) {

		Socket socket = null;

		try {
			socket = new Socket("localhost", 5001);

			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
			writer.println("배가 너무 불러"); // 줄바꿈 처리를 하자
			
			// 서버로 부터 데이터를 받기 위한 입력 스트림이 필요하다.
			// 대상은 소켓이다.
			BufferedReader reader =
					new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			String messge = reader.readLine();
			System.out.println("서버측 응답 : " + messge);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
