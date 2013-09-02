package de.uni_leipzig.imise.data;

import java.util.HashMap;
import java.util.List;


public class DiffVersion {

	private List<Item> equalItems;
	
	private List<Item> addedItems;
	
	private List<Item> deletedItems;
	
	private HashMap<Item,Item> oldNewItemMap;
	
	private HashMap<Item,Item> newOldItemMap;
	
	private HashMap<String,HashMap<String,PropertyMapping>> modifiedItems;
	
	
	public List<Item> getEqualItems() {
		return equalItems;
	}

	public void setEqualItems(List<Item> equalItems) {
		this.equalItems = equalItems;
	}

	public List<Item> getAddedItems() {
		return addedItems;
	}

	public void setAddedItems(List<Item> addedItems) {
		this.addedItems = addedItems;
	}

	public List<Item> getDeletedItems() {
		return deletedItems;
	}

	public void setDeletedItems(List<Item> deletedItems) {
		this.deletedItems = deletedItems;
	}

	public HashMap<String, HashMap<String, PropertyMapping>> getModifiedItems() {
		return modifiedItems;
	}

	public void setModifiedItems(
			HashMap<String, HashMap<String, PropertyMapping>> modifiedItems) {
		this.modifiedItems = modifiedItems;
	}

	/**
	 * @return the oldNewItemMap
	 */
	public HashMap<Item,Item> getOldNewItemMap() {
		return oldNewItemMap;
	}

	/**
	 * @param oldNewItemMap the oldNewItemMap to set
	 */
	public void setOldNewItemMap(HashMap<Item,Item> oldNewItemMap) {
		this.oldNewItemMap = oldNewItemMap;
	}

	/**
	 * @return the newOldItemMap
	 */
	public HashMap<Item,Item> getNewOldItemMap() {
		return newOldItemMap;
	}

	/**
	 * @param newOldItemMap the newOldItemMap to set
	 */
	public void setNewOldItemMap(HashMap<Item,Item> newOldItemMap) {
		this.newOldItemMap = newOldItemMap;
	}

	
	
	
	
}
