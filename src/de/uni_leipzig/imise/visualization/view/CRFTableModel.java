package de.uni_leipzig.imise.visualization.view;

import java.util.HashMap;


import javax.swing.table.AbstractTableModel;

import org.apache.log4j.Logger;



public class CRFTableModel extends  AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(CRFTableModel.class);
	
	 private String[] columnNames;
	 private HashMap<String,Integer> revColumnName;
	 private HashMap<Integer,Row> data;
	 
	 
	 public CRFTableModel(){
		 data = new HashMap<Integer,Row>();
	 }
	 
	 public CRFTableModel(String...columns ){
		 data = new HashMap<Integer,Row>();
		 this.setColumns(columns);
	 }
	    public int getColumnCount() {
	        return columnNames.length;
	    }

	    public int getRowCount() {
	        return data.size();
	    }

	    public String getColumnName(int col) {
	        return columnNames[col];
	    }

	    public Object getValueAt(int row, int col) {
	        return data.get(row).get(col);
	    }
	    
	    public Object getValueAt(int row, String col) {
	        return data.get(row).get(revColumnName.get(col));
	    }

	    @SuppressWarnings("unchecked")
		public Class getColumnClass(int c) {
	    	log.info(c);
	    	log.info(this);
	        return getValueAt(0, c).getClass();
	    }
	    
	    public void setValueAt(Object value, int row, int col) {
	        data.get(row).setValue(col, value);
	        fireTableCellUpdated(row, col);
	    }
	    
	    public void setValueAt(Object value, int row, String col) {
	        data.get(row).setValue(col, value);
	        
	        fireTableCellUpdated(row, revColumnName.get(col));
	    }
	    
	    public void setColumns(String...columns){
	    	int i=0;
	    	columnNames = columns;
	    	if (revColumnName==null)
	    	revColumnName= new HashMap<String,Integer>();
	    	else revColumnName.clear();
	    	for(String c :columns){
	    		revColumnName.put(c,i);
	    		i++;
	    	}
	    }
	    
	    public void clear(){
	    	
	    	int lastRow =this.data.size()-1;
	    	this.data.clear();
	    	if (lastRow>=0)
	    		this.fireTableRowsDeleted(0, lastRow);
	    }
	    
	    public void removeRow(int row){
	    	this.data.remove(row);
	    	this.fireTableRowsDeleted(row, row);
	    	int newPos=	row;
	    	int oldPos = row+1; 
	    	Row r= this.data.get(oldPos); 
	    	while (r!=null){
	    	
	    		this.data.remove(oldPos);
	    		this.fireTableRowsDeleted(oldPos, oldPos);
	    		this.data.put(newPos,r);
	    		this.fireTableRowsInserted(newPos, newPos);
	    		oldPos++;
	    		newPos++;
	    		r = this.data.get(oldPos);
	    	}
	    	
	    }
	    
	    
	    public int addRow(){
	    	Row r = new Row(revColumnName);
	    	int row = this.data.size();
	    	this.data.put(row, r);
	    	this.fireTableRowsInserted(row, row);
	    	return row;
	    }
	    
	    public String toString(){
	    	return this.data.toString();
	    }
	    public class Row{

	    	
	    	private HashMap<Integer,Object> rowMap;
	    	private HashMap<String,Integer> nameColMap;
	    	public Row(HashMap<String,Integer> revMap){
	    		this.rowMap = new HashMap<Integer,Object>();
	    		for (Integer col: revMap.values()){
	    			rowMap.put(col, "");
	    		}
	    		this.nameColMap = revMap;
	    	}
	    	
	    	
	    	public Object get(int col) {
	    		// TODO Auto-generated method stub
	    		return this.rowMap.get(col);
	    	}
	    	
	    	public Object get(String col) {
	    		// TODO Auto-generated method stub
	    		return this.rowMap.get(nameColMap.get(col));
	    	}
	    	
	    	public void setValue(int col,Object o){
	    		this.rowMap.put(col, o);
	    	
	    	}
	    	
	    	public void setValue(String col,Object o){
	    		this.rowMap.put(nameColMap.get(col), o);
	    		
	    	}
	    	
	    	public String toString(){
	    		return rowMap.toString();
	    	}
	    }
}


