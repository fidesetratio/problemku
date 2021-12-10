package com.app.mapper;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.app.model.*;
import com.app.model.request.RequestTrackingPolis;

public interface VegaMapper {
		
	// Stored Procedure
	public void storedProcedureGetBiaya(HashMap<String, Object> params);

	public void storedProcedureSubmitFinancialTransaction(HashMap<String, Object> hashMap);

	// Insert
	
	
	public void insertMstInbox(MstInbox mstInbox);
	
	public void insertSavedProvider(SavedProvider savedProvider);
	
	public void insertLstHistActvWs(LstHistActivityWS lstHistActivityWS);

	public void insertSmsServerOut(SmsServerOut smsOut);

	public void insertNewuser(LstUserSimultaneous lstUserSimultaneous);

	public void insertMstMpolTrans(Topup topup);

	public void insertMstMpolTransBill(DetailBillingRequest topup);

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
	
	public void insertLstUlangan(HashMap<String, Object> hashMap);
	
	public void insertNotifToken(NotifToken notifToken_new);
	
	public void insertNotification(PushNotif pushNotif);
	
	public void insertSubmitEndorseHr(HashMap<String, Object> hashMap);
	
	public void insertSubmitHistoryEndorse(HashMap<String, Object> hashMap);

	// Select
	
	public List<SavedProvider> selectproviderbyusername(String username);
	
	public ArrayList<MpolisConfiguration> selectMpolisConfiguration();

	public String selectEncrypt(String value);
	public BigDecimal selectCheckEnableClaimCorp(String no_polis);
	
	

	public User decryptPassword(User user);

	public LstUserSimultaneous selectLoginAuthenticate(LstUserSimultaneous lstUserSimultaneous);

	public Pemegang selectNomorPolis(Pemegang pemegang);

	public Pemegang selectNomorPolisNotRegister(HashMap<String, Object> hashMap);

	public Pemegang selectKtp(HashMap<String, Object> hashMap);

	public Pemegang selectPemegang(Pemegang pemegang);

	public DataUsulan selectDataUsulan(DataUsulan dataUsulan);
	
	public Sales selectSales(String reg_spaj);

	public ArrayList<TertanggungTambahan> selectTertanggungTambahan(String spaj);

	public ArrayList<ProductRider> selectProductRider(String spaj);

	public Tertanggung selectTertanggung(Tertanggung tertanggung);

	public ArrayList<User> selectDetailedPolis(String username);

	public ArrayList<PolisMri> getListPolisMri(HashMap<String, Object> hashMap);

	public Integer selectCountPhoneNumber(String phone_no);

	public User selectForgotUsernameIndividual(HashMap<String, Object> hashMap);

	public User selectForgotPassword(User user);

	public ArrayList<UnitLink> selectUnitLink(String reg_spaj);
	
	public Double selectTotalTunggakanUnitLink(String reg_spaj);

	public ArrayList<UnitLink> selectDetailUnitLink(HashMap<String, Object> hashMap);

	public ArrayList<Nav> selectListNav();

	public ArrayList<Nav> selectDetailNav(HashMap<String, Object> hashMap);

	public ArrayList<Provider> selectProvider();

	public ArrayList<KlaimKesehatan> selectKlaimkesehatan(ViewClaim viewClaim);
	
	public KlaimKesehatan selectCountKlaimkesehatan(ViewClaim viewClaim);

	public Integer selectCountIdSimultanByIdSimultan(String id_simultan);

	public Pemegang selectGetSPAJ(Pemegang pemegang);

	public String getKodeCabang(String no_polis);

	public List<Billing> selectBilling(HashMap<String, Object> hashMap);

	public Integer selectCheckUsername(String username);

	public ArrayList<Provinsi> selectListProvinsi();

	public ArrayList<Provinsi> selectListKabupaten(HashMap<String, Object> hashMap);

	public ArrayList<Provinsi> selectListKecamatan(HashMap<String, Object> hashMap);

	public String selectListKecamatanById(String lskc_id);
	public String selectListKabupatenById(String lska_id);

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

	public Rekening selectRekeningForBillingPrem(String reg_spaj);

