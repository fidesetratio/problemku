package com.app.model;

import com.app.utils.PolicyAlterationUtility;

public class IndexPolicyAlteration {
	
	private boolean isArray;
	private String jsonGroup;
	private Integer index;
	private String keyArray;
	private String key;
	private DetailPolicyAlteration detailPolicyAlteration;
	private String oldColumn;
	private String newColumn;
	
	
	
	public IndexPolicyAlteration(boolean isArray, String jsonGroup, Integer index, String keyArray, String key,
			DetailPolicyAlteration detailPolicyAlteration) {
		super();
		this.isArray = isArray;
		this.jsonGroup = jsonGroup;
		this.index = index;
		this.keyArray = keyArray;
		this.key = key;
		this.detailPolicyAlteration = detailPolicyAlteration;
		this.oldColumn = PolicyAlterationUtility.getSpesificIndexOld(key);
		this.newColumn = PolicyAlterationUtility.getSpesificIndexNew(key);
		
	}



	public boolean isArray() {
		return isArray;
	}



	public void setArray(boolean isArray) {
		this.isArray = isArray;
	}



	public String getJsonGroup() {
		return jsonGroup;
	}



	public void setJsonGroup(String jsonGroup) {
		this.jsonGroup = jsonGroup;
	}



	public Integer getIndex() {
		return index;
	}



	public void setIndex(Integer index) {
		this.index = index;
	}



	public String getKeyArray() {
		return keyArray;
	}



	public void setKeyArray(String keyArray) {
		this.keyArray = keyArray;
	}



	public String getKey() {
		return key;
	}



	public void setKey(String key) {
		this.key = key;
	}



	public DetailPolicyAlteration getDetailPolicyAlteration() {
		return detailPolicyAlteration;
	}



	public void setDetailPolicyAlteration(DetailPolicyAlteration detailPolicyAlteration) {
		this.detailPolicyAlteration = detailPolicyAlteration;
	}



	public String getOldColumn() {
		return oldColumn;
	}



	public void setOldColumn(String oldColumn) {
		this.oldColumn = oldColumn;
	}



	public String getNewColumn() {
		return newColumn;
	}



	public void setNewColumn(String newColumn) {
		this.newColumn = newColumn;
	}
}
