package MANAGER;

import java.io.*;

public class InputOutput extends Thread{
	BufferedReader input;
	PrintWriter output;
	String lastLine;
	public InputOutput(InputStream inputStream,OutputStream outputStream) {
		try {

			this.input=new BufferedReader(new InputStreamReader(inputStream));
			this.output=new PrintWriter(outputStream);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void run() {
		
		try{
			String line;
			while((line=input.readLine()) != null) {
				output.println(input.readLine());
				lastLine= line;
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	public String getLastLine() {
			return lastLine;
	}
	
}