package de.uni_leipzig.imise.classification;

import java.util.HashMap;
import java.util.Map.Entry;

import de.uni_leipzig.imise.data.PropertyMapping;
import de.uni_leipzig.imise.data.constants.CategoryConstants;
import de.uni_leipzig.imise.data.constants.ItemConstants;

public class ChangeClassifier {
	
	private HashMap<String,String> classMap ;
	public ChangeClassifier(){
		this.classMap =new HashMap<String,String>();
		classMap.put(ItemConstants.COLUMN_NUMBER, CategoryConstants.LAYOUT);
		classMap.put(ItemConstants.DATA_TYPE, CategoryConstants.RES_RANGE);
		classMap.put(ItemConstants.DEFAULT_VALUE, CategoryConstants.RES_RANGE);
		classMap.put(ItemConstants.DESCRIPTION_LABEL, CategoryConstants.SEMANTIC);
		classMap.put(ItemConstants.GROUP_LABEL, CategoryConstants.RELATION);
		classMap.put(ItemConstants.HEADER, CategoryConstants.SEMANTIC);
		classMap.put(ItemConstants.LEFT_ITEM_TEXT, CategoryConstants.SEMANTIC);
		classMap.put(ItemConstants.PAGE_NUMBER, CategoryConstants.LAYOUT);
		classMap.put(ItemConstants.PARENT_ITEM, CategoryConstants.RELATION);
		classMap.put(ItemConstants.PHI, CategoryConstants.SECURITY);
		classMap.put(ItemConstants.QUESTION_NUMBER, CategoryConstants.LAYOUT);
		classMap.put(ItemConstants.REQUIRED, CategoryConstants.OPTION);
		classMap.put(ItemConstants.RESPONSE_LABEL, CategoryConstants.RES_RANGE);
		classMap.put(ItemConstants.RESPONSE_LAYOUT, CategoryConstants.LAYOUT);
		classMap.put(ItemConstants.RESPONSE_OPTIONS_TEXT, CategoryConstants.RES_RANGE);
		classMap.put(ItemConstants.RESPONSE_TYPE, CategoryConstants.RES_RANGE);
	    classMap.put(ItemConstants.RESPONSE_VALUES_OR_CALCULATIONS, CategoryConstants.RES_RANGE);
	    classMap.put(ItemConstants.RIGHT_ITEM_TEXT, CategoryConstants.SEMANTIC);
	    classMap.put(ItemConstants.SECTION_LABEL, CategoryConstants.LAYOUT);
	    classMap.put(ItemConstants.UNITS, CategoryConstants.RES_RANGE);
	    classMap.put(ItemConstants.VALIDATION, CategoryConstants.SECURITY);
	    classMap.put(ItemConstants.SUBHEADER, CategoryConstants.SEMANTIC);
	    classMap.put(ItemConstants.VALIDATION_ERROR_MESSAGE, CategoryConstants.MESSAGE);
	    classMap.put(ItemConstants.WIDTH_DECIMAL, CategoryConstants.LAYOUT);
		
	}
	public  HashMap<String,HashMap<String,PropertyMapping>> classify ( HashMap<String,HashMap<String,PropertyMapping>> propertyMapping){
		for (Entry<String,HashMap<String,PropertyMapping>> e: propertyMapping.entrySet()){
			for (Entry<String,PropertyMapping> e2:e.getValue().entrySet()){
				String cat = classMap.get(e2.getKey());
				
				switch (cat){
				case CategoryConstants.SEMANTIC:
					
					if (e2.getValue().getNewValue().toString().startsWith(
							e2.getValue().getOldValue().toString())){
						cat+=" specification";
					}
					break;
				}
				e2.getValue().setRelevance(cat);
			}
		}
		return propertyMapping;
	}
}
