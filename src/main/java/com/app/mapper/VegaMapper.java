package com.app.mapper;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

import com.app.model.Article;
import com.app.model.Beneficiary;
import com.app.model.BenefitCorporate;
import com.app.model.Billing;
import com.app.model.ClaimCorporate;
import com.app.model.ClaimLimit;
import com.app.model.ClaimSubmission;
import com.app.model.ClaimSubmissionCorporate;
import com.app.model.CostFinancialTransaction;
import com.app.model.DataUsulan;
import com.app.model.DetailClaimCorporate;
import com.app.model.DetailWithdraw;
import com.app.model.Endorse;
import com.app.model.Fund;
import com.app.model.KlaimKesehatan;
import com.app.model.LstHistActivityWS;
import com.app.model.LstUserSimultaneous;
import com.app.model.MpolisConfiguration;
import com.app.model.Nav;
import com.app.model.Pemegang;
import com.app.model.PenerimaManfaat;
import com.app.model.PolicyAlteration;
import com.app.model.PowerSave;
import com.app.model.ProductRider;
import com.app.model.ProductUtama;
import com.app.model.Provider;
import com.app.model.Provinsi;
import com.app.model.Rekening;
import com.app.model.Sales;
import com.app.model.SmsServerOut;
import com.app.model.StableLink;
import com.app.model.StableSave;
import com.app.model.SwitchingRedirection;
import com.app.model.Tertanggung;
import com.app.model.TertanggungTambahan;
import com.app.model.Topup;
import com.app.model.TrackingPolis;
import com.app.model.UnitLink;
import com.app.model.User;
import com.app.model.UserCorporate;
import com.app.model.VersionCode;
import com.app.model.ViewClaim;
import com.app.model.Withdraw;
import com.app.model.request.RequestTrackingPolis;

public interface VegaMapper {
		
	// Stored Procedure
	public void storedProcedureGetBiaya(HashMap<String, Object> params);

	public void storedProcedureSubmitFinancialTransaction(HashMap<String, Object> hashMap);

	// Insert
	public void insertLstHistActvWs(LstHistActivityWS lstHistActivityWS);

	public void insertSmsServerOut(SmsServerOut smsOut);

	public void insertNewuser(LstUserSimultaneous lstUserSimultaneous);

	public void insertMstMpolTrans(Topup topup);

	public void insertMstMpolTransDet(Topup topup);

	public void insertSwitching(HashMap<String, Object> hashMap);

	public void insertRedirection(HashMap<String, Object> hashMap);

	public void insertDetailSwitching(HashMap<String, Object> hashMap);

	public void insertDetailRedirection(HashMap<String, Object> hashMap);

	public void insertWithdraw(HashMap<String, Object> hashMap);

	public void insertDetailWithdraw(HashMap<String, Object> hashMap);

	public void insertClaimSubmissionTrans(HashMap<String, Object> hashMap);

	public void insertDetailClaimSubmissionTrans(HashMap<String, Object> hashMap);
	
	public void insertClaimSubmissionCorporate(HashMap<String, Object> hashMap);
	
	public void insertEndorse(HashMap<String, Object> hashMap);
	
	public void insertDetailEndorse(HashMap<String, Object> hashMap);

	// Select
	public ArrayList<MpolisConfiguration> selectMpolisConfiguration();

	public String selectEncrypt(String value);

	public User decryptPassword(User user);

	public LstUserSimultaneous selectLoginAuthenticate(LstUserSimultaneous lstUserSimultaneous);

	public Pemegang selectNomorPolis(Pemegang pemegang);

	public Pemegang selectNomorPolisNotRegister(Pemegang pemegang);

	public Pemegang selectKtp(String mspe_no_identity);

	public Pemegang selectPemegang(Pemegang pemegang);

	public DataUsulan selectDataUsulan(DataUsulan dataUsulan);
	
	public Sales selectSales(String no_polis);

	public ArrayList<TertanggungTambahan> selectTertanggungTambahan(String spaj);

	public ArrayList<ProductRider> selectProductRider(String spaj);

	public Tertanggung selectTertanggung(Tertanggung tertanggung);

	public ArrayList<User> selectDetailedPolis(String username);

	public Integer selectCountPhoneNumber(String phone_no);

	public User selectForgotUsernameIndividual(HashMap<String, Object> hashMap);

	public User selectForgotPassword(User user);

	public ArrayList<UnitLink> selectUnitLink(String reg_spaj);

	public ArrayList<UnitLink> selectDetailUnitLink(HashMap<String, Object> hashMap);

	public ArrayList<Nav> selectListNav();

	public ArrayList<Nav> selectDetailNav(HashMap<String, Object> hashMap);

	public ArrayList<Provider> selectProvider();

	public ArrayList<KlaimKesehatan> selectKlaimkesehatan(ViewClaim viewClaim);

	public Integer selectCountIdSimultanByIdSimultan(String id_simultan);

	public Pemegang selectGetSPAJ(Pemegang pemegang);

	public String getKodeCabang(String no_polis);

