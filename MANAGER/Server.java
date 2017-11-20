package MANAGER;

import java.util.*;
import java.net.*;
import java.io.*;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class Server{
	static JTextArea area= new JTextArea("Connected on 5357");
	private static List<clientHandler> clients= new LinkedList<clientHandler>();
	public static void main(String[] args) {
		System.out.println("Done");
		JFrame frame= new JFrame();
		frame.setSize(300,300);
		frame.add(area);
		frame.setVisible(true);

		System.out.println("Done");
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		Server server= new Server();
		server.startServer(7777);

		
	}
	private static void startServer(int portNumber) {
		ServerSocket port;
		try {
			port= new ServerSocket(portNumber);
			while(true) {
				System.out.println("Waiting on connection "+portNumber);
				clientHandler newClient= new clientHandler(port.accept());
				area.setText(area.getText()+"connected");
				synchronized(clients) {
					clients.add(newClient);
				}
				newClient.start();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	public static void sendAll(String message,clientHandler ch) {
		try {
			area.setText(area.getText()+" message");
			synchronized(clients){
				for(clientHandler c : clients) {
					if(c!=ch) {
						c.send(message);
					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}
	private static class clientHandler extends Thread {
		BufferedReader input;
		PrintWriter output;
		public clientHandler(Socket socket) {
			try {
				input= new BufferedReader(new InputStreamReader(socket.getInputStream()));
				output= new PrintWriter(socket.getOutputStream(),true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		public void send(String message) {
			output.println(message);
		}
		public void run() {
			try {
				String line;
				while((line=input.readLine())!=null) {
					sendAll(line,this);
				}
			}catch(Exception e) {
				e.printStackTrace();
			}finally{
				send("ending connection");
				synchronized(clients) {
					clients.remove(this);
				}
				
			}
		}
	}
}
