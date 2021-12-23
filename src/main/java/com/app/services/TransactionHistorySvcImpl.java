package com.app.services;

import com.app.exception.HandleSuccessOrNot;
import com.app.model.DownloadAttachment;
import com.app.model.LstTransaksi;
import com.app.model.TransactionHistory;
import com.app.utils.DateUtils;
import com.app.utils.ResponseMessage;
import com.app.utils.VegaCustomResourceLoader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransactionHistorySvcImpl implements TransactionHistorySvc {

    private static final Logger logger = LogManager.getLogger(TransactionHistorySvcImpl.class);

    @Autowired
    private VegaServices vegaServices;
    @Autowired
    private VegaCustomResourceLoader customResourceLoader;
    @Autowired
    private DateUtils dateUtils;

    @Override
    public List<LstTransaksi> dropDownListTransaksi() {
        List<LstTransaksi> getLstTransaksi = mapLstTransaksi(vegaServices.getListTransaksi());
        List<LstTransaksi> otherTransaction = otherTransactions();
        getLstTransaksi.addAll(otherTransaction);
        return getLstTransaksi;
    }

    @Override
    public List<TransactionHistory> getTransactionHistory(String reg_spaj, String transaction_type, String start_date, String end_date) throws ParseException {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime firstDay = dateUtils.getFirstDay(now);
        List<TransactionHistory> mpolTrans;
        List<TransactionHistory> policyAlteration;
        List<TransactionHistory> claimSubmission;
        if (start_date != null && end_date != null) {
            Date start_to_date = dateUtils.getFormatterFormat(start_date, DateUtils.YEAR_MONTH);
            Date end_to_date = dateUtils.getFormatterFormat(end_date, DateUtils.YEAR_MONTH);
            start_date = dateUtils.format(dateUtils.convertToLocalDateViaMilisecond(start_to_date), DateUtils.FORMAT_DAY_MONTH_YEAR);
            end_date = dateUtils.format(dateUtils.convertToLocalDateViaMilisecond(end_to_date).plusMonths(1), DateUtils.FORMAT_DAY_MONTH_YEAR);
        } else {
            start_date = dateUtils.format(firstDay, DateUtils.FORMAT_DAY_MONTH_YEAR);
            end_date = dateUtils.format(now.plusMonths(1), DateUtils.FORMAT_DAY_MONTH_YEAR);
        }

        mpolTrans = vegaServices.selectHistoryTransaksi(null, reg_spaj, start_date, end_date);
        policyAlteration = vegaServices.getTransactionPolicyAlteration(reg_spaj, start_date, end_date);
        claimSubmission = vegaServices.getTransactionHistoryClaimSubmission(reg_spaj, start_date, end_date);
        mpolTrans = mapTransactionMpolTrans(mpolTrans);
        mpolTrans.addAll(policyAlteration);
        mpolTrans.addAll(claimSubmission);
        if (transaction_type != null && !transaction_type.equals("")) {
            return mpolTrans.stream().filter(v -> v.getTransaction_type().equals(transaction_type)).collect(Collectors.toList());
        }
        return mpolTrans;
    }

    @Override
    public TransactionHistory getDetailTransactionHistory(String kode_transaksi, String reg_spaj) throws ParseException {
        List<TransactionHistory> historyList = getTransactionHistory(reg_spaj, null, null, null);
        if (historyList != null && historyList.size() > 0) {
            Optional<TransactionHistory> optionalTransactionHistory = historyList.stream().filter(v -> v.getKode_transaksi().equals(kode_transaksi)).findFirst();
            if (optionalTransactionHistory.isPresent()) {
                return optionalTransactionHistory.get();
            }
        }
        return null;
    }

    @Override
    public ResponseEntity<Resource> downloadAttachmentHistory(DownloadAttachment requestBody, HttpServletRequest request, HttpServletResponse response) throws IOException {
        File file = new File(requestBody.getPath());
        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        return ResponseEntity.ok()
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }

    private List<TransactionHistory> mapTransactionMpolTrans(List<TransactionHistory> historyList) {
        historyList = historyList.stream().peek(v -> {
                    v.setTransaction_desc(v.getTransaction_type());
                    switch (v.getLt_id()) {
                        case 22:
                            v.setTransaction_type("TRANS-01");
                            break;
                        case 19:
                            v.setTransaction_type("TRANS-02");
                            break;
                        case 4:
                            v.setTransaction_type("TRANS-03");
                            break;
                        case 20:
                            v.setTransaction_type("TRANS-04");
                            break;
                        case 2:
                            v.setTransaction_type("TRANS-05");
                            break;
                        case 3:
                            v.setTransaction_type("TRANS-06");
                            break;
                    }
                }
        ).collect(Collectors.toList());
        return historyList;
    }

    private List<LstTransaksi> mapLstTransaksi(List<LstTransaksi> lstTransaksis) {
        lstTransaksis = lstTransaksis.stream().peek(v -> {
                    switch (v.getLt_id()) {
                        case 22:
                            v.setType_transaksi("TRANS-01");
                            break;
                        case 19:
                            v.setType_transaksi("TRANS-02");
                            break;
                        case 4:
                            v.setType_transaksi("TRANS-03");
                            break;
                        case 20:
                            v.setType_transaksi("TRANS-04");
                            break;
                        case 2:
                            v.setType_transaksi("TRANS-05");
                            break;
                        case 3:
                            v.setType_transaksi("TRANS-06");
                            break;
                    }
                }
        ).collect(Collectors.toList());
        return lstTransaksis;
    }

    private List<LstTransaksi> otherTransactions() {
        List<LstTransaksi> otherTransactions = new ArrayList<>();
        LstTransaksi lstTransaksiA = new LstTransaksi();
        lstTransaksiA.setLt_transksi(null);
        lstTransaksiA.setLt_transksi("Policy Alteration");
        lstTransaksiA.setType_transaksi("TRANS-07");
        LstTransaksi lstTransaksiB = new LstTransaksi();
        lstTransaksiB.setLt_transksi(null);
        lstTransaksiB.setLt_transksi("Claim Submission");
        lstTransaksiB.setType_transaksi("TRANS-08");

        otherTransactions.add(lstTransaksiA);
        otherTransactions.add(lstTransaksiB);
        return otherTransactions;
    }


}
