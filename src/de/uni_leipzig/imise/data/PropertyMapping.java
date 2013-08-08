package de.uni_leipzig.imise.data;

public class PropertyMapping {

	public PropertyMapping(Object o, Object o2) {
		this.setOldValue(o);
		this.setNewValue(o2);
	}

	private Object oldValue;
	
	private Object newValue;
	
	
	
	/**
	 * @return the oldValue
	 */
	public Object getOldValue() {
		return oldValue;
	}

	/**
	 * @param oldValue the oldValue to set
	 */
	public void setOldValue(Object oldValue) {
		this.oldValue = oldValue;
	}

	/**
	 * @return the newValue
	 */
	public Object getNewValue() {
		return newValue;
	}

	/**
	 * @param newValue the newValue to set
	 */
	public void setNewValue(Object newValue) {
		this.newValue = newValue;
	}

	
	

	public String toString(){
		return System.getProperty("line.separator")+"old:"+oldValue+System.getProperty("line.separator")+"new:"+newValue+System.getProperty("line.separator");
	}
	
}
