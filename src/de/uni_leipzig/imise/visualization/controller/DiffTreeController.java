package de.uni_leipzig.imise.visualization.controller;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import de.uni_leipzig.imise.visualization.view.CellConstants;
import de.uni_leipzig.imise.visualization.view.EventConstants;
import de.uni_leipzig.imise.visualization.view.CRFTableModel;

public class DiffTreeController implements TreeSelectionListener,ListSelectionListener{

	
	private PropertyChangeSupport change;
	private JTable t;
	private HashMap<String,String> selectDiffMapping;
	private HashMap<String,String> selectItemMap;
	/**
	 * version number of the CRF, which is the basic of the tree
	 */
	private Integer version;
	
	public DiffTreeController(){
		this.change = new PropertyChangeSupport(this);
		selectDiffMapping = new HashMap<String,String>();
		selectItemMap = new HashMap<String,String>();
	}
	
	public void setT(JTable t) {
		this.t = t;
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		TreePath path = e.getNewLeadSelectionPath();
		if (path!=null)
		if (path.getPathCount()==2){
			HashMap<String,String> oldMap = new HashMap<String,String>();
			for (Entry <String,String>entry :this.selectItemMap.entrySet()){
				oldMap.put(entry.getKey(), entry.getValue());
			}
			selectItemMap.clear();
			this.selectItemMap.put(CellConstants.ITEM_COL, path.getLastPathComponent().toString());
			this.selectItemMap.put(CellConstants.VERSION_COL, version.toString());
			
			change.firePropertyChange(EventConstants.SELECT_ITEM,oldMap, selectItemMap);
		}
		
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		CRFTableModel dtm = (CRFTableModel) t.getModel();
		if (!t.getSelectionModel().isSelectionEmpty()){
			int index = t.getSelectionModel().getMinSelectionIndex();
			
			HashMap<String,String> oldMap = new HashMap<String,String>();
			for (Entry <String,String>entry :this.selectDiffMapping.entrySet()){
				oldMap.put(entry.getKey(), entry.getValue());
			}
			this.selectDiffMapping.clear();
			if (dtm.getValueAt(index, CellConstants.TYPE_COL).equals(CellConstants.MOD_TYPE)){
				this.selectDiffMapping.put(CellConstants.ITEM_COL, (String) dtm.getValueAt(index, CellConstants.ITEM_COL));
				this.selectDiffMapping.put(CellConstants.VERSION_COL, (String) dtm.getValueAt(index, CellConstants.VERSION_COL).toString());
			}
			change.firePropertyChange(EventConstants.SELECT_DIFF_ITEM,oldMap, selectDiffMapping);
			
		}
	}

	public void addPropertyChangeListener(PropertyChangeListener propPanel) {
		
	change.addPropertyChangeListener(propPanel);
		
	}
	
	public void removePropertyChangeListener(PropertyChangeListener propPanel) {
			change.removePropertyChangeListener(propPanel);
	}

	public void setVersion(Integer version) {
		this.version = version;
		
	}

}
