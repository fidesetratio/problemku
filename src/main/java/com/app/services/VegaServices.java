package com.app.services;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.mapper.VegaMapper;
import com.app.model.Article;
import com.app.model.LstHistActivityWS;
import com.app.model.LstUserSimultaneous;
import com.app.model.MpolisConfiguration;
import com.app.model.Provinsi;
import com.app.model.User;
import com.app.model.UserCorporate;

@Component
public class VegaServices {
	@Autowired
	private SqlSession sqlSession;
	
	// Insert
	public void insertLstHistActvWs(LstHistActivityWS lstHistActivityWS) throws Exception {
		try {
			VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
			dao.insertLstHistActvWs(lstHistActivityWS);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//Select
	
	public String selectEncrypt(String value) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectEncrypt(value);
	}
	
	public LstUserSimultaneous selectLoginAuthenticate(LstUserSimultaneous lstUserSimultaneous) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectLoginAuthenticate(lstUserSimultaneous);
	}
	
	public HashMap<String, Object> configuration() {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> params = new HashMap<>();
		List<MpolisConfiguration> list = dao.selectMpolisConfiguration();
		for (MpolisConfiguration c : list) {
			params.put(c.getConf_name(), c.getConf_value());
		}
		return params;
	}
	
	public LstUserSimultaneous selectDataLstUserSimultaneous(String username) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectDataLstUserSimultaneous(username);
	}
	
	public ArrayList<UserCorporate> selectListPolisCorporate(String mcl_id_employee) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mcl_id_employee", mcl_id_employee);
		return dao.selectListPolisCorporate(hashMap);
	}
	
	public User selectUserIndividual(String username) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectUserIndividual(username);
	}
	
	public Integer selectCountDeathClaim(String username) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectCountDeathClaim(username);
	}
	
	public ArrayList<User> selectDetailedPolis(String username) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectDetailedPolis(username);
	}
	
	public UserCorporate selectUserCorporate(String username) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectUserCorporate(username);
	}
	
	public User decryptPassword(User user) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.decryptPassword(user);
	}
	
	public Provinsi selectProvinsi2(Integer lspr_id, Integer lska_id, Integer lskc_id, Integer lskl_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("lspr_id", lspr_id);
		hashMap.put("lska_id", lska_id);
		hashMap.put("lskc_id", lskc_id);
		hashMap.put("lskl_id", lskl_id);
		return dao.selectProvinsi2(hashMap);
	}
	
	public Integer selectCheckDate(String dateVal) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectCheckDate(dateVal);
	}
	
	public String selectCutoffTransactionLink() {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectCutoffTransactionLink();
	};

	public String selectCutoffTransactionMagnaAndPrimeLink() {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectCutoffTransactionMagnaAndPrimeLink();
	}
	
	public ArrayList<Article> selectListArticle(Integer pageNumber, Integer pageSize) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("pageNumber", pageNumber);
		hashMap.put("pageSize", pageSize);
		return dao.selectListArticle(hashMap);
	}
	
	// Update
	public void updateUserKeyName(LstUserSimultaneous user) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.updateUserKeyName(user);
	}
	
	public void updateActivityStatus(User user) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.updateActivityStatus(user);
	}
}
