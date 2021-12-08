package com.app.model;

import com.app.services.VegaServices;
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
	private Boolean useDatabase;
	private Boolean hookBeforeValue;
	private Boolean isOldValueString;
	private String oldValueString;
	private String newValue;


	public IndexPolicyAlteration(boolean isArray, String jsonGroup, Integer index, String keyArray, String key,
								 DetailPolicyAlteration detailPolicyAlteration) {
		super();
		this.isArray = isArray;
		this.jsonGroup = jsonGroup;
		this.index = index;
		this.keyArray = keyArray;
		this.key = key;
		this.detailPolicyAlteration = detailPolicyAlteration;
		this.oldColumn = PolicyAlterationUtility.getSpesificIndexOld(detailPolicyAlteration.getId_endors(),key);
		this.newColumn = PolicyAlterationUtility.getSpesificIndexNew(detailPolicyAlteration.getId_endors(),key);
		this.useDatabase = false;
		this.hookBeforeValue = false;
		this.isOldValueString = false;
		this.oldValueString = "";


	}

	public IndexPolicyAlteration(boolean isArray, String jsonGroup, Integer index, String keyArray, String key,
								 DetailPolicyAlteration detailPolicyAlteration, VegaServices vegaServices) {
		super();
		this.isArray = isArray;
		this.jsonGroup = jsonGroup;
		this.index = index;
		this.keyArray = keyArray;
		this.key = key;
		this.detailPolicyAlteration = detailPolicyAlteration;
		this.hookBeforeValue = false;
		this.isOldValueString = false;
		this.oldValueString = "";

		if(detailPolicyAlteration != null) {
			if(detailPolicyAlteration.getId_endors() != null) {

				this.oldColumn = PolicyAlterationUtility.getSpesificIndexOld(detailPolicyAlteration.getId_endors(),key);
				this.newColumn = PolicyAlterationUtility.getSpesificIndexNew(detailPolicyAlteration.getId_endors(),key);
			};
		};
		EndorseKeyAndValue keyAndvaluetmp = new EndorseKeyAndValue();
		this.oldColumn = keyAndvaluetmp.getOldKey();
		this.newColumn = keyAndvaluetmp.getNewKey();

		this.useDatabase = true;



		if(this.useDatabase && this.detailPolicyAlteration != null) {
			if(detailPolicyAlteration.getId_endors() != null) {
				EndorseKeyAndValue keyAndValue = PolicyAlterationUtility.createMapping(detailPolicyAlteration.getId_endors(),  key ,detailPolicyAlteration, vegaServices);
				this.oldColumn = keyAndValue.getOldKey();
				this.newColumn = keyAndValue.getNewKey();
				this.hookBeforeValue = keyAndValue.getHookBeforeValue();
				if(this.hookBeforeValue) {
					this.newValue = keyAndValue.getNewValueKey();
				}

				this.isOldValueString = keyAndValue.getIsOldKeyString();
				this.oldValueString = keyAndValue.getOldKeyString();



			};

		}

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

	public Boolean getUseDatabase() {
		return useDatabase;
	}

	public void setUseDatabase(Boolean useDatabase) {
		this.useDatabase = useDatabase;
	}

	public Boolean getHookBeforeValue() {
		return hookBeforeValue;
	}

	public void setHookBeforeValue(Boolean hookBeforeValue) {
		this.hookBeforeValue = hookBeforeValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	public Boolean getIsOldValueString() {
		return isOldValueString;
	}

	public void setIsOldValueString(Boolean isOldValueString) {
		this.isOldValueString = isOldValueString;
	}

	public String getOldValueString() {
		return oldValueString;
	}

	public void setOldValueString(String oldValueString) {
		this.oldValueString = oldValueString;
	}
}
