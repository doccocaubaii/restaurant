package vn.softdreams.easypos.repository.impl;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.dto.invoice.InvoiceListResponse;
import vn.softdreams.easypos.dto.invoice.InvoiceSearchResponse;
import vn.softdreams.easypos.dto.invoice.InvoiceStatsResult;
import vn.softdreams.easypos.dto.product.SaleProductStatsRequest;
import vn.softdreams.easypos.dto.product.SaleProductStatsResult;
import vn.softdreams.easypos.repository.InvoiceRepositoryCustom;
import vn.softdreams.easypos.service.dto.PublishListRequest;
import vn.softdreams.easypos.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static vn.softdreams.easypos.config.Constants.TAX_AUTHORITY_CODE_DEFAULT;

public class InvoiceRepositoryImpl implements InvoiceRepositoryCustom {

    private final Logger log = LoggerFactory.getLogger(InvoiceRepositoryCustom.class);

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<InvoiceSearchResponse> findAllByCompanyIdAndIds(List<Integer> ids, Integer companyId) {
        List<InvoiceSearchResponse> companyResponseDTO = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("  from invoice where company_id = :companyId and id in :ids");
        params.put("companyId", companyId);
        params.put("ids", ids);
        //        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(*) " + strQuery);
        //        Common.setParams(countQuery, params);
        //        Number count = (Number) countQuery.getSingleResult();
        //        if (count.intValue() > 0) {
        Query query = entityManager.createNativeQuery("select id, ikey " + strQuery, "InvoiceSearchResponse");
        Common.setParams(query, params);
        companyResponseDTO = query.getResultList();
        //        }
        return companyResponseDTO;
    }

    @Override
    public InvoiceStatsResult getInvoiceStats(Integer comId, String fromDate, String toDate) {
        List<InvoiceStatsResult> results;
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" from invoice i where i.company_id = :company_id ");
        params.put("company_id", comId);
        Common.addDateSearchCustom(fromDate, toDate, params, strQuery, "i.arising_date", "invoice");
        Query query = entityManager.createNativeQuery(
            "select sum(iif(tax_check_status is null, 1, 0)) newCount, " +
            "sum(iif(tax_check_status = -1, 1, 0)) processingCount, " +
            "sum(iif(tax_check_status = 1, 1, 0)) doneCount, " +
            "count(id) allCount " +
            strQuery,
            "InvoiceStatsResult"
        );
        Common.setParams(query, params);
        results = query.getResultList();

