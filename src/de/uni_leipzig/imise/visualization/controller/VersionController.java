package de.uni_leipzig.imise.visualization.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreePath;

import de.uni_leipzig.imise.data.managment.VersionManager;
import de.uni_leipzig.imise.visualization.view.CellConstants;
import de.uni_leipzig.imise.visualization.view.EventConstants;
import de.uni_leipzig.imise.visualization.view.TreeFrame;
import de.uni_leipzig.imise.visualization.view.VersionPanel;

public class VersionController implements ActionListener, InternalFrameListener, TreeSelectionListener {

	
	private VersionManager vm ;
	private PropertyChangeSupport change;
	private VersionPanel vp ;
	private int selectedVersion;
	private HashMap<String,String> selectItemMap;
	
	public VersionController(VersionManager vm,VersionPanel vp){
		this.vm = vm;
		this.vp = vp;
		this.change = new PropertyChangeSupport(this);
		this.selectItemMap = new HashMap<String,String>();
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
		selectedVersion =((TreeFrame)e.getInternalFrame()).getVersion();
		
	}

	@Override
	public void internalFrameDeactivated(InternalFrameEvent e) {
		// TODO Auto-generated method stub
		
	}

	
	
	@Override
	public void valueChanged(TreeSelectionEvent e) {
		TreePath path = e.getNewLeadSelectionPath();
		if (path!=null){
			DefaultMutableTreeNode o = (DefaultMutableTreeNode) path.getPathComponent(0);
			
			if (path.getPathCount()==2){
				String itemLabel = path.getLastPathComponent().toString();
				HashMap<String,String> oldMap = new HashMap<String,String>();
				for (Entry <String,String>entry :this.selectItemMap.entrySet()){
					oldMap.put(entry.getKey(), entry.getValue());
				}
				selectItemMap.clear();
				String version =selectedVersion+"";
				this.selectItemMap.put(CellConstants.ITEM_COL, path.getLastPathComponent().toString());
				this.selectItemMap.put(CellConstants.VERSION_COL, version.toString());
				
				change.firePropertyChange(EventConstants.SELECT_ITEM,oldMap, selectItemMap);
			}
		}
		
	}
	
	public void addPropertyChangeListener (PropertyChangeListener l){
		change.addPropertyChangeListener(l);
	}
	
	public void removePropertyChangeListener (PropertyChangeListener l){
		change.removePropertyChangeListener(l);
	}
	
}
