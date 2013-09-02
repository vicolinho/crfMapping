package de.uni_leipzig.imise.data.managment;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import de.uni_leipzig.imise.data.CRFVersion;

public class VersionManager {

	public static final String ADD_VERSIONS ="addVersions";


	public static final String CLEAR_VERSIONS = "clearVersions";
	
	
	private static VersionManager instance;
	PropertyChangeSupport change ;
	private TreeMap<Integer,CRFVersion> versions ;
	
	
	private VersionManager (){
		change = new PropertyChangeSupport (this);
		versions = new TreeMap<Integer,CRFVersion> ();
	}
	
	
	private void addVersion(CRFVersion v){
		this.versions.put(v.getVersion(), v);
	}
	
	
	
	public void addVersions (List<CRFVersion> vs){
		
		for (CRFVersion v:vs)
			this.addVersion(v);
		change.firePropertyChange(ADD_VERSIONS, null, this.versions);
	}
	

	public void clear (){
		versions.clear();
		this.change.firePropertyChange(CLEAR_VERSIONS, false, true);
	}
	
	public TreeMap<Integer,CRFVersion> getVersions ()
	{
		return versions;
	}


	public void addPropertyChangeListener(PropertyChangeListener l){
		change.addPropertyChangeListener(l);
	}
	
	
	
	public static VersionManager getInstance() {
		if (instance==null)
			instance = new VersionManager();
		return instance;
	}
}
