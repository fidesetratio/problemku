package com.app.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.app.model.SavedProvider;
import com.app.services.VegaServices;

@RestController
public class ProviderController {


	private static final Logger logger = LogManager.getLogger(ProviderController.class);

	@Autowired
	private VegaServices services;

	@RequestMapping(value = "/saveprovider", produces = "application/json", method = RequestMethod.POST)
	public String saveProvider(@RequestBody SavedProvider savedProvider,HttpServletRequest request) {
		String ok = "Ok";
		try {
			services.insertSavedProvider(savedProvider);
		}catch(Exception e) {
			ok = "There is problem with connection please contact our administrator";
			logger.error("Path: " + request.getServletPath() + " Error: " + e);

		}
		
		return ok;
	}

	
	@RequestMapping(value = "/searchbyusername", produces = "application/json", method = RequestMethod.POST)
	public List<SavedProvider> searchbyusername(@RequestBody SearchProvider searchProvider,HttpServletRequest request) {
		List<SavedProvider> provider = new ArrayList<SavedProvider>();
		
		try
		{
			provider = services.selectproviderbyusername(searchProvider.getUsername());
			
		}catch(Exception e) {
			logger.error("Path: " + request.getServletPath() + " Error: " + e);

		}
		
		
		return provider;
	}
	
	
	
   private static class SearchProvider{
	    private String username;

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}
   }
}