        if (!results.isEmpty()) {
            return results.get(0);
        }
        return new InvoiceStatsResult();
    }

    @Override
    public List<SaleProductStatsResult> getProductSaleStats(SaleProductStatsRequest request) {
        List<SaleProductStatsResult> results;
        String strQuery = " EXEC ProductSalesStatistic ?, ?, ?, ?, ?, ? ";

        Query query = entityManager.createNativeQuery(strQuery, "SaleProductStatsResult");
        query.setParameter(1, request.getComId());
        query.setParameter(2, request.getFromDate());
        query.setParameter(3, request.getToDate());
        query.setParameter(4, request.getStatus());
        query.setParameter(5, request.getTaxCheckStatus());
        query.setParameter(6, request.getPattern());
        results = query.getResultList();
        return results;
    }

    @Override
    public List<Integer> getAllIdInvoices(PublishListRequest publishListRequest, Integer comId) {
        List<Integer> invoiceList = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("  FROM invoice i ");
        //        if (!Strings.isNullOrEmpty(keyword)) {
        strQuery.append(" JOIN bill b on i.bill_id = b.id ");
        //        }
        strQuery.append(" WHERE i.company_id = :comId AND i.tax_authority_code != :taxAuthorityCode ");
        params.put("comId", comId);
        params.put("taxAuthorityCode", TAX_AUTHORITY_CODE_DEFAULT);
        if (publishListRequest.getTaxCheckStatus() != null) {
            if (publishListRequest.getTaxCheckStatus() == 0) {
                strQuery.append(" AND i.tax_check_status is null ");
            } else {
                strQuery.append(" AND i.tax_check_status = :type ");
                params.put("type", publishListRequest.getTaxCheckStatus());
            }
        }
        if ((!Strings.isNullOrEmpty(publishListRequest.getFromDate()) || !Strings.isNullOrEmpty(publishListRequest.getToDate()))) {
            Common.addDateSearchCustom(
                publishListRequest.getFromDate(),
                publishListRequest.getToDate(),
                params,
                strQuery,
                "i.arising_date",
                "arising_date"
            );
        }

        if (!Strings.isNullOrEmpty(publishListRequest.getPattern())) {
            strQuery.append(" AND i.pattern LIKE :pattern ");
            params.put("pattern", "%" + publishListRequest.getPattern().trim() + "%");
        }
        if (!Strings.isNullOrEmpty(publishListRequest.getCustomerName())) {
            strQuery.append(" AND i.customer_name LIKE :customerName  ");
            params.put("customerName", "%" + publishListRequest.getCustomerName().trim() + "%");
        }
        if (!Strings.isNullOrEmpty(publishListRequest.getNo())) {
            strQuery.append(" AND i.no LIKE :no  ");
            params.put("no", "%" + publishListRequest.getNo().trim() + "%");
        }
        if (publishListRequest.getListID() != null && publishListRequest.getListID().size() > 0) {
            strQuery.append(" AND i.id not in :listId  ");
            params.put("listId", publishListRequest.getListID());
        }
        strQuery.append(" ORDER BY arising_date DESC");
        Query query = entityManager.createNativeQuery("SELECT i.id id  " + strQuery, Integer.class);
        Common.setParams(query, params);
        invoiceList = query.getResultList();
        return invoiceList;
    }

    public Page<InvoiceListResponse> getAllInvoices(
        Integer taxCheckStatus,
        String fromDate,
        String toDate,
        String pattern,
        String customerName,
        String no,
        Integer companyId,
        Pageable pageable,
        boolean isCountAll,
        boolean paramCheckAll,
        List<Integer> listId
    ) {
        List<InvoiceListResponse> invoiceList = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("  FROM invoice i ");
        //        if (!Strings.isNullOrEmpty(keyword)) {
        strQuery.append(" JOIN bill b on i.bill_id = b.id ");
        //        }
        strQuery.append(" WHERE i.company_id = :comId AND i.tax_authority_code != :taxAuthorityCode ");
        params.put("comId", companyId);
        params.put("taxAuthorityCode", TAX_AUTHORITY_CODE_DEFAULT);
        if (taxCheckStatus != null) {
            if (taxCheckStatus == 0) {
                strQuery.append(" AND i.tax_check_status is null ");
            } else {
                strQuery.append(" AND i.tax_check_status = :type ");
                params.put("type", taxCheckStatus);
            }
        }
        if ((!Strings.isNullOrEmpty(fromDate) || !Strings.isNullOrEmpty(toDate))) {
            Common.addDateSearchCustom(fromDate, toDate, params, strQuery, "i.arising_date", "arising_date");
        }

        if (!Strings.isNullOrEmpty(pattern)) {
            strQuery.append(" AND i.pattern LIKE :pattern ");
            params.put("pattern", "%" + pattern.trim() + "%");
        }
        if (!Strings.isNullOrEmpty(customerName)) {
            strQuery.append(" AND i.customer_name LIKE :customerName  ");
            params.put("customerName", "%" + customerName.trim() + "%");
        }
        if (!Strings.isNullOrEmpty(no)) {
            strQuery.append(" AND i.no LIKE :no  ");
            params.put("no", "%" + no.trim() + "%");
        }
        if (paramCheckAll) {
            if (listId != null && listId.size() > 0) {
                strQuery.append(" AND i.id not in  :listId  ");
                params.put("listId", listId);
            }
        } else {
            if (listId != null && listId.size() > 0) {
                strQuery.append(" AND i.id in  :listId  ");
                params.put("listId", listId);
            }
        }
        Number count = 0;
        if (isCountAll) {
            Query countQuery = entityManager.createNativeQuery("SELECT COUNT(*) " + strQuery);
            Common.setParams(countQuery, params);
            count = (Number) countQuery.getSingleResult();
        }
        strQuery.append(" ORDER BY arising_date DESC");
        Query query = entityManager.createNativeQuery(
            "SELECT i.id id,  " +
            "b.id billId,  " +
            "i.customer_id customerId,  " +
            "i.customer_name customerName,  " +
            "i.customer_code customerCode,  " +
            "i.pattern pattern,  " +
            "i.no no,  " +
            "i.arising_date arisingDate,  " +
            "i.publish_date publishDate,  " +
            "i.total_amount totalAmount,  " +
            "i.currency_unit currencyUnit,  " +
            "i.type type,  " +
            "i.status status,  " +
            "i.tax_check_status taxCheckStatus,  " +
            "i.tax_authority_code taxAuthorityCode,  " +
            "i.ikey ikey,  " +
            "i.error_publish errorPublish ,  " +
            "i.create_time createTime, " +
            "i.update_time updateTime " +
            strQuery,
            "InvoiceResponseListDTO"
        );
        if (pageable != null) {
            Common.setParamsWithPageable(query, params, pageable);
        } else {
            Common.setParams(query, params);
        }
        invoiceList = query.getResultList();
        return new PageImpl<>(
            invoiceList,
            pageable != null ? pageable : PageRequest.of(0, 1),
            isCountAll ? count.longValue() : invoiceList.size()
        );
    }
}
