package com.app.model;

import java.io.Serializable;

public class PolicyAlteration implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2356621085798776512L;	
	private String cara_bayar;
	private Integer nama_bank_payor;
	private Integer cabang_bank_payor;
	private String kota_bank_payor;
	private String no_rekening_payor;
	private String pemilik_rekening_payor;
	private String masa_berlaku;
	private String hubungan_payor;
	private String nama_payor;
	private String nama_perusahaan;
	private String jabatan;
	private String alamat_rumah;
	private String negara;
	private String propinsi;
	private String kabupaten;
	private String kecamatan;
	private String kelurahan;
	private String kodepos;
	private String area_code_rumah;
	private String telpon_rumah;
	private String no_hp;
	private String tujuan;
	private String sumber_dana;
	private String mkl_kerja;
	private String mkl_penghasilan;
	private String mkl_smbr_penghasilan;
	
	private String status_tt;
	private Integer agama_tt;
	private Integer kewarganegaraan_tt;
	private String nama_perusahaan_tt;
	private String jabatan_tt;
	private String tipe_usaha_tt;
	
	private String no_polis;
	private String nama_pp;
	private String jenis_produk;
	private Integer status;
	private Integer agama;
	private Integer kewarganegaraan_pp;
	private String npwp;
	private String nama_perusahaan_pp;
	private String jabatan_pp;
	private String uraian_pekerjaan;
	private String alamat_kantor;
	private Integer propinsi_kantor;
	private Integer kabupaten_kantor;
	private Integer kecamatan_kantor;
	private Integer kelurahan_kantor;
	private Integer kodepos_kantor;
	private String area_code_rumah_pp;
	private String telpon_rumah_pp;
	private String alamat_rumah_pp;
	private Integer propinsi_rumah;
	private Integer kabupaten_rumah;
	private Integer kecamatan_rumah;
	private Integer kelurahan_rumah;
	private Integer kodepos_rumah;
	private String alamat_tpt_tinggal;
	private Integer korespondensi_flag;
	private Integer nama_bank_pp;
	private Integer cabang_bank_pp;
	private String kota_bank_pp;
	private String no_rekening_pp;
	private String pemilik_rekening_pp;
	private String email;
	private String tipe_usaha_pp;
	
	private Integer negara_tinggal;
	private Integer propinsi_tinggal;
	private Integer kabupaten_tinggal;
	private Integer kecamatan_tinggal;
	private Integer kelurahan_tinggal;
	private Integer kodepos_tinggal;
	
	private String reg_spaj;
	private String mcl_id;
	private Integer lsag_id;
	private Integer lsne_id;
	private Integer mspe_sts_mrt;
	
	public Integer getNama_bank_payor() {
		return nama_bank_payor;
	}
	public void setNama_bank_payor(Integer nama_bank_payor) {
		this.nama_bank_payor = nama_bank_payor;
	}
	public Integer getCabang_bank_payor() {
		return cabang_bank_payor;
	}
	public void setCabang_bank_payor(Integer cabang_bank_payor) {
		this.cabang_bank_payor = cabang_bank_payor;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getAgama() {
		return agama;
	}
	public void setAgama(Integer agama) {
		this.agama = agama;
	}
	public Integer getKewarganegaraan_pp() {
		return kewarganegaraan_pp;
	}
	public void setKewarganegaraan_pp(Integer kewarganegaraan_pp) {
		this.kewarganegaraan_pp = kewarganegaraan_pp;
	}
	public Integer getPropinsi_kantor() {
		return propinsi_kantor;
	}
	public void setPropinsi_kantor(Integer propinsi_kantor) {
		this.propinsi_kantor = propinsi_kantor;
	}
	public Integer getKabupaten_kantor() {
		return kabupaten_kantor;
	}
	public void setKabupaten_kantor(Integer kabupaten_kantor) {
		this.kabupaten_kantor = kabupaten_kantor;
	}
	public Integer getKecamatan_kantor() {
		return kecamatan_kantor;
	}
	public void setKecamatan_kantor(Integer kecamatan_kantor) {
		this.kecamatan_kantor = kecamatan_kantor;
	}
	public Integer getKelurahan_kantor() {
		return kelurahan_kantor;
	}
	public void setKelurahan_kantor(Integer kelurahan_kantor) {
		this.kelurahan_kantor = kelurahan_kantor;
	}
	public Integer getKodepos_kantor() {
		return kodepos_kantor;
	}
	public void setKodepos_kantor(Integer kodepos_kantor) {
		this.kodepos_kantor = kodepos_kantor;
	}
	public Integer getPropinsi_rumah() {
		return propinsi_rumah;
	}
	public void setPropinsi_rumah(Integer propinsi_rumah) {
		this.propinsi_rumah = propinsi_rumah;
	}
	public Integer getKabupaten_rumah() {
		return kabupaten_rumah;
	}
	public void setKabupaten_rumah(Integer kabupaten_rumah) {
		this.kabupaten_rumah = kabupaten_rumah;
	}
	public Integer getKecamatan_rumah() {
		return kecamatan_rumah;
	}
	public void setKecamatan_rumah(Integer kecamatan_rumah) {
		this.kecamatan_rumah = kecamatan_rumah;
	}
	public Integer getKelurahan_rumah() {
		return kelurahan_rumah;
	}
	public void setKelurahan_rumah(Integer kelurahan_rumah) {
		this.kelurahan_rumah = kelurahan_rumah;
	}
	public Integer getKodepos_rumah() {
		return kodepos_rumah;
	}
	public void setKodepos_rumah(Integer kodepos_rumah) {
		this.kodepos_rumah = kodepos_rumah;
	}
	public Integer getNama_bank_pp() {
		return nama_bank_pp;
	}
	public void setNama_bank_pp(Integer nama_bank_pp) {
		this.nama_bank_pp = nama_bank_pp;
	}
	public Integer getCabang_bank_pp() {
		return cabang_bank_pp;
	}
	public void setCabang_bank_pp(Integer cabang_bank_pp) {
		this.cabang_bank_pp = cabang_bank_pp;
	}
	public Integer getNegara_tinggal() {
		return negara_tinggal;
	}
	public void setNegara_tinggal(Integer negara_tinggal) {
		this.negara_tinggal = negara_tinggal;
	}
	public Integer getKodepos_tinggal() {
		return kodepos_tinggal;
	}
	public void setKodepos_tinggal(Integer kodepos_tinggal) {
		this.kodepos_tinggal = kodepos_tinggal;
	}
	public Integer getAgama_tt() {
		return agama_tt;
	}
	public void setAgama_tt(Integer agama_tt) {
		this.agama_tt = agama_tt;
	}
	public Integer getKewarganegaraan_tt() {
		return kewarganegaraan_tt;
	}
	public void setKewarganegaraan_tt(Integer kewarganegaraan_tt) {
		this.kewarganegaraan_tt = kewarganegaraan_tt;
	}
	public Integer getPropinsi_tinggal() {
		return propinsi_tinggal;
	}
	public void setPropinsi_tinggal(Integer propinsi_tinggal) {
		this.propinsi_tinggal = propinsi_tinggal;
	}
	public Integer getKabupaten_tinggal() {
		return kabupaten_tinggal;
	}
	public void setKabupaten_tinggal(Integer kabupaten_tinggal) {
		this.kabupaten_tinggal = kabupaten_tinggal;
	}
	public Integer getKecamatan_tinggal() {
		return kecamatan_tinggal;
	}
	public void setKecamatan_tinggal(Integer kecamatan_tinggal) {
		this.kecamatan_tinggal = kecamatan_tinggal;
	}
	public Integer getKelurahan_tinggal() {
		return kelurahan_tinggal;
	}
	public void setKelurahan_tinggal(Integer kelurahan_tinggal) {
		this.kelurahan_tinggal = kelurahan_tinggal;
	}
	public Integer getMspe_sts_mrt() {
		return mspe_sts_mrt;
	}
	public void setMspe_sts_mrt(Integer mspe_sts_mrt) {
		this.mspe_sts_mrt = mspe_sts_mrt;
	}
	public Integer getLsne_id() {
		return lsne_id;
	}
	public void setLsne_id(Integer lsne_id) {
		this.lsne_id = lsne_id;
	}
	public Integer getLsag_id() {
		return lsag_id;
	}
	public void setLsag_id(Integer lsag_id) {
		this.lsag_id = lsag_id;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public String getMcl_id() {
		return mcl_id;
	}
	public void setMcl_id(String mcl_id) {
		this.mcl_id = mcl_id;
	}
	public String getTipe_usaha_tt() {
		return tipe_usaha_tt;
	}
	public void setTipe_usaha_tt(String tipe_usaha_tt) {
		this.tipe_usaha_tt = tipe_usaha_tt;
	}
	public String getTipe_usaha_pp() {
		return tipe_usaha_pp;
	}
	public void setTipe_usaha_pp(String tipe_usaha_pp) {
		this.tipe_usaha_pp = tipe_usaha_pp;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNo_polis() {
		return no_polis;
	}
	public void setNo_polis(String no_polis) {
		this.no_polis = no_polis;
	}
	public String getNama_pp() {
		return nama_pp;
	}
	public void setNama_pp(String nama_pp) {
		this.nama_pp = nama_pp;
	}
	public String getJenis_produk() {
		return jenis_produk;
	}
	public void setJenis_produk(String jenis_produk) {
		this.jenis_produk = jenis_produk;
	}
	public String getNpwp() {
		return npwp;
	}
	public void setNpwp(String npwp) {
		this.npwp = npwp;
	}
	public String getNama_perusahaan_pp() {
		return nama_perusahaan_pp;
	}
	public void setNama_perusahaan_pp(String nama_perusahaan_pp) {
		this.nama_perusahaan_pp = nama_perusahaan_pp;
	}
	public String getJabatan_pp() {
		return jabatan_pp;
	}
	public void setJabatan_pp(String jabatan_pp) {
		this.jabatan_pp = jabatan_pp;
	}
	public String getUraian_pekerjaan() {
		return uraian_pekerjaan;
	}
	public void setUraian_pekerjaan(String uraian_pekerjaan) {
		this.uraian_pekerjaan = uraian_pekerjaan;
	}
	public String getAlamat_kantor() {
		return alamat_kantor;
	}
	public void setAlamat_kantor(String alamat_kantor) {
		this.alamat_kantor = alamat_kantor;
	}
	public String getArea_code_rumah_pp() {
		return area_code_rumah_pp;
	}
	public void setArea_code_rumah_pp(String area_code_rumah_pp) {
		this.area_code_rumah_pp = area_code_rumah_pp;
	}
	public String getTelpon_rumah_pp() {
		return telpon_rumah_pp;
	}
	public void setTelpon_rumah_pp(String telpon_rumah_pp) {
		this.telpon_rumah_pp = telpon_rumah_pp;
	}
	public String getAlamat_rumah_pp() {
		return alamat_rumah_pp;
	}
	public void setAlamat_rumah_pp(String alamat_rumah_pp) {
		this.alamat_rumah_pp = alamat_rumah_pp;
	}
	public String getAlamat_tpt_tinggal() {
		return alamat_tpt_tinggal;
	}
	public void setAlamat_tpt_tinggal(String alamat_tpt_tinggal) {
		this.alamat_tpt_tinggal = alamat_tpt_tinggal;
	}
	public Integer getKorespondensi_flag() {
		return korespondensi_flag;
	}
	public void setKorespondensi_flag(Integer korespondensi_flag) {
		this.korespondensi_flag = korespondensi_flag;
	}
	public String getKota_bank_pp() {
		return kota_bank_pp;
	}
	public void setKota_bank_pp(String kota_bank_pp) {
		this.kota_bank_pp = kota_bank_pp;
	}
	public String getNo_rekening_pp() {
		return no_rekening_pp;
	}
	public void setNo_rekening_pp(String no_rekening_pp) {
		this.no_rekening_pp = no_rekening_pp;
	}
	public String getPemilik_rekening_pp() {
		return pemilik_rekening_pp;
	}
	public void setPemilik_rekening_pp(String pemilik_rekening_pp) {
		this.pemilik_rekening_pp = pemilik_rekening_pp;
	}
	public String getStatus_tt() {
		return status_tt;
	}
	public void setStatus_tt(String status_tt) {
		this.status_tt = status_tt;
	}

	public String getNama_perusahaan_tt() {
		return nama_perusahaan_tt;
	}
	public void setNama_perusahaan_tt(String nama_perusahaan_tt) {
		this.nama_perusahaan_tt = nama_perusahaan_tt;
	}
	public String getJabatan_tt() {
		return jabatan_tt;
	}
	public void setJabatan_tt(String jabatan_tt) {
		this.jabatan_tt = jabatan_tt;
	}
	public String getCara_bayar() {
		return cara_bayar;
	}
	public void setCara_bayar(String cara_bayar) {
		this.cara_bayar = cara_bayar;
	}
	public String getKota_bank_payor() {
		return kota_bank_payor;
	}
	public void setKota_bank_payor(String kota_bank_payor) {
		this.kota_bank_payor = kota_bank_payor;
	}
	public String getNo_rekening_payor() {
		return no_rekening_payor;
	}
	public void setNo_rekening_payor(String no_rekening_payor) {
		this.no_rekening_payor = no_rekening_payor;
	}
	public String getPemilik_rekening_payor() {
		return pemilik_rekening_payor;
	}
	public void setPemilik_rekening_payor(String pemilik_rekening_payor) {
		this.pemilik_rekening_payor = pemilik_rekening_payor;
	}
	public String getMasa_berlaku() {
		return masa_berlaku;
	}
	public void setMasa_berlaku(String masa_berlaku) {
		this.masa_berlaku = masa_berlaku;
	}
	public String getHubungan_payor() {
		return hubungan_payor;
	}
	public void setHubungan_payor(String hubungan_payor) {
		this.hubungan_payor = hubungan_payor;
	}
	public String getNama_payor() {
		return nama_payor;
	}
	public void setNama_payor(String nama_payor) {
		this.nama_payor = nama_payor;
	}
	public String getNama_perusahaan() {
		return nama_perusahaan;
	}
	public void setNama_perusahaan(String nama_perusahaan) {
		this.nama_perusahaan = nama_perusahaan;
	}
	public String getJabatan() {
		return jabatan;
	}
	public void setJabatan(String jabatan) {
		this.jabatan = jabatan;
	}
	public String getAlamat_rumah() {
		return alamat_rumah;
	}
	public void setAlamat_rumah(String alamat_rumah) {
		this.alamat_rumah = alamat_rumah;
	}
	public String getNegara() {
		return negara;
	}
	public void setNegara(String negara) {
		this.negara = negara;
	}
	public String getPropinsi() {
		return propinsi;
	}
	public void setPropinsi(String propinsi) {
		this.propinsi = propinsi;
	}
	public String getKabupaten() {
		return kabupaten;
	}
	public void setKabupaten(String kabupaten) {
		this.kabupaten = kabupaten;
	}
	public String getKecamatan() {
		return kecamatan;
	}
	public void setKecamatan(String kecamatan) {
		this.kecamatan = kecamatan;
	}
	public String getKelurahan() {
		return kelurahan;
	}
	public void setKelurahan(String kelurahan) {
		this.kelurahan = kelurahan;
	}
	public String getKodepos() {
		return kodepos;
	}
	public void setKodepos(String kodepos) {
		this.kodepos = kodepos;
	}
	public String getArea_code_rumah() {
		return area_code_rumah;
	}
	public void setArea_code_rumah(String area_code_rumah) {
		this.area_code_rumah = area_code_rumah;
	}
	public String getTelpon_rumah() {
		return telpon_rumah;
	}
	public void setTelpon_rumah(String telpon_rumah) {
		this.telpon_rumah = telpon_rumah;
	}
	public String getNo_hp() {
		return no_hp;
	}
	public void setNo_hp(String no_hp) {
		this.no_hp = no_hp;
	}
	public String getTujuan() {
		return tujuan;
	}
	public void setTujuan(String tujuan) {
		this.tujuan = tujuan;
	}
	public String getSumber_dana() {
		return sumber_dana;
	}
	public void setSumber_dana(String sumber_dana) {
		this.sumber_dana = sumber_dana;
	}
	public String getMkl_kerja() {
		return mkl_kerja;
	}
	public void setMkl_kerja(String mkl_kerja) {
		this.mkl_kerja = mkl_kerja;
	}
	public String getMkl_penghasilan() {
		return mkl_penghasilan;
	}
	public void setMkl_penghasilan(String mkl_penghasilan) {
		this.mkl_penghasilan = mkl_penghasilan;
	}
	public String getMkl_smbr_penghasilan() {
		return mkl_smbr_penghasilan;
	}
	public void setMkl_smbr_penghasilan(String mkl_smbr_penghasilan) {
		this.mkl_smbr_penghasilan = mkl_smbr_penghasilan;
	}
}