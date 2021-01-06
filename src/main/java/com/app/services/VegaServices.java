package com.app.services;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.mapper.VegaMapper;

@Component
public class VegaServices {
	@Autowired
	private SqlSession sqlSession;
	
	
	public String selectEncrypt(String value) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectEncrypt(value);
	}

}
