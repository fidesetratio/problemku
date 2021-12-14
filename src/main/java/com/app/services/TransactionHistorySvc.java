package com.app.services;

import com.app.model.DownloadAttachment;
import com.app.model.LstTransaksi;
import com.app.model.TransactionHistory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.List;

public interface TransactionHistorySvc {

    List<LstTransaksi> dropDownListTransaksi();

    List<TransactionHistory> getTransactionHistory(String reg_spaj, String transaction_type, String start_date, String end_date) throws ParseException;

    TransactionHistory getDetailTransactionHistory(String kode_transaksi, String reg_spaj) throws ParseException;

    String downloadAttachmentHistory(DownloadAttachment requestBody, HttpServletRequest request, HttpServletResponse response);

}
