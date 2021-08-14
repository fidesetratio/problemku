package com.app.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONObject;

import com.app.services.VegaServices;

public class EndorsePolicyAlteration implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5577393152312579813L;
	private String msen_endors_no;
	private String reg_spaj;
	private Integer msen_internal;
	private String msen_alasan;
	private Date msen_input_date;
	private Integer msen_endors_cost;
	private Date msen_active_date;
    private Integer msen_print;
	private Integer lspd_id;
	private Integer msen_tahun_ke;
	private Integer msen_premi_ke;
	private Integer msen_prod_ke;
	private Integer lus_id;
	private Integer msen_ke;
	private Date msen_tgl_trans;
    private Integer msen_auto_rider;
    private Integer flag_ps;
    private Integer msen_proses_bsm;
    private Integer msen_flag_uw;
    private Integer msen_flag_ttp;
    private Date msen_tgl_rk;
    private String msen_no_ssk;
    private Integer msen_aksep_uw;
    private Integer msen_user_aksep;
    private Date msen_tgl_aksep;
    private Integer msen_issue_spaj;
    private Integer msen_reproses;
    private Date msen_tgl_filing;
    
    private String tgl_awal;
    private String tgl_akhir;
    private String input_date;
    
    private Integer lsje_id;
    private String lsje_jenis;
    private String lsje_status;
    private String status;
    private String grouping;
    
    
    private String msde_new1;
    private String msde_new2;
    private String msde_new3;
    private String msde_new4;
    private String msde_new5;
    private String msde_new6;
    private String msde_new7;
    private String msde_new8;
   
    private String msde_new9;
    private String msde_new10;
    private String msde_new11;
    private String msde_new12;
    private String msde_new13;
    private String msde_new14;
    private String msde_new15;
    private String msde_new16;
    private String msde_new17;
    private String msde_new18;
    private String msde_new19;
    private String msde_new20;
    private String msde_new21;
    private String msde_new22;
    private String msde_new23;
    private String msde_new24;
    private String msde_new25;
    
    
    
    
    
    private String key;
    private String value;
    
    
    
    
    public HashMap<String,String> convertToMsdeNewHashMap(){
    	HashMap<String,String> map = new HashMap<String, String>();
    	if(msde_new1 != null) {
    			map.put("msde_new1", this.msde_new1);
    	}
    	if(msde_new2 != null) {
			map.put("msde_new2", this.msde_new2);
	}
    	if(msde_new3 != null) {

			map.put("msde_new3", this.msde_new3);
	}
    	if(msde_new4 != null) {
			map.put("msde_new4", this.msde_new4);
	}
    	if(msde_new5 != null) {
			map.put("msde_new5", this.msde_new5);
	}
    
    	if(msde_new6 != null) {
			map.put("msde_new6", this.msde_new6);
	}
    	if(msde_new7 != null) {
			map.put("msde_new7", this.msde_new7);
	}
    	if(msde_new8 != null) {
			map.put("msde_new8", this.msde_new8);
	}
    	
    	if(msde_new9 != null) {
			map.put("msde_new9", this.msde_new9);
	}
    	if(msde_new10 != null) {
			map.put("msde_new10", this.msde_new10);
	}
    	if(msde_new11 != null) {
			map.put("msde_new11", this.msde_new11);
	}
    	if(msde_new12 != null) {
			map.put("msde_new12", this.msde_new12);
	}
    	if(msde_new13 != null) {
			map.put("msde_new13", this.msde_new13);
	}
    	if(msde_new14 != null) {
			map.put("msde_new14", this.msde_new14);
	}
    	if(msde_new15 != null) {
			map.put("msde_new15", this.msde_new15);
	}
    	if(msde_new16 != null) {
			map.put("msde_new16", this.msde_new16);
	}
    	if(msde_new17 != null) {
			map.put("msde_new17", this.msde_new17);
	}
    
    	if(msde_new18 != null) {
			map.put("msde_new18", this.msde_new18);
	}	if(msde_new19 != null) {
		map.put("msde_new19", this.msde_new19);
}	if(msde_new20 != null) {
	map.put("msde_new20", this.msde_new20);
}	if(msde_new21 != null) {
	map.put("msde_new21", this.msde_new21);
}	if(msde_new22 != null) {
	map.put("msde_new22", this.msde_new22);
}
if(msde_new23 != null) {
	map.put("msde_new23", this.msde_new23);
}if(msde_new24 != null) {
	map.put("msde_new24", this.msde_new24);
}if(msde_new25 != null) {
	map.put("msde_new25", this.msde_new25);
}
    	return map;
    }
    public static JSONObject toJSonObject(EndorsePolicyAlteration endorse) {
    	JSONObject obj =new JSONObject();
    	obj.put("grouping", endorse.getGrouping());
    	obj.put("input_date", endorse.getInput_date());
    	obj.put("status", endorse.getStatus());
    	obj.put("lsje_jenis", endorse.getLsje_jenis());
    	obj.put("lsje_id", endorse.getLsje_id());
    	obj.put("msen_alasan", endorse.getMsen_alasan());
    	obj.put("msen_endorse_no", endorse.getMsen_endors_no());
    	obj.put("key", endorse.getKey());
    	obj.put("value", endorse.getValue());
    	
        	
    	return obj;
    	
    }
    
    

    
	public String getGrouping() {
		return grouping;
	}
	public void setGrouping(String grouping) {
		this.grouping = grouping;
	}
	public String getLsje_status() {
		return lsje_status;
	}
	public void setLsje_status(String lsje_status) {
		this.lsje_status = lsje_status;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getLsje_id() {
		return lsje_id;
	}
	public void setLsje_id(Integer lsje_id) {
		this.lsje_id = lsje_id;
	}
	public String getLsje_jenis() {
		return lsje_jenis;
	}
	public void setLsje_jenis(String lsje_jenis) {
		this.lsje_jenis = lsje_jenis;
	}
	public String getMsen_endors_no() {
		return msen_endors_no;
	}
	public void setMsen_endors_no(String msen_endors_no) {
		this.msen_endors_no = msen_endors_no;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public Integer getMsen_internal() {
		return msen_internal;
	}
	public void setMsen_internal(Integer msen_internal) {
		this.msen_internal = msen_internal;
	}
	public String getMsen_alasan() {
		return msen_alasan;
	}
	public void setMsen_alasan(String msen_alasan) {
		this.msen_alasan = msen_alasan;
	}
	public Date getMsen_input_date() {
		return msen_input_date;
	}
	public void setMsen_input_date(Date msen_input_date) {
		this.msen_input_date = msen_input_date;
	}
	public Integer getMsen_endors_cost() {
		return msen_endors_cost;
	}
	public void setMsen_endors_cost(Integer msen_endors_cost) {
		this.msen_endors_cost = msen_endors_cost;
	}
	public Date getMsen_active_date() {
		return msen_active_date;
	}
	public void setMsen_active_date(Date msen_active_date) {
		this.msen_active_date = msen_active_date;
	}
	public Integer getMsen_print() {
		return msen_print;
	}
	public void setMsen_print(Integer msen_print) {
		this.msen_print = msen_print;
	}
	public Integer getLspd_id() {
		return lspd_id;
	}
	public void setLspd_id(Integer lspd_id) {
		this.lspd_id = lspd_id;
	}
	public Integer getMsen_tahun_ke() {
		return msen_tahun_ke;
	}
	public void setMsen_tahun_ke(Integer msen_tahun_ke) {
		this.msen_tahun_ke = msen_tahun_ke;
	}
	public Integer getMsen_premi_ke() {
		return msen_premi_ke;
	}
	public void setMsen_premi_ke(Integer msen_premi_ke) {
		this.msen_premi_ke = msen_premi_ke;
	}
	public Integer getMsen_prod_ke() {
		return msen_prod_ke;
	}
	public void setMsen_prod_ke(Integer msen_prod_ke) {
		this.msen_prod_ke = msen_prod_ke;
	}
	public Integer getLus_id() {
		return lus_id;
	}
	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
	}
	public Integer getMsen_ke() {
		return msen_ke;
	}
	public void setMsen_ke(Integer msen_ke) {
		this.msen_ke = msen_ke;
	}
	public Date getMsen_tgl_trans() {
		return msen_tgl_trans;
	}
	public void setMsen_tgl_trans(Date msen_tgl_trans) {
		this.msen_tgl_trans = msen_tgl_trans;
	}
	public Integer getMsen_auto_rider() {
		return msen_auto_rider;
	}
	public void setMsen_auto_rider(Integer msen_auto_rider) {
		this.msen_auto_rider = msen_auto_rider;
	}
	public Integer getFlag_ps() {
		return flag_ps;
	}
	public void setFlag_ps(Integer flag_ps) {
		this.flag_ps = flag_ps;
	}
	public Integer getMsen_proses_bsm() {
		return msen_proses_bsm;
	}
	public void setMsen_proses_bsm(Integer msen_proses_bsm) {
		this.msen_proses_bsm = msen_proses_bsm;
	}
	public Integer getMsen_flag_uw() {
		return msen_flag_uw;
	}
	public void setMsen_flag_uw(Integer msen_flag_uw) {
		this.msen_flag_uw = msen_flag_uw;
	}
	public Integer getMsen_flag_ttp() {
		return msen_flag_ttp;
	}
	public void setMsen_flag_ttp(Integer msen_flag_ttp) {
		this.msen_flag_ttp = msen_flag_ttp;
	}
	public Date getMsen_tgl_rk() {
		return msen_tgl_rk;
	}
	public void setMsen_tgl_rk(Date msen_tgl_rk) {
		this.msen_tgl_rk = msen_tgl_rk;
	}
	public String getMsen_no_ssk() {
		return msen_no_ssk;
	}
	public void setMsen_no_ssk(String msen_no_ssk) {
		this.msen_no_ssk = msen_no_ssk;
	}
	public Integer getMsen_aksep_uw() {
		return msen_aksep_uw;
	}
	public void setMsen_aksep_uw(Integer msen_aksep_uw) {
		this.msen_aksep_uw = msen_aksep_uw;
	}
	public Integer getMsen_user_aksep() {
		return msen_user_aksep;
	}
	public void setMsen_user_aksep(Integer msen_user_aksep) {
		this.msen_user_aksep = msen_user_aksep;
	}
	public Date getMsen_tgl_aksep() {
		return msen_tgl_aksep;
	}
	public void setMsen_tgl_aksep(Date msen_tgl_aksep) {
		this.msen_tgl_aksep = msen_tgl_aksep;
	}
	public Integer getMsen_issue_spaj() {
		return msen_issue_spaj;
	}
	public void setMsen_issue_spaj(Integer msen_issue_spaj) {
		this.msen_issue_spaj = msen_issue_spaj;
	}
	public Integer getMsen_reproses() {
		return msen_reproses;
	}
	public void setMsen_reproses(Integer msen_reproses) {
		this.msen_reproses = msen_reproses;
	}
	public Date getMsen_tgl_filing() {
		return msen_tgl_filing;
	}
	public void setMsen_tgl_filing(Date msen_tgl_filing) {
		this.msen_tgl_filing = msen_tgl_filing;
	}
	public String getTgl_akhir() {
		return tgl_akhir;
	}
	public void setTgl_akhir(String tgl_akhir) {
		this.tgl_akhir = tgl_akhir;
	}
	public String getTgl_awal() {
		return tgl_awal;
	}
	public void setTgl_awal(String tgl_awal) {
		this.tgl_awal = tgl_awal;
	}
	public String getInput_date() {
		return input_date;
	}
	public void setInput_date(String input_date) {
		this.input_date = input_date;
	}




	public String getMsde_new1() {
		return msde_new1;
	}




	public void setMsde_new1(String msde_new1) {
		this.msde_new1 = msde_new1;
	}




	public String getMsde_new2() {
		return msde_new2;
	}




	public void setMsde_new2(String msde_new2) {
		this.msde_new2 = msde_new2;
	}




	public String getMsde_new3() {
		return msde_new3;
	}




	public void setMsde_new3(String msde_new3) {
		this.msde_new3 = msde_new3;
	}




	public String getMsde_new4() {
		return msde_new4;
	}




	public void setMsde_new4(String msde_new4) {
		this.msde_new4 = msde_new4;
	}




	public String getMsde_new5() {
		return msde_new5;
	}




	public void setMsde_new5(String msde_new5) {
		this.msde_new5 = msde_new5;
	}




	public String getMsde_new6() {
		return msde_new6;
	}




	public void setMsde_new6(String msde_new6) {
		this.msde_new6 = msde_new6;
	}




	public String getMsde_new7() {
		return msde_new7;
	}




	public void setMsde_new7(String msde_new7) {
		this.msde_new7 = msde_new7;
	}




	public String getMsde_new8() {
		return msde_new8;
	}




	public void setMsde_new8(String msde_new8) {
		this.msde_new8 = msde_new8;
	}




	public String getKey() {
		return key;
	}




	public void setKey(String key) {
		this.key = key;
	}




	public String getValue() {
		return value;
	}




	public void setValue(String value) {
		this.value = value;
	}
	public String getMsde_new9() {
		return msde_new9;
	}
	public void setMsde_new9(String msde_new9) {
		this.msde_new9 = msde_new9;
	}
	public String getMsde_new10() {
		return msde_new10;
	}
	public void setMsde_new10(String msde_new10) {
		this.msde_new10 = msde_new10;
	}
	public String getMsde_new11() {
		return msde_new11;
	}
	public void setMsde_new11(String msde_new11) {
		this.msde_new11 = msde_new11;
	}
	public String getMsde_new12() {
		return msde_new12;
	}
	public void setMsde_new12(String msde_new12) {
		this.msde_new12 = msde_new12;
	}
	public String getMsde_new13() {
		return msde_new13;
	}
	public void setMsde_new13(String msde_new13) {
		this.msde_new13 = msde_new13;
	}
	public String getMsde_new14() {
		return msde_new14;
	}
	public void setMsde_new14(String msde_new14) {
		this.msde_new14 = msde_new14;
	}
	public String getMsde_new15() {
		return msde_new15;
	}
	public void setMsde_new15(String msde_new15) {
		this.msde_new15 = msde_new15;
	}
	public String getMsde_new16() {
		return msde_new16;
	}
	public void setMsde_new16(String msde_new16) {
		this.msde_new16 = msde_new16;
	}
	public String getMsde_new17() {
		return msde_new17;
	}
	public void setMsde_new17(String msde_new17) {
		this.msde_new17 = msde_new17;
	}
	public String getMsde_new18() {
		return msde_new18;
	}
	public void setMsde_new18(String msde_new18) {
		this.msde_new18 = msde_new18;
	}
	public String getMsde_new19() {
		return msde_new19;
	}
	public void setMsde_new19(String msde_new19) {
		this.msde_new19 = msde_new19;
	}
	public String getMsde_new20() {
		return msde_new20;
	}
	public void setMsde_new20(String msde_new20) {
		this.msde_new20 = msde_new20;
	}
	public String getMsde_new21() {
		return msde_new21;
	}
	public void setMsde_new21(String msde_new21) {
		this.msde_new21 = msde_new21;
	}
	public String getMsde_new22() {
		return msde_new22;
	}
	public void setMsde_new22(String msde_new22) {
		this.msde_new22 = msde_new22;
	}
	public String getMsde_new23() {
		return msde_new23;
	}
	public void setMsde_new23(String msde_new23) {
		this.msde_new23 = msde_new23;
	}
	public String getMsde_new24() {
		return msde_new24;
	}
	public void setMsde_new24(String msde_new24) {
		this.msde_new24 = msde_new24;
	}
	public String getMsde_new25() {
		return msde_new25;
	}
	public void setMsde_new25(String msde_new25) {
		this.msde_new25 = msde_new25;
	}
}