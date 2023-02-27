package views;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.google.gson.Gson;

import dto.request.RequestDto;

import java.awt.CardLayout;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ClientApplication extends JFrame {

	private static final long serialVersionUID = -4753767777928836759L;
	
	private Gson gson;
	private Socket socket;
	
	private JPanel mainPanel;
	private CardLayout mainCard;
	
	private JTextField usernameField;
	private JTextField sendMessageField;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientApplication frame = new ClientApplication();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ClientApplication() {
		
		/*========<< init >>========*/
		gson = new Gson();
		try {
			socket = new Socket("127.0.0.1", 9090);
			ClientRecive clientRecive = new ClientRecive(socket);
			clientRecive.start();
			
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
			
		} catch (ConnectException e1) {
			JOptionPane.showMessageDialog(this, "서버에 접속할 수 없습니다.", "접속오류", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
			
		} catch (IOException e1) {
			e1.printStackTrace();
		} 
		
		
		/*========<< frame set >>========*/
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(600, 150, 480, 800);
//		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

		
		/*========<< panels >>========*/
		mainPanel = new JPanel();
		
		JPanel loginPanel = new JPanel();
		JPanel roomListPanel = new JPanel();
		JPanel roomPanel = new JPanel();
		
		
		/*========<< layout >>========*/
		mainCard = new CardLayout();

		mainPanel.setLayout(mainCard);
		loginPanel.setLayout(null);
		roomListPanel.setLayout(null);
		roomPanel.setLayout(null);

		
		/*========<< panel set >>========*/
		setContentPane(mainPanel);
		
		mainPanel.add(loginPanel, "loginPanel");
		mainPanel.add(roomListPanel, "roomListPanel");
		mainPanel.add(roomPanel, "roomPanel");

		
		/*========<< login panel >>========*/
		JButton enterButton = new JButton("접속하기");
		usernameField = new JTextField();
		usernameField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					enterButton.doClick();			// enterButton을 클릭했을 때와 동일한 동작을 수행해라
				}
			}
		});
		
		usernameField.setBounds(12, 495, 430, 36);
		loginPanel.add(usernameField);
		usernameField.setColumns(10);
		
		enterButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				RequestDto<String> usernameCheckReqDto =
						new RequestDto<String>("usernameCheck", usernameField.getText());
				sendRequest(usernameCheckReqDto);
			}
		});
		enterButton.setBounds(12, 541, 430, 36);
		loginPanel.add(enterButton);
		
		
		/*========<< roomList panel >>========*/
		JScrollPane roomListScroll = new JScrollPane();
		roomListScroll.setBounds(100, 0, 354, 751);
		roomListPanel.add(roomListScroll);
		
		JList roomList = new JList();
		roomListScroll.setViewportView(roomList);
		
		JButton createRoomButton = new JButton("방생성");
		createRoomButton.setBounds(12, 10, 76, 76);
		roomListPanel.add(createRoomButton);
		
		
		/*========<< room panel >>========*/
		JScrollPane joinUserListScroll = new JScrollPane();
		joinUserListScroll.setBounds(0, 0, 350, 70);
		roomPanel.add(joinUserListScroll);
		
		JList joinUserList = new JList();
		joinUserListScroll.setViewportView(joinUserList);
		
		JButton roomExitButton = new JButton("나가기");
		roomExitButton.setBounds(349, 0, 105, 70);
		roomPanel.add(roomExitButton);
		
		JScrollPane chattingContentScroll = new JScrollPane();
		chattingContentScroll.setBounds(0, 80, 454, 594);
		roomPanel.add(chattingContentScroll);
		
		JTextArea chattingContent = new JTextArea();
		chattingContentScroll.setViewportView(chattingContent);
		
		sendMessageField = new JTextField();
		sendMessageField.setBounds(0, 684, 350, 67);
		roomPanel.add(sendMessageField);
		sendMessageField.setColumns(10);
		
		JButton sendButton = new JButton("전송");
		sendButton.setBounds(349, 684, 105, 67);
		roomPanel.add(sendButton);
	}
	
	private void sendRequest(RequestDto<?> requestDto) {			// send를 자주 사용하기에, 메소드로 빼줌
		String reqJson = gson.toJson(requestDto);
		OutputStream outputStream = null;
		PrintWriter printWriter = null;
		
		try {
			outputStream = socket.getOutputStream();
			printWriter = new PrintWriter(outputStream, true);
			printWriter.println(reqJson);
			System.out.println("클라이언트 -> 서버" + reqJson);
			
		} catch (IOException e) {
			e.printStackTrace();
			
		} finally {
			printWriter.close();
			
			try {
				outputStream.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
