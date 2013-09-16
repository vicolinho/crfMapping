package de.uni_leipzig.imise.visualization.view;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import de.uni_leipzig.imise.data.DiffVersion;
import de.uni_leipzig.imise.data.PropertyMapping;
import de.uni_leipzig.imise.data.VersionPair;
import de.uni_leipzig.imise.data.managment.DiffVersionManager;
import de.uni_leipzig.imise.data.managment.VersionManager;
import de.uni_leipzig.imise.visualization.controller.DiffTreeController;

public class PropertyDiffPanel extends JPanel implements PropertyChangeListener {

	
	private DiffTable table;
	
	private CRFTableModel propertyModel; 
	private VersionManager vm ;
	private DiffVersionManager dvm;
	public PropertyDiffPanel(){
		
		super(new BorderLayout());
		this.vm = VersionManager.getInstance();
		vm.addPropertyChangeListener(this);
		this.dvm = DiffVersionManager.getInstance();
		this.initGui();
		
	}
	private void  initGui(){
		propertyModel = new CRFTableModel(CellConstants.PROPERTY_COL,CellConstants.OLD_VALUE,
				CellConstants.NEW_VALUE,CellConstants.RELEVANCE);
		table = new DiffTable(propertyModel);
		JScrollPane view = new JScrollPane(table);
		this.add(view,BorderLayout.CENTER);
		
	}
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(EventConstants.SELECT_DIFF_ITEM)){
			propertyModel.clear();
			HashMap<String,String> map =(HashMap<String, String>) evt.getNewValue();
			if (map.size()>0){
				int v1 = Integer.parseInt(map.get(CellConstants.VERSION_COL));
				Integer v2 = vm.getVersions().higherKey(v1);
				if (v2 != null){
					VersionPair vp = new VersionPair(v1,v2);
					DiffVersion dv = dvm.getDiffVersionMap().get(vp);
					HashMap<String, PropertyMapping> propMap=
					  dv.getModifiedItems().get(map.get(CellConstants.ITEM_COL));
					
					for (Entry<String,PropertyMapping> e: propMap.entrySet()){
						int r = propertyModel.addRow();
						propertyModel.setValueAt(e.getKey(), r, CellConstants.PROPERTY_COL);
						propertyModel.setValueAt(e.getValue().getOldValue(), r, CellConstants.OLD_VALUE);
						propertyModel.setValueAt(e.getValue().getNewValue(), r, CellConstants.NEW_VALUE);
					}//for each property map
				} //higher key is not null
			}//map size >0
		}else if (evt.getPropertyName().equals(VersionManager.CLEAR_VERSIONS)){
			this.propertyModel.clear();
		}
		
	}
}
