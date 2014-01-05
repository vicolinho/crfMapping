package de.uni_leipzig.imise.data;

public class VersionPair implements Comparable{

	
	private int oldVersion;
	
	

	private int newVersion;

	
	
	public VersionPair (int v1,int v2){
		this.oldVersion =v1;
		this.newVersion = v2;
	}
	/**
	 * @return the v1
	 */
	public int getOldVersion() {
		return oldVersion;
	}

	/**
	 * @param v1 the v1 to set
	 */
	public void setOldVersion(int v1) {
		this.oldVersion = v1;
	}

	/**
	 * @return the v2
	 */
	public int getNewVersion() {
		return newVersion;
	}

	/**
	 * @param v2 the v2 to set
	 */
	public void setNewVersion(int v2) {
		this.newVersion = v2;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + oldVersion;
		result = prime * result + newVersion;
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
		VersionPair other = (VersionPair) obj;
		if (oldVersion != other.oldVersion)
			return false;
		if (newVersion != other.newVersion)
			return false;
		return true;
	}

	@Override
	public int compareTo(Object o) {
		VersionPair v2 = (VersionPair) o;
		Integer vnr1 = this.hashCode();
		Integer vnr2 = v2.hashCode();
		return vnr1.compareTo(vnr2);
	}
	
	
}
