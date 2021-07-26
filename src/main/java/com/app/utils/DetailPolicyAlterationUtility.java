package com.app.utils;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.app.model.DetailPolicyAlteration;
import com.google.gson.JsonObject;

public class DetailPolicyAlterationUtility {
	
	private HashMap<String,HashMap<String,DetailPolicyAlteration>> map;
	private HashMap<String, HashMap<String,HashMap<String,DetailPolicyAlteration>>> mapArray;
	private HashMap<String, DetailPolicyAlteration> ds;
	private JSONObject jsonObject;
		
	public DetailPolicyAlterationUtility() {
		map = new HashMap<String, HashMap<String,DetailPolicyAlteration>>();
		mapArray = new HashMap<String, HashMap<String,HashMap<String,DetailPolicyAlteration>>>();
		ds = new HashMap<String, DetailPolicyAlteration>();
		jsonObject = new JSONObject();
			
	}
	
	public void add(String mainKey, String key, DetailPolicyAlteration data) {
		 	if(map.get(mainKey) == null) {
		 		map.put(mainKey, new HashMap<String, DetailPolicyAlteration>());
		 	}
		 	map.get(mainKey).put(key, data);
	}
	
	public void addArray0(String mainKey,Integer index ,String indexKey, String key, DetailPolicyAlteration data) {
		if(mapArray.get(mainKey) == null) {
			mapArray.put(mainKey, new HashMap<String,HashMap<String,DetailPolicyAlteration>>());
	 	}
		
		HashMap<String, HashMap<String, DetailPolicyAlteration>> m = mapArray.get(mainKey);
		String keyIndex = mainKey+":"+indexKey+":"+index;
		if(indexKey != null  && index != null) {
			if(m.get(keyIndex) == null) {
				m.put(keyIndex, new HashMap<String, DetailPolicyAlteration>());
			}
			m.get(keyIndex).put(key, data);
		};
	}
	
	
	
	public JSONObject toJSON() {
			for(String k: this.map.keySet()) {
				JSONObject object = new JSONObject();
				HashMap<String, DetailPolicyAlteration> tmp = this.map.get(k);
				for(String k2: tmp.keySet()) {
					object.put(k2, (tmp.get(k2) == null?JSONObject.NULL:DetailPolicyAlteration.convertToJson(tmp.get(k2))));
					
				}
				jsonObject.put(k,object);
			}
			
			for(String k: this.mapArray.keySet()) {
				JSONArray object = new JSONArray();
				HashMap<String, HashMap<String, DetailPolicyAlteration>> t = this.mapArray.get(k);
				boolean isNull = false;
				if(t.keySet().size()<=0) {
					isNull = true;
				}else {
					for(String at:t.keySet()) {
						String keys[] = at.split(":");
						HashMap<String,DetailPolicyAlteration> tmp = t.get(at);
						JSONObject b = new JSONObject();
						JSONObject bt = new JSONObject();
						for(String tt:tmp.keySet()) {
							bt.put(tt, (tmp.get(tt) == null?JSONObject.NULL:DetailPolicyAlteration.convertToJson(tmp.get(tt))));
							
						}
						b.put(keys[1], bt);
						object.put(b);
					}
				};
			
				jsonObject.put(k,(isNull==true?new JSONArray():object));
			}
			
			
			return jsonObject;
	}

}
