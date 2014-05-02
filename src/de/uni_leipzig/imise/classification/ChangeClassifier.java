package de.uni_leipzig.imise.classification;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import de.uni_leipzig.imise.data.PropertyMapping;
import de.uni_leipzig.imise.data.constants.CategoryConstants;
import de.uni_leipzig.imise.data.constants.ItemConstants;
import de.uni_leipzig.imise.diff.calculation.Measures;

public class ChangeClassifier {
	private static final Logger log = Logger.getLogger(ChangeClassifier.class);
	private HashMap<String,String> classMap ;
	private HashSet<String> notSmallEditCategories;
	public static final float SMALL_THRESH = 3;
	public ChangeClassifier(){
		this.classMap =new HashMap<String,String>();
		notSmallEditCategories = new HashSet<String>();
		classMap.put(ItemConstants.COLUMN_NUMBER, CategoryConstants.LAYOUT);
		classMap.put(ItemConstants.DATA_TYPE, CategoryConstants.RESPONSE);
		classMap.put(ItemConstants.DEFAULT_VALUE, CategoryConstants.RESPONSE);
		classMap.put(ItemConstants.RESPONSE_LABEL, CategoryConstants.RESPONSE);
		classMap.put(ItemConstants.RESPONSE_LAYOUT, CategoryConstants.LAYOUT);
		classMap.put(ItemConstants.RESPONSE_OPTIONS_TEXT, CategoryConstants.RESPONSE);
		classMap.put(ItemConstants.RESPONSE_TYPE, CategoryConstants.RESPONSE);
	    classMap.put(ItemConstants.RESPONSE_VALUES_OR_CALCULATIONS, CategoryConstants.RESPONSE);
	    classMap.put(ItemConstants.UNITS, CategoryConstants.RESPONSE);
	    classMap.put(ItemConstants.DESCRIPTION_LABEL, CategoryConstants.SEMANTIC);
		classMap.put(ItemConstants.GROUP_LABEL, CategoryConstants.RELATION);
		classMap.put(ItemConstants.HEADER, CategoryConstants.SEMANTIC);
		classMap.put(ItemConstants.LEFT_ITEM_TEXT, CategoryConstants.SEMANTIC);
		classMap.put(ItemConstants.PAGE_NUMBER, CategoryConstants.LAYOUT);
		classMap.put(ItemConstants.PARENT_ITEM, CategoryConstants.RELATION);
		classMap.put(ItemConstants.PHI, CategoryConstants.VALIDATION);
		classMap.put(ItemConstants.QUESTION_NUMBER, CategoryConstants.LAYOUT);
		classMap.put(ItemConstants.REQUIRED, CategoryConstants.RESPONSE);
		
	    classMap.put(ItemConstants.RIGHT_ITEM_TEXT, CategoryConstants.SEMANTIC);
	    classMap.put(ItemConstants.SECTION_LABEL, CategoryConstants.LAYOUT);
	   
	    classMap.put(ItemConstants.VALIDATION, CategoryConstants.VALIDATION);
	    classMap.put(ItemConstants.SUBHEADER, CategoryConstants.SEMANTIC);
	    classMap.put(ItemConstants.VALIDATION_ERROR_MESSAGE, CategoryConstants.VALIDATION);
	    classMap.put(ItemConstants.WIDTH_DECIMAL, CategoryConstants.LAYOUT);
		this.notSmallEditCategories.add(ItemConstants.PHI);
		this.notSmallEditCategories.add(ItemConstants.PAGE_NUMBER);
		this.notSmallEditCategories.add(ItemConstants.COLUMN_NUMBER);
		this.notSmallEditCategories.add(ItemConstants.REQUIRED);
		this.notSmallEditCategories.add(ItemConstants.RESPONSE_VALUES_OR_CALCULATIONS);
		this.notSmallEditCategories.add(ItemConstants.DATA_TYPE);
	}
	/**
	 * classify the change. The classification base on the changed column
	 * @param propertyMapping
	 * @return
	 */
	public  HashMap<String,HashMap<String,PropertyMapping>> classify ( HashMap<String,HashMap<String,PropertyMapping>> propertyMapping){
		for (Entry<String,HashMap<String,PropertyMapping>> e: propertyMapping.entrySet()){
			for (Entry<String,PropertyMapping> e2:e.getValue().entrySet()){
				String cat = classMap.get(e2.getKey());
				String newValue; 
				String oldValue; 
				
				
				if (!this.notSmallEditCategories.contains(e2.getKey())){
					newValue = (String) e2.getValue().getNewValue();
					oldValue = (String) e2.getValue().getOldValue();
					float sim = Measures.editDistance(newValue,oldValue);
					
					if (sim<SMALL_THRESH){
						cat = CategoryConstants.EDIT;
						e2.getValue().setRelevance(cat);
					
						continue;
					}
				}
				
				switch (cat){
				case CategoryConstants.SEMANTIC:
					if (e2.getValue().getNewValue()!= null && e2.getValue().getOldValue()!=null)
					if (e2.getValue().getNewValue().toString().startsWith(
							e2.getValue().getOldValue().toString())){
						cat= CategoryConstants.SEMANTIC_SPEC;
					}
					break;
				}
				e2.getValue().setRelevance(cat);
			}
		}
		return propertyMapping;
	}
}
