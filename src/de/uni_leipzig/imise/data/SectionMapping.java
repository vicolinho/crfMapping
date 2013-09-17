package de.uni_leipzig.imise.data;

public class SectionMapping {

	private Section oldSec;
	private Section newSec;

	public SectionMapping(Section oldSec, Section newSec) {
		this.setOldSec(oldSec);
		this.setNewSec(newSec);
	}

	/**
	 * @return the oldSec
	 */
	public Section getOldSec() {
		return oldSec;
	}

	/**
	 * @param oldSec the oldSec to set
	 */
	public void setOldSec(Section oldSec) {
		this.oldSec = oldSec;
	}

	/**
	 * @return the newSec
	 */
	public Section getNewSec() {
		return newSec;
	}

	/**
	 * @param newSec the newSec to set
	 */
	public void setNewSec(Section newSec) {
		this.newSec = newSec;
	}

}
