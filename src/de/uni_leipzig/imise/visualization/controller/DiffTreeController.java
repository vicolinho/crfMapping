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

import de.uni_leipzig.imise.visualization.view.CellConstants;
import de.uni_leipzig.imise.visualization.view.DiffTableModel;

public class DiffTreeController implements TreeSelectionListener,ListSelectionListener{

	public static final String SELECT_DIFF_ITEM = "selectDiffItem";
	private PropertyChangeSupport change;
	private JTable t;
	private HashMap<String,String> selectMapping;
	
	public DiffTreeController(){
		this.change = new PropertyChangeSupport(this);
		selectMapping = new HashMap<String,String>();
	}
	
	public void setT(JTable t) {
		this.t = t;
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		DiffTableModel dtm = (DiffTableModel) t.getModel();
		if (!t.getSelectionModel().isSelectionEmpty()){
			int index = t.getSelectionModel().getMinSelectionIndex();
			if (dtm.getValueAt(index, CellConstants.TYPE_COL).equals(CellConstants.MOD_TYPE)){
				HashMap<String,String> oldMap = new HashMap<String,String>();
				for (Entry <String,String>entry :this.selectMapping.entrySet()){
					oldMap.put(entry.getKey(), entry.getValue());
				}
				this.selectMapping.clear();
				this.selectMapping.put(CellConstants.ITEM_COL, (String) dtm.getValueAt(index, CellConstants.ITEM_COL));
				this.selectMapping.put(CellConstants.VERSION_COL, (String) dtm.getValueAt(index, CellConstants.VERSION_COL).toString());
				change.firePropertyChange(SELECT_DIFF_ITEM,oldMap, selectMapping);
			}
		}
	}

	public void addPropertyChangeListener(PropertyChangeListener propPanel) {
		
	change.addPropertyChangeListener(propPanel);
		
	}
	
	public void removePropertyChangeListener(PropertyChangeListener propPanel) {
			change.removePropertyChangeListener(propPanel);
	}

}
