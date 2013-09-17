package de.uni_leipzig.imise.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Section {

	private String sectionLabel;
	
	private int position;
	
	private Map<String,Object> properties;

	
	public Section(){
		this.properties = new HashMap<String,Object>();
	}
	public void setSectionLabel(String stringCellValue) {
		this.sectionLabel =stringCellValue;
		
	}
	
	public void addProperty (String key,Object value){
		this.properties.put(key, value);
	}
	
	public Map<String,Object> getProperties(){
		return this.properties;
	}
	
	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getSectionLabel() {
		return sectionLabel;
	}
	
	public String toString (){
		return this.sectionLabel +": "+properties.toString();
		
	}
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((sectionLabel == null) ? 0 : sectionLabel.hashCode());
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
		Section other = (Section) obj;
		if (sectionLabel == null) {
			if (other.sectionLabel != null)
				return false;
		} else if (!sectionLabel.equals(other.sectionLabel))
			return false;
		return true;
	}
	public int compareTo(Object i2){
		Integer i1= this.position;
		Integer pos2 =((Section)i2).position;
		return i1.compareTo(pos2);
	}
	public float match(Section newSec) {
		
		return levensthein(this.sectionLabel,newSec.sectionLabel);
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
	public List<String> diff(Section s2) {
		List<String> diffList = new ArrayList<String>();
		for (Entry<String,Object> e:this.properties.entrySet()){
			Object o2 = s2.getProperties().get(e.getKey());
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
	
}
