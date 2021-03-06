package com.app.services;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.app.model.*;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.mapper.VegaMapper;
import com.app.model.request.RequestTrackingPolis;

@Component
public class VegaServices {
	@Autowired
	private SqlSession sqlSession;
	//VegaMapper

	// Stored Procedure
	public void storedProcedureGetBiaya(String reg_spaj, String kode_regis, Integer kode_trans, BigDecimal amount,
			Integer proses, Integer flag_insert) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("reg_spaj", reg_spaj);
		params.put("kode_regis", kode_regis);
		params.put("kode_trans", kode_trans);
		params.put("amount", amount);
		params.put("proses", proses);
		params.put("flag_insert", flag_insert);
		params.put("hasil", null);
		dao.storedProcedureGetBiaya(params);
	}

	public void storedProcedureSubmitFinancialTransaction(String reg_spaj, String kode_trans) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("kode_trans", kode_trans);
		hashMap.put("hasil", null);
		dao.storedProcedureSubmitFinancialTransaction(hashMap);
	}

	// Insert
	public void insertLstHistActvWs(LstHistActivityWS lstHistActivityWS) throws Exception {
		try {
			VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
			dao.insertLstHistActvWs(lstHistActivityWS);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void insertSmsServerOut(SmsServerOut smsOut) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.insertSmsServerOut(smsOut);
	}

	public void insertNewuser(LstUserSimultaneous lstUserSimultaneous) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.insertNewuser(lstUserSimultaneous);
	}

	public void insertMstMpolTrans(Topup topup) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.insertMstMpolTrans(topup);
	}

	public void insertMstMpolTransBill(DetailBillingRequest billingRequest) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.insertMstMpolTransBill(billingRequest);
	}

	public void insertMstMpolTransDet(Topup topup) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.insertMstMpolTransDet(topup);
	}

	public void insertSwitching(String mpt_id_switching, String date_created_java1, String reg_spaj, Integer lt_id,
			String lku_id, BigDecimal mpt_jumlah, BigDecimal mpt_unit, String date_created_java2, String payor_name,
			String working_days) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mpt_id_switching", mpt_id_switching);
		hashMap.put("date_created_java1", date_created_java1);
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("lt_id", lt_id);
		hashMap.put("lku_id", lku_id);
		hashMap.put("mpt_jumlah", mpt_jumlah);
		hashMap.put("mpt_unit", mpt_unit);
		hashMap.put("date_created_java2", date_created_java2);
		hashMap.put("payor_name", payor_name);
		hashMap.put("working_days", working_days);
		dao.insertSwitching(hashMap);
//			return (Integer) hashMap.get("MPT_ID");
	}

	public void insertEndorse(String msen_endors_no, String reg_spaj, String msen_alasan, Integer lspd_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("msen_endors_no", msen_endors_no);
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("msen_alasan", msen_alasan);
		hashMap.put("lspd_id", lspd_id);
		dao.insertEndorse(hashMap);
	}

	public void insertRedirection(String mpt_id_redirection, String date_created_java1, String reg_spaj, Integer lt_id,
			String lku_id, String date_created_java2, String payor_name, String mpt_id_switching) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mpt_id_redirection", mpt_id_redirection);
		hashMap.put("date_created_java1", date_created_java1);
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("lt_id", lt_id);
		hashMap.put("lku_id", lku_id);
		hashMap.put("date_created_java2", date_created_java2);
		hashMap.put("payor_name", payor_name);
		hashMap.put("mpt_id_switching", mpt_id_switching);
		dao.insertRedirection(hashMap);
