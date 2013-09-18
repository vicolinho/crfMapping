package de.uni_leipzig.imise.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uni_leipzig.imise.diff.calculation.Measures;

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
		
		return Measures.levenshtein(this.sectionLabel,newSec.sectionLabel);
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
