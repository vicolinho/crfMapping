package de.uni_leipzig.imise.data;

import java.util.Map;

/**
 * This class holds the items, sections and groups objects of an CRF sheet. 
 * It's identifiable with the name and version number.
 * @author loco
 *
 */
public class CRFVersion {

	private int version;
	
	private String name;
	
	
	private Map<String,Group> groups;
	
	private Map<String,Section> sections;
	
	private Map<String,Item> items;

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public Map<String, Group> getGroups() {
		return groups;
	}

	public void setGroups(Map<String, Group> groups) {
		this.groups = groups;
	}

	public Map<String, Section> getSections() {
		return sections;
	}

	public void setSections(Map<String, Section> sections) {
		this.sections = sections;
	}

	public Map<String, Item> getItems() {
		return items;
	}

	public void setItems(Map<String, Item> items) {
		this.items = items;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	
}
