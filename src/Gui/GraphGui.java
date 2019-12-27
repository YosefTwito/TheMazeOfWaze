package Gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;


import algorithms.*;
import dataStructure.*;
import utils.*;

/**
 * This class makes a gui window to represent a graph and
 * use the Algorithms from class Graph_Algo on live.
 * (use the methods and represent it on the gui window while it is still up).
 * @author YosefTwito and EldarTakach
 */

public class GraphGui extends JFrame implements ActionListener, MouseListener{

	private static final long serialVersionUID = 1L;

	graph gr;
	graph original;

	public GraphGui(graph g){
		this.gr=new DGraph((DGraph)g);
		this.original=new DGraph((DGraph)g);
		initGUI(g);
	}


	public void paint(Graphics d) {
		super.paint(d);

		if (gr != null) {
			//get nodes
			Collection<node_data> nodes = gr.getV();

			for (node_data n : nodes) {
				//draw nodes
				Point3D p = n.getLocation();
				d.setColor(Color.BLACK);
				d.fillOval(p.ix(), p.iy(), 11, 11);

				//draw nodes-key's
				d.setColor(Color.BLUE);
				d.drawString(""+n.getKey(), p.ix()-4, p.iy()-4);

				//check if there are edges
				if (gr.edgeSize()==0) { continue; }
				if ((gr.getE(n.getKey())!=null)) {
					//get edges
					Collection<edge_data> edges = gr.getE(n.getKey());
					for (edge_data e : edges) {
						//draw edges
						d.setColor(Color.GREEN);
						((Graphics2D) d).setStroke(new BasicStroke(2,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
						Point3D p2 = gr.getNode(e.getDest()).getLocation();
						d.drawLine(p.ix()+5, p.iy()+5, p2.ix()+5, p2.iy()+5);
						//draw direction
						d.setColor(Color.MAGENTA);
						d.fillOval((int)((p.ix()*0.7)+(0.3*p2.ix()))+2, (int)((p.iy()*0.7)+(0.3*p2.iy())), 9, 9);
						//draw weight
						String sss = ""+String.valueOf(e.getWeight());
						d.drawString(sss, 1+(int)((p.ix()*0.7)+(0.3*p2.ix())), (int)((p.iy()*0.7)+(0.3*p2.iy()))-2);
					}
				}	
			}
		}	
	}


	private void initGUI(graph g) {
		this.gr=g;
		this.setSize(1280, 720);
		this.setTitle("Hello and welcome to The Maze Of Waze !");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ImageIcon img = new ImageIcon("mari.png");
		this.setIconImage(img.getImage());
		this.setResizable(true);


		MenuBar menuBar = new MenuBar();
		this.setMenuBar(menuBar);

		Menu file = new Menu("File ");
		menuBar.add(file);

		Menu alg  = new Menu("Algorithms ");
		menuBar.add(alg);
		
		MenuItem item1 = new MenuItem("Init Original Graph");
		item1.addActionListener(this);
		file.add(item1);

		MenuItem item2 = new MenuItem("Init From textFile ");
		item2.addActionListener(this);
		file.add(item2);

		MenuItem item3 = new MenuItem("Save as textFile ");
		item3.addActionListener(this);
		file.add(item3);

		MenuItem item4 = new MenuItem("Save as png ");
		item4.addActionListener(this);
		file.add(item4);

		MenuItem item5 = new MenuItem("Show Shortest Path  ");
		item5.addActionListener(this);
		alg.add(item5);

		MenuItem item12 = new MenuItem("Shortest Path Distance");
		item12.addActionListener(this);
		alg.add(item12);

		MenuItem item6 = new MenuItem("The SalesMan Problem");
		item6.addActionListener(this);
		alg.add(item6);

		MenuItem item7 = new MenuItem("Is it Conncected ?"  );
		item7.addActionListener(this);
		alg.add(item7);

	}

	@Override
	public void actionPerformed(ActionEvent Command) {
		String str = Command.getActionCommand();		
		Graph_Algo t=new Graph_Algo();
		JFileChooser j;
		FileNameExtensionFilter filter;

		switch(str) {
		
		case "Init Original Graph": 
			initGUI(this.original);
			break;

		case "Init From textFile ": ////////////////////////////////////// gotta check /////////////////
			System.out.println("Init From textFile: ");
			t=new Graph_Algo();

			j = new JFileChooser(FileSystemView.getFileSystemView());
			j.setDialogTitle("Init graph out of text file.."); 
			filter = new FileNameExtensionFilter(" .txt","txt");
			j.setFileFilter(filter);

			int returnVal = j.showOpenDialog(null);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				System.out.println("You chose to open this file: " + j.getSelectedFile().getName());
				t.init(j.getSelectedFile().getAbsolutePath());
			}			
			break;

		case "Save as textFile ": ////////////////////////////////////// gotta check /////////////////
			System.out.println("Save as textFile: ");
			t=new Graph_Algo();

			j = new JFileChooser(FileSystemView.getFileSystemView());
			j.setDialogTitle("Save graph to text file.."); 
			filter = new FileNameExtensionFilter(" .txt","txt");
			j.setFileFilter(filter);

			int userSelection = j.showSaveDialog(null);
			if (userSelection == JFileChooser.APPROVE_OPTION) {
				System.out.println("Save as file: " + j.getSelectedFile().getAbsolutePath());
				t.save(j.getSelectedFile().getAbsolutePath());
			}
			break;

		case "Save as png ": // done !
			try {
				BufferedImage i = new BufferedImage(this.getWidth(), this.getHeight()+45, BufferedImage.TYPE_INT_RGB);
				Graphics g = i.getGraphics();
				paint(g);
				ImageIO.write(i, "png", new File("SavedGraph.png"));
				System.out.println("Saved as png: SavedGraph.png");
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case "Show Shortest Path  ": ///////////////////////////////////// done - gotta check///////////////
			try {
				System.out.println("Show Shortest Path ");
				JFrame SSPin = new JFrame();
				String SourceNodeSSP = JOptionPane.showInputDialog(SSPin,"Enter Source-Node:");
				String DestNodeSSP = JOptionPane.showInputDialog(SSPin,"Enter Destination-Node:");

				int srcSSP = Integer.parseInt(SourceNodeSSP);
				int destSSP = Integer.parseInt(DestNodeSSP);

				Graph_Algo newGSSP = new Graph_Algo();
				newGSSP.init(gr);

				List<node_data> SSPdis = newGSSP.shortestPath(srcSSP, destSSP);
				graph gr_new=new DGraph();
				gr_new.addNode(SSPdis.get(0));
				for (int i=1; i<SSPdis.size(); i++) {
					gr_new.addNode(SSPdis.get(i));
					gr_new.connect(SSPdis.get(i-1).getKey(), SSPdis.get(i).getKey(), this.gr.getEdge(SSPdis.get(i-1).getKey(), SSPdis.get(i).getKey()).getWeight());
				}

				//gr_new.addNode((node_data)new node(new Point3D(500,500),0)); tilll eldar pushhhhhhhhhh
				//gr_new.addNode((node_data)new node(new Point3D(600,250),0));
				this.initGUI(gr_new);
			}	
			catch (Exception e) {
				e.printStackTrace();
			}
			break;

		case "Shortest Path Distance": ////////////////////////////////////////// done - gotta check //////////////
			try
			{
				JFrame SPDinput = new JFrame();
				String SourceNodeSPD = JOptionPane.showInputDialog(SPDinput,"Enter Source-Node:");
				String DestNodeSPD = JOptionPane.showInputDialog(SPDinput,"Enter Destination-Node:");

				int srcSPD = Integer.parseInt(SourceNodeSPD);
				int destSPD = Integer.parseInt(DestNodeSPD);

				Graph_Algo newg = new Graph_Algo();
				
				newg.init(this.gr);
				double x = newg.shortestPathDist(srcSPD, destSPD);
				JOptionPane.showMessageDialog(SPDinput, "The Shortest Path Distance is: " + x);

				System.out.println("Shortest Path Distance is:" + x);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			break;

		case "The SalesMan Problem": 
			JFrame TSPinput = new JFrame();
			String SourceNodeTSP = JOptionPane.showInputDialog(TSPinput,"How many nodes ?");
			int manyTSP = Integer.parseInt(SourceNodeTSP);
			List<Integer> TSPnodes = new ArrayList<Integer>();
			for (int i=0; i<manyTSP; i++) {
				SourceNodeTSP = JOptionPane.showInputDialog(TSPinput,"Enter node-key "+(i+1)+"/"+manyTSP+": "+"\n"
			+"so far you entered: \n");
				int TSPkey = Integer.parseInt(SourceNodeTSP);
				TSPnodes.add(TSPkey);
			}
			
			Graph_Algo newGTSP = new Graph_Algo();
			newGTSP.init(gr);
			List<node_data> TSP = newGTSP.TSP(TSPnodes);
			graph gr_new=new DGraph();
			gr_new.addNode(TSP.get(0));
			for (int i=1; i<TSP.size(); i++) {
				gr_new.addNode(TSP.get(i));
				gr_new.connect(TSP.get(i-1).getKey(), TSP.get(i).getKey(), this.gr.getEdge(TSP.get(i-1).getKey(), TSP.get(i).getKey()).getWeight());
			}

			//gr_new.addNode((node_data)new node(new Point3D(100,500),0)); //TILL ELDAR PUSHHHHH
			//gr_new.addNode((node_data)new node(new Point3D(600,100),0));
			this.initGUI(gr_new);
			
			break;

		case "Is it Conncected ?": ////////////////////////////////////////// done - gotta check //////////////
			JFrame isIt = new JFrame();			
			Graph_Algo isCga = new Graph_Algo();
			isCga.init(this.gr);
			if (isCga.isConnected()) { 
				System.out.println("The graph is Connected !");
				JOptionPane.showMessageDialog(isIt, "The graph is Connected !");
				}
			else { 
				System.out.println("The graph is not Connected !");
				JOptionPane.showMessageDialog(isIt, "The graph is not Connected !");				
			}
			break;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {;}
	@Override
	public void mousePressed(MouseEvent e) {;}
	@Override
	public void mouseReleased(MouseEvent e) {;}
	@Override
	public void mouseEntered(MouseEvent e) {;}
	@Override
	public void mouseExited(MouseEvent e) {;}
}
