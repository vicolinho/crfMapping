package de.uni_leipzig.imise.data;

import java.util.Map;

public class CRFVersion {

	private int version;
	
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
	
	
}
