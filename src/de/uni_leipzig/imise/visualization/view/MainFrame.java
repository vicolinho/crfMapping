package de.uni_leipzig.imise.visualization.view;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JSplitPane;
import javax.swing.JButton;
import javax.swing.JTabbedPane;

import de.uni_leipzig.imise.visualization.controller.DiffTreeController;


public class MainFrame extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private VersionPanel vp ;
	private DiffPanel diffPanel;
	private DiffTreeController dtc;
	private PropertyDiffPanel propPanel;
	
	public MainFrame(){
		super();
		dtc = new DiffTreeController();
		setTitle("CRF Mapping Tool");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(new Dimension(900,600));
		
		CRFMenuBar menuBar = new CRFMenuBar();
		setJMenuBar(menuBar);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.3);
		getContentPane().add(splitPane, BorderLayout.CENTER);
		
		JSplitPane splitPane_1 = new JSplitPane();
		splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setRightComponent(splitPane_1);
		
		diffPanel = new DiffPanel(dtc);
		splitPane_1.setResizeWeight(0.6);
		splitPane_1.setLeftComponent(diffPanel);
		
		propPanel = new PropertyDiffPanel();
		dtc.addPropertyChangeListener(propPanel);
		splitPane_1.setRightComponent(propPanel);
		vp = new VersionPanel();
		splitPane.setLeftComponent(vp);
		this.setVisible(true);
		
	}
	
	public static void main (String[] args){
		MainFrame f = new MainFrame();
	}
}