	public ArrayList<StableLink> selectDetailStableLink(User user);

	public BigInteger selectGetMptId();
	
	public Integer selectGetLspdId(String reg_spaj);
	
	public String selectGetNoEndors();
	public String getInboxId();

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
	

	public Integer selectListSwitchingTotal(HashMap<String, Object> hashMap);
	
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

	public Integer selectListWithdrawTotal(HashMap<String, Object> hashMap);

	
	public Withdraw selectDataFormWithdraw(String reg_spaj);

	public ArrayList<Withdraw> selectSaldoWithdraw(String reg_spaj);

	public ArrayList<DetailWithdraw> selectViewDetailWithdrawMPolis(HashMap<String, Object> hashMap);

	public Withdraw selectViewWithdraw(HashMap<String, Object> hashMap);

	public String selectCheckOTP(HashMap<String, Object> hashMap);

	public OtpTest selectTopActiveOtp();

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
	
	public PolicyAlteration selectPolicyHolder(String no_polis);
	
	public PolicyAlteration selectKorespondensi(String no_polis);
	
	public ArrayList<Endorse> selectListPolicyAlteration(String reg_spaj);
	public ArrayList<Endorse> selectListPolicyAlterationInProcessing(String reg_spaj);
	public ArrayList<EndorsePolicyAlteration> selectListPolicyAlterationByendorseId(HashMap<String,Object> params);
	
	public EndorseMapping selectMapEndorse(EndorseMapping endorseMapping);
	public EndorseMapping selectMapEndorseForKey(EndorseMapping endorseMapping);
	public List<EndorseMapping> selectDirectMapEndorse();
	
	public String selectDynamicQuery(SQLAdapter adapter);
	
	public ArrayList<DropdownPolicyAlteration> selectListPernikahan();
	public String selectListPernikahanById(String lsst_id);
	public String selectListProvinsiById(String lspr_id);

	public ArrayList<DropdownPolicyAlteration> selectListAgama();
	
	public String selectListAgamaById(String lsag_id);
	public ArrayList<DropdownPolicyAlteration> selectListNegara();
	
	public ArrayList<DropdownPolicyAlteration> selectListJenisUsaha();
	
	public ArrayList<DropdownPolicyAlteration> selectListPekerjaan();
	
	public ArrayList<DropdownPolicyAlteration> selectListBank();
	public String selectListBankById(String lsbp_id);
	public String selectCabangBankByLbnId(String lbn_id);
	public String selectListNegaraById(String lsne_id);
	
	public ArrayList<DropdownPolicyAlteration> selectCabangBank(Integer lsbp_id_req);
	
	public ArrayList<DropdownPolicyAlteration> selectListRelation();
	
	public Endorse selectListJenisEndors(Integer lsje_id);
	
	public String selectMclId_PP(String reg_spaj);
	
	public PembayarPremi selectPembayarPremi(String no_polis);
	
	public ArrayList<Inbox> selectInbox(String username);
	
	public NotifToken selectNotifToken(String userid);
	
	public Article selectCountListArticle();
	
	public ArrayList<TransactionHistory> selectTransactionHistory(HashMap<String, Object> hashMap);
	
	public ArrayList<TransactionHistory> selectTransactionHistoryDropDown(HashMap<String, Object> hashMap);
	
	public String selectGetOnlyRegSpaj(String no_polis);
	
	public Integer selectCountListBilling(HashMap<String, Object> hashMap);
	
	public MstOTPSimultaneousDet selectDetailOTP(MstOTPSimultaneousDet mstOTPSimultaneousDet);
	
	public MstOTPSimultaneous selectDataOTP(MstOTPSimultaneous paramSelectData);

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
	
	public ArrayList<UserHR> selectListPolisHRUser(String eb_hr_username);
	
	public ArrayList<UserHR> selectListPesertaHR(HashMap<String, Object> hashMap);
	
	public ArrayList<ReportHr> selectListReportHr(HashMap<String, Object> hashMap);
	
	public ArrayList<DownloadReportHr> selectPathReportHr(String no_batch);
	
