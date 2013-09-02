package de.uni_leipzig.imise.visualization.view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class DiffTable extends JTable {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DiffTable(){
		super();
	
	}
	
	public DiffTable(DiffTableModel dtm){
		super(dtm);	
		
	}
	
	public DiffTable(DefaultTableModel dtm){
		super(dtm);		
	}
	/*
	public void setModel(DiffTableModel m){
		this.setModel(tableModel);
		DefaultTableColumnModel dcm = new DefaultTableColumnModel();
		for (int c = 0; c<m.getColumnCount();c++){
			TableColumn tc = new TableColumn();
			tc.setHeaderValue(m.getColumnName(c));
			dcm.addColumn(tc);
		}
		this.setColumnModel(dcm);
	}*/
	
	public static void main (String[] args){
		DiffTableModel dtm = new DiffTableModel(CellConstants.VERSION_COL,CellConstants.ITEM_COL,
				CellConstants.MOD_TYPE);
	
		
		DiffTable dt = new DiffTable();
		int r = dtm.addRow();
		dtm.setValueAt(new Integer(1), r, 0);
		dtm.setValueAt("test", r, 1);
		dtm.setValueAt("g", r, 2);
		
		//dt.setFillsViewportHeight(true);
		JFrame f= new JFrame();
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JPanel p = new JPanel();
		p.add(new JScrollPane(dt));
		f.setContentPane(p);
		f.setVisible(true);
		dt.setModel(dtm);
		
	}
}