	public ArrayList<Billing> selectBilling(HashMap<String, Object> hashMap);

	public Integer selectCheckUsername(String username);

	public ArrayList<Provinsi> selectListProvinsi();

	public ArrayList<Provinsi> selectListKabupaten(HashMap<String, Object> hashMap);

	public ArrayList<Provinsi> selectListKecamatan(HashMap<String, Object> hashMap);

	public ArrayList<Provinsi> selectListKelurahan(HashMap<String, Object> hashMap);

	public ArrayList<Provinsi> selectListKodePos(HashMap<String, Object> hashMap);

	public ArrayList<Topup> selectListTopup(HashMap<String, Object> hashMap);

	public Topup selectListTopupUsingID(String mpt_id);

	public Topup selectViewTopupPaper(HashMap<String, Object> hashMap);

	public ArrayList<Topup> selectListInvestasiTopup(String mpt_id);
	
	public ArrayList<Topup> selectListInvestasi(String no_polis);

	public ArrayList<Topup> selectInvestasiTopupPaper(HashMap<String, Object> hashMap);

	public Integer selectCountMessageInboxUnread(String username);

	public Integer selectCheckDeathClaim(String id_simultan);

	public Topup selectProductforTopup(String no_polis);

	public ArrayList<Fund> selectFundOfProduct(Topup topup);

	public ArrayList<PowerSave> selectPowerSave(User user);

	public ArrayList<StableSave> selectStableSave(User user);

	public ArrayList<StableLink> selectStableLink(User user);

	public Rekening selectRekeningForTopup(String reg_spaj);

	public ArrayList<StableLink> selectDetailStableLink(User user);

	public BigInteger selectGetMptId();
	
	public Integer selectGetLspdId(String reg_spaj);
	
	public String selectGetNoEndors();

	public BigInteger selectGetMpcId();

	public User selectUserIndividual(String username);

	public Provinsi selectProvinsiResult(Provinsi provinsi);

	public BigDecimal selectSumJumlahTopup(String reg_spaj);

	public Topup selectStartEndActivePolicy(String reg_spaj);

	public Integer selectCountDeathClaim(String username);

	public ArrayList<TrackingPolis> selectTrackingPolis(RequestTrackingPolis requestTrackingPolis);

	public ArrayList<TrackingPolis> selectCountDataTrackingPolis(RequestTrackingPolis requestTrackingPolis);

	public Integer selectPersentaseBiayaTopup(String reg_spaj);

	public User selectDataForEmailCS(String username);

	public ArrayList<Fund> selectFundDefaultUser(String reg_spaj);

	public Integer selectCountLstUlangan(String username);

	public Integer selectCountIdSimultanByUsername(HashMap<String, Object> hasmap);

	public User selectDataUser(HashMap<String, Object> hasmap);

	public ArrayList<VersionCode> selectVersionCode(HashMap<String, Object> hashmap);

	public Provinsi selectProvinsi2(HashMap<String, Object> hashMap);

	public User selectBasicInformationForFinancialTransaction(String no_polis);

	public ClaimSubmission selectBasicInformationForClaimSubmission(String no_polis);

	public ArrayList<SwitchingRedirection> selectListSwitchingAndRedirection(HashMap<String, Object> hashMap);

	public ArrayList<SwitchingRedirection> selectListSwitching(HashMap<String, Object> hashMap);
	
	public ArrayList<SwitchingRedirection> selectListRedirection(HashMap<String, Object> hashMap);
	
	public String selectFlagTrans(String mpt_id);
	
	public SwitchingRedirection selectViewSwitchingRedirection1(HashMap<String, Object> hashMap);
	
	public SwitchingRedirection selectViewSwitchingRedirection1_1(HashMap<String, Object> hashMap);

	public ArrayList<SwitchingRedirection> selectViewSwitchingRedirection2(HashMap<String, Object> hashMap);

	public ArrayList<SwitchingRedirection> selectViewSwitchingRedirection2_2(HashMap<String, Object> hashMap);

	public ArrayList<SwitchingRedirection> selectViewSwitchingRedirection3(HashMap<String, Object> hashMap);

	public Integer selectCheckStatusTransaction(HashMap<String, Object> hashMap);

	public ArrayList<CostFinancialTransaction> selectBiayaForFinancialTransaction(HashMap<String, Object> hashMap);

	public ArrayList<Withdraw> selectListWithdraw(HashMap<String, Object> hashMap);

	public Withdraw selectDataFormWithdraw(String reg_spaj);

	public ArrayList<Withdraw> selectSaldoWithdraw(String reg_spaj);

	public ArrayList<DetailWithdraw> selectViewDetailWithdrawMPolis(HashMap<String, Object> hashMap);

	public Withdraw selectViewWithdraw(HashMap<String, Object> hashMap);

	public String selectCheckOTP(HashMap<String, Object> hashMap);

	public SwitchingRedirection selectViewSwitchingPaper(HashMap<String, Object> hashMap);
	
	public SwitchingRedirection selectViewSwitchingPaper_1(HashMap<String, Object> hashMap);

