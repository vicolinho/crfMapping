package de.uni_leipzig.imise.visualization.view;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

import de.uni_leipzig.imise.data.CRFVersion;
import de.uni_leipzig.imise.data.Item;
import de.uni_leipzig.imise.data.constants.ItemConstants;
import de.uni_leipzig.imise.data.managment.VersionManager;

public class ItemPanel extends JPanel implements PropertyChangeListener{

	
	private static final Logger log = Logger.getLogger(ItemPanel.class.getName());
	private CRFTableModel itemTableModel ;
	
	private JTable table;
	
	private VersionManager vm ;

	private TitledBorder tb; 
	
	public ItemPanel(){
		super(new BorderLayout());
		itemTableModel = new CRFTableModel(CellConstants.PROPERTY_COL,CellConstants.VALUE);
		table = new JTable(itemTableModel);
		tb = new TitledBorder("item");
		this.setBorder(tb);
		JScrollPane pane =new JScrollPane(table);
		this.add(pane,BorderLayout.CENTER);
		vm = VersionManager.getInstance();
		vm.addPropertyChangeListener(this);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(EventConstants.SELECT_ITEM)){
			itemTableModel.clear();
			HashMap<String,String> map =(HashMap<String, String>) evt.getNewValue();
			int verNr = Integer.parseInt(map.get(CellConstants.VERSION_COL));
			CRFVersion v = vm.getVersions().get(verNr);
			Item i = v.getItems().get(map.get(CellConstants.ITEM_COL));
			tb.setTitle(i.getItemLabel());
			
			for (String prop : ItemConstants.PROPERTY_ARRAY){
				Object obj = i.getProperties().get(prop);
				int r = itemTableModel.addRow();
				itemTableModel.setValueAt(prop, r, CellConstants.PROPERTY_COL);
				itemTableModel.setValueAt(obj, r, CellConstants.VALUE);
			}
			this.updateUI();
			
			
		}else if (evt.getPropertyName().equals(VersionManager.CLEAR_VERSIONS)){
			log.info("release item table");
			itemTableModel.clear();
		}
		
	}

	
}
