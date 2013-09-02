package de.uni_leipzig.imise.visualization.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import de.uni_leipzig.imise.data.managment.VersionManager;
import de.uni_leipzig.imise.visualization.view.TreeFrame;
import de.uni_leipzig.imise.visualization.view.VersionPanel;

public class VersionController implements ActionListener, InternalFrameListener {

	
	private VersionManager vm ;
	
	private VersionPanel vp ;
	
	public VersionController(VersionManager vm,VersionPanel vp){
		this.vm = vm;
		this.vp = vp;
	}
	
	public void release (){
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(VersionPanel.ADD_VERSION_TREE)){
			Integer verNr = vp.getSelectedVersion();
			if (verNr !=null){
				vp.addVersionTree(verNr);
			}
		}
	}

	@Override
	public void internalFrameOpened(InternalFrameEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void internalFrameClosing(InternalFrameEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void internalFrameClosed(InternalFrameEvent e) {
		TreeFrame f= (TreeFrame) e.getInternalFrame();
		vp.removeTreeFrame(f.getVersion());
		vp.remove(e.getInternalFrame());
		
	}

	@Override
	public void internalFrameIconified(InternalFrameEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void internalFrameDeiconified(InternalFrameEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void internalFrameActivated(InternalFrameEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void internalFrameDeactivated(InternalFrameEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
