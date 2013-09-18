package de.uni_leipzig.imise.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uni_leipzig.imise.data.constants.ItemConstants;
import de.uni_leipzig.imise.diff.calculation.Measures;

public class Item implements Comparable{

	private int position;
	
	private String itemLabel;
	
	private Map<String,Object> properties;
	
	
	public Item(){
		properties = new HashMap<String,Object>();
	}
	
	public void addProperty (String key,Object value){
		this.properties.put(key, value);
	}
	
	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getItemLabel() {
		return itemLabel;
	}

	public void setItemLabel(String itemLabel) {
		this.itemLabel = itemLabel;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}
	
	public String toString(){
		return this.itemLabel+":" +System.getProperty("line.separator")+
				properties.toString();
	}
	
	
	public  List<String> diff(Item i2){
	
			List<String> diffList = new ArrayList<String>();
			for (Entry<String,Object> e:this.properties.entrySet()){
				Object o2 = i2.getProperties().get(e.getKey());
				Object o =e.getValue();
				if (o instanceof Double){
					if (o!= o2)
						diffList.add(e.getKey());
				}else if (o instanceof String){
					if (!o.equals(o2))
						diffList.add(e.getKey());
				}
				
			}
		return diffList;
	}
	
	public float match(Item i2){
		String descr1 = (String) this.properties.get(ItemConstants.DESCRIPTION_LABEL);
		String descr2 = (String) i2.properties.get(ItemConstants.DESCRIPTION_LABEL);
		float nameSim = Measures.levenshtein(this.itemLabel, i2.itemLabel);
		float descrSim= Measures.levenshtein(descr1, descr2);
		return (nameSim +descrSim)/2f;
	}
	
	
	
	
	
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((itemLabel == null) ? 0 : itemLabel.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		if (itemLabel == null) {
			if (other.itemLabel != null)
				return false;
		} else if (!itemLabel.equals(other.itemLabel))
			return false;
		return true;
	}

	public int compareTo(Object i2){
		Integer i1= this.position;
		Integer pos2 =((Item)i2).position;
		return i1.compareTo(pos2);
	}
	public static void main (String[] arg){
		Item i = new Item();
		System.out.print(Measures.levenshtein("ME_CRP_U", "ME_CRP_U_2"));
	}


}

