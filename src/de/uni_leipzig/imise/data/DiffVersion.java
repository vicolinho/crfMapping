package de.uni_leipzig.imise.data;

import java.util.HashMap;
import java.util.List;

/**
 * This class holds the changed/unchanged objects of two versions
 * @author loco
 *
 */

public class DiffVersion {

	/**
	 * unchanged items
	 */
	private List<Item> equalItems;
	
	/**
	 * added items
	 */
	private List<Item> addedItems;
	
	/**
	 * deleted item
	 */
	private List<Item> deletedItems;
	
	/**
	 * map, which holds the mappings of modified items.
	 * It maps a old item to a new item
	 */
	private HashMap<Item,Item> oldNewItemMap;
	
	/**
	 * map, which holds the mappings of modified items.
	 * It maps a new item to a old item
	 */
	private HashMap<Item,Item> newOldItemMap;
	
	
	/**
	 * This map save the relation between the modified items and the changed properties.
	 * The key of the map are the old item labels and the value is a map, which contains the property name
	 * as key and the {@code PropertyMapping} as value.
	 */
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
