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
import de.uni_leipzig.imise.data.Section;
import de.uni_leipzig.imise.data.SectionMapping;
import de.uni_leipzig.imise.io.VersionReader;

public class DiffCalculator {

	private static final Logger log = Logger.getLogger(DiffCalculator.class.getName());
	private List<Item> addedItems;
	
	private List<Item> deletedItems;
	
	private List <Item> equalItems;
	
	
	private List <Section> addedSections;
	
	private List <Section> deletedSections;
	
	private List <Section> equalSections;
	
	
	private HashMap<Section,Section> oldNewSectionMap;
	
	private HashMap<Section,Section> newOldSectionMap;
	
	private  HashMap<String, HashMap<String, PropertyMapping>> modifiedSections;
	
	
	private HashMap<Item,Item> oldNewItemMap;
	
	private HashMap<Item,Item> newOldItemMap;
	
	/**
	 * map of modified old item as key and the property mapping
	 */
	private HashMap<String, HashMap<String, PropertyMapping>> modifiedItems;
	
	
	
	
	
	public DiffCalculator(){
		
	}
	public void calculateDiff(CRFVersion v1, CRFVersion v2){
		this.addedItems = new ArrayList<Item>();
		this.deletedItems = new ArrayList<Item>();
		this.equalItems = new ArrayList<Item>();
		oldNewItemMap =new HashMap<Item,Item>();
		newOldItemMap = new HashMap<Item,Item>();
		modifiedItems= new HashMap<String,HashMap<String,PropertyMapping>>();
		//this.calcDiffSection(v1, v2);
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
				this.oldNewItemMap.put(oldItem, im.getNewItem());
				this.newOldItemMap.put(im.getNewItem(), oldItem);
				this.modifiedItems.put(oldItem.getItemLabel(), itemModifiedMap);
				
			}
		}//each old item
		
		this.deletedItems.removeAll(oldItems);
		this.addedItems.removeAll(newItems);
	}
	
	
	
	
	private void findEqualItems(CRFVersion v1, CRFVersion v2){
		//log.info(set1.size()+"");
		for (Entry <String,Item>e1 : v1.getItems().entrySet()){
			for (Entry <String,Item> e2: v2.getItems().entrySet()){
				Item i1 = e1.getValue();
				Item i2 = e2.getValue();
				if (i1.getProperties().equals(i2.getProperties())&&
						i1.getItemLabel().equals(i2.getItemLabel())){
					equalItems.add(i2);
				}
			}
		}
		
		//log.info(set1.size() +" "+ equalSet.size()+" "+set2.size());
	
		for (Item i1 : v1.getItems().values()){
			if (!equalItems.contains(i1)){
				deletedItems.add(i1);
			}
		}
		for (Item i2: v2.getItems().values()){
			if (!equalItems.contains(i2)){
				addedItems.add(i2);
			}
		}
		
	}
	
	private void calcDiffSection(CRFVersion v1, CRFVersion v2){
		this.addedSections = new ArrayList<Section>();
		this.deletedSections = new ArrayList<Section>();
		
		this.equalSections = new ArrayList<Section>();
		oldNewSectionMap =new HashMap<Section,Section>();
		newOldSectionMap = new HashMap<Section,Section>();
		modifiedSections= new HashMap<String,HashMap<String,PropertyMapping>>();
		List<Section> oldSections = new ArrayList<Section>();
		List<Section> newSections = new ArrayList<Section>();
		this.findEqualSections(v1,v2);
		for (Section oldSec : deletedSections){
			TreeMap<Float,SectionMapping> topMatches = new TreeMap<Float,SectionMapping>();
			for (Section newSec: addedSections){
				float sim = oldSec.match(newSec);
				if (sim>0.6f){// potential modified mapping
					SectionMapping sm = new SectionMapping(oldSec,newSec);
					topMatches.put(sim, sm);	
				}
			}//each new item
			if (!topMatches.isEmpty()){
				SectionMapping sm = topMatches.lastEntry().getValue();
				//choose the best mapping
				List<String> diffProperties=null;
				diffProperties = oldSec.diff(sm.getNewSec());
				HashMap<String, PropertyMapping> sectionModifiedMap = new HashMap<String,PropertyMapping>();
				//generate the property mapping with old and new value
				for (String property : diffProperties){
					PropertyMapping m = new PropertyMapping(oldSec.getProperties().get(property),
							sm.getNewSec().getProperties().get(property));
					sectionModifiedMap.put(property, m);
				}
				//delete the items from the set of added items and deleted items
				oldSections.add(sm.getOldSec());newSections.add(sm.getNewSec());
				this.oldNewSectionMap.put(oldSec, sm.getNewSec());
				this.newOldSectionMap.put(sm.getNewSec(), oldSec);
				this.modifiedSections.put(oldSec.getSectionLabel(), sectionModifiedMap);
			}
		}//each old item
		
		this.deletedSections.removeAll(oldSections);
		this.addedSections.removeAll(newSections);
		
		
		
	}
	
	private void findEqualSections(CRFVersion v1, CRFVersion v2){
		for (Entry<String,Section> e1 : v1.getSections().entrySet()){
			for (Entry<String,Section> e2: v2.getSections().entrySet()){
				Section s1 = e1.getValue();
				Section s2 = e2.getValue();
				if (s1.getProperties().equals(s2.getProperties())){
					this.equalSections.add(s2);
				}
			}
		}
		
		for (Section s1: v1.getSections().values()){
			if (!equalSections.contains(s1)){
				deletedSections.add(s1);
			}
		}
		for (Section s2 : v2.getSections().values()){
			if (!equalSections.contains(s2)){
				addedSections.add(s2);
			}
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
	public HashMap<String, HashMap<String, PropertyMapping>> getModifiedItems() {
		return modifiedItems;
	}
	public void setModifiedItems(HashMap<String, HashMap<String, PropertyMapping>> modifiedItems) {
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
	
	
	/**
	 * @return the addedSections
	 */
	public List <Section> getAddedSections() {
		return addedSections;
	}
	/**
	 * @param addedSections the addedSections to set
	 */
	public void setAddedSections(List <Section> addedSections) {
		this.addedSections = addedSections;
	}
	/**
	 * @return the deletedSections
	 */
	public List <Section> getDeletedSections() {
		return deletedSections;
	}
	/**
	 * @param deletedSections the deletedSections to set
	 */
	public void setDeletedSections(List <Section> deletedSections) {
		this.deletedSections = deletedSections;
	}
	/**
	 * @return the equalSections
	 */
	public List <Section> getEqualSections() {
		return equalSections;
	}
	/**
	 * @param equalSections the equalSections to set
	 */
	public void setEqualSections(List <Section> equalSections) {
		this.equalSections = equalSections;
	}
	/**
	 * @return the oldNewSectionMap
	 */
	public HashMap<Section,Section> getOldNewSectionMap() {
		return oldNewSectionMap;
	}
	/**
	 * @param oldNewSectionMap the oldNewSectionMap to set
	 */
	public void setOldNewSectionMap(HashMap<Section,Section> oldNewSectionMap) {
		this.oldNewSectionMap = oldNewSectionMap;
	}
	/**
	 * @return the newOldSectionMap
	 */
	public HashMap<Section,Section> getNewOldSectionMap() {
		return newOldSectionMap;
	}
	/**
	 * @param newOldSectionMap the newOldSectionMap to set
	 */
	public void setNewOldSectionMap(HashMap<Section,Section> newOldSectionMap) {
		this.newOldSectionMap = newOldSectionMap;
	}
	/**
	 * @return the modifiedSections
	 */
	public HashMap<String, HashMap<String, PropertyMapping>> getModifiedSections() {
		return modifiedSections;
	}
	/**
	 * @param modifiedSections the modifiedSections to set
	 */
	public void setModifiedSections(HashMap<String, HashMap<String, PropertyMapping>> modifiedSections) {
		this.modifiedSections = modifiedSections;
	}
	public void clearAll(){
		this.addedItems.clear();
		this.deletedItems.clear();
		this.equalItems.clear();
		this.modifiedItems.clear();
		oldNewItemMap.clear();
		newOldItemMap.clear();
		this.addedSections.clear();
		this.deletedSections.clear();
		this.equalSections.clear();
		this.modifiedSections.clear();
		oldNewSectionMap.clear();
		newOldSectionMap.clear();
		
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
			
			for (Entry<String,HashMap<String,PropertyMapping>>e :dc.getModifiedItems().entrySet()){
				if (isLogging){
					fw.append(e.getKey()+"-"+dc.getOldNewItemMap().get(v1.getItems().get(e.getKey())).getItemLabel()
					+System.getProperty("line.separator")+"properties:"+
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
