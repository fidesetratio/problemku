package com.app.model;

import java.io.Serializable;

public class Payor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2356621085798776512L;	
	private String reg_spaj;
	private Integer msaw_number;
    private String msaw_first;
    private String msaw_birth;
    private String lsre_relation;
    private Integer msaw_persen;
    private Integer msaw_sex;
    
    private DetailPolicyAlteration cara_bayar;
	private DetailPolicyAlteration nama_bank_payor;
	private DetailPolicyAlteration cabang_bank_payor;
	private DetailPolicyAlteration kota_bank_payor;
	private DetailPolicyAlteration no_rekening_payor;
	private DetailPolicyAlteration pemilik_rekening_payor;
	private DetailPolicyAlteration masa_berlaku;
	private DetailPolicyAlteration hubungan_payor;
	private DetailPolicyAlteration nama_payor;
	private DetailPolicyAlteration nama_perusahaan;
	private DetailPolicyAlteration jabatan;
	private DetailPolicyAlteration alamat_rumah;
	private DetailPolicyAlteration negara;
	private DetailPolicyAlteration propinsi;
	private DetailPolicyAlteration kabupaten;
	private DetailPolicyAlteration kecamatan;
	private DetailPolicyAlteration kelurahan;
	private DetailPolicyAlteration kodepos;
	private DetailPolicyAlteration area_code_rumah;
	private DetailPolicyAlteration telpon_rumah;
	private DetailPolicyAlteration no_hp;
	private DetailPolicyAlteration tujuan;
	private DetailPolicyAlteration sumber_dana;
	private DetailPolicyAlteration mkl_kerja;
	private DetailPolicyAlteration mkl_penghasilan;
	private DetailPolicyAlteration mkl_smbr_penghasilan;
    
	public DetailPolicyAlteration getCara_bayar() {
		return cara_bayar;
	}
	public void setCara_bayar(DetailPolicyAlteration cara_bayar) {
		this.cara_bayar = cara_bayar;
	}
	public DetailPolicyAlteration getNama_bank_payor() {
		return nama_bank_payor;
	}
	public void setNama_bank_payor(DetailPolicyAlteration nama_bank_payor) {
		this.nama_bank_payor = nama_bank_payor;
	}
	public DetailPolicyAlteration getCabang_bank_payor() {
		return cabang_bank_payor;
	}
	public void setCabang_bank_payor(DetailPolicyAlteration cabang_bank_payor) {
		this.cabang_bank_payor = cabang_bank_payor;
	}
	public DetailPolicyAlteration getKota_bank_payor() {
		return kota_bank_payor;
	}
	public void setKota_bank_payor(DetailPolicyAlteration kota_bank_payor) {
		this.kota_bank_payor = kota_bank_payor;
	}
	public DetailPolicyAlteration getNo_rekening_payor() {
		return no_rekening_payor;
	}
	public void setNo_rekening_payor(DetailPolicyAlteration no_rekening_payor) {
		this.no_rekening_payor = no_rekening_payor;
	}
	public DetailPolicyAlteration getPemilik_rekening_payor() {
		return pemilik_rekening_payor;
	}
	public void setPemilik_rekening_payor(DetailPolicyAlteration pemilik_rekening_payor) {
		this.pemilik_rekening_payor = pemilik_rekening_payor;
	}
	public DetailPolicyAlteration getMasa_berlaku() {
		return masa_berlaku;
	}
	public void setMasa_berlaku(DetailPolicyAlteration masa_berlaku) {
		this.masa_berlaku = masa_berlaku;
	}
	public DetailPolicyAlteration getHubungan_payor() {
		return hubungan_payor;
	}
	public void setHubungan_payor(DetailPolicyAlteration hubungan_payor) {
		this.hubungan_payor = hubungan_payor;
	}
	public DetailPolicyAlteration getNama_payor() {
		return nama_payor;
	}
	public void setNama_payor(DetailPolicyAlteration nama_payor) {
		this.nama_payor = nama_payor;
	}
	public DetailPolicyAlteration getNama_perusahaan() {
		return nama_perusahaan;
	}
	public void setNama_perusahaan(DetailPolicyAlteration nama_perusahaan) {
		this.nama_perusahaan = nama_perusahaan;
	}
	public DetailPolicyAlteration getJabatan() {
		return jabatan;
	}
	public void setJabatan(DetailPolicyAlteration jabatan) {
		this.jabatan = jabatan;
	}
	public DetailPolicyAlteration getAlamat_rumah() {
		return alamat_rumah;
	}
	public void setAlamat_rumah(DetailPolicyAlteration alamat_rumah) {
		this.alamat_rumah = alamat_rumah;
	}
	public DetailPolicyAlteration getNegara() {
		return negara;
	}
	public void setNegara(DetailPolicyAlteration negara) {
		this.negara = negara;
	}
	public DetailPolicyAlteration getPropinsi() {
		return propinsi;
	}
	public void setPropinsi(DetailPolicyAlteration propinsi) {
		this.propinsi = propinsi;
	}
	public DetailPolicyAlteration getKabupaten() {
		return kabupaten;
	}
	public void setKabupaten(DetailPolicyAlteration kabupaten) {
		this.kabupaten = kabupaten;
	}
	public DetailPolicyAlteration getKecamatan() {
		return kecamatan;
	}
	public void setKecamatan(DetailPolicyAlteration kecamatan) {
		this.kecamatan = kecamatan;
	}
	public DetailPolicyAlteration getKelurahan() {
		return kelurahan;
	}
	public void setKelurahan(DetailPolicyAlteration kelurahan) {
		this.kelurahan = kelurahan;
	}
	public DetailPolicyAlteration getKodepos() {
		return kodepos;
	}
	public void setKodepos(DetailPolicyAlteration kodepos) {
		this.kodepos = kodepos;
	}
	public DetailPolicyAlteration getArea_code_rumah() {
		return area_code_rumah;
	}
	public void setArea_code_rumah(DetailPolicyAlteration area_code_rumah) {
		this.area_code_rumah = area_code_rumah;
	}
	public DetailPolicyAlteration getTelpon_rumah() {
		return telpon_rumah;
	}
	public void setTelpon_rumah(DetailPolicyAlteration telpon_rumah) {
		this.telpon_rumah = telpon_rumah;
	}
	public DetailPolicyAlteration getNo_hp() {
		return no_hp;
	}
	public void setNo_hp(DetailPolicyAlteration no_hp) {
		this.no_hp = no_hp;
	}
	public DetailPolicyAlteration getTujuan() {
		return tujuan;
	}
	public void setTujuan(DetailPolicyAlteration tujuan) {
		this.tujuan = tujuan;
	}
	public DetailPolicyAlteration getSumber_dana() {
		return sumber_dana;
	}
	public void setSumber_dana(DetailPolicyAlteration sumber_dana) {
		this.sumber_dana = sumber_dana;
	}
	public DetailPolicyAlteration getMkl_kerja() {
		return mkl_kerja;
	}
	public void setMkl_kerja(DetailPolicyAlteration mkl_kerja) {
		this.mkl_kerja = mkl_kerja;
	}
	public DetailPolicyAlteration getMkl_penghasilan() {
		return mkl_penghasilan;
	}
	public void setMkl_penghasilan(DetailPolicyAlteration mkl_penghasilan) {
		this.mkl_penghasilan = mkl_penghasilan;
	}
	public DetailPolicyAlteration getMkl_smbr_penghasilan() {
		return mkl_smbr_penghasilan;
	}
	public void setMkl_smbr_penghasilan(DetailPolicyAlteration mkl_smbr_penghasilan) {
		this.mkl_smbr_penghasilan = mkl_smbr_penghasilan;
	}
	public Integer getMsaw_sex() {
		return msaw_sex;
	}
	public void setMsaw_sex(Integer msaw_sex) {
		this.msaw_sex = msaw_sex;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public Integer getMsaw_number() {
		return msaw_number;
	}
	public void setMsaw_number(Integer msaw_number) {
		this.msaw_number = msaw_number;
	}
	public String getMsaw_first() {
		return msaw_first;
	}
	public void setMsaw_first(String msaw_first) {
		this.msaw_first = msaw_first;
	}
	public String getMsaw_birth() {
		return msaw_birth;
	}
	public void setMsaw_birth(String msaw_birth) {
		this.msaw_birth = msaw_birth;
	}
	public String getLsre_relation() {
		return lsre_relation;
	}
	public void setLsre_relation(String lsre_relation) {
		this.lsre_relation = lsre_relation;
	}
	public Integer getMsaw_persen() {
		return msaw_persen;
	}
	public void setMsaw_persen(Integer msaw_persen) {
		this.msaw_persen = msaw_persen;
	}
    
    
}