//			return (Integer) hashMap.get("MPT_ID");
	}

	public void insertDetailSwitching(String mpt_id, String lji_id, Integer mpt_persen, BigDecimal mpt_jumlah,
			BigDecimal mpt_unit, String mpt_dk, String lji_id_ke, Integer persen_ke, BigDecimal jumlah_ke, BigDecimal unit_ke) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mpt_id", mpt_id);
		hashMap.put("lji_id", lji_id);
		hashMap.put("mpt_persen", mpt_persen);
		hashMap.put("mpt_jumlah", mpt_jumlah);
		hashMap.put("mpt_unit", mpt_unit);
		hashMap.put("mpt_dk", mpt_dk);
		hashMap.put("lji_id_ke", lji_id_ke);
		hashMap.put("persen_ke", persen_ke);
		hashMap.put("jumlah", jumlah_ke);
		hashMap.put("unit_ke", unit_ke);
		dao.insertDetailSwitching(hashMap);
	}

	public void insertDetailEndorse(String msen_endors_no, Integer lsje_id, String msde_old1, String msde_old2, String msde_old3, String msde_old4, String msde_old5, String msde_old6,String msde_old7,String msde_old8,
			String msde_old9,String msde_old10,String msde_old11,String msde_old12,String msde_old13,String msde_old14,String msde_old15,String msde_old16,String msde_old17,String msde_old18,String msde_old19,String msde_old20
			,String msde_old21,String msde_old22,String msde_old23,String msde_old24,String msde_old25
			, String msde_new1, String msde_new2, String msde_new3, String msde_new4, String msde_new5, String msde_new6,String msde_new7,String msde_new8,String msde_new9,String msde_new10,String msde_new11
			,String msde_new12,String msde_new13,String msde_new14,String msde_new15,String msde_new16,String msde_new17,String msde_new18,String msde_new19,String msde_new20
			,String msde_new21,String msde_new22,String msde_new23,String msde_new24,String msde_new25, boolean isUpdate
			) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("msen_endors_no", msen_endors_no);
		hashMap.put("lsje_id", lsje_id);
		hashMap.put("msde_old1", msde_old1);
		hashMap.put("msde_old2", msde_old2);
		hashMap.put("msde_old3", msde_old3);
		hashMap.put("msde_old4", msde_old4);
		hashMap.put("msde_old5", msde_old5);
		hashMap.put("msde_old6", msde_old6);
		hashMap.put("msde_old7", msde_old7);
		hashMap.put("msde_old8", msde_old8);
		hashMap.put("msde_old9", msde_old9);
		hashMap.put("msde_old10", msde_old10);
		hashMap.put("msde_old11", msde_old11);
		hashMap.put("msde_old12", msde_old12);
		hashMap.put("msde_old13", msde_old13);
		hashMap.put("msde_old14", msde_old14);
		hashMap.put("msde_old15", msde_old15);
		hashMap.put("msde_old16", msde_old16);
		hashMap.put("msde_old17", msde_old17);
		hashMap.put("msde_old18", msde_old18);
		hashMap.put("msde_old19", msde_old19);
		hashMap.put("msde_old20", msde_old20);

		hashMap.put("msde_old21", msde_old21);
		hashMap.put("msde_old22", msde_old22);
		hashMap.put("msde_old23", msde_old23);
		hashMap.put("msde_old24", msde_old24);
		hashMap.put("msde_old25", msde_old25);

		hashMap.put("msde_new1", msde_new1);
		hashMap.put("msde_new2", msde_new2);
		hashMap.put("msde_new3", msde_new3);
		hashMap.put("msde_new4", msde_new4);
		hashMap.put("msde_new5", msde_new5);
		hashMap.put("msde_new6", msde_new6);
		hashMap.put("msde_new7", msde_new7);
		hashMap.put("msde_new8", msde_new8);
		hashMap.put("msde_new9", msde_new9);
		hashMap.put("msde_new10", msde_new10);
		hashMap.put("msde_new11", msde_new11);
		hashMap.put("msde_new12", msde_new12);
		hashMap.put("msde_new13", msde_new13);
		hashMap.put("msde_new14", msde_new14);
		hashMap.put("msde_new15", msde_new15);
		hashMap.put("msde_new16", msde_new16);
		hashMap.put("msde_new17", msde_new17);
		hashMap.put("msde_new18", msde_new18);
		hashMap.put("msde_new19", msde_new19);
		hashMap.put("msde_new20", msde_new20);

		hashMap.put("msde_new21", msde_new21);
		hashMap.put("msde_new22", msde_new22);
		hashMap.put("msde_new23", msde_new23);
		hashMap.put("msde_new24", msde_new24);
		hashMap.put("msde_new25", msde_new25);
		boolean isTemp = lsje_id == 96 && msde_old3 != null && msde_new3 != null;
		if (lsje_id == 96 && msde_old6 != null && msde_new6 != null) isTemp = true;
		if (lsje_id == 96 && msde_old8 != null && msde_new8 != null) isTemp = true;
		if (lsje_id == 96 && msde_old9 != null && msde_new9 != null) isTemp = true;
		if (lsje_id == 96 && msde_old12 != null && msde_new12 != null) isTemp = true;
		if (lsje_id == 96 && msde_old13 != null && msde_new13 != null) isTemp = true;
		if (lsje_id == 96 && msde_old14 != null && msde_new14 != null) isTemp = true;
		if (lsje_id == 68 && msde_old11 != null && msde_new11 != null) isTemp = true;
		if (lsje_id == 62 && msde_old11 != null && msde_new11 != null) isTemp = true;
		if (lsje_id == 96 && msde_old1 != null && msde_new1 != null) isTemp = true;
		if (lsje_id == 96 && msde_old2 != null && msde_new2 != null) isTemp = true;
		if (lsje_id == 96 && msde_old21 != null && msde_new21 != null) isTemp = true;
		if (lsje_id == 111 && msde_old1 != null && msde_new1 != null) isTemp = true;
		if (lsje_id == 112 && msde_old1 != null && msde_new1 != null) isTemp = true;
		if (lsje_id == 96 && msde_old20 != null && msde_new20 != null) isTemp = true;
		if (lsje_id == 96 && msde_old22 != null && msde_new22 != null) isTemp = true;
		if (lsje_id == 96 && msde_old23 != null && msde_new23 != null) isTemp = true;
		if (lsje_id == 6 && msde_old1 != null && msde_new1 != null) isTemp = true;
		if (lsje_id == 6 && msde_old2 != null && msde_new2 != null) isTemp = true;
		if (lsje_id == 96 && msde_old16 != null && msde_new16 != null) isTemp = true;
		if (lsje_id == 96 && msde_old17 != null && msde_new17 != null) isTemp = true;
		if (lsje_id == 96 && msde_old19 != null && msde_new19 != null) isTemp = true;
		if (lsje_id == 3 && msde_old3 != null && msde_new3 != null) isTemp = true;
		if (lsje_id == 34 && msde_old4 != null && msde_new4 != null) isTemp = true;

		if (isTemp){
			if (isUpdate){
				double isUpdatable = dao.countUpdatableEndorseTempDet(hashMap);
				if (isUpdatable > 0){
					dao.updateDetTempEndorseByMsenAndEndorseId(hashMap);
				} else {
					dao.insertDetailEndorseTemp(hashMap);
				}
			} else {
				dao.insertDetailEndorseTemp(hashMap);
			}
		} else {
			if (isUpdate){
				double isUpdatable = dao.countUpdatableEndorseDet(hashMap);
				if (isUpdatable > 0){
					dao.updateDetEndorseByMsenAndEndorseId(hashMap);
				} else {
					dao.updateDetEndorseByMsenAndEndorseId(hashMap);
				}
			} else {
				dao.insertDetailEndorse(hashMap);
			}
		}
	}

	public void insertDetailRedirection(String mpt_id, String lji_id, Float mpt_persen, String mpt_dk) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mpt_id", mpt_id);
		hashMap.put("lji_id", lji_id);
		hashMap.put("mpt_persen", mpt_persen);
		hashMap.put("mpt_dk", mpt_dk);
		dao.insertDetailRedirection(hashMap);
	}

	public void insertWithdraw(String mpt_id, String req_date_java, String reg_spaj, Integer lt_id, String lku_id,
			BigDecimal mpt_jumlah, BigDecimal mpt_unit, String created_date_java, String payor_name, String rekening,
			String bank_name, String path_bsb, String working_days) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mpt_id", mpt_id);
		hashMap.put("req_date", req_date_java);
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("lt_id", lt_id);
		hashMap.put("lku_id", lku_id);
		hashMap.put("mpt_jumlah", mpt_jumlah);
		hashMap.put("mpt_unit", mpt_unit);
		hashMap.put("created_date", created_date_java);
		hashMap.put("payor_name", payor_name);
		hashMap.put("path_bsb", path_bsb);
		hashMap.put("rekening", rekening);
		hashMap.put("bank_name", bank_name);
		hashMap.put("working_days", working_days);
		dao.insertWithdraw(hashMap);
	}

	public void insertDetailWithdraw(String mpt_id, String lji_id, BigDecimal mpt_jumlah, BigDecimal mpt_unit) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mpt_id", mpt_id);
		hashMap.put("lji_id", lji_id);
		hashMap.put("mpt_jumlah", mpt_jumlah);
		hashMap.put("mpt_unit", mpt_unit);
		dao.insertDetailWithdraw(hashMap);
	}

	public void insertClaimSubmissionTrans(BigInteger mpc_id, String reg_spaj, Integer mste_insured_no,
			String patienname, String lku_id, Integer lsbs_id, Integer lsdbs_number, String date_ri_1, String date_ri_2,
			Integer amount_ri, Integer jenisclaim, String no_hp, String email, String path_claim, Integer lssp_id,
			String groupclaimjenis, Integer double_cover_claim) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mpc_id", mpc_id);
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("mste_insured_no", mste_insured_no);
		hashMap.put("patienname", patienname);
		hashMap.put("lku_id", lku_id);
		hashMap.put("lsbs_id", lsbs_id);
		hashMap.put("lsdbs_number", lsdbs_number);
		hashMap.put("date_ri_1", date_ri_1);
		hashMap.put("date_ri_2", date_ri_2);
		hashMap.put("amount_ri", amount_ri);
		hashMap.put("jenisclaim", jenisclaim);
		hashMap.put("no_hp", no_hp);
		hashMap.put("email", email);
		hashMap.put("path_claim", path_claim);
		hashMap.put("lssp_id", lssp_id);
		hashMap.put("groupclaimjenis", groupclaimjenis);
		hashMap.put("double_cover_claim", double_cover_claim);
		dao.insertClaimSubmissionTrans(hashMap);
	}

	public void insertDetailClaimSubmissionTrans(BigInteger mpc_id, String reg_spaj, String accountno, Integer lsbp_id,
			String nama_cabang, String atasnama) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mpc_id", mpc_id);
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("accountno", accountno);
		hashMap.put("lsbp_id", lsbp_id);
		hashMap.put("nama_cabang", nama_cabang);
		hashMap.put("atasnama", atasnama);
		dao.insertDetailClaimSubmissionTrans(hashMap);
	}

	public void insertClaimSubmissionCorporate(String mpcc_id, String reg_spaj, String mcl_id, String no_reg, Integer lssp_id,
			String jenis_claim, String start_date, String end_date, Integer double_cover_claim, String path_storage,
			String bank, String no_rekening) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mpcc_id", mpcc_id);
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("mcl_id", mcl_id);
		hashMap.put("no_reg", no_reg);
		hashMap.put("lssp_id", lssp_id);
		hashMap.put("jenis_claim", jenis_claim);
		hashMap.put("start_date", start_date);
		hashMap.put("end_date", end_date);
		hashMap.put("double_cover_claim", double_cover_claim);
		hashMap.put("path_storage", path_storage);
		hashMap.put("bank", bank);
		hashMap.put("no_rekening", no_rekening);
		dao.insertClaimSubmissionCorporate(hashMap);
	}

	// Select
	public static ArrayList<Object> serializableList(List<Object> dataList) {
		if (dataList != null) {
			return new ArrayList<>(dataList);
		} else {
			return null;
		}
	}

	public BigDecimal selectCheckEnableClaimCorp(String no_polis) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectCheckEnableClaimCorp(no_polis);
	}


	public String selectDynamicQuery(SQLAdapter adapter) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectDynamicQuery(adapter);
	}


	public void updateDynamicQuery(SQLAdapter adapter ) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		 dao.updateDynamicQuery(adapter);

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

	public String selectEncrypt(String value) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectEncrypt(value);
	}


	public EndorseMapping selectMapEndorse(EndorseMapping endorse) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectMapEndorse(endorse);
	}

	public EndorseMapping selectMapEndorseForKey(EndorseMapping endorse) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectMapEndorseForKey(endorse);
	}

	public List<EndorseMapping> selectDirectMapEndorse() {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectDirectMapEndorse();
	}

	public LstUserSimultaneous selectLoginAuthenticate(LstUserSimultaneous lstUserSimultaneous) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectLoginAuthenticate(lstUserSimultaneous);
	}

	public Pemegang selectNomorPolis(Pemegang pemegang) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectNomorPolis(pemegang);
	}

	public Pemegang selectNomorPolisNotRegister(Pemegang pemegang) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mssm_pemegang", pemegang.getMssm_pemegang());
		hashMap.put("lstb_id", pemegang.getLstb_id());
		hashMap.put("mspo_policy_no", pemegang.getMspo_policy_no());
		return dao.selectNomorPolisNotRegister(hashMap);
	}

	public Pemegang selectKtp(Pemegang pemegang) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mssm_pemegang", pemegang.getMssm_pemegang());
		hashMap.put("lstb_id", pemegang.getLstb_id());
		hashMap.put("mspe_no_identity", pemegang.getMspo_policy_no());
		return dao.selectKtp(hashMap);
	}

	public User decryptPassword(User user) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.decryptPassword(user);
	}

	public Pemegang selectPemegang(Pemegang pemegang) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectPemegang(pemegang);
	}

	public ArrayList<PenerimaManfaat> selectPenerimaManfaat(String no_polis) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectPenerimaManfaat(no_polis);
	}

	public DataUsulan selectDataUsulan(DataUsulan dataUsulan) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectDataUsulan(dataUsulan);
	}

	public Sales selectSales(String reg_spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectSales(reg_spaj);
	}

	public ArrayList<TertanggungTambahan> selectTertanggungTambahan(String spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectTertanggungTambahan(spaj);
	}

	public ArrayList<ProductRider> selectProductRider(String spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectProductRider(spaj);
	}

	public Tertanggung selectTertanggung(Tertanggung tertanggung) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectTertanggung(tertanggung);
	}

	public ArrayList<User> selectDetailedPolis(String username) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectDetailedPolis(username);
	}

	public ArrayList<PolisMri> getListPolisMri(String username) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> map = new HashMap<>();
		map.put("username", username);
		return dao.getListPolisMri(map);
	}

	public ArrayList<UnitLink> selectUnitLink(String reg_spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectUnitLink(reg_spaj);
	}


	public Double selectTotalTunggakanUnitLink(String reg_spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectTotalTunggakanUnitLink(reg_spaj);
	}
	public ArrayList<UnitLink> selectDetailUnitLink(String spaj, String lji_id, Integer pageNumber, Integer pageSize,
			String startDate, String endDate) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("lji_id", lji_id);
		hashMap.put("spaj", spaj);
		hashMap.put("pageNumber", pageNumber);
		hashMap.put("pageSize", pageSize);
		hashMap.put("startDate", startDate);
		hashMap.put("endDate", endDate);
		return dao.selectDetailUnitLink(hashMap);
	}

	public ArrayList<Nav> selectListNav() {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectListNav();
	}

	public ArrayList<Nav> selectDetailNav(Integer lji_id, Integer nilai) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("lji_id", lji_id);
		hashMap.put("nilai", nilai);
		return dao.selectDetailNav(hashMap);
	}

	public User selectForgotUsernameIndividual(String ktp_or_nopolis, String dob) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("ktp_or_nopolis", ktp_or_nopolis);
		hashMap.put("dob", dob);
		return dao.selectForgotUsernameIndividual(hashMap);
	}

	public User selectForgotPassword(User user) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectForgotPassword(user);
	}

	public Integer selectCountPhoneNumber(String phone_no) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectCountPhoneNumber(phone_no);
	}

	public ArrayList<KlaimKesehatan> selectKlaimkesehatan(String spaj, Integer pageNumber, Integer pageSize,
			String startDate, String endDate) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		ViewClaim viewClaim = new ViewClaim();
		viewClaim.setSpaj(spaj);
		viewClaim.setPageNumber(pageNumber);
		viewClaim.setPageSize(pageSize);
		viewClaim.setStartDate(startDate);
		viewClaim.setEndDate(endDate);
		return dao.selectKlaimkesehatan(viewClaim);
	}

	public boolean selectCountIdSimultanByIdSimultan(String id_simultan) {
		boolean flag = false;
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		Integer data = dao.selectCountIdSimultanByIdSimultan(id_simultan);
		if (data.equals(1)) {
			flag = true;
		}
		return flag;
	}

	public ArrayList<Provider> selectProvider() {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectProvider();
	}

	public Pemegang selectGetSPAJ(Pemegang pemegang) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectGetSPAJ(pemegang);
	}

	public String getKodeCabang(String no_polis) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.getKodeCabang(no_polis);
	}

	public List<Billing> selectBilling(String reg_spaj, Integer pageNumber, Integer pageSize, String startDate,
			String endDate) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("spaj", reg_spaj);
		hashMap.put("pageNumber", pageNumber);
		hashMap.put("pageSize", pageSize);
		hashMap.put("startDate", startDate);
		hashMap.put("endDate", endDate);
		return dao.selectBilling(hashMap);
	}

	public Integer selectCheckUsername(String username) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectCheckUsername(username);
	}

	public ArrayList<Provinsi> selectListProvinsi() {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectListProvinsi();
	}
	public String selectListProvinsiById(String lspr_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectListProvinsiById(lspr_id);
	}
	public ArrayList<Provinsi> selectListKabupaten(Integer lspr_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("lspr_id", lspr_id);
		return dao.selectListKabupaten(hashMap);
	}

	public ArrayList<Provinsi> selectListKecamatan(Integer lspr_id, Integer lska_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("lspr_id", lspr_id);
		hashMap.put("lska_id", lska_id);
		return dao.selectListKecamatan(hashMap);
	}

	public String selectListKecamatanById(String lskc_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectListKecamatanById(lskc_id);
	}

	public String selectListKabupatenById(String lska_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectListKabupatenById(lska_id);
	}

	public ArrayList<Provinsi> selectListKelurahan(Integer lspr_id, Integer lska_id, Integer lskc_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("lspr_id", lspr_id);
		hashMap.put("lska_id", lska_id);
		hashMap.put("lskc_id", lskc_id);
		return dao.selectListKelurahan(hashMap);
	}

	public ArrayList<Provinsi> selectListKodePos(Integer lspr_id, Integer lska_id, Integer lskc_id, Integer lskl_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("lspr_id", lspr_id);
		hashMap.put("lska_id", lska_id);
		hashMap.put("lskc_id", lskc_id);
		hashMap.put("lskl_id", lskl_id);
		return dao.selectListKodePos(hashMap);
	}

	public ArrayList<Topup> selectListTopup(String reg_spaj, Integer pageNumber, Integer pageSize) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("pageNumber", pageNumber);
		hashMap.put("pageSize", pageSize);
		return dao.selectListTopup(hashMap);
	}

	public Topup selectListTopupUsingID(String mpt_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectListTopupUsingID(mpt_id);
	}

	public Topup selectViewTopupPaper(String mpt_id, String reg_spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mpt_id", mpt_id);
		hashMap.put("reg_spaj", reg_spaj);
		return dao.selectViewTopupPaper(hashMap);
	}

	public ArrayList<Topup> selectListInvestasiTopup(String mpt_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectListInvestasiTopup(mpt_id);
	}

	public ArrayList<Topup> selectListInvestasi(String no_polis) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectListInvestasi(no_polis);
	}

	public Integer selectCountMessageInboxUnread(String username) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectCountMessageInboxUnread(username);
	}

	public Integer selectCheckDeathClaim(String id_simultan) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectCheckDeathClaim(id_simultan);
	}

	public Topup selectProductforTopup(String no_polis) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectProductforTopup(no_polis);
	}

	public ArrayList<Fund> selectFundOfProduct(Topup topup) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectFundOfProduct(topup);
	}

	public ArrayList<Fund> selectFundOfProductSwitching(String lsbs_id, String lku_id, String reg_spaj,
			ArrayList<String> lji_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("lsbs_id", lsbs_id);
		hashMap.put("lku_id", lku_id);
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("lji_id", lji_id);
		return dao.selectFundOfProductSwitching(hashMap);
	}

	public ArrayList<PowerSave> selectPowerSave(User user) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectPowerSave(user);
	}

	public ArrayList<StableSave> selectStableSave(User user) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectStableSave(user);
	}

	public ArrayList<StableLink> selectStableLink(User user) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectStableLink(user);
	}

	public Rekening selectRekeningForTopup(String reg_spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectRekeningForTopup(reg_spaj);
	}

	public Rekening selectRekeningForBillingPrem(String reg_spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectRekeningForBillingPrem(reg_spaj);
	}

	public ArrayList<StableLink> selectDetailStableLink(User user) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectDetailStableLink(user);
	}

	public BigInteger selectGetMptId() {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectGetMptId();
	}

	public Integer selectGetLspdId(String reg_spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectGetLspdId(reg_spaj);
	}

	public String selectGetNoEndors() {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectGetNoEndors();
	}


	public String getInboxId() {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.getInboxId();
	}
	public BigInteger selectGetMpcId() {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectGetMpcId();
	}

	public User selectUserIndividual(String username) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectUserIndividual(username);
	}

	public Provinsi selectProvinsiResult(Provinsi provinsi) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectProvinsiResult(provinsi);
	}

	public BigDecimal selectSumJumlahTopup(String reg_spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectSumJumlahTopup(reg_spaj);
	}

	public Topup selectStartEndActivePolicy(String reg_spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectStartEndActivePolicy(reg_spaj);
	}

	public Integer selectCountDeathClaim(String username) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectCountDeathClaim(username);
	}

	public ArrayList<TrackingPolis> selectTrackingPolis(RequestTrackingPolis requestTrackingPolis) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectTrackingPolis(requestTrackingPolis);
	}

	public ArrayList<TrackingPolis> selectCountDataTrackingPolis(RequestTrackingPolis requestTrackingPolis) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectCountDataTrackingPolis(requestTrackingPolis);
	}

	public Integer selectPersentaseBiayaTopup(String reg_spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectPersentaseBiayaTopup(reg_spaj);
	}

	public User selectDataForEmailCS(String username) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectDataForEmailCS(username);
	}

	public Integer selectCountLstUlangan(String username) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectCountLstUlangan(username);
	}

	public ArrayList<Fund> selectFundDefaultUser(String reg_spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectFundDefaultUser(reg_spaj);
	}

	public Integer selectCountIdSimultanByUsername(String mcl_first, String mspe_date_birth) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashmap = new HashMap<>();
		hashmap.put("mcl_first", mcl_first);
		hashmap.put("mspe_date_birth", mspe_date_birth);
		return dao.selectCountIdSimultanByUsername(hashmap);
	}

	public User selectDataUser(String username, String reg_spaj, String no_polis) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("username", username);
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("no_polis", no_polis);
		return dao.selectDataUser(hashMap);
	}

	public ArrayList<VersionCode> selectVersionCode(Integer flag_app, Integer flag_platform) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashmap = new HashMap<>();
		hashmap.put("flag_app", flag_app);
		hashmap.put("flag_platform", flag_platform);
		return dao.selectVersionCode(hashmap);
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

	public User selectBasicInformationForFinancialTransaction(String no_polis) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectBasicInformationForFinancialTransaction(no_polis);
	}

	public ClaimSubmission selectBasicInformationForClaimSubmission(String no_polis) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectBasicInformationForClaimSubmission(no_polis);
	}

	public ArrayList<SwitchingRedirection> selectListSwitchingAndRedirection(String reg_spaj, Integer pageNumber,
			Integer pageSize) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("pageNumber", pageNumber);
		hashMap.put("pageSize", pageSize);
		return dao.selectListSwitchingAndRedirection(hashMap);
	}

	public ArrayList<SwitchingRedirection> selectListSwitching(String reg_spaj, Integer pageNumber,
			Integer pageSize) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("pageNumber", pageNumber);
		hashMap.put("pageSize", pageSize);
		return dao.selectListSwitching(hashMap);
	}

	public Integer selectListSwitchingTotal(String reg_spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		return dao.selectListSwitchingTotal(hashMap);
	}
	public ArrayList<SwitchingRedirection> selectListRedirection(String reg_spaj, Integer pageNumber,
			Integer pageSize) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("pageNumber", pageNumber);
		hashMap.put("pageSize", pageSize);
		return dao.selectListRedirection(hashMap);
	}

	public String selectFlagTrans(String mpt_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectFlagTrans(mpt_id);
	}

	public SwitchingRedirection selectViewSwitchingPaper(String mpt_id, String reg_spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("arrayData", mpt_id);
		return dao.selectViewSwitchingPaper(hashMap);
	}

	public SwitchingRedirection selectViewSwitchingPaper_1(ArrayList<String> arrayData, String reg_spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("arrayData", arrayData);
		return dao.selectViewSwitchingPaper_1(hashMap);
	}

	public ArrayList<SwitchingRedirection> selectViewDetailSwitchingPaper(String mpt_id, String reg_spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("mpt_id", mpt_id);
		return dao.selectViewDetailSwitchingPaper(hashMap);
	}

	public SwitchingRedirection selectViewSwitchingRedirection1(String mpt_id, String no_polis) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mpt_id", mpt_id);
		hashMap.put("no_polis", no_polis);
		return dao.selectViewSwitchingRedirection1(hashMap);
	}

	public SwitchingRedirection selectViewSwitchingRedirection1_1(ArrayList<String> arrayData, String no_polis) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("arrayData", arrayData);
		hashMap.put("no_polis", no_polis);
		return dao.selectViewSwitchingRedirection1_1(hashMap);
	}

	public ArrayList<SwitchingRedirection> selectViewSwitchingRedirection2_2(ArrayList<String> arrayData,
			String no_polis) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("arrayData", arrayData);
		hashMap.put("no_polis", no_polis);
		return dao.selectViewSwitchingRedirection2_2(hashMap);
	}

	public ArrayList<SwitchingRedirection> selectViewSwitchingRedirection2(String mpt_id,
			String no_polis) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mpt_id", mpt_id);
		hashMap.put("no_polis", no_polis);
		return dao.selectViewSwitchingRedirection2(hashMap);
	}

	public ArrayList<SwitchingRedirection> selectViewSwitchingRedirection3(String mpt_id, String no_polis) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mpt_id", mpt_id);
		hashMap.put("no_polis", no_polis);
		return dao.selectViewSwitchingRedirection3(hashMap);
	}

	public Integer selectCheckStatusTransaction(String reg_spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		return dao.selectCheckStatusTransaction(hashMap);
	}

	public ArrayList<CostFinancialTransaction> selectBiayaForFinancialTransaction(String reg_spaj, String mpt_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("mpt_id", mpt_id);
		return dao.selectBiayaForFinancialTransaction(hashMap);
	}

	public ArrayList<Withdraw> selectListWithdraw(String reg_spaj, Integer pageNumber, Integer pageSize) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("pageNumber", pageNumber);
		hashMap.put("pageSize", pageSize);
		return dao.selectListWithdraw(hashMap);
	}


	public Integer selectListWithdrawTotal(String reg_spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		return dao.selectListWithdrawTotal(hashMap);
	}
	public Withdraw selectViewWithdraw(String mpt_id, String no_polis) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("no_polis", no_polis);
		hashMap.put("mpt_id", mpt_id);
		return dao.selectViewWithdraw(hashMap);
	}

	public ArrayList<DetailWithdraw> selectViewDetailWithdrawMPolis(String mpt_id, String reg_spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mpt_id", mpt_id);
		hashMap.put("reg_spaj", reg_spaj);
		return dao.selectViewDetailWithdrawMPolis(hashMap);
	}

	public ArrayList<Withdraw> selectSaldoWithdraw(String reg_spaj) throws InterruptedException {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectSaldoWithdraw(reg_spaj);
	}

	public Withdraw selectDataFormWithdraw(String reg_spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectDataFormWithdraw(reg_spaj);
	}

	public String selectCheckOTP(String no_hp, Integer menu_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("no_hp", no_hp);
		hashMap.put("menu_id", menu_id);
		return dao.selectCheckOTP(hashMap);
	}

	public Withdraw selectViewWithdrawPaper(String mpt_id, String reg_spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mpt_id", mpt_id);
		hashMap.put("reg_spaj", reg_spaj);
		return dao.selectViewWithdrawPaper(hashMap);
	}

	public ArrayList<DetailWithdraw> selectViewDetailWithdrawPaper(String mpt_id, String no_polis) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mpt_id", mpt_id);
		hashMap.put("no_polis", no_polis);
		return dao.selectViewDetailWithdrawPaper(hashMap);
	}

	public ArrayList<Topup> selectInvestasiTopupPaper(String mpt_id, String reg_spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mpt_id", mpt_id);
		hashMap.put("reg_spaj", reg_spaj);
		return dao.selectInvestasiTopupPaper(hashMap);
	}

	public Integer selectCountTransId(String mpt_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectCountTransId(mpt_id);
	}

	public ArrayList<ClaimSubmission> selectListClaimSubmission(String reg_spaj, Integer pageNumber, Integer pageSize) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("pageNumber", pageNumber);
		hashMap.put("pageSize", pageSize);
		return dao.selectListClaimSubmission(hashMap);
	}

	public ArrayList<ClaimLimit> selectClaimLimit(String no_polis) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("no_polis", no_polis);
		return dao.selectClaimLimit(hashMap);
	}

	public ClaimSubmission selectViewClaimsubmission(String reg_spaj, BigInteger mpc_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("mpc_id", mpc_id);
		return dao.selectViewClaimsubmission(hashMap);
	}

	public ArrayList<ClaimSubmission> selectAllTertanggungAndProductClaimSubmission(String reg_spaj, Integer type,
			String nama) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("type", type);
		hashMap.put("nama", nama);
		return dao.selectAllTertanggungAndProductClaimSubmission(hashMap);
	}

	public ArrayList<ClaimSubmission> selectChooseJenisClaim(String reg_spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectChooseJenisClaim(reg_spaj);
	}

	public ClaimSubmission selectGeneratePdfClaim(Integer type, String reg_spaj, String nama) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("type", type);
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("nama", nama);
		return dao.selectGeneratePdfClaim(hashMap);
	}

	public Integer selectCountTransIdClaim(String mpc_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectCountTransIdClaim(mpc_id);
	}

	public Integer selectCheckDate(String dateVal) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectCheckDate(dateVal);
	}

	public Integer selectCheckBiayaTransaksi(String mpt_id, String reg_spaj, Integer lt_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mpt_id", mpt_id);
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("lt_id", lt_id);
		return dao.selectCheckBiayaTransaksi(hashMap);
	}

	public String selectCutoffTransactionLink() {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectCutoffTransactionLink();
	};

	public String selectCutoffTransactionMagnaAndPrimeLink() {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectCutoffTransactionMagnaAndPrimeLink();
	}

	public ProductUtama selectProductCode(String reg_spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectProductCode(reg_spaj);
	}

	public Rekening selectCheckRekeningNasabahIndividu(String mspo_policy_no) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectCheckRekeningNasabahIndividu(mspo_policy_no);
	}

	// Corporate
	public UserCorporate selectCheckUserCorporateRegister(String no_polis, String kode, String dob) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("no_polis", no_polis);
		hashMap.put("kode", kode);
		hashMap.put("dob", dob);
		return dao.selectCheckUserCorporateRegister(hashMap);
	}

	public Integer selectCheckUserCorporateExistsMpolis(String mcl_id_employee) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mcl_id_employee", mcl_id_employee);
		return dao.selectCheckUserCorporateExistsMpolis(hashMap);
	}

	public Integer selectCheckUserCorporatePhoneNumberExistsMpolis(String no_hp) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectCheckUserCorporatePhoneNumberExistsMpolis(no_hp);
	}

	public ArrayList<UserCorporate> selectListPolisCorporate(String mcl_id_employee) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mcl_id_employee", mcl_id_employee);
		return dao.selectListPolisCorporate(hashMap);
	}

	public LstUserSimultaneous selectDataLstUserSimultaneous(String username) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectDataLstUserSimultaneous(username);
	}

	public UserCorporate selectUserCorporate(String username) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectUserCorporate(username);
	}

	public ArrayList<ClaimCorporate> selectListClaimCorporate(String reg_spaj, String mste_insured, Integer pageNumber,
			Integer pageSize) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("mste_insured", mste_insured);
		hashMap.put("pageNumber", pageNumber);
		hashMap.put("pageSize", pageSize);
		return dao.selectListClaimCorporate(hashMap);
	}

	public ArrayList<DetailClaimCorporate> selectDetailClaimCorporate(String no_claim) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectDetailClaimCorporate(no_claim);
	}

	public ArrayList<BenefitCorporate> selectListBenefitCorporate(String reg_spaj, String mste_insured) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("mste_insured", mste_insured);
		return dao.selectListBenefitCorporate(hashMap);
	}

	public ArrayList<Article> selectListArticle(Integer pageNumber, Integer pageSize) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("pageNumber", pageNumber);
		hashMap.put("pageSize", pageSize);
		return dao.selectListArticle(hashMap);
	}

	public UserCorporate selectBegDateEndDateCorporate(String no_polis) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectBegDateEndDateCorporate(no_polis);
	}

	public UserCorporate selectForgotUsernameCorporate(String no_polis, String kode, String dob) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("no_polis", no_polis);
		hashMap.put("kode", kode);
		hashMap.put("dob", dob);
		return dao.selectForgotUsernameCorporate(hashMap);
	}

	public ClaimSubmissionCorporate selectClaimSubmissionCorporate(String reg_spaj, String mste_insured) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("mste_insured", mste_insured);
		return dao.selectClaimSubmissionCorporate(hashMap);
	}

	public ArrayList<ClaimSubmissionCorporate> selectJenisClaimSubmissionCorporate(String reg_spaj,
			String mste_insured) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("mste_insured", mste_insured);
		return dao.selectJenisClaimSubmissionCorporate(hashMap);
	}

	public ArrayList<ClaimSubmissionCorporate> selectDocumentJenisClaimCorporate(String value, Integer language_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("value", value);
		hashMap.put("language_id", language_id);
		return dao.selectDocumentJenisClaimCorporate(hashMap);
	}

	public ArrayList<ClaimSubmissionCorporate> selectListClaimSubmissionCorporate(String reg_spaj, String mste_insured,
			Integer pageNumber, Integer pageSize) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("mste_insured", mste_insured);
		hashMap.put("pageNumber", pageNumber);
		hashMap.put("pageSize", pageSize);
		return dao.selectListClaimSubmissionCorporate(hashMap);
	}

	public ClaimSubmissionCorporate selectViewClaimSubmissionCorporate(String reg_spaj, String mste_insured,
			String mpcc_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("mste_insured", mste_insured);
		hashMap.put("mpcc_id", mpcc_id);
		return dao.selectViewClaimSubmissionCorporate(hashMap);
	}

	public String selectGetMpccId() {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectGetMpccId();
	}

	public Integer selectCheckMpccId(String mpcc_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectCheckMpccId(mpcc_id);
	}

	public Rekening selectCheckRekeningNasabahCorporate(String msbc_spaj, String mcl_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("msbc_spaj", msbc_spaj);
		hashMap.put("mcl_id", mcl_id);
		return dao.selectCheckRekeningNasabahCorporate(hashMap);
	}

	public User selectCheckPhoneNumberIndividu(String mspo_policy_no) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectCheckPhoneNumberIndividu(mspo_policy_no);
	}

	public UserCorporate selectCheckPhoneNumberCorporate(String reg_spaj, String mste_insured) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("mste_insured", mste_insured);
		return dao.selectCheckPhoneNumberCorporate(hashMap);
	}

	// Update
	public void updateUserKeyName(LstUserSimultaneous user) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> data = new HashMap<>();
		data.put("UPDATE_DATE_TIME", user.getUPDATE_DATE_TIME());
		data.put("LAST_LOGIN_DATE_TIME", user.getLAST_LOGIN_DATE_TIME());
		data.put("FLAG_ACTIVE", user.getFLAG_ACTIVE());
		data.put("LAST_LOGIN_DEVICE", user.getLAST_LOGIN_DEVICE());
		data.put("USERNAME", user.getUSERNAME());
		dao.updateUserKeyName(data);
	}

	public void updateLspdId(String reg_spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.updateLspdId(reg_spaj);
	}

	public void updatePassword(User user) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.updatePassword(user);
	}

	public void updateDataMstAddressNew(Pemegang pemegang) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.updateDataMstAddressNew(pemegang);
	}

	public void updateDataMstAddressBilling(Pemegang pemegang) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.updateDataMstAddressBilling(pemegang);
	}

	public void updateKeyLoginClear(User user) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.updateKeyLoginClear(user);
	}

	public void deleteAllInbox(User user) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.deleteAllInbox(user);
	}
	public void deleteSavedProvider(String username, String  positionx,String positiony) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("username", username);
		params.put("positionx", positionx);
		params.put("positiony", positiony);
		dao.deleteSavedProvider(params);
	}



	public void updateInboxStatus(User user) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.updateInboxStatus(user);
	}

	public void updateActivityStatus(User user) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.updateActivityStatus(user);
	}

	public void updateLinkAccount(String reg_spaj, String id_simultan, String mcl_id_employee, String account_no_dplk, String username) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		LstUserSimultaneous lstUserSimultaneous = new LstUserSimultaneous();
		lstUserSimultaneous.setREG_SPAJ(reg_spaj);
		lstUserSimultaneous.setID_SIMULTAN(id_simultan);
		lstUserSimultaneous.setMCL_ID_EMPLOYEE(mcl_id_employee);
		lstUserSimultaneous.setAccount_no_dplk(account_no_dplk);
		lstUserSimultaneous.setUSERNAME(username);
		dao.updateLinkAccount(lstUserSimultaneous);
	}

	public void updateUnlinkAccountCorporate(String username) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		UserCorporate userCorporate = new UserCorporate();
		userCorporate.setUsername(username);
		dao.updateUnlinkAccountCorporate(userCorporate);
	}

	public void updateFurtherClaimIndividu(String mpc_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.updateFurtherClaimIndividu(mpc_id);
	}

	public void updateFurtherClaimCorporate(String mpcc_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.updateFurtherClaimCorporate(mpcc_id);
	}

	public String selectCheckOTP(String no_hp, Integer jenis_id, Integer menu_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("no_hp", no_hp);
		hashMap.put("jenis_id", jenis_id);
		hashMap.put("menu_id", menu_id);
		return dao.selectCheckOTP(hashMap);
	}

	public OtpTest selectTopActiveOtp() {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectTopActiveOtp();
	}

	public OtpTest selectTopActiveOtp(String no_hp) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectTopActiveOtpByNoHtp(no_hp);
	}

	public Endorse selectGetPremiumHolidayDate(String reg_spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectGetPremiumHolidayDate(reg_spaj);
	}

	public Endorse selectListPremiumHoliday(String reg_spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectListPremiumHoliday(reg_spaj);
	}

	public ArrayList<Beneficiary> selectListBeneficiary(String no_polis) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectListBeneficiary(no_polis);
	}

	public PolicyAlteration selectPayor(String no_polis) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectPayor(no_polis);
	}

	public PolicyAlteration selectInsured(String no_polis) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectInsured(no_polis);
	}

	public PolicyAlteration selectPolicyHolder(String no_polis) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectPolicyHolder(no_polis);
	}

	public PolicyAlteration selectKorespondensi(String no_polis) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectKorespondensi(no_polis);
	}

	public ArrayList<Endorse> selectListPolicyAlteration(String reg_spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectListPolicyAlteration(reg_spaj);
	}

	public ArrayList<Endorse> selectListPolicyAlterationInProcessing(String reg_spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectListPolicyAlterationInProcessing(reg_spaj);
	}
	public ArrayList<EndorsePolicyAlteration> selectListPolicyAlterationByendorseId(String reg_spaj, String msen_endorse_no, String lsje_id, String grouping, String status) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String,Object> params = new HashMap<String, Object>();
		params.put("reg_spaj", reg_spaj);
		params.put("msen_endorse_no", msen_endorse_no);
		params.put("grouping", grouping);
		params.put("ljse_id", lsje_id);
		params.put("status", status);


		return dao.selectListPolicyAlterationByendorseId(params);
	}


	public ArrayList<DropdownPolicyAlteration> selectListPernikahan() {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectListPernikahan();
	}

	public String selectListPernikahanById(String lsst_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectListPernikahanById(lsst_id);
	}

	public ArrayList<DropdownPolicyAlteration> selectListAgama() {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectListAgama();
	}
	public String selectListAgamaById(String lsag_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectListAgamaById(lsag_id);
	}

	public ArrayList<DropdownPolicyAlteration> selectListNegara() {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectListNegara();
	}

	public ArrayList<DropdownPolicyAlteration> selectListJenisUsaha() {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectListJenisUsaha();
	}

	public ArrayList<DropdownPolicyAlteration> selectListPekerjaan() {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectListPekerjaan();
	}

	public ArrayList<DropdownPolicyAlteration> selectListBank() {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectListBank();
	}
	public String selectListBankById(String lsbp_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectListBankById(lsbp_id);
	}
	public String selectCabangBankByLbnId(String lbn_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectCabangBankByLbnId(lbn_id);
	}
	public String selectListNegaraById(String lbn_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectListNegaraById(lbn_id);
	}

	public ArrayList<DropdownPolicyAlteration> selectListRelation() {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectListRelation();
	}

	public Endorse selectListJenisEndors(Integer lsje_id) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectListJenisEndors(lsje_id);
	}

	public void updateAgama(PolicyAlteration policyAlteration) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.updateAgama(policyAlteration);
	}





	public void insertLstUlangan(String reg_spaj, String msen_alasan, Integer counter) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("msen_alasan", msen_alasan);
		hashMap.put("counter", counter);
			dao.insertLstUlangan(hashMap);
	}


	public void insertMstInbox(MstInbox mstInbox) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.insertMstInbox(mstInbox);
	}


	public void insertSavedProvider(SavedProvider savedProvider) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.insertSavedProvider(savedProvider);
	}

	public List<SavedProvider> selectproviderbyusername(String username){
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectproviderbyusername(username);
	}

	public String selectMclId_PP(String reg_spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectMclId_PP(reg_spaj);
	}

	public void updateAlamatKantor(PolicyAlteration policyAlteration) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.updateAlamatKantor(policyAlteration);
	}

	public void updateKewarganegaraan(PolicyAlteration policyAlteration) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.updateKewarganegaraan(policyAlteration);
	}

	public void updateStatus(PolicyAlteration policyAlteration) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.updateStatus(policyAlteration);
	}

	public void updateJenisPekerjaan(PolicyAlteration policyAlteration) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.updateJenisPekerjaan(policyAlteration);
	}

	public void updateJenisCompanyPekerjaan(PolicyAlteration policyAlteration) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.updateJenisCompanyPekerjaan(policyAlteration);
	}

	public void updateCompanyPekerjaan(PolicyAlteration policyAlteration) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.updateCompanyPekerjaan(policyAlteration);
	}

	public void updatejobdesc(PolicyAlteration policyAlteration) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.updatejobdesc(policyAlteration);
	}

	public PembayarPremi selectPembayarPremi(String no_polis) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectPembayarPremi(no_polis);
	}

	public ArrayList<Inbox> selectInbox(String username) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectInbox(username);
	}

	public NotifToken selectNotifToken(String userid) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectNotifToken(userid);
	}

	public void updateNotifToken(NotifToken notifToken) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.updateNotifToken(notifToken);
	}

	public void insertNotifToken(NotifToken notifToken_new) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.insertNotifToken(notifToken_new);
	}

	public void insertNotification(PushNotif pushNotif) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.insertNotification(pushNotif);
	}

	public ArrayList<DropdownPolicyAlteration> selectCabangBank(Integer lsbp_id_req) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectCabangBank(lsbp_id_req);
	}

	public Article selectCountListArticle() {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectCountListArticle();
	}

	public KlaimKesehatan selectCountKlaimkesehatan(String spaj) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		ViewClaim viewClaim = new ViewClaim();
		viewClaim.setSpaj(spaj);
		return dao.selectCountKlaimkesehatan(viewClaim);
	}

	public ArrayList<UserHR> selectListPolisHRUser(String eb_hr_username) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectListPolisHRUser(eb_hr_username);
	}

	public ArrayList<UserHR> selectListPesertaHR(String eb_hr_username, String search_policy, Integer search_type, String policy_no) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("eb_hr_username", eb_hr_username);
		hashMap.put("search_policy", search_policy);
		hashMap.put("search_type", search_type);
		hashMap.put("policy_no", policy_no);
		return dao.selectListPesertaHR(hashMap);
	}

	public ArrayList<ReportHr> selectListReportHr(String no_polis, String no_batch, String beg_date, String end_date, Integer pageNumber, Integer pageSize) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("no_polis", no_polis);
		hashMap.put("no_batch", no_batch);
		hashMap.put("beg_date", beg_date);
		hashMap.put("end_date", end_date);
		hashMap.put("pageNumber", pageNumber);
		hashMap.put("pageSize", pageSize);
		return dao.selectListReportHr(hashMap);
	}

	public ArrayList<DownloadReportHr> selectPathReportHr(String no_batch) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectPathReportHr(no_batch);
	}

	public Integer selectCountReportHr(String no_polis) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectCountReportHr(no_polis);
	}

	public ArrayList<ViewMclFirst> selectListPolisOrion(String msag_id, String mspo_policy_no, String mcl_first) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("msag_id", msag_id);
		hashMap.put("mspo_policy_no", mspo_policy_no);
		hashMap.put("mcl_first", mcl_first);
		return dao.selectListPolisOrion(hashMap);
	}

	public String selectGetIdTicket() {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectGetIdTicket();
	}

	public void insertSubmitEndorseHr(String id_ticket, Integer id_group, String nik_req, String subject, String description) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("id_ticket", id_ticket);
		hashMap.put("id_group", id_group);
		hashMap.put("nik_req", nik_req);
		hashMap.put("subject", subject);
		hashMap.put("description", description);
		dao.insertSubmitEndorseHr(hashMap);
	}

	public void insertSubmitHistoryEndorse(String id_ticket, String history, String update_by) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("id_ticket", id_ticket);
		hashMap.put("history", history);
		hashMap.put("update_by", update_by);
		dao.insertSubmitHistoryEndorse(hashMap);
	}

	public ArrayList<EndorseHr> selectListEndorseHr(String no_polis) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectListEndorseHr(no_polis);
	}

	public EndorseHr selectViewEndorseHr(String id_ticket) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectViewEndorseHr(id_ticket);
	}

	public EndorseHr selectPrepareEndorseHr(String no_polis) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectPrepareEndorseHr(no_polis);
	}

	public ArrayList<TransactionHistory> selectTransactionHistory(String reg_spaj, String startDate, String endDate, String jenis_transaksi) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("startDate", startDate);
		hashMap.put("endDate", endDate);
		hashMap.put("jenis_transaksi", jenis_transaksi);
		return dao.selectTransactionHistory(hashMap);
	}

	public ArrayList<TransactionHistory> selectTransactionHistoryDropDown(String reg_spaj, String startDate, String endDate, String jenis_transaksi) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("startDate", startDate);
		hashMap.put("endDate", endDate);
		hashMap.put("jenis_transaksi", jenis_transaksi);
		return dao.selectTransactionHistoryDropDown(hashMap);
	}
	public String selectGetOnlyRegSpaj(String no_polis) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectGetOnlyRegSpaj(no_polis);
	}

	public Integer selectCountListBilling(String reg_spaj, String startDate, String endDate) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("reg_spaj", reg_spaj);
		hashMap.put("startDate", startDate);
		hashMap.put("endDate", endDate);
		return dao.selectCountListBilling(hashMap);
	}

	public MstOTPSimultaneousDet selectDetailOTP(MstOTPSimultaneousDet mstOTPSimultaneousDet) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectDetailOTP(mstOTPSimultaneousDet);
	}

	public MstOTPSimultaneous selectDataOTP(MstOTPSimultaneous paramSelectData) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectDataOTP(paramSelectData);
	}

	public void updateOtpUsed(MstOTPSimultaneous paramUpdateOtpUsed) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.updateOtpUsed(paramUpdateOtpUsed);
	}

	public void updateAttemptOtp(MstOTPSimultaneous paramUpdate) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.updateAttemptOtp(paramUpdate);
	}

	public void updateStatusAttemptOtp(MstOTPSimultaneous paramUpdate) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		dao.updateStatusAttemptOtp(paramUpdate);
	}

	public User selectDataPolisByPolisNo(String policy_number, String username){
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("policy_number", policy_number);
		hashMap.put("username", username);
		return dao.selectDataPolisByPolisNo(hashMap);
	}

	public List<Pemegang> filterIndividuAndMri(String policy_number, String ktp){
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("policy_number", policy_number);
		hashMap.put("no_ktp", ktp);
		return dao.filterIndividuAndMri(hashMap);
	}

	public List<Pemegang> filterByIdSimultanRegSpajNoPolis(String username){
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("username", username);
		return dao.filterByIdSimultanRegSpajNoPolis(hashMap);
	}

	public MRIdataPolis getDataMri(String no_polis){
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectDataMri(no_polis);
	}

	public MRIdataPolis getDataMriBrosure(String no_polis){
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectDataMriBrosure(no_polis);
	}

	public MpolTransData getMpolTransByMptId(String mpt_id){
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectMpolTransByMptId(mpt_id);
	}

	public String getConfigAdmedika() {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectConfigAdmedika();
	}

	public DetailPesertaCorporate getDetailPesertaCorporate(String mste_insured, String regSpaj){
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> map = new HashMap<>();
		map.put("reg_spaj", regSpaj);
		map.put("mste_insured", mste_insured);
		return dao.detailPesertaCorporate(map);
	}

	public SummaryPayment getByMptId(String mpt_id){
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.findByMptId(mpt_id);
	}

	public SummaryPayment getMspaTrxId(String mpt_id){
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.findByMstrxId(mpt_id);
	}

	public void updatePathSummaryDetail(String mpt_id, String path){
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> map = new HashMap<>();
		map.put("mpt_id", mpt_id);
		map.put("path", path);
		dao.updatePathSummary(map);
	}

	public DPLKAccountModel findAccountDplk(String account_no, String dob){
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("account_no", account_no);
		hashMap.put("birth_date", dob);
		return dao.findAccountDplk(hashMap);
	}

	public boolean isExistUsername(String username){
		User isExist = findByUsernameDplk(username);
		return isExist != null;
	}

	public boolean isExistAccount(String account_no){
		User isExist = findByAccountNoDplk(account_no);
		return isExist != null;
	}

	public DPLKAccountModel getInfoDplkByAccNo(String acc_no){
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.getInfoDplkByAccNo(acc_no);
	}

	public User findByUsernameDplk(String username){
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.findByUsernameDplk(username);
	}

	public User findByAccountNoDplk(String account_no){
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.findByAccountNoDplk(account_no);
	}

	public List<LstTransaksi> getListTransaksi(){
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.selectLstTransaksi();
	}

	public List<TransactionHistory> selectHistoryTransaksi(Integer lt_id, String reg_spaj,
														   String start_date, String end_date) {
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> data = new HashMap<>();
		data.put("lt_id", lt_id);
		data.put("reg_spaj", reg_spaj);
		data.put("start_date", start_date);
		data.put("end_date", end_date);
		return dao.selectHistoryTransaksi(data);
	}

	public List<DailyPriceFundDplk> getJenisInvestDplkByAccNo(String acc_no){
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.getJenisInvestDplkByAccNo(acc_no);
	}

	public List<LstTransaksiDplk> getTransaksiFund(String acc_no, String start_date, String end_date){
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> data = new HashMap<>();
		data.put("acc_no", acc_no);
		data.put("start_date", start_date);
		data.put("end_date", end_date);
		return dao.getLstTransDplk(data);
	}

	public List<TransactionHistory> getTransactionPolicyAlteration(String reg_spaj, String start_date, String end_date){
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> data = new HashMap<>();
		data.put("reg_spaj", reg_spaj);
		data.put("start_date", start_date);
		data.put("end_date", end_date);
		return dao.selectHistoryPolicyAlteration(data);
	}

	public List<TransactionHistory> getTransactionHistoryClaimSubmission(String reg_spaj, String start_date, String end_date){
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> data = new HashMap<>();
		data.put("reg_spaj", reg_spaj);
		data.put("start_date", start_date);
		data.put("end_date", end_date);
		return dao.selectHistoryClaimSubmissionIndividu(data);
	}

	public List<EnrollPesertaAdmedika> getEnrollPesertaAdmedikaCorporate(String mcl_id){
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mcl_id", mcl_id);
		return dao.enrollPesertaAdmedikaCorporate(hashMap);
	}

	public List<DataPlanPeserta> getDataPlanPesertaCorporate(String no_kartu){
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.getPlanAdmedikaCorporate(no_kartu);
	}

	public String getConfigDocument(){
		VegaMapper dao = sqlSession.getMapper(VegaMapper.class);
		return dao.getConfigDocumentCenter();
	}

}
