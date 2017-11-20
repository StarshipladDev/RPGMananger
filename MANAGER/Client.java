package MANAGER;

import java.io.*;
import java.net.*;

public class Client{
	InputOutput input;
	InputOutput output;
	Socket socket;
	public Client(int port, String host) {
		try {
			socket= new Socket(host,port);
			System.out.println("yoyooy");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			output=new InputOutput(System.in,socket.getOutputStream());
			input= new InputOutput(socket.getInputStream(),System.out);
			output.start();
			input.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public String getInput(){
		return input.getLastLine();
	}
}
