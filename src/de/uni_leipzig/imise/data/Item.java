package de.uni_leipzig.imise.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uni_leipzig.imise.data.constants.ItemConstants;

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
	
	public boolean equals (Item i2){
		return this.itemLabel.equals(i2.itemLabel);
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
		float nameSim = this.levensthein(this.itemLabel, i2.itemLabel);
		float descrSim= this.levensthein(descr1, descr2);
		return (nameSim +descrSim)/2f;
	}
	
	
	
	
	private float levensthein (String s1,String s2){
		int matrix[][] = new int[s1.length() + 1][s2.length() + 1];
	    for (int i = 0; i < s1.length() + 1; i++) {
	      matrix[i][0] = i;
	    }
	    for (int i = 0; i < s2.length() + 1; i++) {
	      matrix[0][i] = i;
	    }
	    for (int a = 1; a < s1.length() + 1; a++) {
	      for (int b = 1; b < s2.length() + 1; b++) {
	        int right = 0;
	        if (s1.charAt(a - 1) != s2.charAt(b - 1)) {
	          right = 1;
	        }
	        int mini = matrix[a - 1][b] + 1;
	        if (matrix[a][b - 1] + 1 < mini) {
	          mini = matrix[a][b - 1] + 1;
	        }
	        if (matrix[a - 1][b - 1] + right < mini) {
	          mini = matrix[a - 1][b - 1] + right;
	        }
	        matrix[a][b] = mini;
	        
	      }
	    }
	    return 1-(float)(matrix[s1.length()][s2.length()])/(float)Math.max(s1.length(), s2.length());
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
		System.out.print(i.levensthein("ME_CRP_U", "ME_CRP_U_2"));
	}


}

