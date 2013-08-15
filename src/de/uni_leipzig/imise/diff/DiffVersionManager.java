package de.uni_leipzig.imise.diff;

import java.util.HashMap;
import java.util.HashSet;

import de.uni_leipzig.imise.data.Item;
import de.uni_leipzig.imise.data.ItemMapping;
import de.uni_leipzig.imise.data.PropertyMapping;

public class DiffVersionManager {

	
	public HashSet<Item> getEqualItems(int start,int end){
		return null;
		
	}
	
	public HashSet<Item> getAddedItems (int start, int end){
		return null;
	}
	
	public HashSet <Item> getDeletedItems (int start, int end){
		return null;
		
	}
	public HashMap<ItemMapping,HashMap<String,PropertyMapping>> getModifiedItems(int start,int end){
		return null;
	}
}