	public Integer selectCountReportHr(String no_polis);
	
	public ArrayList<ViewMclFirst> selectListPolisOrion(HashMap<String, Object> hashMap);
	
	public String selectGetIdTicket();
	
	public ArrayList<EndorseHr> selectListEndorseHr(String no_polis);
	
	public EndorseHr selectViewEndorseHr(String id_ticket);
	
	public EndorseHr selectPrepareEndorseHr(String no_polis);

	// Update
	public void updateUserKeyName(HashMap<String, Object> data);
	
	public void updateLspdId(String reg_spaj);

	public void updatePassword(User user);

	public void updateDataMstAddressNew(Pemegang pemegang);

	public void updateDataMstAddressBilling(Pemegang pemegang);

	public void updateKeyLoginClear(User user);

	public void deleteAllInbox(User user);
	
	public void deleteSavedProvider(HashMap<String,Object> params );

	public void updateInboxStatus(User user);

	public void updateActivityStatus(User user);

	public void updateLinkAccount(LstUserSimultaneous lstUserSimultaneous);

	public void updateUnlinkAccountCorporate(UserCorporate userCorporate);
	
	public void updateFurtherClaimIndividu(String mpc_id);
	
	public void updateFurtherClaimCorporate(String mpcc_id);

	public void updateAgama(PolicyAlteration policyAlteration);
	
	public void updateDynamicQuery(SQLAdapter adapter );

	public void updateAlamatKantor(PolicyAlteration policyAlteration);

	public void updateKewarganegaraan(PolicyAlteration policyAlteration);

	public void updateStatus(PolicyAlteration policyAlteration);


	public void updateJenisPekerjaan(PolicyAlteration policyAlteration);


	public void updateCompanyPekerjaan(PolicyAlteration policyAlteration);
	

	public void updateJenisCompanyPekerjaan(PolicyAlteration policyAlteration);

	public void updatejobdesc(PolicyAlteration policyAlteration);
	
	public void updateNotifToken(NotifToken notifToken);

	public void updateOtpUsed(MstOTPSimultaneous paramUpdateOtpUsed);

	public void updateAttemptOtp(MstOTPSimultaneous paramUpdate);

	public void updateStatusAttemptOtp(MstOTPSimultaneous paramUpdate);

	public User selectDataPolisByPolisNo(HashMap<String, Object> hashMap);

	public List<Pemegang> filterIndividuAndMri(HashMap<String, Object> hashMap);

	public List<Pemegang> filterByIdSimultanRegSpajNoPolis(HashMap<String, Object> hashMap);

	public MRIdataPolis selectDataMri(String no_polis);

	public MRIdataPolis selectDataMriBrosure(String no_polis);

	public MpolTransData selectMpolTransByMptId(String mpt_id);

	public String selectConfigAdmedika();

	public DetailPesertaCorporate detailPesertaCorporate(HashMap<String, Object> hashMap);

	public SummaryPayment findByMptId(String mpt_id);

	public SummaryPayment findByMstrxId(String mspa_trx);

	public void updatePathSummary(HashMap<String, Object> hashMap);

	public DPLKAccountModel findAccountDplk(HashMap<String, Object> hashMap);

	public User findByUsernameDplk(String username);

	public User findByAccountNoDplk(String account_no);

	public DPLKAccountModel getInfoDplkByAccNo(String acc_no);

	public List<LstTransaksi> selectLstTransaksi();

	public List<TransactionHistory> selectHistoryTransaksi(HashMap<String, Object> hashMap);

	public List<JenisInvestDplk> getJenisInvestDplk();

	public List<DailyPriceFundDplk> getJenisInvestDplkByAccNo(String acc_no);

	public List<LstTransaksiDplk> getLstTransDplk(HashMap<String, Object> hashMap);

	public List<TransactionHistory> selectHistoryPolicyAlteration(HashMap<String, Object> data);

	public List<TransactionHistory> selectHistoryClaimSubmissionIndividu(HashMap<String, Object> data);

	public List<EnrollPesertaAdmedika> enrollPesertaAdmedikaCorporate(String username);
}
