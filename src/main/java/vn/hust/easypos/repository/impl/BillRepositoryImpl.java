package vn.hust.easypos.repository.impl;

import com.google.common.base.Strings;
import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.hust.easypos.repository.BillRepositoryCustom;
import vn.hust.easypos.service.dto.bill.BillItemResponse;
import vn.hust.easypos.service.dto.bill.BillStatItem;
import vn.hust.easypos.service.dto.bill.BillStatsResult;
import vn.hust.easypos.service.util.Common;

public class BillRepositoryImpl implements BillRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Page<BillItemResponse> searchBills(
        Pageable pageable,
        Integer status,
        String fromDate,
        String toDate,
        String keyword,
        Integer comId
    ) {
        List<BillItemResponse> billList = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" from BILL b ");
        strQuery.append(" left join bill_payment bp on b.id = bp.bill_id ");
        strQuery.append(" WHERE com_id = :comId ");
        params.put("comId", comId);
        if (status != null) {
            strQuery.append(" AND status = :status ");
            params.put("status", status);
        }
        if (!Strings.isNullOrEmpty(fromDate) || !Strings.isNullOrEmpty(toDate)) {
            Common.addDateSearchCustom(fromDate, toDate, params, strQuery, "b.created_date", "bill");
        }
        if (!Strings.isNullOrEmpty(keyword)) {
            strQuery.append(" AND (b.customer_normalized_name like :keyword) ");
            params.put("keyword", "%" + Common.normalizedName(Arrays.asList(keyword)) + "%");
        }
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(*) " + strQuery);
        Common.setParams(countQuery, params);
        Number count = (Number) countQuery.getSingleResult();
        if (count.intValue() > 0) {
            String sort = " order by b.last_modified_date desc ";
            Query query = entityManager.createNativeQuery(
                "select b.id id, " +
                "       b.code code, " +
                "       b.total_amount  totalAmount, " +
                "       b.customer_name customerName, " +
                "       b.status status, " +
                "       bp.payment_method paymentMethod, " +
                "       b.created_date createTime, " +
                "       b.bill_date billDate " +
                strQuery +
                sort,
                "BillResponseItemDTO"
            );
            Common.setParamsWithPageable(query, params, pageable);
            billList = query.getResultList();
        }

        return new PageImpl<>(billList, pageable, count.intValue());
    }

    @Override
    public List<BillStatItem> getBillMoney(Integer comId, String fromDate, String toDate, String format) {
        List<BillStatItem> items = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" FROM bill b ");
        strQuery.append(" WHERE b.com_id = :com_id and status = 1 ");
        params.put("com_id", comId);
        Common.addDateSearchCustom(fromDate, toDate, params, strQuery, "b.bill_date", "bill");
        Query query = entityManager.createNativeQuery(
            "SELECT format(bill_date, '" +
            format +
            "') time, SUM(b.total_amount) money " +
            strQuery +
            " GROUP BY format(bill_date, '" +
            format +
            "') " +
            " ORDER BY format(bill_date, '" +
            format +
            "') ",
            "BillMoneyResultItem"
        );
        Common.setParams(query, params);
        items = query.getResultList();
        return items;
    }

    @Override
    public BillStatsResult getBillStats(Integer comId, String fromDate, String toDate) {
        List<BillStatsResult> results;
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" from bill b where b.com_id = :com_id ");
        params.put("com_id", comId);
        Common.addDateSearchCustom(fromDate, toDate, params, strQuery, "b.bill_date", "bill");
        Query query = entityManager.createNativeQuery(
            "select sum(iif(b.status = 0, 1, 0)) processingCount, count(b.id) allCount " + strQuery,
            "BillStatsResult"
        );
        Common.setParams(query, params);
        results = query.getResultList();

        if (!results.isEmpty()) {
            return results.get(0);
        }
        return new BillStatsResult();
    }
}
