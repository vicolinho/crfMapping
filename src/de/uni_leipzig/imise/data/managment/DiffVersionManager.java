package de.uni_leipzig.imise.data.managment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import de.uni_leipzig.imise.data.CRFVersion;
import de.uni_leipzig.imise.data.DiffVersion;
import de.uni_leipzig.imise.data.Item;
import de.uni_leipzig.imise.data.VersionPair;

public class DiffVersionManager {
	private TreeMap<VersionPair,DiffVersion> versionDiffMap;
	private HashMap<String,List<String>> changeGraph;
	private static DiffVersionManager instance;
	
	private DiffVersionManager (){
		versionDiffMap = new TreeMap<VersionPair,DiffVersion>();
		changeGraph = new HashMap<String,List<String>>();
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
	}
	
	public HashMap<Item,List<VersionPair>> getDiffVersionsPerItem(CRFVersion v){
		HashMap<Item,List<VersionPair>> itemVersionMap = new HashMap<Item,List<VersionPair>>();
		changeGraph.clear();
		VersionPair startKey = null ;
		for (VersionPair vp : this.versionDiffMap.keySet()){
			if (vp.getV2()==v.getVersion()){
				startKey = vp;
				break;
			}
		}
		VersionPair nextKey = startKey;
		for (Entry<String,Item> e :v.getItems().entrySet()){
			Item currentItem = e.getValue();
			DiffVersion dv;
			nextKey = startKey;
			
			while (nextKey!=null){
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
					
					currentItem = dv.getNewOldItemMap().get(currentItem);
				}
				
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
				}
				
				nextKey = this.versionDiffMap.lowerKey(nextKey);
				
			}
			
		}
		return itemVersionMap;
	}
	
	public HashMap<String,List<String>> getChangeGraph(){
		return this.changeGraph;
	}
	
	
	public static DiffVersionManager getInstance(){
		if (instance ==null){
			instance = new DiffVersionManager();
		}
		return instance;
	}
}
