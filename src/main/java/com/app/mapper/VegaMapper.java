package com.app.mapper;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

import com.app.model.Article;
import com.app.model.LstHistActivityWS;
import com.app.model.LstUserSimultaneous;
import com.app.model.MpolisConfiguration;
import com.app.model.Provinsi;
import com.app.model.User;
import com.app.model.UserCorporate;

public interface VegaMapper {
	public String selectEncrypt(String value);
	
	// Stored Procedure
	
	//Insert
	
	public void insertLstHistActvWs(LstHistActivityWS lstHistActivityWS);
	
	//Select
	
	public LstUserSimultaneous selectLoginAuthenticate(LstUserSimultaneous lstUserSimultaneous);
	
	public ArrayList<MpolisConfiguration> selectMpolisConfiguration();
	
	public LstUserSimultaneous selectDataLstUserSimultaneous(String username);
	
	public ArrayList<UserCorporate> selectListPolisCorporate(HashMap<String, Object> hashMap);
	
	public User selectUserIndividual(String username);
	
	public Integer selectCountDeathClaim(String username);

	public ArrayList<User> selectDetailedPolis(String username);
	
	public UserCorporate selectUserCorporate(String username);
	
	public User decryptPassword(User user);
	
	public Provinsi selectProvinsi2(HashMap<String, Object> hashMap);

	public Integer selectCheckDate(String dateVal);

	public String selectCutoffTransactionLink();

	public String selectCutoffTransactionMagnaAndPrimeLink();
	
	public ArrayList<Article> selectListArticle(HashMap<String, Object> hashMap);
	
	//Update

	public void updateUserKeyName(LstUserSimultaneous user);

	public void updateActivityStatus(User user);
}
