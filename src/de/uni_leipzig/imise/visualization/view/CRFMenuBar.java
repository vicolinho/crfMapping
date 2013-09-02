package de.uni_leipzig.imise.visualization.view;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import de.uni_leipzig.imise.data.CRFVersion;
import de.uni_leipzig.imise.data.managment.VersionManager;
import de.uni_leipzig.imise.io.VersionReader;


public final class CRFMenuBar extends JMenuBar {
	
	
	private VersionReader vr ;
	private VersionManager vm ;
	static final Logger log = Logger.getLogger(CRFMenuBar.class.getName());
	public CRFMenuBar(){
		vm = VersionManager.getInstance();
		vr = new VersionReader();
		JMenu mnNewMenu = new JMenu("Datei");
		mnNewMenu.setSize(80, 20);
		JMenuItem loadItem = new JMenuItem("Öffnen");
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
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(true);
		fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
		fileChooser.setDialogTitle("CRF Version laden");
		int state = fileChooser.showDialog(null, "Laden");
		List<CRFVersion> versions = new ArrayList<CRFVersion>();
		if (state == JFileChooser.APPROVE_OPTION){
			fileChooser.getSelectedFile();
			File[] files = fileChooser.getSelectedFiles();
			for (File f : files){
				try {
					CRFVersion v = vr.readVersion(f.getAbsolutePath());
					versions.add(v);
				} catch (IOException e) {
					JOptionPane.showConfirmDialog(null, "Fehler beim Laden der CRF Version:"+ f.getName(),
							"Ladefehler", JOptionPane.ERROR_MESSAGE);
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