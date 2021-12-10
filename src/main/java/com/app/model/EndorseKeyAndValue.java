package com.app.model;

public class EndorseKeyAndValue {
    private String newKey;
    private String oldKey;
    private String oldKeyString;


    private Boolean hookBeforeValue;
    private Boolean isOldKeyString;
    private String newValueKey;


    public EndorseKeyAndValue() {
        this.newKey = "msde_new1";
        this.oldKey = "msde_old1";
        this.hookBeforeValue = false;
        this.isOldKeyString = false;
        this.oldKeyString = "";

    }

    public String getNewKey() {
        return newKey;
    }

    public void setNewKey(String newKey) {
        this.newKey = newKey;
    }

    public String getOldKey() {
        return oldKey;
    }

    public void setOldKey(String oldKey) {
        this.oldKey = oldKey;
    }

    public Boolean getHookBeforeValue() {
        return hookBeforeValue;
    }

    public void setHookBeforeValue(Boolean hookBeforeValue) {
        this.hookBeforeValue = hookBeforeValue;
    }

    public String getNewValueKey() {
        return newValueKey;
    }

    public void setNewValueKey(String newValueKey) {
        this.newValueKey = newValueKey;
    }

    public String getOldKeyString() {
        return oldKeyString;
    }

    public void setOldKeyString(String oldKeyString) {
        this.oldKeyString = oldKeyString;
    }

    public Boolean getIsOldKeyString() {
        return isOldKeyString;
    }

    public void setIsOldKeyString(Boolean isOldKeyString) {
        this.isOldKeyString = isOldKeyString;
    }

}
