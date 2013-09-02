package de.uni_leipzig.imise.data;

public class VersionPair implements Comparable{

	
	private int v1;
	
	

	private int v2;

	
	
	public VersionPair (int v1,int v2){
		this.v1 =v1;
		this.v2 = v2;
	}
	/**
	 * @return the v1
	 */
	public int getV1() {
		return v1;
	}

	/**
	 * @param v1 the v1 to set
	 */
	public void setV1(int v1) {
		this.v1 = v1;
	}

	/**
	 * @return the v2
	 */
	public int getV2() {
		return v2;
	}

	/**
	 * @param v2 the v2 to set
	 */
	public void setV2(int v2) {
		this.v2 = v2;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + v1;
		result = prime * result + v2;
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
		if (v1 != other.v1)
			return false;
		if (v2 != other.v2)
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
