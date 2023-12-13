package vn.softdreams.easypos.repository.impl;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.easypos.constants.BusinessTypeConstants;
import vn.softdreams.easypos.constants.CommonConstants;
import vn.softdreams.easypos.dto.receiptpayment.GetAllTransactionsAlternative;
import vn.softdreams.easypos.dto.receiptpayment.ReceiptPayment;
import vn.softdreams.easypos.repository.ReceiptPaymentRepositoryCustom;
import vn.softdreams.easypos.service.impl.GenCodeServiceImpl;
import vn.softdreams.easypos.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
@Transactional
public class ReceiptPaymentRepositoryImpl implements ReceiptPaymentRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    private final Logger log = LoggerFactory.getLogger(GenCodeServiceImpl.class);

    @Override
    public GetAllTransactionsAlternative getAllTransactions(
        Pageable pageable,
        Integer comId,
        String fromDate,
        String toDate,
        Integer type,
        String keyword,
        Boolean isCountAll,
        boolean paramCheckAll,
        List<Integer> ids
    ) {
        GetAllTransactionsAlternative response = new GetAllTransactionsAlternative();
        List<ReceiptPayment> mcReceiptList;
        List<ReceiptPayment> mcPaymentList = new ArrayList<>();
        Map<String, Object> receiptParams = new HashMap<>();
        StringBuilder receiptFromQuery = new StringBuilder();
        receiptFromQuery.append(" FROM mc_receipt mr join business_type bt on mr.business_type_id = bt.id WHERE mr.com_id = :comId ");
        receiptParams.put("comId", comId);
        Map<String, Object> paymentParams = new HashMap<>();
        StringBuilder paymentFromQuery = new StringBuilder();
        paymentFromQuery.append(" FROM mc_payment mp join business_type bt on mp.business_type_id = bt.id WHERE mp.com_id = :comId ");
        paymentParams.put("comId", comId);

        if (!Strings.isNullOrEmpty(fromDate) || !Strings.isNullOrEmpty(toDate)) {
            Common.addDateSearchCustom(fromDate, toDate, receiptParams, receiptFromQuery, "date", "Date");
            Common.addDateSearchCustom(fromDate, toDate, paymentParams, paymentFromQuery, "date", "Date");
        }

        if (!Strings.isNullOrEmpty(keyword)) {
            receiptFromQuery.append(
                " AND (mr.customer_name LIKE :keyword OR CAST(CAST(mr.amount AS BIGINT) AS NVARCHAR) LIKE :keyword OR bt.business_type_name LIKE :keyword) "
            );
            paymentFromQuery.append(
                " AND (mp.customer_name LIKE :keyword OR CAST(CAST(mp.amount AS BIGINT) AS NVARCHAR) LIKE :keyword OR bt.business_type_name LIKE :keyword) "
            );
            receiptParams.put("keyword", "%" + keyword + "%");
            paymentParams.put("keyword", "%" + keyword + "%");
        }

        StringBuilder receiptQuerySelect = new StringBuilder();
        receiptQuerySelect.append(
            "SELECT mr.id id," +
            "0 type," +
            "mr.type_desc typeDesc," +
            "mr.date date," +
            "mr.no no," +
            "mr.amount amount, " +
            "mr.customer_name customerName," +
            "mr.customer_id customerId," +
            "mr.description note," +
            "mr.business_type_id businessTypeId, " +
            "bt.business_type_name businessTypeName "
        );

        StringBuilder paymentQuerySelect = new StringBuilder();
        paymentQuerySelect.append(
            "SELECT mp.id id," +
            "1 type," +
            "mp.type_desc typeDesc," +
            "mp.date date," +
            "mp.no no," +
            "mp.amount amount, " +
            "mp.customer_name customerName," +
            "mp.customer_id customerId," +
            "mp.description note," +
            "mp.business_type_id businessTypeId, " +
            "bt.business_type_name businessTypeName "
        );
        Query countReceiptQuery = entityManager.createNativeQuery("SELECT SUM(IIF(mr.amount IS NULL, 0, mr.amount)) " + receiptFromQuery);
        Common.setParams(countReceiptQuery, receiptParams);
        Number countReceipt = (Number) countReceiptQuery.getSingleResult();
        if (countReceipt == null) {
            countReceipt = 0;
        }
        response.setReceiptAmount(
            BigDecimal.valueOf(countReceipt.longValue()).setScale(CommonConstants.REGISTER_PASSWORD_LENGTH, RoundingMode.UNNECESSARY)
        );

        Query countPaymentQuery = entityManager.createNativeQuery("SELECT SUM(IIF(mp.amount IS NULL, 0, mp.amount)) " + paymentFromQuery);
        Common.setParams(countPaymentQuery, paymentParams);
        Number countPayment = (Number) countPaymentQuery.getSingleResult();
        if (countPayment == null) {
            countPayment = 0;
        }
        response.setPaymentAmount(
            BigDecimal.valueOf(countPayment.longValue()).setScale(CommonConstants.REGISTER_PASSWORD_LENGTH, RoundingMode.UNNECESSARY)
        );
        if (paramCheckAll) {
            if (ids != null && ids.size() > 0) {
                receiptFromQuery.append(" AND mr.id not in :ids ");
                receiptParams.put("ids", ids);
                paymentFromQuery.append(" AND mp.id not in :ids ");
                paymentParams.put("ids", ids);
            }
        } else {
            if (ids != null && ids.size() > 0) {
                receiptFromQuery.append(" AND mr.id in :ids ");
                receiptParams.put("ids", ids);
                paymentFromQuery.append(" AND mp.id in :ids ");
                paymentParams.put("ids", ids);
            }
        }

        Number count = 0;
        Query countQuery = null;
        if (Objects.equals(BusinessTypeConstants.Type.RECEIPT, type)) {
            countQuery = entityManager.createNativeQuery("SELECT COUNT(*) " + receiptFromQuery);
            Common.setParams(countQuery, receiptParams);
            count = (Number) countQuery.getSingleResult();
        } else if (Objects.equals(BusinessTypeConstants.Type.PAYMENT, type)) {
            countQuery = entityManager.createNativeQuery("SELECT COUNT(*) " + paymentFromQuery);
            Common.setParams(countQuery, paymentParams);
            count = (Number) countQuery.getSingleResult();
        } else if (type == null) {
            countQuery = entityManager.createNativeQuery("SELECT COUNT(*) " + receiptFromQuery);
            Common.setParams(countQuery, receiptParams);
            count = (Number) countQuery.getSingleResult();
            countQuery = entityManager.createNativeQuery("SELECT COUNT(*) " + paymentFromQuery);
            Common.setParams(countQuery, paymentParams);
            count = count.longValue() + (int) countQuery.getSingleResult();
        }
        response.setReceiptAmount(
            BigDecimal.valueOf(countReceipt.longValue()).setScale(CommonConstants.REGISTER_PASSWORD_LENGTH, RoundingMode.UNNECESSARY)
        );

        if (type == null) {
            Query receiptQuery = entityManager.createNativeQuery(
                receiptQuerySelect.append(receiptFromQuery) +
                " union " +
                paymentQuerySelect.append(paymentFromQuery) +
                " ORDER BY date desc ",
                "McReceipt"
            );
            receiptParams.putAll(paymentParams);
            if (pageable != null) {
                Common.setParamsWithPageable(receiptQuery, receiptParams, pageable);
            } else {
                Common.setParams(receiptQuery, receiptParams);
            }
            mcReceiptList = receiptQuery.getResultList();
            mcReceiptList.sort((o1, o2) -> {
                if (o1.getDate() == null && o2.getDate() == null) {
                    return 0;
                } else if (o1.getDate() == null) {
                    return 1;
                } else if (o2.getDate() == null) {
                    return -1;
                } else {
                    return o2.getDate().compareTo(o1.getDate());
                }
            });
            if (!mcReceiptList.isEmpty() && pageable != null) {
                mcReceiptList = mcReceiptList.subList(0, Math.min(mcReceiptList.size(), pageable.getPageSize()));
            }
        } else {
            receiptFromQuery.append(" ORDER BY mr.date DESC ");
            paymentFromQuery.append(" ORDER BY mp.date DESC ");
            Query receiptQuery = entityManager.createNativeQuery(String.valueOf(receiptQuerySelect.append(receiptFromQuery)), "McReceipt");
            Query paymentQuery = entityManager.createNativeQuery(String.valueOf(paymentQuerySelect.append(paymentFromQuery)), "McPayment");
            if (pageable != null) {
                Common.setParamsWithPageable(receiptQuery, receiptParams, pageable);
                Common.setParamsWithPageable(paymentQuery, paymentParams, pageable);
            } else {
                Common.setParams(receiptQuery, receiptParams);
                Common.setParams(paymentQuery, paymentParams);
            }
            mcReceiptList = receiptQuery.getResultList();
            mcPaymentList = paymentQuery.getResultList();
            mcReceiptList.forEach(x -> x.setType(0));
            mcPaymentList.forEach(x -> x.setType(1));
        }
        if (Objects.equals(BusinessTypeConstants.Type.RECEIPT, type) || type == null) {
            response.setReceiptPaymentList(
                pageable != null
                    ? new PageImpl<>(mcReceiptList, pageable, Boolean.TRUE.equals(isCountAll) ? count.longValue() : mcReceiptList.size())
                    : new PageImpl<>(mcReceiptList)
            );
        } else if (Objects.equals(BusinessTypeConstants.Type.PAYMENT, type)) {
            response.setReceiptPaymentList(
                pageable != null
                    ? new PageImpl<>(mcPaymentList, pageable, Boolean.TRUE.equals(isCountAll) ? count.longValue() : mcPaymentList.size())
                    : new PageImpl<>(mcPaymentList)
            );
        } else log.error("Loại chứng từ không hợp lệ");
        return response;
    }
}
