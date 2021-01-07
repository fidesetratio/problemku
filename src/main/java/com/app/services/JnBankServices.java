package com.app.services;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import com.app.mapper.JnBankMapper;
import com.app.model.JnBank;

@Service
public class JnBankServices {
	@Autowired
	private SqlSession sqlSession;
	
	@Autowired
	private PlatformTransactionManager transactionManager;
	
	public List<JnBank> getAll(){
		JnBankMapper dao=sqlSession.getMapper(JnBankMapper.class);
		return dao.getAll();
	}
}
