package com.app.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.app.feignclient.ServiceNotification;
import com.app.model.DetailPolicyAlteration;
import com.app.model.DropdownPolicyAlteration;
import com.app.model.Endorse;
import com.app.model.EndorsePolicyAlteration;
import com.app.model.IndexPolicyAlteration;
import com.app.model.Pemegang;
import com.app.model.PolicyAlteration;
import com.app.model.PolicyAlterationKeyAndValue;
import com.app.model.Tertanggung;
import com.app.services.VegaServices;
import com.app.services.VegaServicesProd;
import com.app.utils.DetailPolicyAlterationUtility;
import com.app.utils.PolicyAlterationCounter;
import com.app.utils.PolicyAlterationListener;
import com.app.utils.PolicyAlterationUtility;
import com.app.utils.ResponseMessage;
import com.app.utils.VegaCustomResourceLoader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@RestController
public class PolicyAlterationIndividualController {

	@Autowired
	private VegaServices services;

	@Autowired
	private VegaServicesProd servicesprod;
	
	@Autowired
	ServiceNotification serviceNotification;

	@Autowired
	private VegaCustomResourceLoader customResourceLoader;
	
	private static final Logger logger = LogManager.getLogger(PolicyAlterationIndividualController.class);

	
	
	
	
	
	
	
	
	
	@RequestMapping(value = "/viewstatuspolicyalteration", produces = "application/json", method = RequestMethod.POST)
	public String viewstatuspolicyalteration(@RequestBody String requestViewPolicyAlteration,
			HttpServletRequest request) {
		String result = "";
		
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		String req = requestViewPolicyAlteration;
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String,Object> mapresponse = new HashMap<String,Object>();
		String username = PolicyAlterationUtility.getFirstLevelJsonValue(req, "username");
		String key = PolicyAlterationUtility.getFirstLevelJsonValue(req, "key");
		String no_polis = PolicyAlterationUtility.getFirstLevelJsonValue(req, "no_polis");
		String msen_endorse_no = PolicyAlterationUtility.getFirstLevelJsonValue(req, "msen_endors_no");
		String status =  PolicyAlterationUtility.getFirstLevelJsonValue(req, "status");
		String grouping = PolicyAlterationUtility.getFirstLevelJsonValue(req, "grouping");
		String lsje_id = PolicyAlterationUtility.getFirstLevelJsonValue(req, "jen");
		JSONObject output = new JSONObject();
		
		
		DetailPolicyAlterationUtility modelUtility  = new DetailPolicyAlterationUtility();
		
		
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				
				if(msen_endorse_no != null && grouping != null && lsje_id != null) {
					
					
					Pemegang paramSelectSPAJ = new Pemegang();
					paramSelectSPAJ.setMspo_policy_no(no_polis);
					Pemegang dataSPAJ = services.selectGetSPAJ(paramSelectSPAJ);
					String reg_spaj = dataSPAJ.getReg_spaj();
					
					ArrayList<EndorsePolicyAlteration> endorse = services.selectListPolicyAlterationByendorseId(reg_spaj, msen_endorse_no, lsje_id, grouping,status);

					ArrayList<EndorsePolicyAlteration> endorseresult = new ArrayList<EndorsePolicyAlteration>();
					
					if(endorse.size()>0) {
						//JSONArray arr = new JSONArray();
						for(EndorsePolicyAlteration tmp:endorse) {
								ArrayList<EndorsePolicyAlteration> tmp2 = mappingEndorseBasedToExtractValue(tmp);
								for(EndorsePolicyAlteration t:tmp2) {
									 endorseresult.add(t);
								}
						}
					}else {
					
					}
					
					
					
					
					
					
					
					if(endorseresult.size()>0) {
						JSONArray arr = new JSONArray();
						for(EndorsePolicyAlteration end:endorseresult) {
							arr.put(EndorsePolicyAlteration.toJSonObject(end));
						}
						output.put("result",arr);
					}else {
						output.put("result", "[]");
					}
					
				}else {
					
					
					error = true;
					message = "Please make sure parameter is correct";
					resultErr = ResponseMessage.ERROR_VALIDATION + "Please make sure parameter is correct";
					logger.error("Path: " + request.getServletPath() + " Error: " + resultErr);
					
				}
				
				
			}else {
				error = true;
				message = "Username & Password is not matched";
				resultErr = ResponseMessage.ERROR_VALIDATION + "(Username: " + username + " & Key: " + key + ")";
				logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
			
			}
		
		
		}catch(Exception e) {
			error = true;
			message = ResponseMessage.ERROR_SYSTEM;
			resultErr = "bad exception " + e;
			logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: " + e);

		}
		
		
		
		
		customResourceLoader.updateActivity(username);
		// Insert Log LST_HIST_ACTIVITY_WS
		customResourceLoader.insertHistActivityWS(12, 84, new Date(), req, res, 1, resultErr, start, username);
		
		map.put("error", error);
		map.put("message", message);
		map.put("result", mapresponse);
		if(error) {
			result =  gson.toJson(map);
		}else {
			output.put("error", error);
			output.put("message", message);
			result = output.toString();
		}
		
		return result;
	}
	
	
	private ArrayList<EndorsePolicyAlteration> mappingEndorseBasedToExtractValue(EndorsePolicyAlteration tmp) {
		// TODO Auto-generated method stub
		ArrayList<EndorsePolicyAlteration> array1 = new ArrayList<EndorsePolicyAlteration>();
		HashMap<String,String> data = tmp.convertToMsdeNewHashMap();
		boolean allowed = data.size()>0?true:false;
		Integer lsje_id = tmp.getLsje_id();
		if(allowed) {
			for(String key:data.keySet()) {
				String value = data.get(key);
				PolicyAlterationKeyAndValue policyAlterationKeyAndValue = PolicyAlterationUtility.getKeyAndValueWithDatabase(key,lsje_id, value, services);
				if(policyAlterationKeyAndValue.getKey() != null && policyAlterationKeyAndValue.getValue() != null) {
					EndorsePolicyAlteration t = (EndorsePolicyAlteration) SerializationUtils.clone(tmp);
					t.setKey(policyAlterationKeyAndValue.getKey());
					t.setValue(policyAlterationKeyAndValue.getValue());
					array1.add(t);
					
				}
			}
			
		}
		return array1;
	}


	public HashMap<String,String> calculateValuePolicyAlteration(String lsje_id,String msde_new1,String msde_new2,String msde_new3,String msde_new4,String msde_new5,String msde_new6,String msde_new7,String msde_new8) {
		HashMap<String,String> values = new HashMap<String, String>();
		
		return values;
	}
	
	
	
	
	@RequestMapping(value = "/previewsubmitalteration", produces = "application/json", method = RequestMethod.POST)
	public String previewsubmitalteration(@RequestBody String requestViewPolicyAlteration,
			HttpServletRequest request) {
		String result = "";
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		//String req = gson.toJson(requestViewPolicyAlteration);
		String req = requestViewPolicyAlteration;
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String,Object> mapresponse = new HashMap<String,Object>();
		String username = PolicyAlterationUtility.getFirstLevelJsonValue(req, "username");
		String key = PolicyAlterationUtility.getFirstLevelJsonValue(req, "key");
		String no_polis = PolicyAlterationUtility.getFirstLevelJsonValue(req, "no_polis");
		ArrayList<Exception> internalErrors = new ArrayList<Exception>();
		DetailPolicyAlterationUtility modelUtility  = new DetailPolicyAlterationUtility();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				Pemegang paramSelectSPAJ = new Pemegang();
				paramSelectSPAJ.setMspo_policy_no(no_polis);
				Pemegang dataSPAJ = services.selectGetSPAJ(paramSelectSPAJ);
				String reg_spaj = dataSPAJ.getReg_spaj();
				
				
			
				
				ArrayList<Endorse> inProgressEndorse = services.selectListPolicyAlterationInProcessing(reg_spaj);
				Map<Integer,Endorse> endorseMap = new HashMap<Integer, Endorse>();
				
				for(Endorse e:inProgressEndorse) {
					endorseMap.put(e.getLsje_id(), e);
				}
				
				PolicyAlterationUtility utility = new PolicyAlterationUtility(req, "policyholder,payor,insured,beneficiary,beneficiary_add", new PolicyAlterationListener() {
					
					@Override
					public void listenArray0(DetailPolicyAlteration detailPolicyAlteration, Integer index,String jsonGroup,String keyArray, String key,
							String endorseColumn, String group) {
					
						
						if(detailPolicyAlteration != null) {
							detailPolicyAlteration.setStatus("ALLOWED");
							if(detailPolicyAlteration.getNew_() != null && detailPolicyAlteration.getId_endors() != null) {
								boolean checkingIsInProgess = (endorseMap.get(detailPolicyAlteration.getId_endors()) !=null? true:false);
								
								if(checkingIsInProgess) {
									detailPolicyAlteration.setStatus("INPROGRESS");
								}
							}
							if(detailPolicyAlteration.getId_endors() == null) {
								detailPolicyAlteration.setStatus("IDENDORSENULL");
								
							}
						}
						
						
						modelUtility.addArray0(jsonGroup, index, keyArray, key, detailPolicyAlteration);
					
					}
					
					
					
					@Override
					public void listen(DetailPolicyAlteration detailPolicyAlteration, String jsonGroup, String key,
							String endorseColumn, String group) {
						// TODO Auto-generated method stub
						if(detailPolicyAlteration != null) {
							
								
							
									detailPolicyAlteration.setStatus("ALLOWED");
									if(detailPolicyAlteration.getNew_() != null && detailPolicyAlteration.getId_endors() != null) {
										boolean checkingIsInProgess = (endorseMap.get(detailPolicyAlteration.getId_endors()) !=null? true:false);
										if(checkingIsInProgess) {
											detailPolicyAlteration.setStatus("INPROGRESS");
										}
									}
									if(detailPolicyAlteration.getId_endors() == null) {
										detailPolicyAlteration.setStatus("IDENDORSENULL");
										
									}
						}
						modelUtility.add(jsonGroup, key, detailPolicyAlteration);
					}
					
					@Override
					public void errors(String key, Exception e) {
						// TODO Auto-generated method stub
							internalErrors.add(e);
					}
				}

				
			);
				utility.processparse();
				
				if(internalErrors.size()<=0) {
					message = "successfuly processed";
				}else {
					error = true;
					message = "Internal error occured, please contact administrator";
				}
				
			}else {
		
				error = true;
				message = "Username & Password is not matched";
				resultErr = ResponseMessage.ERROR_VALIDATION + "(Username: " + username + " & Key: " + key + ")";
				logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
			}
		}catch(Exception e) {
			error = true;
			message = ResponseMessage.ERROR_SYSTEM;
			resultErr = "bad exception " + e;
			logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: " + e);

		
		}

		map.put("error", error);
		map.put("message", message);
		map.put("result", mapresponse);
		if(error) {
			result =  gson.toJson(map);
		}else {
			JSONObject output = new JSONObject();
			output.put("error", error);
			output.put("message", message);
			output.put("result", modelUtility.toJSON());
			result = output.toString();
		}
		// Update activity user table LST_USER_SIMULTANEOUS
		customResourceLoader.updateActivity(username);
		// Insert Log LST_HIST_ACTIVITY_WS
		customResourceLoader.insertHistActivityWS(12, 83, new Date(), req, res, 1, resultErr, start, username);
		return result;
	}

	
	public String convertToValue(DetailPolicyAlteration policyAlteration, String key) {
		String str = "";
		// ngak jadi
		
		return str;
		
	}
	
	
	@RequestMapping(value = "/submitpolicyalteration", produces = "application/json", method = RequestMethod.POST)
	public String submitpolicyalteration(@RequestBody String requestViewPolicyAlteration,
			HttpServletRequest request) {
		String result = "";
		Date start = new Date();
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = new Gson();
		gson = builder.create();
		//String req = gson.toJson(requestViewPolicyAlteration);
		String req = requestViewPolicyAlteration;
		String res = null;
		String message = null;
		String resultErr = null;
		Boolean error = false;
		HashMap<String, Object> map = new HashMap<>();
		HashMap<String,Object> mapresponse = new HashMap<String,Object>();
		String username = PolicyAlterationUtility.getFirstLevelJsonValue(req, "username");
		String key = PolicyAlterationUtility.getFirstLevelJsonValue(req, "key");
		String no_polis = PolicyAlterationUtility.getFirstLevelJsonValue(req, "no_polis");
		ArrayList<Exception> internalErrors = new ArrayList<Exception>();
		DetailPolicyAlterationUtility modelUtility  = new DetailPolicyAlterationUtility();
		PolicyAlterationCounter counter = new PolicyAlterationCounter();	
		HashMap<Integer, ArrayList<IndexPolicyAlteration> > listtosubmit = new HashMap<Integer, ArrayList<IndexPolicyAlteration>>();
		HashSet<Integer> inProgress = new HashSet<Integer>();
		try {
			if (customResourceLoader.validateCredential(username, key)) {
				Pemegang paramSelectSPAJ = new Pemegang();
				paramSelectSPAJ.setMspo_policy_no(no_polis);
				Pemegang dataSPAJ = services.selectGetSPAJ(paramSelectSPAJ);
				String reg_spaj = dataSPAJ.getReg_spaj();
				ArrayList<Endorse> inProgressEndorse = services.selectListPolicyAlterationInProcessing(reg_spaj);
				Map<Integer,Endorse> endorseMap = new HashMap<Integer, Endorse>();
				
				for(Endorse e:inProgressEndorse) {
					endorseMap.put(e.getLsje_id(), e);
				}
				
				PolicyAlterationUtility utility = new PolicyAlterationUtility(req, "policyholder,payor,insured,beneficiary,beneficiary_add", new PolicyAlterationListener() {
					
					@Override
					public void listenArray0(DetailPolicyAlteration detailPolicyAlteration, Integer index,String jsonGroup,String keyArray, String key,
							String endorseColumn, String group) {
					
						//if(detailPolicyAlteration != null) {

						Integer lsje_id =0;
						if(detailPolicyAlteration != null) {
							lsje_id = detailPolicyAlteration.getId_endors() == null?0:detailPolicyAlteration.getId_endors();
						};
						IndexPolicyAlteration indexPolicyAlteration = new IndexPolicyAlteration(true, jsonGroup, index, keyArray, key, detailPolicyAlteration,services);
							if(listtosubmit.get(lsje_id) == null) {
								listtosubmit.put(lsje_id, new ArrayList<IndexPolicyAlteration>());
							}
									    
							listtosubmit.get(lsje_id).add(indexPolicyAlteration);
							
//						}
						
						
					}
					
					
					
					@Override
					public void listen(DetailPolicyAlteration detailPolicyAlteration, String jsonGroup, String key,
							String endorseColumn, String group) {
						Integer lsje_id =0;
						if(detailPolicyAlteration != null ) {
						
							lsje_id = detailPolicyAlteration.getId_endors() == null?0:detailPolicyAlteration.getId_endors();
						};
						
						
						IndexPolicyAlteration indexPolicyAlteration = new IndexPolicyAlteration(false, jsonGroup, null, null, key, detailPolicyAlteration,services);
							if(listtosubmit.get(lsje_id) == null) {
								listtosubmit.put(lsje_id, new ArrayList<IndexPolicyAlteration>());
							}
							listtosubmit.get(lsje_id).add(indexPolicyAlteration);
						
					}
					
					@Override
					public void errors(String key, Exception e) {
						// TODO Auto-generated method stub
							internalErrors.add(e);
					}
				}

				
			);
				
				
				
				
				
				
				
				
				utility.processparse();
				
				
				
				
				if(listtosubmit.size()>0) {
				
					for(Integer d:listtosubmit.keySet()) {
						    if(d != null)
						    {						
						    	ArrayList<IndexPolicyAlteration> t = listtosubmit.get(d);
						    	HashMap<String,String> oldvalue = new HashMap<String, String>();
								HashMap<String,String> newvalue = new HashMap<String, String>();
								
								
									    	for(IndexPolicyAlteration g:t) {
									    		DetailPolicyAlteration detailPolicyAlteration = g.getDetailPolicyAlteration();
									    		String jsonGroup = g.getJsonGroup();
								    			Integer index = g.getIndex();
								    			String keyArray = g.getKeyArray();
								    			String keyt = g.getKey();
								    			String keyOld = g.getOldColumn();
								    			String keyNew = g.getNewColumn();
								    			Boolean hookBeforeValue = g.getHookBeforeValue();
								    			String newValue = g.getNewValue();
									    		if(detailPolicyAlteration != null) {
											    		detailPolicyAlteration.setStatus("ALLOWED");
											    		boolean checkingIsInProgess = (endorseMap.get(detailPolicyAlteration.getId_endors()) !=null? true:false);
											    		
											    		if(checkingIsInProgess) {
															detailPolicyAlteration.setStatus("INPROGRESS");
															inProgress.add(d);
														}else {
															String kolom = "";
															oldvalue.put(keyOld, detailPolicyAlteration.getOld());
															
															newvalue.put(keyNew,detailPolicyAlteration.getNew_());
															
															if(hookBeforeValue) {
																newvalue.put(keyNew,newValue);
																	
															}
															
															detailPolicyAlteration.setStatus("SUCCESS");
												    		
															
																									}	
											    		
											    		if(detailPolicyAlteration.getId_endors() == null) {
															detailPolicyAlteration.setStatus("IDENDORSENULL");
														}
									    		}
									    		
									    		if(g.isArray()) {
									    			modelUtility.addArray0(jsonGroup, index, keyArray, keyt, detailPolicyAlteration);
									    		}else {
									    			modelUtility.add(jsonGroup, keyt, detailPolicyAlteration);
													
									    		}
									    	
									    	}
									   
									    	
									    	
									    	
						    	if(d > 0) {
								Integer lsje_id = d;
								Endorse endors = services.selectListJenisEndors(lsje_id);
								String msen_alasan = endors.getLsje_jenis();
								String kolom = "";
						    	String msde_old1 = oldvalue.get("msde_old1")==null?null:oldvalue.get("msde_old1").toString();
								String msde_new1 = newvalue.get("msde_new1")==null?null:newvalue.get("msde_new1").toString();
								
								

								String msde_old2 = oldvalue.get("msde_old2")==null?null:oldvalue.get("msde_old2").toString();
								String msde_new2 = newvalue.get("msde_new2")==null?null:newvalue.get("msde_new2").toString();
								
								String msde_old3 = oldvalue.get("msde_old3")==null?null:oldvalue.get("msde_old3").toString();
								String msde_new3 = newvalue.get("msde_new3")==null?null:newvalue.get("msde_new3").toString();
								

								String msde_old4 = oldvalue.get("msde_old4")==null?null:oldvalue.get("msde_old4").toString();
								String msde_new4 = newvalue.get("msde_new4")==null?null:newvalue.get("msde_new4").toString();
								
								String msde_old5 = oldvalue.get("msde_old5")==null?null:oldvalue.get("msde_old5").toString();
								String msde_new5 = newvalue.get("msde_new5")==null?null:newvalue.get("msde_new5").toString();
					
								String msde_old6 = oldvalue.get("msde_old6")==null?null:oldvalue.get("msde_old6").toString();
								String msde_new6 = newvalue.get("msde_new6")==null?null:newvalue.get("msde_new6").toString();
					
								String msde_old7 = oldvalue.get("msde_old7")==null?null:oldvalue.get("msde_old7").toString();
								String msde_new7 = newvalue.get("msde_new7")==null?null:newvalue.get("msde_new7").toString();
					

								String msde_old8 = oldvalue.get("msde_old8")==null?null:oldvalue.get("msde_old8").toString();
								String msde_new8 = newvalue.get("msde_new8")==null?null:newvalue.get("msde_new8").toString();
								
								String msde_old9 = oldvalue.get("msde_old9")==null?null:oldvalue.get("msde_old9").toString();
								String msde_new9 = newvalue.get("msde_new9")==null?null:newvalue.get("msde_new9").toString();
					
								String msde_old10 = oldvalue.get("msde_old10")==null?null:oldvalue.get("msde_old10").toString();
								String msde_new10 = newvalue.get("msde_new10")==null?null:newvalue.get("msde_new10").toString();
					

								
								
								String msde_old11 = oldvalue.get("msde_old11")==null?null:oldvalue.get("msde_old11").toString();
								String msde_new11 = newvalue.get("msde_new11")==null?null:newvalue.get("msde_new11").toString();
								
								

								String msde_old12 = oldvalue.get("msde_old12")==null?null:oldvalue.get("msde_old12").toString();
								String msde_new12 = newvalue.get("msde_new12")==null?null:newvalue.get("msde_new12").toString();
								
								String msde_old13 = oldvalue.get("msde_old13")==null?null:oldvalue.get("msde_old13").toString();
								String msde_new13 = newvalue.get("msde_new13")==null?null:newvalue.get("msde_new13").toString();
								

								String msde_old14 = oldvalue.get("msde_old14")==null?null:oldvalue.get("msde_old14").toString();
								String msde_new14 = newvalue.get("msde_new14")==null?null:newvalue.get("msde_new14").toString();
								
								String msde_old15 = oldvalue.get("msde_old15")==null?null:oldvalue.get("msde_old15").toString();
								String msde_new15 = newvalue.get("msde_new15")==null?null:newvalue.get("msde_new15").toString();
					
								String msde_old16 = oldvalue.get("msde_old16")==null?null:oldvalue.get("msde_old16").toString();
								String msde_new16 = newvalue.get("msde_new16")==null?null:newvalue.get("msde_new16").toString();
					
								String msde_old17 = oldvalue.get("msde_old17")==null?null:oldvalue.get("msde_old17").toString();
								String msde_new17 = newvalue.get("msde_new17")==null?null:newvalue.get("msde_new17").toString();
					

								String msde_old18 = oldvalue.get("msde_old18")==null?null:oldvalue.get("msde_old18").toString();
								String msde_new18 = newvalue.get("msde_new18")==null?null:newvalue.get("msde_new18").toString();
								
								String msde_old19 = oldvalue.get("msde_old19")==null?null:oldvalue.get("msde_old19").toString();
								String msde_new19 = newvalue.get("msde_new19")==null?null:newvalue.get("msde_new19").toString();
					
								String msde_old20 = oldvalue.get("msde_old20")==null?null:oldvalue.get("msde_old20").toString();
								String msde_new20 = newvalue.get("msde_new20")==null?null:newvalue.get("msde_new20").toString();

								if(!inProgress.contains(d)) {
											if(d == 68 || d == 62 || d == 90 || d==67 || d == 89 || d == 61 || d == 39) {
												for(IndexPolicyAlteration g:t) {
													DetailPolicyAlteration detailPolicyAlteration = g.getDetailPolicyAlteration();
										    		String jsonGroup = g.getJsonGroup();
									    			Integer index = g.getIndex();
									    			String keyArray = g.getKeyArray();
									    			String keyt = g.getKey();
									    			String keyOld = g.getOldColumn();
									    			String keyNew = g.getNewColumn();
									    			String new_ = detailPolicyAlteration.getNew_();
									    			
										    		if(detailPolicyAlteration != null) {
										    			boolean returnofsuccess = directProcess(d, new_,reg_spaj,no_polis,key);
														if(returnofsuccess) {
															detailPolicyAlteration.setStatus("SUCCESS");
														};
												   };
										    		
										    		if(g.isArray()) {
										    			modelUtility.addArray0(jsonGroup, index, keyArray, keyt, detailPolicyAlteration);
										    		}else {
										    			modelUtility.add(jsonGroup, keyt, detailPolicyAlteration);
														
										    		}
										   
												}	
											
												customResourceLoader.PolicyAlterationDirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,msde_old7,msde_old8,msde_old9,msde_old10,
														msde_old11, msde_old12, msde_old13, msde_old14, msde_old15, msde_old16,msde_old17,msde_old18,msde_old19,msde_old20,
														msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6,msde_new7,msde_new8,msde_new9,msde_new10,
														msde_new11, msde_new12, msde_new13, msde_new14, msde_new15, msde_new16,msde_new17,msde_new18,msde_new19,msde_new20
														
														, kolom,counter.getCounter());
												counter.addOne();
												
											
											}else {
											    customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,msde_old7,msde_old8,msde_old9,msde_old10,
											    		msde_old11, msde_old12, msde_old13, msde_old14, msde_old15, msde_old16,msde_old17,msde_old18,msde_old19,msde_old20,
											    		msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6,msde_new7,msde_new8,msde_new9,msde_new10,
											    		msde_new11, msde_new12, msde_new13, msde_new14, msde_new15, msde_new16,msde_new17,msde_new18,msde_new19,msde_new20

											    		, kolom);
											}
											
								};
								
								
						    	};
						    }
					}
					
					
				}
				
				
				
				
				
				
				if(internalErrors.size()<=0) {
					message = "successfuly processed";
				}else {
					error = true;
					message = "Internal error occured, please contact administrator";
				}
				
			}else {
		
				error = true;
				message = "Username & Password is not matched";
				resultErr = ResponseMessage.ERROR_VALIDATION + "(Username: " + username + " & Key: " + key + ")";
				logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: " + resultErr);
			}
		}catch(Exception e) {
			error = true;
			message = ResponseMessage.ERROR_SYSTEM;
			resultErr = "bad exception " + e;
			logger.error("Path: " + request.getServletPath() + " Username: " + username + " Error: " + e);

		
		}

		map.put("error", error);
		map.put("message", message);
		map.put("result", mapresponse);
		if(error) {
			result =  gson.toJson(map);
		}else {
			JSONObject output = new JSONObject();
			output.put("error", error);
			output.put("message", message);
			output.put("result", modelUtility.toJSON());
			result = output.toString();
		}
		// Update activity user table LST_USER_SIMULTANEOUS
		customResourceLoader.updateActivity(username);
		// Insert Log LST_HIST_ACTIVITY_WS
		customResourceLoader.insertHistActivityWS(12, 83, new Date(), req, res, 1, resultErr, start, username);
		return result;
	}
	
	
	public boolean directProcess(Integer lsje_id, String value, String reg_spaj, String no_polis, String key) {
			boolean success = false;
			String valueThatNeedToModified = value;
			Pemegang pemegang = new Pemegang();
			pemegang.setMspo_policy_no(no_polis);
			pemegang = services.selectPemegang(pemegang);
			Tertanggung tertanggung = new Tertanggung();
			tertanggung.setMspo_policy_no(no_polis);
			tertanggung = services.selectTertanggung(tertanggung);
			PolicyAlteration policyAlteration = new PolicyAlteration();
			Integer lsag_id = 6;
			Integer mspe_sts_mrt_new = 1;
			ArrayList<DropdownPolicyAlteration> getListPernikahan = services.selectListPernikahan();
			ArrayList<DropdownPolicyAlteration> getListNegara = services.selectListNegara();
			
			
			switch(lsje_id) {
				case 61: // Perubahan agama pemegang polis
					if(value.equals("ISLAM")) {
						lsag_id = 1;
					} else if(value.equals("KRISTEN PROTESTAN")) {
						lsag_id = 2;
					} else if(value.equals("KRISTEN KATOLIK")) {
						lsag_id = 3;
					} else if(value.equals("BUDHA")) {
						lsag_id = 4;
					} else if(value.equals("HINDU")) {
						lsag_id = 5;
					} else if(value.equals("[NON]")) {
						lsag_id = 0;
					} else if(value.equals("LAIN - LAIN")) {
						lsag_id = 6;
					} 
					if(pemegang != null && pemegang.getMcl_id() != null) {
						policyAlteration.setLsag_id(lsag_id);
						policyAlteration.setMcl_id(pemegang.getMcl_id());
						services.updateAgama(policyAlteration);
						success = true;
					}
				break;
				case 62: // Perubahan agama tertanggung
					if(value.equals("ISLAM")) {
						lsag_id = 1;
					} else if(value.equals("KRISTEN PROTESTAN")) {
						lsag_id = 2;
					} else if(value.equals("KRISTEN KATOLIK")) {
						lsag_id = 3;
					} else if(value.equals("BUDHA")) {
						lsag_id = 4;
					} else if(value.equals("HINDU")) {
						lsag_id = 5;
					} else if(value.equals("[NON]")) {
						lsag_id = 0;
					} else if(value.equals("LAIN - LAIN")) {
						lsag_id = 6;
					} 
					if(tertanggung != null && tertanggung.getMcl_id() != null) {
					policyAlteration.setLsag_id(lsag_id);
					policyAlteration.setMcl_id(tertanggung.getMcl_id());
					services.updateAgama(policyAlteration);
					success = true;
					}
				break;
				case 67:
		
					if(pemegang != null) {
						try {
							policyAlteration.setMcl_id(pemegang.getMcl_id());
							policyAlteration.setMspe_sts_mrt(Integer.parseInt(value));
							services.updateStatus(policyAlteration);
							success = true;
						}catch(NumberFormatException e) {
							
						}
				}
					
				break;
				case 68:
					for(DropdownPolicyAlteration pernikahan: getListPernikahan) {
						Integer lsst_id = pernikahan.getLsst_id();
						String lsst_name = pernikahan.getLsst_name();
						if(lsst_name.equalsIgnoreCase(value.trim())) {
							mspe_sts_mrt_new = lsst_id;
							break;
						}
					}
					
					if(tertanggung != null) {
						try {
						policyAlteration.setMcl_id(tertanggung.getMcl_id());
						policyAlteration.setMspe_sts_mrt(Integer.parseInt(value));
						
						services.updateStatus(policyAlteration);
						success = true;
						}catch(NumberFormatException e) {
							
						}
					}
					
					
				break;	
				
				
				
				case 90:
					if(key != null) {
					  if(key.equalsIgnoreCase("tipe_usaha_tt")) {
						  
						  if(tertanggung != null) {
							      policyAlteration.setMcl_id(tertanggung.getMcl_id());
							      policyAlteration.setTipe_usaha_pp(value);
								  services.updateJenisCompanyPekerjaan(policyAlteration);
								  success = true;
						  }
						  
					  }else if(key.equalsIgnoreCase("jabatan_tt")) {
						  if(tertanggung != null) {
						      policyAlteration.setMcl_id(tertanggung.getMcl_id());
						      policyAlteration.setMpn_job_desc(value);
							  services.updatejobdesc(policyAlteration);
							  success = true;
					  }
					  
						  
					  }else if(key.equalsIgnoreCase("nama_perusahaan_tt")) {
						  policyAlteration.setMcl_id(tertanggung.getMcl_id());
					      policyAlteration.setNama_perusahaan_pp(value);
						  services.updateCompanyPekerjaan(policyAlteration);
						  success = true;
				
					  }
					  
					};
					
				break;
					
				case 89:
					
					if(key != null) {
						  if(key.equalsIgnoreCase("tipe_usaha_pp")) {
							  
							  if(pemegang != null) {
								      policyAlteration.setMcl_id(pemegang.getMcl_id());
								      policyAlteration.setTipe_usaha_pp(value);
									  services.updateJenisCompanyPekerjaan(policyAlteration);
									  success = true;
							  }
							  
						  }else if(key.equalsIgnoreCase("jabatan_pp")) {
							  if(pemegang != null) {
							      policyAlteration.setMcl_id(pemegang.getMcl_id());
							      policyAlteration.setMpn_job_desc(value);
								  services.updatejobdesc(policyAlteration);
								  success = true;
						  }
							  
						  }else if(key.equalsIgnoreCase("nama_perusahaan_pp")) {
							 if(pemegang != null) {
								  policyAlteration.setMcl_id(pemegang.getMcl_id());
							      policyAlteration.setNama_perusahaan_pp(value);
								  services.updateCompanyPekerjaan(policyAlteration);
								  success = true;
							 };
					
						  }
						  
						};
					
				
					break;
					
				case 34:
					if(pemegang != null) {
						policyAlteration.setMcl_id(pemegang.getMcl_id());
						policyAlteration.setAlamat_kantor(value);
						services.updateAlamatKantor(policyAlteration);
						success = true;
					}
				break;
				case 39:
					if(pemegang != null) {
						Integer lsne_id = 0;
						for(DropdownPolicyAlteration n:getListNegara) {
							 lsne_id = n.getLsne_id();
							String lsne_name = n.getLsne_note();
							if(lsne_name.equalsIgnoreCase(value.trim())) {
								break;
								
							}
						}
						if(lsne_id > 0)
						{
						policyAlteration.setMcl_id(pemegang.getMcl_id());
						policyAlteration.setLsne_id(lsne_id);
						services.updateKewarganegaraan(policyAlteration);
						success = true;
						}
					}
				
				break;
			}
			
			return success;
	}
}
