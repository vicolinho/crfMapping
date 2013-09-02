package de.uni_leipzig.imise.visualization.view;



import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import de.uni_leipzig.imise.data.CRFVersion;
import de.uni_leipzig.imise.visualization.controller.VersionController;

public class TreeFrame extends JInternalFrame {
	
	private int versionNr;
	private VersionTree tree;
	
	
	public TreeFrame(int version, VersionController vmc){
		super();
		versionNr = version;
		this.addInternalFrameListener(vmc);
		this.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
		this.setClosable(true);
		this.setAutoscrolls(true);
		this.tree = new VersionTree(null);
		JPanel panel = new JPanel();
		panel.add(tree);
		this.add(panel);
		this.pack();
		this.setVisible(true);
		
	}
	
	
	public void initTree(CRFVersion v ){
		tree.loadVersion(v);
		for (int i=0;i<tree.getRowCount();i++){
			tree.expandRow(i);
		}
		
	}
	
	public int getVersion (){
		return versionNr;
	}
	
	public static void  main(String[] arg){
		JFrame f = new JFrame();
		f.setContentPane(new TreeFrame(0,null));
		f.pack();
	}
}
