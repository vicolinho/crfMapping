package de.uni_leipzig.imise.data;

public class Rule {

	private String ruleType;
	
	private String comp;
	
	private String value;
	
	private String minNumber;
	
	private String maxNumber;
	
	
	private String ruleErrMsg;


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((comp == null) ? 0 : comp.hashCode());
		result = prime * result
				+ ((maxNumber == null) ? 0 : maxNumber.hashCode());
		result = prime * result
				+ ((minNumber == null) ? 0 : minNumber.hashCode());
		result = prime * result
				+ ((ruleErrMsg == null) ? 0 : ruleErrMsg.hashCode());
		result = prime * result
				+ ((ruleType == null) ? 0 : ruleType.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		Rule other = (Rule) obj;
		if (comp == null) {
			if (other.comp != null)
				return false;
		} else if (!comp.equals(other.comp))
			return false;
		if (maxNumber == null) {
			if (other.maxNumber != null)
				return false;
		} else if (!maxNumber.equals(other.maxNumber))
			return false;
		if (minNumber == null) {
			if (other.minNumber != null)
				return false;
		} else if (!minNumber.equals(other.minNumber))
			return false;
		if (ruleErrMsg == null) {
			if (other.ruleErrMsg != null)
				return false;
		} else if (!ruleErrMsg.equals(other.ruleErrMsg))
			return false;
		if (ruleType == null) {
			if (other.ruleType != null)
				return false;
		} else if (!ruleType.equals(other.ruleType))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
	
	
}