	public ArrayList<SwitchingRedirection> selectViewDetailSwitchingPaper(HashMap<String, Object> hashMap);

	public Withdraw selectViewWithdrawPaper(HashMap<String, Object> hashMap);

	public ArrayList<DetailWithdraw> selectViewDetailWithdrawPaper(HashMap<String, Object> hashMap);

	public Integer selectCountTransId(String mpt_id);

	public ArrayList<Fund> selectFundOfProductSwitching(HashMap<String, Object> hashMap);

	public ArrayList<ClaimSubmission> selectListClaimSubmission(HashMap<String, Object> hashMap);
	
	public ArrayList<ClaimLimit> selectClaimLimit(HashMap<String, Object> hashMap);

	public ClaimSubmission selectViewClaimsubmission(HashMap<String, Object> hashMap);

	public ArrayList<ClaimSubmission> selectAllTertanggungAndProductClaimSubmission(HashMap<String, Object> hashMap);

	public ArrayList<ClaimSubmission> selectChooseJenisClaim(String reg_spaj);

	public ClaimSubmission selectGeneratePdfClaim(HashMap<String, Object> hashMap);

	public Integer selectCountTransIdClaim(String mpc_id);

	public Integer selectCheckDate(String dateVal);

	public Integer selectCheckBiayaTransaksi(HashMap<String, Object> hashMap);

	public LstUserSimultaneous selectDataLstUserSimultaneous(String username);

	public String selectCutoffTransactionLink();

	public String selectCutoffTransactionMagnaAndPrimeLink();

	public ProductUtama selectProductCode(String reg_spaj);
	
	public Rekening selectCheckRekeningNasabahIndividu(String mspo_policy_no);
	
	public ArrayList<PenerimaManfaat> selectPenerimaManfaat(String no_polis);
	
	public Endorse selectGetPremiumHolidayDate(String reg_spaj);
	
	public Endorse selectListPremiumHoliday(String reg_spaj);
	
	public ArrayList<Beneficiary> selectListBeneficiary(String no_polis);
	
	public PolicyAlteration selectPayor(String no_polis);
	
	public PolicyAlteration selectInsured(String no_polis);

	// Select Corporate
	public UserCorporate selectCheckUserCorporateRegister(HashMap<String, Object> hashMap);

	public Integer selectCheckUserCorporateExistsMpolis(HashMap<String, Object> hashMap);

	public Integer selectCheckUserCorporatePhoneNumberExistsMpolis(String no_hp);

	public ArrayList<UserCorporate> selectListPolisCorporate(HashMap<String, Object> hashMap);

	public UserCorporate selectUserCorporate(String username);

	public ArrayList<ClaimCorporate> selectListClaimCorporate(HashMap<String, Object> hashMap);

	public ArrayList<DetailClaimCorporate> selectDetailClaimCorporate(String no_claim);

	public ArrayList<BenefitCorporate> selectListBenefitCorporate(HashMap<String, Object> hashMap);

	public ArrayList<Article> selectListArticle(HashMap<String, Object> hashMap);

	public UserCorporate selectBegDateEndDateCorporate(String no_polis);
	
	public UserCorporate selectForgotUsernameCorporate(HashMap<String, Object> hashMap);
	
	public ClaimSubmissionCorporate selectClaimSubmissionCorporate(HashMap<String, Object> hashMap);
	
	public ArrayList<ClaimSubmissionCorporate> selectJenisClaimSubmissionCorporate(HashMap<String, Object> hashMap);
	
	public ArrayList<ClaimSubmissionCorporate> selectDocumentJenisClaimCorporate(HashMap<String, Object> hashMap);
	
	public ArrayList<ClaimSubmissionCorporate> selectListClaimSubmissionCorporate(HashMap<String, Object> hashMap);
	
	public ClaimSubmissionCorporate selectViewClaimSubmissionCorporate(HashMap<String, Object> hashMap);
	
	public String selectGetMpccId();
	
	public Integer selectCheckMpccId(String mpcc_id);
	
	public Rekening selectCheckRekeningNasabahCorporate(HashMap<String, Object> hashMap);
	
	public User selectCheckPhoneNumberIndividu(String mspo_policy_no);
	
	public UserCorporate selectCheckPhoneNumberCorporate(HashMap<String, Object> hashMap);

	// Update
	public void updateUserKeyName(LstUserSimultaneous user);
	
	public void updateLspdId(String reg_spaj);

	public void updatePassword(User user);

	public void updateDataMstAddressNew(Pemegang pemegang);

	public void updateDataMstAddressBilling(Pemegang pemegang);

	public void updateKeyLoginClear(User user);

	public void deleteAllInbox(User user);

	public void updateInboxStatus(User user);

	public void updateActivityStatus(User user);

	public void updateLinkAccount(LstUserSimultaneous lstUserSimultaneous);

	public void updateUnlinkAccountCorporate(UserCorporate userCorporate);
	
	public void updateFurtherClaimIndividu(String mpc_id);
	
	public void updateFurtherClaimCorporate(String mpcc_id);
}
