package de.uni_leipzig.imise.visualization.view;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import org.apache.log4j.Logger;

import de.uni_leipzig.imise.data.CRFVersion;
import de.uni_leipzig.imise.data.managment.VersionManager;
import de.uni_leipzig.imise.io.VersionReader;


public final class CRFMenuBar extends JMenuBar {
	
	
	private VersionReader vr ;
	private VersionManager vm ;
	static final Logger log = Logger.getLogger(CRFMenuBar.class);
	public CRFMenuBar(){
		vm = VersionManager.getInstance();
		vr = new VersionReader();
		JMenu mnNewMenu = new JMenu("File");
		mnNewMenu.setSize(80, 20);
		JMenuItem loadItem = new JMenuItem("Open");
		loadItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
				List<CRFVersion> versions = CRFMenuBar.this.readFile();
				if (versions.size()!=0){
					vm.clear();
					vm.addVersions(versions);
				}
			}
			
		});
		mnNewMenu.add(loadItem);
		add(mnNewMenu);
	}
	
	
	private List<CRFVersion> readFile(){
		
		JFileChooser.setDefaultLocale(Locale.ENGLISH);
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(true);
		
		fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
		fileChooser.setDialogTitle("Load CRF Version");
		int state = fileChooser.showDialog(null, "load");
		List<CRFVersion> versions = new ArrayList<CRFVersion>();
		if (state == JFileChooser.APPROVE_OPTION){
			fileChooser.getSelectedFile();
			File[] files = fileChooser.getSelectedFiles();
			for (File f : files){
				try {
					CRFVersion v = vr.readVersion(f.getAbsolutePath());
					versions.add(v);
				} catch (IOException e) {
					JOptionPane.showConfirmDialog(null, "Error loading the CRF version:"+ f.getName(),
							"load error", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
			}
		}
		return versions;
	}
	
	public static void main (String[] args){
		JFrame frame = new JFrame();
		frame.getContentPane().add(new CRFMenuBar());
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}