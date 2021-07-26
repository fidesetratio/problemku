package com.app.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.app.model.DetailPolicyAlteration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;

public class PolicyAlterationUtility {
		private String json;
		private String parse;
		private HashMap map;
		private HashMap group;
		
		private PolicyAlterationListener listener;
		
		
		public PolicyAlterationUtility(String json, String parse, PolicyAlterationListener listener) {
			this.json = json;
		    this.parse = parse;
		    this.map = new HashMap<String,String>();
		    this.group = new HashMap<Integer,String>();
		    this.generateMap();
		    this.listener = listener;
		}
		
		
		private void generateMap() {
			this.map.put("status_tt", "status_tt");
			this.map.put("agama_tt", "agama_tt");
			this.group.put(3,"DATA PEMEGANG POLIS");
			this.group.put(34,"DATA PEMEGANG POLIS");
			this.group.put(39,"DATA PEMEGANG POLIS");
			this.group.put(61,"DATA PEMEGANG POLIS");
			this.group.put(67,"DATA PEMEGANG POLIS");
			this.group.put(89,"DATA PEMEGANG POLIS");
			this.group.put(93,"DATA PEMEGANG POLIS");
			
			
			this.group.put(40,"DATA TERTANGGUNG UTAMA");
			this.group.put(62,"DATA TERTANGGUNG UTAMA");
			this.group.put(68,"DATA TERTANGGUNG UTAMA");
			this.group.put(90,"DATA TERTANGGUNG UTAMA");
			

			this.group.put(8,"DATA PEMBAYAR PREMI");
			this.group.put(96,"DATA PEMBAYAR PREMI");
			
			this.group.put(93,"DATA PENERIMA MANFAAT");
			
		
		}
		
		public void processparse() {
			String allParseOne[] = parse.split(",");
			JSONObject object = new JSONObject(json);
			ObjectMapper m = new ObjectMapper() ;
			 
			for(String k:allParseOne) {
				Object ob1 = object.get(k);
				
				if(ob1 instanceof JSONObject) {
					
					JSONObject t = (JSONObject) ob1;
					for(String key: t.keySet()) {
						Object o1 = t.get(key);	
						if(o1 != null) {


							if(o1 instanceof JSONObject) {
								DetailPolicyAlteration obj = null;
								try {
									obj = m.readValue( o1.toString(),DetailPolicyAlteration.class);
									String endorseColumn = (this.map.get(key) != null ? (String) this.map.get(key): key);
									String group = (this.group.get(obj.getId_endors()) != null ? (String)this.group.get(obj.getId_endors()):"UNLISTED");
									listener.listen(obj,k,key,endorseColumn,group);
								
								} catch (JsonMappingException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									listener.errors(key, e);
									
								} catch (JsonProcessingException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									listener.errors(key, e);
									
								}
								
							}else  {
								DetailPolicyAlteration obj = null;
								String endorseColumn = null;
								String group = null;
								listener.listen(obj,k,key,endorseColumn,group);
							
								
							}
						}
					}
					
					
							
				}
				else if (ob1 instanceof JSONArray) {
					List<DetailPolicyAlteration> arr = convertToArray((JSONArray)ob1, k);
					if(arr.size()<=0) {
						DetailPolicyAlteration dp = null; 
						String endorseColumn =null;
						String group = null;
						listener.listenArray0(dp,null,k,null,null,null,null); 
						
					};
					
					
				}
				
			}
			
				
		}


		private List<DetailPolicyAlteration> convertToArray(JSONArray array, String kk){
			List<DetailPolicyAlteration> arr = new ArrayList<DetailPolicyAlteration>();
			 ObjectMapper m = new ObjectMapper() ;
			for (int i = 0 ; i < array.length(); i++) {
		        JSONObject obj = (JSONObject)array.get(i);
		        if(obj != null) {
		        	for(String key: obj.keySet()) {
		        		Object tmp = obj.get(key);	
						if(tmp instanceof JSONObject) {
							JSONObject ok = (JSONObject) tmp;
							for(String k : ok.keySet()) {
								Object tmp2 = ok.get(k);
								if(tmp2 != null) {
									
									if(tmp2.toString().equals("null")) {
										DetailPolicyAlteration dp = null; 
										String endorseColumn =null;
										String group = null;
										listener.listenArray0(dp,i,kk,key,k,endorseColumn,group); 
										arr.add(dp);	
										
									}else {
										
										if(tmp2 instanceof JSONObject) {
												JSONObject kt = (JSONObject) tmp2;
												DetailPolicyAlteration dp = null;
												try {
													dp = m.readValue( kt.toString(),DetailPolicyAlteration.class);
													String endorseColumn = (this.map.get(k) != null ? (String) this.map.get(k): k);
													String group = (this.group.get(dp.getId_endors()) != null ? (String)this.group.get(dp.getId_endors()):"UNLISTED");
													listener.listenArray0(dp,i,kk,key,k,endorseColumn,group);
													
													arr.add(dp);
												} catch (JsonMappingException e) {
													listener.errors(key, e);
													e.printStackTrace();
												} catch (JsonProcessingException e) {
													e.printStackTrace();
													listener.errors(key, e);
															
												}
											
										}
									}
								}
								}
						}
		        	}	    		        	
		        	
				}
			}
			return arr;
		}

		
		
		public static String getFirstLevelJsonValue(String json,String key) {
			JSONObject object = new JSONObject(json);
			Object ob1 = object.get(key);
			return ob1.toString();
		}

}

	