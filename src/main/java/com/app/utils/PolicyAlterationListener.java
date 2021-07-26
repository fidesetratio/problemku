package com.app.utils;

import java.util.List;

import com.app.model.DetailPolicyAlteration;

public interface PolicyAlterationListener {
	public void listen(DetailPolicyAlteration detailPolicyAlteration, String jsonGroup, String key, String endorseColumn,String group);
	public  void errors(String key,Exception e);
	public void listenArray0(DetailPolicyAlteration detailPolicyAlteration,Integer index,String jsonGroup,String keyArray,String key, String endorseColumn, String group);
	

}
