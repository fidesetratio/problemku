package com.app.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.app.feignclient.ServiceNotification;
import com.app.model.DetailPolicyAlteration;
import com.app.model.Endorse;
import com.app.model.Pemegang;
import com.app.services.VegaServices;
import com.app.services.VegaServicesProd;
import com.app.utils.DetailPolicyAlterationUtility;
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
								}else {
									 Boolean direct = (detailPolicyAlteration.getFlag_direct()==1?true:false);
									 if(direct) {
											detailPolicyAlteration.setStatus("SUCCESS");
										 
									 }else {
										    String old = detailPolicyAlteration.getOld();
											String new_ = detailPolicyAlteration.getNew_();
											Integer flag_direct = detailPolicyAlteration.getFlag_direct();
											String kolom = endorseColumn;
											Integer lsje_id = detailPolicyAlteration.getId_endors();
											Endorse endors = services.selectListJenisEndors(lsje_id);
											String msen_alasan = endors.getLsje_jenis();
											String msde_old1 = old;
											String msde_new1 = new_;
											String msde_new6 = null;
											String msde_old2 = null;
											String msde_old5 = null;
											String msde_old3 = null;
											String msde_old6 = null;
										
											String msde_old4 = null;
											String msde_new4 = null;
									        String msde_new2 =null;
										    String msde_new3 = null;
										    String msde_new5 = null;
										customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
													msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6, kolom);
										detailPolicyAlteration.setStatus("SUCCESS");
										
									 }
									 
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
										}else {
											 Boolean direct = (detailPolicyAlteration.getFlag_direct()==1?true:false);
											 if(direct) {
											 }else {
												 
												    String old = detailPolicyAlteration.getOld();
													String new_ = detailPolicyAlteration.getNew_();
													Integer flag_direct = detailPolicyAlteration.getFlag_direct();
													String kolom = endorseColumn;
													
													Integer lsje_id = detailPolicyAlteration.getId_endors();
													Endorse endors = services.selectListJenisEndors(lsje_id);
													String msen_alasan = endors.getLsje_jenis();
													String msde_old1 = old;
													String msde_new1 = new_;
													String msde_new6 = null;
													String msde_old2 = null;
													String msde_old5 = null;
													String msde_old3 = null;
													String msde_old6 = null;
												
													String msde_old4 = null;
													String msde_new4 = null;
											        String msde_new2 =null;
												    String msde_new3 = null;
												    String msde_new5 = null;
												customResourceLoader.PolicyAlterationIndirect(reg_spaj, msen_alasan, lsje_id, msde_old1, msde_old2, msde_old3, msde_old4, msde_old5, msde_old6,
															msde_new1, msde_new2, msde_new3, msde_new4, msde_new5, msde_new6, kolom);
											
												detailPolicyAlteration.setStatus("SUCCESS");
												
											 }

												
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
	
	
}
