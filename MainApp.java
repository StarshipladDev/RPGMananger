package MANAGER;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.Format;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
/**
 * MainApp:
 * MainApp is the executable for RGPManager. When it is run it will attempt to
 * connect to a designated serverSocket run by the "Server" application.
 * It will hold a mutable Array list of the 'Unit' Class. 
 * Each 'Unit' class will hold its own data on location which MainApp's
 * built in 'drawPanel' will paint in a color defined in each 'Unit's data.
 * 
 * 
 * @author StarshipladDev
 * @version 1.03 01/01/2018
 * @see Server.java
 */
public class MainApp extends JFrame{
	private static final long serialVersionUID = 1L;
	static Unit selectedUnit;
	static boolean Selected=false;
	static ArrayList<Unit> Units= new ArrayList<Unit>();
	static JLabel unitName;
	static Color bg=Color.green;
	static Socket socket;
	static int background=0;
	static InputOutput output;
	/**
	 * main is the method that runs automatically when mainapp.java instance runs off a machine.
	 * it creates an internal JFrame, and an instance of the internal class 'DrawPanel'. It Places this
	 * DrawPanel in the JFrame, presenting the updating DrawPanel(through the 'Paint' method) to the user.
	 * @param args 
	 * args is the string that is given when mainapp.java is run. initially it was used to pass information
	 * as to which server socket to connect to, but now it isn't utilized.
	 */
	public static void main(String[] args) {
		JPanel panelo= new JPanel();
		JFrame frame = new JFrame();
		JButton button = new JButton("do stuff");
		JButton buttoncolor = new JButton("Change Color");
		buttoncolor.setActionCommand("color");
		button.setBackground(Color.red);
		drawPanel panel = new drawPanel(750,400);
		ActionListener listner= new listenboi(panel);
		/*Timer timer= new Timer(100,listner);
		timer.setActionCommand("timer");*/
		button.addActionListener(listner);
		buttoncolor.addActionListener(listner);
		JPanel panel2 = new JPanel();
		unitName=new JLabel("<html>Hello World!<br>blahblahblah</html>");
		panel2.add(button);
		panel2.add(buttoncolor);
		panel2.add(unitName);
		panel2.setPreferredSize(new Dimension(750,400));
		panel2.setBackground(Color.yellow);
		panelo.setBackground(Color.PINK);
		FlowLayout layout = new FlowLayout();
		layout.setVgap(0);
		layout.setHgap(0);
		panelo.setPreferredSize(new Dimension(750,800));
		panelo.setLayout(layout);
		panelo.add(panel);
		panelo.add(panel2);
		frame.setLayout(layout);
		frame.setBackground(Color.CYAN);
		frame.add(panelo);
		MouseBoi mouse= new MouseBoi(panel);
		frame.addMouseListener(mouse);
		frame.setSize(750,800);
		frame.pack();
		frame.setResizable(false);
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setVisible(true);
		try {
			socket=new Socket("localhost",7777);
			output=new InputOutput(System.in,socket.getOutputStream());
			//InputOutput input= new InputOutput(socket.getInputStream(),System.out);
			//input.start();
			output.start();

			System.out.println("Puts Created Succsessfully");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error");
		}
		try {

			System.out.println("Startign settign scanner up");
			Scanner scanner= new Scanner(socket.getInputStream());
			String line;
			System.out.println("Built scanner reading "+scanner.nextLine());
			while((line=scanner.nextLine()) != null) {
				System.out.println("Got input: "+line);
				String test=line.substring(0,4);
				if(test.matches("CHCL")) {
					if (selectedUnit.character.color==Color.blue) {
						selectedUnit.character.color=Color.pink;


					}else {
						selectedUnit.character.color=Color.blue;
					}
				}else if(test.matches("BKCH")) {
					background++;
					if(background ==1) {
						bg=Color.BLUE;
					}else if(background==2) {
						bg=Color.gray;
					}else {
						bg=Color.green;
						background=0;
					}
				}
				else if(test=="UNMV") {
					selectedUnit.y=getNumber(line.substring(4,8));
					selectedUnit.y=getNumber(line.substring(8,12));
				}
				else if(test.matches("UNAD")) {
					
					Units.add(new Unit("Soldier"+String.valueOf(Units.size()+1),getNumber(line.substring(4,8)),getNumber(line.substring(8,12))));
					System.out.println("Put unit at "+getNumber(line.substring(4,8))+" "+getNumber(line.substring(8,12)));
				}else if(test.matches("UNSL")) {
					System.out.println("Unit "+line.charAt(4)+"selected");

					Selected=true;
					selectedUnit=Units.get(Integer.parseInt(String.valueOf(line.charAt(4))));
					String pop=selectedUnit.character.characterName;
					int popo=selectedUnit.character.health;
					int popopo=selectedUnit.character.damage;
					unitName.setText("<html>Name"+pop+ "<br>"+"Damage:"+popo+ "<br>Health:"+popopo+"</html>");
				}else {
					System.out.println("\""+test+"\" was Bad");
				}
				panel.repaint();
				
			}
			scanner.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Dead");
			e.printStackTrace();
		}
		
	}
	/**
	 * getNumber is a method designed to turn parts of the String read by the server to
	 * send commands to drawPanel into readable ints, so that coordinates can be given
	 * as regular integers.
	 * @param number
	 * getNumber take int 'number' as the formated integer. It is to be used on a 
	 * formated int that may have preceding 0's, and needs to be read as an int without.
	 * @return 
	 * getNumber returns the de-formated integer passed as an argument
	 */
	public static int getNumber(String number){
		int i=0;
		int total=0;
		while(i<3) {
			total=total+((10^(3-i))* Integer.parseInt(String.valueOf(number.charAt(i))));
			i++;
		}
		total=total+Integer.parseInt(String.valueOf(number.charAt(3)));
		return total;
		
	}
	/**
	 * setIn is used to call upon MainApp's ReadWrite thread that 
	 * passes information to a server to be read as commands.
	 * It mostly servers as a call method to output's 'send' command.
	 * @param in
	 * 'in' is the String value of a command sent by the user
	 */
	public static void setIn(String in) {
		output.send(in);
	}
	/**
	 * Mouseboi is a extension of the MouseListner
	 * Class, designed specifically for RPGMananger.
	 * It contains the checks and commands to use 'setIn'
	 * to send commands to the server. It also allows a user to select a unit.
	 * @author Ozzy1
	 * @see java.awt.event.MouseListener
	 * @see MainApp.setIn
	 */
	private static class MouseBoi implements MouseListener{
		drawPanel p;
		public MouseBoi(drawPanel p) {
			this.p=p;
		}
		@Override
		public void mouseClicked(MouseEvent e) {
			System.out.println("mouseclicked");;
		}

