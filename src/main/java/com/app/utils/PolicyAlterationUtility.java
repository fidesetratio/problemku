package com.app.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.app.model.DetailPolicyAlteration;
import com.app.model.PolicyAlterationKeyAndValue;
import com.app.services.VegaServices;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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


		
		
		public static PolicyAlterationKeyAndValue getKeyAndValue(String key, Integer lsje_id, String value, VegaServices services) {
				//if(lsje_id )
			PolicyAlterationKeyAndValue keyAndValue = new PolicyAlterationKeyAndValue();
			
			if(value != null) {
				keyAndValue.setValue(value);
			}
			
			if(lsje_id == 93) {
				if(key.equalsIgnoreCase("msde_new1")) {
					keyAndValue.setKey("kota_bank_pp");
				}
				if(key.equalsIgnoreCase("msde_new2")) {
					keyAndValue.setKey("no_rekening_pp");
				}	
				if(key.equalsIgnoreCase("msde_new3")) {
					keyAndValue.setKey("nama_bank_pp");
					if(value != null) {
					
						try {
							 Integer.parseInt(value.trim());
							 String val = services.selectListBankById(value.trim());
							 keyAndValue.setValue(val);
							 }catch(Exception e) {
								keyAndValue.setValue("error");
							 }
						
					};
				}	

				if(key.equalsIgnoreCase("msde_new4")) {
					keyAndValue.setKey("pemilik_rekening_pp");
			
				}	
				if(key.equalsIgnoreCase("msde_new5")) {
					keyAndValue.setKey("cabang_bank_pp");
					if(value != null) {
						
						try {
							 Integer.parseInt(value.trim());
							 String val = services.selectCabangBankByLbnId(value.trim());
							 keyAndValue.setValue(val);
							 }catch(Exception e) {
								keyAndValue.setValue("error");
							 }
						
					};
			
			
				}	
			}
			if(lsje_id == 89) {

				if(key.equalsIgnoreCase("msde_new1")) {
					keyAndValue.setKey("uraian_pekerjaan");
				}
				if(key.equalsIgnoreCase("msde_new2")) {
					keyAndValue.setKey("jabatan_pp");
				}	
				if(key.equalsIgnoreCase("msde_new3")) {
					keyAndValue.setKey("nama_perusahaan_pp");
				}	

				if(key.equalsIgnoreCase("msde_new4")) {
					keyAndValue.setKey("tipe_usaha_pp");
				}	
				
			}
			
			if(lsje_id==96) {
				if(key.equalsIgnoreCase("msde_new2")) {// 96
					keyAndValue.setKey("pemilik_rekening_payor");
					
				}
			
				if(key.equalsIgnoreCase("msde_new3")) {// 96
					keyAndValue.setKey("cabang_bank_payor");
	
				}
				if(key.equalsIgnoreCase("msde_new4")) {// 96
					keyAndValue.setKey("no_rekening_payor");
									
				}
				if(key.equalsIgnoreCase("msde_new5")) {// 96
					keyAndValue.setKey("kota_bank_payor");
						
				}
			
			}

			if(lsje_id == 3) {
				if(key.equalsIgnoreCase("msde_new1")) { //3
					keyAndValue.setKey("propinsi_tinggal");
					if(value != null) {
						try {
						 Integer.parseInt(value.trim());
						 String val = services.selectListProvinsiById(value.trim());
						 keyAndValue.setValue(val);
						 }catch(Exception e) {
							keyAndValue.setValue("error");
						 }
						 
					}
					
					
				}
				if(key.equalsIgnoreCase("msde_new2")) { //3
					keyAndValue.setKey("kecamatan_tinggal");
					if(value != null) {
						try {
						 Integer.parseInt(value.trim());
						 String val = services.selectListKecamatanById(value.trim());
						 keyAndValue.setValue(val);
						 }catch(Exception e) {
							keyAndValue.setValue("error");
						 }
						 
					}
					
				}
				if(key.equalsIgnoreCase("msde_new3")) { //3
					
					keyAndValue.setKey("kodepos_tinggal");
					
				}
				if(key.equalsIgnoreCase("msde_new4")) { //3
					
					keyAndValue.setKey("alamat_rumah_pp");
					
				}	
				if(key.equalsIgnoreCase("msde_new5")) { //3
					
					keyAndValue.setKey("kabupaten_tinggal");
					if(value != null) {
						try {
						 Integer.parseInt(value.trim());
						 String val = services.selectListKabupatenById(value.trim());
						 keyAndValue.setValue(val);
						 }catch(Exception e) {
							keyAndValue.setValue("error");
						 }
						 
					}
					
				}	

				if(key.equalsIgnoreCase("msde_new6")) { //3
					keyAndValue.setKey("kelurahan_tinggal");
					
				}	

				if(key.equalsIgnoreCase("msde_new7")) { //3
					keyAndValue.setKey("negara_tinggal");
					
				}
				
				
				if(key.equalsIgnoreCase("msde_new8")) { //3
					keyAndValue.setKey("alamat_tpt_tinggal");
				}

				

				if(key.equalsIgnoreCase("msde_new9")) { //3
					keyAndValue.setKey("negara_rumah");
					try {
						 Integer.parseInt(value.trim());
						 String val = services.selectListNegaraById(value.trim());
						 keyAndValue.setValue(val);
						 }catch(Exception e) {
							keyAndValue.setValue("error");
						 }
						 

				}
			}

			
			if(lsje_id == 34) {
				if(key.equalsIgnoreCase("msde_new1")) { //34
					keyAndValue.setKey("kabupaten_kantor");
					if(value != null) {
						try {
						 Integer.parseInt(value.trim());
						 String val = services.selectListKabupatenById(value.trim());
						 keyAndValue.setValue(val);
						 }catch(Exception e) {
							keyAndValue.setValue("error");
						 }
						 
					}
					
				};
					
				
				if(key.equalsIgnoreCase("msde_new2")) { //34
					keyAndValue.setKey("kelurahan_kantor");
					
				};
					

				if(key.equalsIgnoreCase("msde_new3")) { //34
					keyAndValue.setKey("kecamatan_kantor");
					if(value != null) {
						try {
						 Integer.parseInt(value.trim());
						 String val = services.selectListKecamatanById(value.trim());
						 keyAndValue.setValue(val);
						 }catch(Exception e) {
							keyAndValue.setValue("error");
						 }
						 
					}
					
					
				};	
				

				if(key.equalsIgnoreCase("msde_new4")) { //34
					keyAndValue.setKey("negara_kantor");
					if(value != null) {
						try {
						 Integer.parseInt(value.trim());
						 String val = services.selectListNegaraById(value.trim());
						 keyAndValue.setValue(val);
						 }catch(Exception e) {
							keyAndValue.setValue("error");
						 }
						 
					}
					
					
				};	
			}
			
			if(lsje_id == 67) {
				if(key.equalsIgnoreCase("msde_new1")) {
					keyAndValue.setKey("status_pp");
					if(value != null) {
						try {
						 Integer.parseInt(value.trim());
						 String val = services.selectListPernikahanById(value.trim());
						 keyAndValue.setValue(val);
						 }catch(Exception e) {
							keyAndValue.setValue("error");
						 }
						 
					}
	
					
				}
			}
			
			if(lsje_id == 39) {
				if(key.equalsIgnoreCase("msde_new1")) {
					keyAndValue.setKey("kewarganegaraan_pp");
					if(value != null) {
						try {
						 Integer.parseInt(value.trim());
						 String val = services.selectListNegaraById(value.trim());
						 keyAndValue.setValue(val);
						 }catch(Exception e) {
							keyAndValue.setValue("error");
						 }
						 
					}
	
				}
			}
			if(lsje_id == 61) {
				if(key.equalsIgnoreCase("msde_new1")) {
					keyAndValue.setKey("agama_pp");
					if(value != null) {
						try {
						 Integer.parseInt(value.trim());
						 String val = services.selectListAgamaById(value.trim());
						 keyAndValue.setValue(val);
						 }catch(Exception e) {
							keyAndValue.setValue("error");
						 }
						 
					}
				}
			}
			if(lsje_id == 62) {
				if(key.equalsIgnoreCase("msde_new1")) {
					keyAndValue.setKey("agama_tertanggung");
					if(value != null) {
						try {
						 Integer.parseInt(value.trim());
						 String val = services.selectListAgamaById(value.trim());
						 keyAndValue.setValue(val);
						 }catch(Exception e) {
							keyAndValue.setValue("error");
						 }
						 
					}
				}
			}
			if(lsje_id == 68) {
				if(key.equalsIgnoreCase("msde_new1")) {
					keyAndValue.setKey("status_tt");
					if(value != null) {
						try {
						 Integer.parseInt(value.trim());
						 String val = services.selectListPernikahanById(value.trim());
						 keyAndValue.setValue(val);
						 }catch(Exception e) {
							keyAndValue.setValue("error");
						 }
						 
					}
				}
			}
			if(lsje_id == 90) {
				
				
				if(key.equalsIgnoreCase("msde_new1")) {// 90

					keyAndValue.setKey("nama_perusahaan_tt");
					if(value != null) {
						keyAndValue.setValue(value.trim());
					}
				}
				
				if(key.equalsIgnoreCase("msde_new2")) {// 90
					
					keyAndValue.setKey("tipe_usaha_tt");
					
					
				}
				if(key.equalsIgnoreCase("msde_new3")) {// 90
					keyAndValue.setKey("jabatan_tt");
					
				}
				
			}
			return keyAndValue;
		}
		


		public static String getSpesificIndexNew(String key) {
		
			String index = "msde_new1";
			if(key == null) {
				return "";
			}
			if(key.equalsIgnoreCase("propinsi_tinggal")) { //3
					index =  "msde_new1";
				}
				if(key.equalsIgnoreCase("kecamatan_tinggal")) { //3
					index = "msde_new2";
				}
				if(key.equalsIgnoreCase("kodepos_tinggal")) { //3
					index =  "msde_new3";
				}
				if(key.equalsIgnoreCase("alamat_rumah_pp")) { //3
					index = "msde_new4";
				}	
				if(key.equalsIgnoreCase("kabupaten_tinggal")) { //3
					index = "msde_new5";
				}	

				if(key.equalsIgnoreCase("kelurahan_tinggal")) { //3
					index = "msde_new6";
				}	

				if(key.equalsIgnoreCase("negara_tinggal")) { //3
					index = "msde_new7";
				}
				
				
				if(key.equalsIgnoreCase("alamat_tpt_tinggal")) { //3
					index = "msde_new8";
				}

				if(key.equalsIgnoreCase("negara_rumah")) { //3
					index = "msde_new9";
				}

				
				if(key.equalsIgnoreCase("kabupaten_kantor")) { //34
					index = "msde_new1";
				};
					
				
				
				
				if(key.equalsIgnoreCase("kelurahan_kantor")) { //34
					index = "msde_new2";
				};
					

				if(key.equalsIgnoreCase("kecamatan_kantor")) { //34
					index = "msde_new3";
				};	


				if(key.equalsIgnoreCase("negara_kantor")) { //34
					index = "msde_new4";
				};	

				
				if(key.equalsIgnoreCase("nama_bank_payor")) {// 96
					index = "msde_new1";
					
				}
				
				if(key.equalsIgnoreCase("pemilik_rekening_payor")) {// 96
					index = "msde_new2";
					
				}
			
				if(key.equalsIgnoreCase("cabang_bank_payor")) {// 96
					index = "msde_new3";
					
				}
				if(key.equalsIgnoreCase("no_rekening_payor")) {// 96
					index = "msde_new4";
					
				}
				if(key.equalsIgnoreCase("kota_bank_payor")) {// 96
					index = "msde_new5";
					
				}
				
				if(key.equalsIgnoreCase("uraian_pekerjaan")) {// 89
					index = "msde_new1";
					
				}
				
				if(key.equalsIgnoreCase("jabatan_pp")) {// 89
					index = "msde_new2";
					
				}
				if(key.equalsIgnoreCase("nama_perusahaan_pp")) {// 89
					index = "msde_new3";
					
				}
				if(key.equalsIgnoreCase("tipe_usaha_pp")) {// 89
					index = "msde_new4";
					
				}
				
				
				
				if(key.equalsIgnoreCase("nama_perusahaan_tt")) {// 90
					index = "msde_new1";
					
				}
				
				if(key.equalsIgnoreCase("tipe_usaha_tt")) {// 90
					index = "msde_new2";
					
				}
				if(key.equalsIgnoreCase("jabatan_tt")) {// 90
					index = "msde_new3";
					
				}
				

				if(key.equalsIgnoreCase("kota_bank_pp")) {// 93
					index = "msde_new1";
					
					
				}

				if(key.equalsIgnoreCase("no_rekening_pp")) {// 93
					index = "msde_new2";
					
				}
				if(key.equalsIgnoreCase("nama_bank_pp")) {// 93
					index = "msde_new3";
					
				}
				if(key.equalsIgnoreCase("pemilik_rekening_pp")) {// 93
					index = "msde_new4";
					
				}
				if(key.equalsIgnoreCase("cabang_bank_pp")) {// 93
					index = "msde_new5";
					
				}
				return index;
		}



		public static String getSpesificIndexOld(String key) {
			String index = "msde_old1";
			if(key == null) {
				return "";
			}
				
			if(key.equalsIgnoreCase("propinsi_tinggal")) { //3
					index =  "msde_old1";
				}
				if(key.equalsIgnoreCase("kecamatan_tinggal")) { //3
					index = "msde_old2";
				}
				if(key.equalsIgnoreCase("kodepos_tinggal")) { //3
					index =  "msde_old3";
				}
				if(key.equalsIgnoreCase("alamat_rumah_pp")) { //3
					index = "msde_old4";
				}	
				if(key.equalsIgnoreCase("kabupaten_tinggal")) { //3
					index = "msde_old5";
				}	
				if(key.equalsIgnoreCase("kelurahan_tinggal")) { //3
					index = "msde_old6";
				}	
				if(key.equalsIgnoreCase("negara_tinggal")) { //3
					index = "msde_old7";
				}
				if(key.equalsIgnoreCase("alamat_tpt_tinggal")) { //3
					index = "msde_old8";
				}
				if(key.equalsIgnoreCase("negara_rumah")) { //3
					index = "msde_old9";
				}
				
				
				
				
			if(key.equalsIgnoreCase("kabupaten_kantor")) { //34
				index = "msde_old1";
			};
				
			
			if(key.equalsIgnoreCase("kelurahan_kantor")) { //34
				index = "msde_old2";
			};
				

			if(key.equalsIgnoreCase("kecamatan_kantor")) { //34
				index = "msde_old3";
			};	

			if(key.equalsIgnoreCase("negara_kantor")) { //34
				index = "msde_old4";
			};	
			
				
				if(key.equalsIgnoreCase("nama_bank_payor")) {// 96
					index = "msde_old1";
					
				}
				if(key.equalsIgnoreCase("pemilik_rekening_payor")) {// 96
					index = "msde_old2";
					
				}
			
				if(key.equalsIgnoreCase("cabang_bank_payor")) {// 96
					index = "msde_old3";
					
				}
				if(key.equalsIgnoreCase("no_rekening_payor")) {// 96
					index = "msde_old4";
					
				}
				if(key.equalsIgnoreCase("kota_bank_payor")) {// 96
					index = "msde_old5";
					
				}
				
				if(key.equalsIgnoreCase("uraian_pekerjaan")) {// 89
					index = "msde_old1";
					
				}
				
				if(key.equalsIgnoreCase("jabatan_pp")) {// 89
					index = "msde_old2";
					
				}
				if(key.equalsIgnoreCase("nama_perusahaan_pp")) {// 89
					index = "msde_old3";
					
				}
				if(key.equalsIgnoreCase("tipe_usaha_pp")) {// 89
					index = "msde_old4";
					
				}
				

				if(key.equalsIgnoreCase("nama_perusahaan_tt")) {// 90
					index = "msde_old1";
					
				}
				
				if(key.equalsIgnoreCase("tipe_usaha_tt")) {// 90
					index = "msde_old2";
					
				}
				if(key.equalsIgnoreCase("jabatan_tt")) {// 90
					index = "msde_old3";
					
				}
				
				
				if(key.equalsIgnoreCase("kota_bank_pp")) {// 93
					index = "msde_old1";
					
				}

				if(key.equalsIgnoreCase("no_rekening_pp")) {// 93
					index = "msde_old2";
					
				}
				if(key.equalsIgnoreCase("nama_bank_pp")) {// 93
					index = "msde_old3";
					
				}
				if(key.equalsIgnoreCase("pemilik_rekening_pp")) {// 93
					index = "msde_old4";
					
				}
				if(key.equalsIgnoreCase("cabang_bank_pp")) {// 93
					index = "msde_old5";
					
				}
				return index;
		}



}

	