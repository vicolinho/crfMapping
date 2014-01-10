package de.uni_leipzig.imise.data.managment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.log4j.Logger;


import de.uni_leipzig.imise.data.CRFVersion;
import de.uni_leipzig.imise.data.DiffVersion;
import de.uni_leipzig.imise.data.Item;
import de.uni_leipzig.imise.data.PropertyMapping;
import de.uni_leipzig.imise.data.VersionPair;


public class DiffVersionManager {
	private static final Logger log = Logger.getLogger(DiffVersionManager.class);
	
	/**
	 * holds the Diff for two versions
	 */
	private TreeMap<VersionPair,DiffVersion> versionDiffMap;
	/**
	 * save the previous item for each item
	 */
	private HashMap<String,List<String>> changeGraph;
	private static DiffVersionManager instance;
	
	/**
	 * 
	 */
	private HashMap<String,List<String>> categoryItemMap;
	private DiffVersionManager (){
		versionDiffMap = new TreeMap<VersionPair,DiffVersion>();
		changeGraph = new HashMap<String,List<String>>();
		this.categoryItemMap = new HashMap<String,List<String>>();
	}
	
	
	public void addDiff(VersionPair p, DiffVersion df){
		this.versionDiffMap.put(p, df);
	}
	
	
	public TreeMap<VersionPair,DiffVersion> getDiffVersionMap(){
		return this.versionDiffMap;
	}
	//TODO Check that the manager is clear after loadings
	public void clearAll(){
		this.versionDiffMap.clear();
		this.changeGraph.clear();
		this.categoryItemMap.clear();
	}
	
	/**
	 * retrieve for the current version v for each item the version pair, where it changed.
	 * @param lastVersion
	 * @return
	 */
	public HashMap<Item,List<VersionPair>> getDiffVersionsPerItem(CRFVersion firstVersion,CRFVersion lastVersion){
		HashMap<Item,List<VersionPair>> itemVersionMap = new HashMap<Item,List<VersionPair>>();
		changeGraph.clear();
		this.categoryItemMap.clear();
		VersionPair startKey = null ;
		VersionPair endKey =null;
		for (VersionPair vp : this.versionDiffMap.keySet()){
			if (vp.getNewVersion()==lastVersion.getVersion()){
				startKey = vp;
				break;
			}
		}
		for (VersionPair vp : this.versionDiffMap.keySet()){
			if (vp.getOldVersion() == firstVersion.getVersion()){
				endKey = vp;
				break;
			}
		}
		VersionPair nextKey = startKey;
		boolean isFirst;
		for (Entry<String,Item> e :lastVersion.getItems().entrySet()){
			Item currentItem = e.getValue();
			DiffVersion dv;
			nextKey = startKey;
			isFirst =true;
			do{
				if (!isFirst){
					nextKey = this.versionDiffMap.lowerKey(nextKey); //next previous version comparison
				}else isFirst=false;
				dv = this.versionDiffMap.get(nextKey);
				if (dv.getNewOldItemMap().containsKey(currentItem)){
					List<VersionPair> list = itemVersionMap.get(e.getValue());
					List<String> itemList = changeGraph.get(e.getValue().getItemLabel());
					
					
					if (list==null){
						list =new ArrayList<VersionPair>();
						itemVersionMap.put(e.getValue(), list);
					}
					if (itemList==null){
						itemList = new ArrayList<String>();
						changeGraph.put(e.getValue().getItemLabel(), itemList);
					}
					
					
					
					list.add(nextKey);
					itemList.add(dv.getNewOldItemMap().get(currentItem).getItemLabel());
					
					String oldItemLabel = dv.getNewOldItemMap().get(currentItem).getItemLabel();
					HashMap<String,PropertyMapping> propMap = dv.getModifiedItems().get(oldItemLabel);
					for (PropertyMapping pm: propMap.values()){
						List<String> itemCatList = categoryItemMap.get(pm.getRelevance());
						if (itemCatList ==null){
							itemCatList = new ArrayList<String>();
							categoryItemMap.put(pm.getRelevance(), itemCatList);
						}
							itemCatList.add(e.getValue().getItemLabel());
					}
					currentItem = dv.getNewOldItemMap().get(currentItem);	
				}//is modified item 
				
				if (dv.getAddedItems().contains(currentItem)){
					List<VersionPair> list = itemVersionMap.get(e.getValue());
					List<String> itemList = changeGraph.get(e.getValue().getItemLabel());
					if (list==null){
						list =new ArrayList<VersionPair>();
						itemVersionMap.put(e.getValue(), list);
					}
					if (itemList==null){
						itemList = new ArrayList<String>();
						changeGraph.put(e.getValue().getItemLabel(), itemList);
					}
					
					list.add(nextKey);
					itemList.add(currentItem.getItemLabel());
					break;
				} //is added item
				
				
			}while (!nextKey.equals(endKey));
		}//each item of the chosen version
		log.info("calculate item Map for visualization");
		return itemVersionMap;
	}
	
	public HashMap<String,List<String>> getChangeGraph(){
		return this.changeGraph;
	}
	
	public HashMap<String, List<String>> getCategoryItemMap() {
		return categoryItemMap;
	}


	public void setCategoryItemMap(HashMap<String, List<String>> categoryItemMap) {
		this.categoryItemMap = categoryItemMap;
	}


	public boolean isEmpty(){
		return this.versionDiffMap.isEmpty();
	}
	public static DiffVersionManager getInstance(){
		if (instance ==null){
			instance = new DiffVersionManager();
		}
		return instance;
	}
}