		@Override
		public void mouseEntered(MouseEvent e) {

		}

		@Override
		public void mouseExited(MouseEvent e) {

		}

		@Override
		public void mousePressed(MouseEvent e) {

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			int x=e.getX()-e.getX()%10-10;
			int y=e.getY()-e.getY()%10-30;
			if(SwingUtilities.isLeftMouseButton(e)){
				System.out.println("left");
				int ye=0;
				while(Selected!=true&&(ye<Units.size())) {
					if(Units.get(ye).x==x&&(Units.get(ye).y==y)) {
						setIn("UNSL"+ye);
						
					}else{
						ye++;
					}

				}
				if(Selected!=true) {

					setIn(String.format("UNAD%04d%04d",x,y));
					

				}
			}
			else if(SwingUtilities.isRightMouseButton(e)){
				System.out.println("right");
				if (Selected){
					boolean taken=false;
					int ye=0;
					while(ye<Units.size()) {
						if(Units.get(ye).x==x&&(Units.get(ye).y==y)) {
							taken=true;
						}
						ye++;
					}if(!taken) {
						setIn("UNMV"+String.format("%04d%04d",x,y));


					}

				}
			}
			System.out.println("Mouse Released at "+x+" " +y);
			p.repaint();

		}

	}
	private static class drawPanel extends JPanel{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		int x=0;
		int y=0;
		int b=0;
		int c=0;
		BufferedImage img;
		public  drawPanel(int width, int height ) {
			try {
				File file= new File("p.jpg");
				img=ImageIO.read(file);
			}catch(Exception e) {
				System.out.println("dasasd");
				e.printStackTrace();
			}
			setPreferredSize(new Dimension(width,height));
		}
		public void paint(Graphics g) {

			g.setColor(bg);
			g.fillRect(0,0,getWidth(),getHeight());
			g.drawImage(img,0,0,img.getWidth(),img.getHeight(),0,0,img.getWidth(),img.getHeight(),null);
			int i=0;
			g.setColor(Color.black);
			while(i<getWidth()/10) {
				g.drawLine(i*10,0,i*10,getHeight());
				i++;
			}
			int f=0;
			g.setColor(Color.black);
			while(f<getHeight()/10) {
				g.drawLine(0,f*10,getWidth(),f*10);
				f++;
			}
			int y=0;

			if(Units.size()>0) {

				while(y<Units.size()) {
					g.setColor(Units.get(y).character.color);
					g.fillRect(Units.get(y).x,Units.get(y).y,getWidth()/i,getHeight()/f);
					y++;
				}

			}
			if(Selected==true) {
				g.setColor(Color.red);
				g.fillRect(selectedUnit.x,selectedUnit.y,getWidth()/i,getHeight()/f);
			}


		}
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
		}

	}
	
	private static class listenboi implements ActionListener{
		drawPanel d;
		public listenboi(drawPanel d) {
			this.d=d;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			if(selectedUnit!=null) {
				if(e.getActionCommand()=="color") {
					setIn("CHCL");
				}
				else {
					setIn("CHBK");
					
				}

				d.repaint();
			}
			// TODO Auto-generated method stub

		}

	}
	private class Client{
		InputOutput input;
		InputOutput output;
		Socket socket;
		public Client(Socket socket) {
			System.out.println("yoyooy");
			try {
				output=new InputOutput(System.in,socket.getOutputStream());
				input= new InputOutput(socket.getInputStream(),System.out);
				output.start();
				input.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
