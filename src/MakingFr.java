
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MakingFr {
	public static void main(String az[]) {
		firstInterface fi = new firstInterface();
		

	}
	
}
class firstInterface implements ActionListener{
	JFrame J;
	JMenuItem M1, M2;
	JMenuBar MenuBar;
	
	public firstInterface() {
		
		J = new JFrame();
		MenuBar= new JMenuBar();
		JMenu Menu1=new JMenu("File");
		M1=new JMenuItem("Open");
		M2=new JMenuItem("Exit");
		J.setJMenuBar(MenuBar);
		Menu1.add(M1);
		Menu1.add(M2);
		MenuBar.add(Menu1);
		
		
		J.setSize(800, 750);
		J.setVisible(true);
		
		
		
		
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}


