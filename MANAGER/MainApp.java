package MANAGER;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.*;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
public class MainApp extends JFrame{
	static Unit selectedUnit;
	static boolean Selected=false;
	static ArrayList<Unit> Units= new ArrayList();
	static JLabel unitName;
	static Color bg=Color.green;
	static Client client;
	static String command;
	static String oldCommand;
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


	}private static class MouseBoi implements MouseListener{
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
						System.out.println("Unit "+ye+"selected");
						Selected=true;
						selectedUnit=Units.get(ye);
						ye=Units.size();
						String pop=selectedUnit.character.characterName;
						int popo=selectedUnit.character.health;
						int popopo=selectedUnit.character.damage;
						unitName.setText("<html>Name"+pop+ "<br>"+"Damage:"+popo+ "<br>Health:"+popopo+"</html>");
					}else{
						ye++;
					}
					
				}
				if(Selected!=true) {
					Units.add(new Unit("Soldier"+String.valueOf(Units.size()+1), x, y));
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
						selectedUnit.x=x;
						selectedUnit.y=y;
						Selected=false;
						unitName.setText("<html>Name"+"<br>"+"Damage:"+"<br>Health:"+"</html>");
						
					}
					
				}
			}
			System.out.println("Mouse Released at "+p.x+" " +p.y);
			p.repaint();

		}

	}
	private static class drawPanel extends JPanel{
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
		int background=0;
		@Override
		public void actionPerformed(ActionEvent e) {
			if(selectedUnit!=null) {
				if(e.getActionCommand()=="color") {
					if (selectedUnit.character.color==Color.blue) {
						selectedUnit.character.color=Color.pink;
					}else {
						selectedUnit.character.color=Color.blue;
					}
				}
				else {
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
				
				d.repaint();
			}
			// TODO Auto-generated method stub
			
		}
		
	}
}
