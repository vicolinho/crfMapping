package de.uni_leipzig.imise.diff.Calculation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import de.uni_leipzig.imise.data.CRFVersion;
import de.uni_leipzig.imise.data.Item;
import de.uni_leipzig.imise.data.ItemMapping;
import de.uni_leipzig.imise.data.PropertyMapping;
import de.uni_leipzig.imise.io.VersionReader;

public class DiffCalculator {

	private static final Logger log = Logger.getLogger(DiffCalculator.class.getName());
	private List<Item> addedItems;
	
	private List<Item> deletedItems;
	
	private List <Item> equalItems;
	
	private HashMap <ItemMapping,HashMap<String,PropertyMapping>> modifiedItems;
	
	
	
	
	
	public DiffCalculator(){
		this.addedItems = new ArrayList<Item>();
		this.deletedItems = new ArrayList<Item>();
		this.equalItems = new ArrayList<Item>();
		 modifiedItems= new HashMap<ItemMapping,HashMap<String,PropertyMapping>>();
	}
	public void calculateDiff(CRFVersion v1, CRFVersion v2){
		this.findEqualItems(v1, v2);
		List <Item> oldItems = new ArrayList<Item>();
		List <Item> newItems = new ArrayList<Item>();
		for (Item oldItem : deletedItems){
			
			List<String> diffProperties=null;
			for (Item newItem: addedItems){
				diffProperties = oldItem.diff(newItem,0.95f);
				if (diffProperties!= null){
					HashMap<String, PropertyMapping> itemModifiedMap = new HashMap<String,PropertyMapping>();
					for (String property : diffProperties){
						PropertyMapping m = new PropertyMapping(oldItem.getProperties().get(property),
								newItem.getProperties().get(property));
						itemModifiedMap.put(property, m);
					}
					oldItems.add(oldItem);newItems.add(newItem);
					
					
					ItemMapping im = new ItemMapping(oldItem,newItem);
					this.modifiedItems.put(im, itemModifiedMap);
					break;
				}
			}//each new item
		}//each old item
		this.deletedItems.removeAll(oldItems);
		this.addedItems.removeAll(newItems);
		this.findSynonymeItems();
		
	}
	
	private void findSynonymeItems(){
		for (Item oldItem :this.deletedItems){
			for (Item newItem : addedItems){
				List<String> diff = oldItem.diffSynonyme(newItem);
				if (diff.size()==0){
					log.warning(oldItem.getItemLabel() +"is equal"+ newItem.getItemLabel());
					
				}
			}
		}
	}
	
	
	private void findEqualItems(CRFVersion v1, CRFVersion v2){
		Set<String> set1 = v1.getItems().keySet();
		log.info(set1.size()+"");
		Set<String> equalSet = new HashSet<String>();
		Collections.addAll(equalSet, v1.getItems().keySet().toArray(new String[]{}));
		Set<String> set2 = v2.getItems().keySet();
		equalSet.retainAll(set2);
		log.info(set1.size() +" "+ equalSet.size()+" "+set2.size());
		for (String oldItem : set1){
			if (!equalSet.contains(oldItem)){
				deletedItems.add(v1.getItems().get(oldItem));
			}
		}
		
		for (String newItem :set2){
			if (!equalSet.contains(newItem)){
				addedItems.add(v2.getItems().get(newItem));
			}
		}
		
		for (String eqItem: equalSet){
			equalItems.add(v1.getItems().get(eqItem));
		}
		
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
	public List<Item> getEqualItems() {
		return equalItems;
	}
	public void setEqualItems(List<Item> equalItems) {
		this.equalItems = equalItems;
	}
	public HashMap<ItemMapping,HashMap<String,PropertyMapping>> getModifiedItems() {
		return modifiedItems;
	}
	public void setModifiedItems(HashMap<ItemMapping, HashMap<String, PropertyMapping>> modifiedItems) {
		this.modifiedItems = modifiedItems;
	}
	public static void main (String[] args){
		VersionReader vr = new VersionReader();
		CRFVersion v1,v2;
		long start = System.currentTimeMillis();
		try {
		v1 = vr.readVersion(args[0]);
		for (int i = 1; i<args.length;i++){
			v2 = vr.readVersion(args[i]);
			DiffCalculator dc = new DiffCalculator ();
			dc.calculateDiff(v1, v2);
			float changeRatio = 1-(float)dc.getEqualItems().size()/(float)v2.getItems().size();
			System.out.println("items of v1: "+v1.getItems().size());
			System.out.println("items of v2: "+v2.getItems().size());
			System.out.println("equal Items: "+dc.getEqualItems().size());
			System.out.println("deleted items "+ dc.getDeletedItems().size() );
			System.out.println("added Items "+dc.getAddedItems().size());
			System.out.println("modified items:"+dc.getModifiedItems().size());
			System.out.println ("modified items:" );
			System.out.println("elapsed time: "+ (System.currentTimeMillis()-start));
			for (Entry<ItemMapping,HashMap<String,PropertyMapping>>e :dc.getModifiedItems().entrySet()){
				System.out.println(e.getKey().toString()+System.getProperty("line.separator")+"properties:"+
			System.getProperty("line.separator")+e.getValue().toString());
			}
			
			v1 =v2;	
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
	}
}
