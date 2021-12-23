package com.app.services;

import com.app.model.DownloadAttachment;
import com.app.model.LstTransaksi;
import com.app.model.TransactionHistory;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface TransactionHistorySvc {

    List<LstTransaksi> dropDownListTransaksi();

    List<TransactionHistory> getTransactionHistory(String reg_spaj, String transaction_type, String start_date, String end_date) throws ParseException;

    TransactionHistory getDetailTransactionHistory(String kode_transaksi, String reg_spaj) throws ParseException;

    ResponseEntity<Resource> downloadAttachmentHistory(DownloadAttachment requestBody, HttpServletRequest request, HttpServletResponse response) throws IOException;

}
