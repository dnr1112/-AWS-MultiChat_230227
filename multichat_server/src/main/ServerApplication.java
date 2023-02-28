package main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;

public class ServerApplication {

	public static void main(String[] args) {
		JFrame serverFrame = new JFrame("서버");
		serverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		serverFrame.setBounds(1500, 50, 200, 100);
		serverFrame.setVisible(true);
		
		
		try {
			ServerSocket serverSocket = new ServerSocket(9090);
			
			while(true) {
				Socket socket = serverSocket.accept();		// socket 받아오는걸 기다리다가 socket에 할당
				ConnectedSocket connectedSocket = new ConnectedSocket(socket);
				connectedSocket.start();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
