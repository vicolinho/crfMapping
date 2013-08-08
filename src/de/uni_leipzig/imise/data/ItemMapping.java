package de.uni_leipzig.imise.data;

import java.io.Serializable;

public class ItemMapping implements Serializable{
	
	private Item oldItem;
	

	private Item newItem;
	
	
	public ItemMapping(Item oldItem, Item newItem) {
		this.oldItem = oldItem;
		this.newItem = newItem;

	}
	public Item getOldItem() {
		return oldItem;
	}
	public void setOldItem(Item oldItem) {
		this.oldItem = oldItem;
	}
	public Item getNewItem() {
		return newItem;
	}
	public void setNewItem(Item newItem) {
		this.newItem = newItem;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((newItem == null) ? 0 : newItem.hashCode());
		result = prime * result + ((oldItem == null) ? 0 : oldItem.hashCode());
		return result;
	}
	
	public String toString(){
		return this.oldItem.getItemLabel()+"==>"+this.newItem.getItemLabel();
	}
}
