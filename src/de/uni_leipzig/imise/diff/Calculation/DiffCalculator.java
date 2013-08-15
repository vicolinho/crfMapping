package de.uni_leipzig.imise.diff.calculation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
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
			TreeMap<Float,ItemMapping> topMatches = new TreeMap<Float,ItemMapping>();
			
			
			for (Item newItem: addedItems){
				float sim = oldItem.match(newItem);
				if (sim>0.6f){// potential modified mapping
					ItemMapping im = new ItemMapping(oldItem,newItem);
					topMatches.put(sim, im);	
				}
			}//each new item
			if (!topMatches.isEmpty()){
				ItemMapping im = topMatches.lastEntry().getValue();
				//choose the best mapping
				List<String> diffProperties=null;
				diffProperties = oldItem.diff(im.getNewItem());
				HashMap<String, PropertyMapping> itemModifiedMap = new HashMap<String,PropertyMapping>();
				//generate the property mapping with old and new value
				for (String property : diffProperties){
					PropertyMapping m = new PropertyMapping(oldItem.getProperties().get(property),
							im.getNewItem().getProperties().get(property));
					itemModifiedMap.put(property, m);
				}
				//delete the items from the set of added items and deleted items
				oldItems.add(im.getOldItem());newItems.add(im.getNewItem());
				this.modifiedItems.put(im, itemModifiedMap);
			}
		}//each old item
		
		this.deletedItems.removeAll(oldItems);
		this.addedItems.removeAll(newItems);
	}
	
	
	
	
	private void findEqualItems(CRFVersion v1, CRFVersion v2){
		Set<String> set1 = v1.getItems().keySet();
		//log.info(set1.size()+"");
		Set<String> equalSet = new HashSet<String>();
		Collections.addAll(equalSet, v1.getItems().keySet().toArray(new String[]{}));
		Set<String> set2 = v2.getItems().keySet();
		equalSet.retainAll(set2);
		//log.info(set1.size() +" "+ equalSet.size()+" "+set2.size());
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
	
	
	public static void main (String[] arg){
		VersionReader vr = new VersionReader();
		CRFVersion v1,v2;
		long start = System.currentTimeMillis();
		OutputStreamWriter fw = null; 
		boolean isLogging = false;
		try {
			Properties p = new Properties();
			p.load(new FileInputStream("config.properties"));
			String[] files = p.getProperty("versions").split(";");
			isLogging =Boolean.parseBoolean(p.getProperty("isLogging"));
			if (isLogging){
			Date d = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd-hh_mm");
			FileOutputStream st =new FileOutputStream("test.log");
			fw = new OutputStreamWriter(st,"UTF-8");
			
			}
		v1 = vr.readVersion(files[0]);
		for (int i = 1; i<files.length;i++){
			v2 = vr.readVersion(files[i]);
			DiffCalculator dc = new DiffCalculator ();
			dc.calculateDiff(v1, v2);
			float changeRatio = 1-(float)dc.getEqualItems().size()/(float)v2.getItems().size();
			System.out.println("compare: "+ files[i-1]+" and "+files[i]);
			System.out.println("items of v1: "+v1.getItems().size());
			System.out.println("items of v2: "+v2.getItems().size());
			System.out.println("equal Items: "+dc.getEqualItems().size());
			System.out.println("deleted items "+ dc.getDeletedItems().size() );
			System.out.println("added Items "+dc.getAddedItems().size());
			System.out.println("modified items:"+dc.getModifiedItems().size());
			System.out.println ("modified items:" );
			System.out.println("elapsed time: "+ (System.currentTimeMillis()-start));
			
			
			if (isLogging){
				
				fw.append(System.getProperty("line.separator")+"compare: "+ files[i-1]+" and "+files[i]+System.getProperty("line.separator"));
				fw.append("items of v1: "+v1.getItems().size()+System.getProperty("line.separator"));
				fw.append("items of v2: "+v2.getItems().size()+System.getProperty("line.separator"));
				fw.append("equal Items: "+dc.getEqualItems().size()+System.getProperty("line.separator"));
				fw.append("deleted items "+ dc.getDeletedItems().size()+System.getProperty("line.separator"));
				fw.append("added Items "+dc.getAddedItems().size()+System.getProperty("line.separator"));
				fw.append("modified items:"+dc.getModifiedItems().size()+System.getProperty("line.separator"));
				
				fw.append("------------------added Items--------------------"+System.getProperty("line.separator"));
				fw.append(dc.getAddedItems().toString()+System.getProperty("line.separator"));
				fw.append("----------------deleted Items--------------------"+System.getProperty("line.separator"));
				fw.append(dc.getDeletedItems().toString()+System.getProperty("line.separator"));
				fw.append("---------------modified Items--------------------"+System.getProperty("line.separator"));
			}
			
			for (Entry<ItemMapping,HashMap<String,PropertyMapping>>e :dc.getModifiedItems().entrySet()){
				System.out.println(e.getKey().toString()+System.getProperty("line.separator")+"properties:"+
			System.getProperty("line.separator")+e.getValue().toString());
				if (isLogging){
					fw.append(e.getKey().toString()+System.getProperty("line.separator")+"properties:"+
			System.getProperty("line.separator")+e.getValue().toString()+System.getProperty("line.separator"));
				}
			}
			
			v1 =v2;	
			}
		if (isLogging){
			fw.close();
		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
	}
}
